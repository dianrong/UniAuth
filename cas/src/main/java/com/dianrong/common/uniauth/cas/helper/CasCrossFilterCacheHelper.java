package com.dianrong.common.uniauth.cas.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.PostConstruct;
import javax.servlet.FilterConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianrong.common.uniauth.cas.helper.thread.RefreshCasCrossFilterCacheRunnable;
import com.dianrong.common.uniauth.cas.helper.thread.SingleScheduledThreadPool;
import com.dianrong.common.uniauth.cas.model.CasCrossFilterCacheModel;
import com.dianrong.common.uniauth.cas.service.CfgService;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;

/**.
 * 用于辅助cas的cross filter获取缓存数据
 * ps. 目前只缓存orgin数据
 * @author wanglin
 */
public final class CasCrossFilterCacheHelper {
	/**.
	 * 日志对象
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**.
	 * 调用远程服务的service
	 */
	@Autowired
	private CfgService cfgService;
	
	/**.
	 * 缓存对象(保证该对象是否默认值的)
	 */
	private volatile CasCrossFilterCacheModel cacheModel;
	
	/**.
	 * 缓存orgin的正则表达式
	 */
	private volatile Set<Pattern> patternCache;
	
	
	/**
     * origins from {@link FilterConfig}.
     */
    private String defualt_cors_allowed_origins;
	
	/**.
	 * 获取origin列表的接口方法
	 * @return orgin 正则表达式列表
	 */
	public Set<String> getOriginCacheSet(){
		if(this.cacheModel == null) {
			throw new RuntimeException("CasCrossFilterCacheHelper init failed!!");
		}
		return this.cacheModel.getOrginRegular();
	}
	
	/**.
	 * 获取origin列表的接口方法
	 * @return orgin 正则表达式列表
	 */
	public Set<Pattern> getOriginRegularCacheSet(){
		return this.patternCache;
	}
	
	/**.
	 * init  设置缓存的初始值
	 */
	@PostConstruct
	private void init(){
		// 直接刷新缓存
		refreshCache();
		//开启异步刷新线程
		SingleScheduledThreadPool.instance.loadScheduledTask(new RefreshCasCrossFilterCacheRunnable(this), 
				AppConstants.CAS_CFG_CACHE_REFRESH_PERIOD_MILLES, 
				AppConstants.CAS_CFG_CACHE_REFRESH_PERIOD_MILLES);
	}
	
	/**.
	 *  刷新缓存
	 */
	public void refreshCache(){
		try {
			List<ConfigDto> cfges =  cfgService.queryConfigDtoByLikeCfgKeys(AppConstants.CAS_CFG_KEY_CROSS_FILTER_ORIGIN_PREFIX);
			if(cfges != null &&  !cfges.isEmpty()) {
				//刷新缓存
				List<String> regulars = new ArrayList<String>();
				for(ConfigDto cto: cfges) {
					if(AppConstants.CAS_CFG_TYPE_TEXT.equalsIgnoreCase(cto.getCfgType()) && !StringUtil.strIsNullOrEmpty(cto.getValue())){
						regulars.add(cto.getValue());
					}
				}
				this.cacheModel = new CasCrossFilterCacheModel(regulars);
			}
		} catch (Exception ex) {
			logger.warn("CasCrossFilterCacheHelper refresh cache exception", ex);
		}
		if(this.cacheModel == null) {
			if(!StringUtil.strIsNullOrEmpty(this.defualt_cors_allowed_origins)){
				//使用默认值
				this.cacheModel = new CasCrossFilterCacheModel(this.defualt_cors_allowed_origins);
			} else {
				//使用默认值
				this.cacheModel = new CasCrossFilterCacheModel();
			}
		}
		
		//refresh regular cache
		Set<Pattern> tempPattern = new HashSet<Pattern>();
		for(String torigin : this.cacheModel.getOrginRegular()) {
			try {
				Pattern tp = Pattern.compile(torigin);
				tempPattern.add(tp);
            } catch(PatternSyntaxException pse) {
                logger.error("invalid regular pattern : " + torigin);
            }
		}
		this.patternCache = Collections.unmodifiableSet(tempPattern);
	}

	/**
	 * @return the defualt_cors_allowed_origins
	 */
	public String getDefualt_cors_allowed_origins() {
		return defualt_cors_allowed_origins;
	}

	/**
	 * @param defualt_cors_allowed_origins the defualt_cors_allowed_origins to set
	 */
	public void setDefualt_cors_allowed_origins(String defualt_cors_allowed_origins) {
		this.defualt_cors_allowed_origins = defualt_cors_allowed_origins;
	}
}
