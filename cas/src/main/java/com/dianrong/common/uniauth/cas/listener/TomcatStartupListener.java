package com.dianrong.common.uniauth.cas.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianrong.common.uniauth.cas.helper.thread.RfreshCasCfgCacheRunnable;
import com.dianrong.common.uniauth.cas.helper.thread.SingleScheduledThreadPool;
import com.dianrong.common.uniauth.common.cons.AppConstants;

/**.
 * tomcat启动时的监听listener
 * @author wanglin
 *
 */
public class TomcatStartupListener implements ServletContextListener {
	/**.
	 * 日志对象
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("tomcat setup, and contextInitialized event invoked");
		//tomcat 启动  启动起线程开始慢慢刷新缓存
		SingleScheduledThreadPool.instance.loadScheduledTask(new RfreshCasCfgCacheRunnable(sce.getServletContext()), 0L, AppConstants.CAS_CFG_CACHE_REFRESH_PERIOD_MILLES);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//关闭线程池
		SingleScheduledThreadPool.instance.shutDownNow();
		logger.info("tomcat shutdown, and contextDestroyed event invoked");
	}
}
