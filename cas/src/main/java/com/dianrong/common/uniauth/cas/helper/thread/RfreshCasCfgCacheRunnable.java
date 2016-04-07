package com.dianrong.common.uniauth.cas.helper.thread;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianrong.common.uniauth.cas.helper.CasCfgResourceRefreshHelper;
import com.dianrong.common.uniauth.cas.model.CasCfgCacheModel;
import com.dianrong.common.uniauth.common.cons.AppConstants;

/**.
 * 刷新cas cfg的线程
 * @author wanglin
 */
public final class RfreshCasCfgCacheRunnable extends DeadCircleRunnable{
	/**.
	 * 日志对象
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**.
	 * tomcat 上下文对象 用于在jsp中共享缓存
	 */
	private ServletContext application;
	
	/**.
	 * 更新缓存
	 * @param application
	 */
	public RfreshCasCfgCacheRunnable(ServletContext application){
		this.application = application;
	}

	@Override
	protected void execut() {
		try {
			CasCfgCacheModel cacheModel = CasCfgResourceRefreshHelper.instance.refreshCacheAndGet();
			if(cacheModel != null){
				//刷新缓存  不考虑线程安全的问题  因为只要是把最新的数据刷进去就OK
				application.setAttribute(AppConstants.CAS_CFG_CACHE_MODEL_APPLICATION_KEY, cacheModel);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}
	
	@Override
	protected long sleepMilles() {
		return AppConstants.CAS_CFG_CACHE_REFRESH_PERIOD_MILLES;
	}
}
