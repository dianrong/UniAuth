package com.dianrong.common.uniauth.common.switches;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import com.alibaba.fastjson.JSON;

/**
 * 开关注册器
 * @author dreamlee
 *
 */
public class SwitchRegistry {
	
	private static final Log log = LogFactory.getLog(SwitchRegistry.class);
	
	public static final String SWITCH_PATH_PREFIX = "/com/dianrong/switch";
	
	private static ZooKeeper zooKeeper;
	
	private static AtomicBoolean inited = new AtomicBoolean(false);
	
	public static void init(){
		if(!inited.compareAndSet(false, true)){
			return;
		}
		try {
			zooKeeper = new ZooKeeper(System.getProperty("DR_CFG_ZOOKEEPER_ENV_URL"), 200, null);
			Stat exists = zooKeeper.exists(SWITCH_PATH_PREFIX, false);
			if(exists == null){
				zooKeeper.create(SWITCH_PATH_PREFIX, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
		} catch (Exception e) {
			log.error("init SwitchRegistry error", e);
		}
	}
	
	
	public static void register(String appName,Class<?> switchClass) throws Exception{
		if(!inited.get()){
			log.error("registry not inited,please invoke init first!");
			return;
		}
		Field[] fields = switchClass.getFields();
		String appPath = SWITCH_PATH_PREFIX+"/" +appName;
		
		//创建应用主路径
		createPathIfNecessary(appPath,"".getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
		for(Field f : fields){
			if(Modifier.isStatic(f.getModifiers()) && f.isAnnotationPresent(Switch.class)){
				Switch switchDesc = f.getAnnotation(Switch.class);
				String name = switchDesc.name();
				if(StringUtils.isBlank(name)){
					name = f.getName();
				}
				f.setAccessible(true);
				String filedPath = appPath +"/" +name;
				
				SwitchReceiver receier = (SwitchReceiver)switchDesc.recieiver().newInstance();
				//读取远程持久化数据，优先读取指定ip的数据
				if(zooKeeper.exists(filedPath +"/"+getLocalAddress(), false) != null){
					byte[] data = zooKeeper.getData(filedPath +"/"+getLocalAddress(), false, null);
					receier.receive(f, new String(data));
				}else if(zooKeeper.exists(filedPath +"/ALL", false) != null){
					byte[] data = zooKeeper.getData(filedPath +"/ALL", false, null);
					receier.receive(f, new String(data));
				}
				createPathIfNecessary(filedPath, "".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
				createPathIfNecessary(filedPath +"/ALL" , JSON.toJSONString(f.get(null)).getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
				createPathIfNecessary(filedPath +"/"+getLocalAddress() , JSON.toJSONString(f.get(null)).getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
				SwitchWatcher watcher = new SwitchWatcher(f,receier);
				zooKeeper.getData(filedPath+"/ALL",  watcher, null);
				zooKeeper.getData(filedPath +"/"+getLocalAddress(),  watcher, null);
			}
		}
		
	}
	
	
	private static class SwitchWatcher implements Watcher{
		
		private Field f;
		private SwitchReceiver receiver;
		
		public SwitchWatcher(Field f, SwitchReceiver receiver) {
			this.f = f;
			this.receiver = receiver;
		}

		@Override
		public void process(WatchedEvent event) {
			System.out.println(event.getType());
			try {
				byte[] data = zooKeeper.getData(event.getPath(), this, null);
				receiver.receive(f,new String(data));
			} catch (Exception e) {
				log.error("press node error", e);
			}
		}
		
	}
	
	
	private static String getLocalAddress() {
		try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();   
            Collection<InetAddress> addresses = new ArrayList<InetAddress>();
            
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();   
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();   
                while (inetAddresses.hasMoreElements()) {   
                    InetAddress inetAddress = inetAddresses.nextElement();   
                    addresses.add(inetAddress);   
                }
            }
            
            for (InetAddress address : addresses) {
            	if (!address.isLoopbackAddress() &&!(address instanceof Inet6Address)){
            		return address.getHostAddress();
            	}
            }
               
        } catch (SocketException e) {   
        	log.error("getLocalAddress error", e);
        }   
		return "";
	}


	private static void createPathIfNecessary(String path,byte[] data,List<ACL> acl, CreateMode createMode) throws KeeperException, InterruptedException{
		if(zooKeeper.exists(path, false) == null){
			zooKeeper.create(path, data, acl, createMode);
		}
	}
	
}
