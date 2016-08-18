package com.dianrong.common.uniauth.common.util;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.google.common.collect.Maps;

public class TaskInvoker {
	
	private static final Log log = LogFactory.getLog(TaskInvoker.class);
	
	public static final String TASK_PATH_PREFIX = "/com/dianrong/cfg/1.0.0/task_";
	
	private static final Map<String,TaskExecutor> TASK_REGISTRY = Maps.newConcurrentMap();
	
	private static ZooKeeper zooKeeper;
	
	private static AtomicBoolean inited = new AtomicBoolean(false);
	
	private static AtomicBoolean locked = new AtomicBoolean(false);
	
	public static void register(String name,TaskExecutor exec){
		if(!locked.get()) return;
		TASK_REGISTRY.put(name, exec);
		try {
			zooKeeper.create(TASK_PATH_PREFIX+"/"+name, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (KeeperException | InterruptedException e) {
			log.error("register error", e);
		}
		try {
			zooKeeper.getData(TASK_PATH_PREFIX+"/"+name, true, null);
		} catch (KeeperException | InterruptedException e) {
			log.error("watch data error", e);
		}
	}
	
	/**
	 * 初始化
	 */
	public static void init(){
		if(!inited.compareAndSet(false, true)){
			return;
		}
		try {
			zooKeeper = new ZooKeeper(System.getProperty("DR_CFG_ZOOKEEPER_ENV_URL"), 200, null);
			Stat exists = zooKeeper.exists(TASK_PATH_PREFIX, false);
			zooKeeper.register(new TaskWatch());
			if(exists == null){
				try{
					zooKeeper.create(TASK_PATH_PREFIX, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}catch(Exception e){
					log.error("create path:"+TASK_PATH_PREFIX+" error", e);
				}
			}
			
			try{
				zooKeeper.create(TASK_PATH_PREFIX+"/lock", null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				locked.set(true);
			}catch(Exception e){
				log.debug("get lock failed");
				zooKeeper.close();
			}
			
		} catch (Exception e) {
			log.error("init SwitchRegistry error", e);
		}
	}
	
	private static class TaskWatch implements Watcher{

		@Override
		public void process(WatchedEvent event) {
			if(event.getType() == EventType.NodeDataChanged){
				//取到节点数据并且重新监听
				try {
					byte[] data = zooKeeper.getData(event.getPath(), true, null);
					String subPath = event.getPath().substring(TASK_PATH_PREFIX.length()+1);
					if(TASK_REGISTRY.containsKey(subPath)){
						TASK_REGISTRY.get(subPath).execut(new String(data));
					}
				} catch (Exception e) {
					log.error("process error,path:"+event.getPath(), e);
				}
			}
		}
		
	}

	public static interface TaskExecutor{
		
		/**
		 * 执行任务
		 * @param cmd
		 */
		public void execut(String cmd); 
		
	}
}



