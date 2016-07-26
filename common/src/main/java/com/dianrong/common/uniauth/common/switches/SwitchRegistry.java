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
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import com.google.common.collect.Maps;

/**
 * 开关注册器
 * @author dreamlee
 *
 */
public class SwitchRegistry {
	
	private static final Log log = LogFactory.getLog(SwitchRegistry.class);
	
	public static final String SWITCH_PATH_PREFIX = "/com/dianrong/switch";
	
	private static final Map<String, Map<String,SwitchHolder>> SWITCHS = Maps.newConcurrentMap();
	
	private static ZooKeeper zooKeeper;
	
	private static AtomicBoolean inited = new AtomicBoolean(false);
	
	public static void init(){
		if(!inited.compareAndSet(false, true)){
			return;
		}
		try {
			zooKeeper = new ZooKeeper(System.getProperty("DR_CFG_ZOOKEEPER_ENV_URL"), 200, null);
			zooKeeper.register(new SwitchWatch());
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
		
		if(SWITCHS.containsKey(appName)) return;
		Map<String,SwitchHolder> switchs = Maps.newHashMap();
		SWITCHS.put(appName, switchs);
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
				
				SwitchReceiver receiver = (SwitchReceiver)switchDesc.recieiver().newInstance();
				//读取远程持久化数据，优先读取指定ip的数据
				if(zooKeeper.exists(filedPath +"/"+getLocalAddress(), false) != null &&
						zooKeeper.getData(filedPath +"/"+getLocalAddress(), false,null) != null){
					byte[] data = zooKeeper.getData(filedPath +"/"+getLocalAddress(), false, null);
					try{
						receiver.receive(f, new String(data));
					}catch(Exception e){
						log.error("init "+f.getName()+" error", e);
					}
					
				}else if(zooKeeper.exists(filedPath +"/ALL", false) != null
						&& zooKeeper.getData(filedPath +"/ALL", false,null) != null){
					byte[] data = zooKeeper.getData(filedPath +"/ALL", false, null);
					try{
						receiver.receive(f, new String(data));
					}catch(Exception e){
						log.error("init "+f.getName()+" error", e);
					}
				}
				switchs.put(name, new SwitchHolder(f, receiver));
				createPathIfNecessary(filedPath, "".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
				createPathIfNecessary(filedPath +"/ALL" , null, Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
				createPathIfNecessary(filedPath +"/"+getLocalAddress() , null, Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
				zooKeeper.getData(filedPath+"/ALL",  true,null);
				zooKeeper.getData(filedPath +"/"+getLocalAddress(),true,null);
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
	
	
	private static class SwitchWatch implements Watcher{

		@Override
		public void process(WatchedEvent event) {
			if(event.getType() == EventType.NodeDataChanged){
				//取到节点数据并且重新监听
				try {
					byte[] data = zooKeeper.getData(event.getPath(), true, null);
					String subPath = event.getPath().substring(SWITCH_PATH_PREFIX.length()+1);
					String[] names = subPath.split("/");
					if(names.length>1){
						String appName = names[0];
						String switchName = names[1];
						Map<String, SwitchHolder> appSwitchs = SWITCHS.get(appName);
						if(appSwitchs != null && !appSwitchs.isEmpty()){
							SwitchHolder holder = appSwitchs.get(switchName);
							holder.getReceiver().receive(holder.getF(), new String(data));
						}
					}
				} catch (Exception e) {
					log.error("process error,path:"+event.getPath(), e);
				}
			}
		}
		
	}
	
	
	private static class SwitchHolder{
		private Field f;
		private SwitchReceiver receiver;
		public SwitchHolder(Field f, SwitchReceiver receiver) {
			super();
			this.f = f;
			this.receiver = receiver;
		}
		public Field getF() {
			return f;
		}
		public SwitchReceiver getReceiver() {
			return receiver;
		}
	}
}
