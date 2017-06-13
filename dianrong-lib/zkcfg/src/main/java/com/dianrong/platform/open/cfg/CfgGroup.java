package com.dianrong.platform.open.cfg;

import java.nio.charset.Charset;

import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class CfgGroup extends CfgBaseGroup {

  private static final long serialVersionUID = 1L;

  private static Logger logger = LoggerFactory.getLogger(CfgGroup.class);

  public CfgGroup() {}

  /**
   * 构造方法.
   */
  public CfgGroup(String connectString, String rootNode, boolean keepAlive) {
    super.connectString = connectString;
    super.cfgRootNodeName = rootNode;
    super.needKeepAlive = keepAlive;
    super.init();
  }

  private CfgGroup internalCfgGroup;

  @Override
  public String get(Object key) {
    String value;
    if (internalCfgGroup != null) {
      value = internalCfgGroup.get(key);
      if (!StringUtils.isEmpty(value)) {
        if (NULL_VALUE_IN_ZK.equals(value)) {
          return null;
        } else {
          return value;
        }
      }
    }
    value = super.get(key);
    if (NULL_VALUE_IN_ZK.equals(value)) {
      return null;
    } else {
      return value;
    }
  }

  /**
   * 更新.
   */
  public void updateCfg(String fullPath, Object obj) {
    checkStatus(fullPath, obj);
    checkExist(fullPath);
    String value = String.valueOf(obj);
    try {
      client.setData().forPath(fullPath, value.getBytes(Charset.forName("utf-8")));
      super.put(formatFullPathToKey(fullPath), value);
    } catch (Exception e) {
      // should never happen
      logger.error("CfgGroup UpdateCfg Exception, exception = {}", e);
    }
  }


  /**
   * 添加配置.
   */
  public void addCfg(String fullPath, Object obj) {
    checkStatus(fullPath, obj);
    Stat stat = null;
    try {
      stat = client.checkExists().forPath(fullPath);
    } catch (Exception e) {
      // should never happen
      logger.error("CfgGroup AddCfg Exception, exception = {}", e);
    }
    if (stat != null) {
      throw new RuntimeException(fullPath + " Already Exists.");
    }
    String value = String.valueOf(obj);
    try {
      client.create().forPath(fullPath, value.getBytes(Charset.forName("utf-8")));
      super.put(formatFullPathToKey(fullPath), value);
    } catch (Exception e) {
      // should never happen
      logger.error("CfgGroup AddCfg Exception, exception = {}", e);
    }
  }

  /**
   * 删除配置.
   */
  public void deleteCfg(String fullPath) {
    checkStatus(fullPath, new Object());
    checkExist(fullPath);
    try {
      client.delete().forPath(fullPath);
      super.remove(formatFullPathToKey(fullPath));
    } catch (Exception e) {
      // should never happen
      logger.error("CfgGroup DeleteCfg Exception, exception = {}", e);
    }
  }

  private void checkExist(String fullPath) {
    Stat stat = null;
    try {
      stat = client.checkExists().forPath(fullPath);
    } catch (Exception e) {
      // should never happen
      logger.error("CfgGroup CheckExist Exception, exception = {}", e);
    }
    if (stat == null) {
      throw new RuntimeException(fullPath + " Does Not Exist.");
    }
  }

  private void checkStatus(String fullPath, Object value) {
    if (value == null) {
      throw new RuntimeException("Value To Set Can Not Be Null.");
    }
    if (StringUtils.isEmpty(cfgRootNodeName) || !fullPath.startsWith(cfgRootNodeName)) {
      throw new RuntimeException(
          "Permission Deny, " + fullPath + " Does Not Belong To This Group.");
    }
    if (client == null) {
      throw new RuntimeException("CfgGroup Disconnect From Zookeeper.");
    }
  }

  public boolean addCfgEventListener(CfgEventListener listener) {
    return listeners.add(listener);
  }

  public boolean removeCfgEventListener(CfgEventListener listener) {
    return listeners.remove(listener);
  }

  public void setInternalCfgGroup(CfgGroup internalCfgGroup) {
    this.internalCfgGroup = internalCfgGroup;
  }
}
