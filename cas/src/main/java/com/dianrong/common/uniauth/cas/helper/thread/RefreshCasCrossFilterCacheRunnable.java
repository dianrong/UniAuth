package com.dianrong.common.uniauth.cas.helper.thread;

import com.dianrong.common.uniauth.cas.helper.CasCrossFilterCacheHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于异步刷新CAS的cross filter的cache.
 *
 * @author wanglin
 */
@Slf4j
public final class RefreshCasCrossFilterCacheRunnable implements Runnable {

  /**
   * 缓存刷新的helper.
   */
  private CasCrossFilterCacheHelper helper;

  public RefreshCasCrossFilterCacheRunnable(CasCrossFilterCacheHelper helper) {
    if (helper == null) {
      throw new NullPointerException();
    }
    this.helper = helper;
  }

  @Override
  public void run() {
    try {
      this.helper.refreshCache();
    } catch (Exception ex) {
      log.warn(this.getClass().getName() + " exception :" + ex.getMessage());
    }
  }
}
