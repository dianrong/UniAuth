package com.dianrong.common.uniauth.cas.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.dianrong.common.uniauth.cas.helper.thread.DeadCircleThreadManager;
import com.dianrong.common.uniauth.cas.helper.thread.RfreshCasCfgCacheRunnable;
import com.dianrong.common.uniauth.cas.helper.thread.SingleThreadPool;

/**.
 * tomcat启动时的监听listener
 * @author wanglin
 *
 */
public class TomcatStartupListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//tomcat 启动  启动起线程开始慢慢刷新缓存
		SingleThreadPool.instance.loadTask(new RfreshCasCfgCacheRunnable(sce.getServletContext()));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//关闭所有的死循环线程
		DeadCircleThreadManager.stopAllThread();
		//关闭线程池
		SingleThreadPool.instance.shutDownNow();
	}
}
