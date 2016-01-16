package com.dianrong.common.uniauth.client.support;


import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.common.io.Resources;

public class ZooKeeperConfig extends ConcurrentHashMap<String, String> implements PathChildrenCacheListener
{
  private static final long serialVersionUID = -3902163527221096390L;
  private static Logger logger = LoggerFactory.getLogger(ZooKeeperConfig.class);
  public static final String NULL_VALUE_IN_ZK = "null";
  public static final String PATH_DELIMITER = "/";

  public String connectString;
  public String cfgRootNodeName;
  public String location;
  public boolean needKeepAlive;
  protected CuratorFramework client;
  protected PathChildrenCache cache;
  private int cfgCacheThreadNum = 2;

  private int maxRetries = 3;

  private int baseSleepTimes = 5000;

  private int connectionTimeout = 5000;

  public ZooKeeperConfig(){
	  connectString = System.getProperty("DR_CFG_ZOOKEEPER_ENV_URL");
	  if(connectString == null){
		  throw new IllegalArgumentException("Cannot get connectString from system properties['DR_CFG_ZOOKEEPER_ENV_URL']");
	  }
  }
  
  public void init() {
    if (!StringUtils.isEmpty(this.cfgRootNodeName)) {
      logger.debug("cfgRootNodeName:" + cfgRootNodeName);
      initialClient();
      getAllCfgsItemFromOneNode(this.cfgRootNodeName);
    }
    overridePartialCfgs(this.location);
    if (!this.needKeepAlive) {
      closeConection();
    }
    logger.info("CfgGroup Initial Success, Cfg Size Is : {}", Integer.valueOf(size()));
    logger.info("The detailed data:" + this);
  }

  private void initialClient() {
    if (StringUtils.isEmpty(this.connectString))
      throw new RuntimeException("Missing Connection String.");
    try
    {
      RetryPolicy retryPolicy = new ExponentialBackoffRetry(this.baseSleepTimes, this.maxRetries);
      this.client = CuratorFrameworkFactory.builder().connectString(this.connectString).retryPolicy(retryPolicy).connectionTimeoutMs(this.connectionTimeout).build();

      this.client.start();

      this.cache = new PathChildrenCache(this.client, this.cfgRootNodeName, true);
      this.cache.start();
      Executor executor = Executors.newFixedThreadPool(this.cfgCacheThreadNum);
      this.cache.getListenable().addListener(this, executor);
    } catch (Exception e) {
      logger.error("CfgGroup Initial Exception, exception = {}", e);
    }
  }

  private void overridePartialCfgs(String location) {
    Properties properties = new Properties();
    try {
      properties.load(Resources.getResource(location).openStream());
    } catch (Exception e) {
      logger.warn("CfgGroup Properties Does Not Initial Successfully,Location Is Unaviliable.");
    }
    if (properties != null) {
      Enumeration keys = properties.keys();
      while (keys.hasMoreElements()) {
        Object element = keys.nextElement();
        super.put(String.valueOf(element), properties.getProperty(String.valueOf(element)));
      }
    }
  }

  private void getAllCfgsItemFromOneNode(String path) {
    try {
      Stat stat = (Stat)this.client.checkExists().forPath(path);
      if (stat != null) {
        String value = new String((byte[])this.client.getData().forPath(path));
        super.put(formatFullPathToKey(path), value);
        List<String> cfgs = (List<String>)this.client.getChildren().forPath(path);
        if (!cfgs.isEmpty())
          for (String child : cfgs)
            getAllCfgsItemFromOneNode(path + "/" + child);
      }
    }
    catch (Exception e)
    {
      logger.error("Get All Cfgs From One Node Error : {}", e);
    }
  }

  public static String formatFullPathToKey(String fullPath) {
    return fullPath.substring(fullPath.lastIndexOf("/") + 1, fullPath.length());
  }

  public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
    throws Exception
  {
    String data = new String(event.getData().getData());
    String path = event.getData().getPath();
    Stat stat = event.getData().getStat();
    PathChildrenCacheEvent.Type type = event.getType();
    logger.debug("CfgGroup Content Change data={}, path={}, stat={}, type={}", new Object[] { data, path, stat, type });
    if (type == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
      String key = formatFullPathToKey(path);
      super.put(key, data);
    }
  }

  private void closeConection() {
    if (this.client != null) {
      try {
        this.client.close();
      } catch (Exception e) {
        logger.warn("CfgGroup Close Error Exception : {}", e);
      } finally {
        this.client = null;
      }
      try
      {
        this.cache.close();
      } catch (Exception e) {
        logger.warn("CfgGroup Close Error Exception : {}", e);
      } finally {
        this.cache = null;
      }
    }
  }

  public void setCfgRootNodeName(String cfgRootNodeName) {
    this.cfgRootNodeName = cfgRootNodeName;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setNeedKeepAlive(boolean needKeepAlive) {
    this.needKeepAlive = needKeepAlive;
  }
}
