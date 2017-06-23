package com.dianrong.common.uniauth.common.customer.basicauth.cache;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by denghb on 6/22/17.
 */
@Slf4j
public class CacheMapManager {

  public final long DEFAULT_EXPIRES_TIME_SECONDS = 5 * 60;

  private final int MAX_EXPIRES_TIME = 10 * 60 * 1000;

  private static Map<String, CacheMapVO> cacheMap = new ConcurrentHashMap<>(); // 缓存容器

  private volatile static CacheMapManager cacheMapManager; // 缓存实例对象

  private volatile boolean updateFlag = true;// 正在更新时的阀门，为false时表示当前没有更新缓存，为true时表示当前正在更新缓存

  private CacheMapManager() {

  }

  /**
   * 采用单例模式获取缓存对象实例
   */
  public static CacheMapManager getInstance() {
    if (null == cacheMapManager) {
      synchronized (CacheMapManager.class) {
        if (null == cacheMapManager) {
          cacheMapManager = new CacheMapManager();
        }
      }
    }
    return cacheMapManager;
  }

  public Object get(final String key) {
    if (this.updateFlag) {
      log.info("缓存正在更新,暂时不走缓存拿数据.");
      return null;
    }
    CacheMapVO cacheMapVO = cacheMap.get(key);
    long now = new Date().getTime();
    // 判断是否有这个值存在
    if (cacheMapVO == null) {
      return null;
      // 判断保存的值是否过期
    } else if (now >= cacheMapVO.getExpires()) {
      cacheMap.remove(key);
      return null;
    } else {
      return cacheMapVO.getValue();
    }
  }

  public void delete(final String key) {
    cacheMap.remove(key);
  }

  public void set(final String key, final Object value,
      final long seconds) {
    this.updateFlag = true;// 正在更新
    log.info("正在更新缓存...");
    // 增加值的工作
    CacheMapVO cacheMapVO = new CacheMapVO();
    cacheMapVO.setValue(value);
    long now = new Date().getTime();

    long expires = seconds * 1000;
    if (expires >= MAX_EXPIRES_TIME) {
      expires = MAX_EXPIRES_TIME;
    }
    expires += now;
    cacheMapVO.setExpires(expires);
    cacheMap.put(key, cacheMapVO);
    this.updateFlag = false;// 正在更新
    log.info("缓存更新完成.");
  }

  public void set(final String key, final Object value) {
    set(key, value, DEFAULT_EXPIRES_TIME_SECONDS);
  }

  public void clearCache() {
    cacheMap.clear();
  }
}
