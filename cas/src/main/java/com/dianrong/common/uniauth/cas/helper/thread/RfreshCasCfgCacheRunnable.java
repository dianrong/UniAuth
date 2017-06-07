package com.dianrong.common.uniauth.cas.helper.thread;

import javax.servlet.ServletContext;

import com.dianrong.common.uniauth.cas.helper.CasCfgResourceRefreshHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * . 刷新cas cfg的线程
 *
 * @author wanglin
 */
@Slf4j
public final class RfreshCasCfgCacheRunnable implements Runnable {

  /**
   * . tomcat 上下文对象 用于在jsp中共享缓存
   */
  private ServletContext application;

  /**
   * . 更新缓存
   */
  public RfreshCasCfgCacheRunnable(ServletContext application) {
    this.application = application;
  }

  @Override
  public void run() {
    try {
      CasCfgResourceRefreshHelper.INSTANCE.refreshCache();
      // if(cacheModel != null){
      // //刷新缓存 不考虑线程安全的问题 因为只要是把最新的数据刷进去就OK
      // application.setAttribute(AppConstants.CAS_CFG_CACHE_MODEL_APPLICATION_KEY,
      // cacheModel);
      // }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
