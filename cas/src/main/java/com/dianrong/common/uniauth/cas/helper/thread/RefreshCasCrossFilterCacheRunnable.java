package com.dianrong.common.uniauth.cas.helper.thread;

import com.dianrong.common.uniauth.cas.helper.CasCrossFilterCacheHelper;

import lombok.extern.slf4j.Slf4j;

/**.
 * 用于异步刷新cas的cross filter的cache
 * @author wanglin
 */
@Slf4j
public final class RefreshCasCrossFilterCacheRunnable implements Runnable{
	
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
			log.warn(this.getClass().getName() + " exception :" + ex.getMessage());
		}
	}
}
