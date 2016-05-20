package com.dianrong.common.uniauth.cas.helper.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianrong.common.uniauth.cas.helper.CasCrossFilterCacheHelper;

/**.
 * 用于异步刷新cas的cross filter的cache
 * @author wanglin
 */
public final class RefreshCasCrossFilterCacheRunnable implements Runnable{
	/**.
	 * 日志对象
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**.
	 * 缓存刷新的heloer
	 */
	private CasCrossFilterCacheHelper helper;
	
	/**.
	 * 构造函数
	 * @param helper CasCrossFilterCacheHelper
	 */
	public RefreshCasCrossFilterCacheRunnable(CasCrossFilterCacheHelper helper){
		if(helper == null) {
			throw new NullPointerException();
		}
		this.helper = helper;
	}
	
	@Override
	public void run() {
		try {
			this.helper.refreshCache();
		} catch(Exception ex){
			logger.warn(this.getClass().getName() + " exception :" + ex.getMessage());
		}
	}
}
