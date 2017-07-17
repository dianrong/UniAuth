package com.dianrong.common.uniauth.cas.listener;

import com.dianrong.common.uniauth.cas.helper.thread.RfreshCasCfgCacheRunnable;
import com.dianrong.common.uniauth.cas.helper.thread.SingleScheduledThreadPool;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import lombok.extern.slf4j.Slf4j;

/**
 * . tomcat启动时的监听listener
 *
 * @author wanglin
 */
@Slf4j
public class TomcatStartupListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    log.info("tomcat setup, and contextInitialized event invoked");
    // Tomcat启动, 启动起线程开始慢慢刷新缓存
    SingleScheduledThreadPool.INSTANCE
        .loadScheduledTask(new RfreshCasCfgCacheRunnable(sce.getServletContext()), 0L,
            AppConstants.CAS_CFG_CACHE_REFRESH_PERIOD_MILLES);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // 关闭线程池
    SingleScheduledThreadPool.INSTANCE.shutDownNow();
    log.info("tomcat shutdown, and contextDestroyed event invoked");
  }
}
