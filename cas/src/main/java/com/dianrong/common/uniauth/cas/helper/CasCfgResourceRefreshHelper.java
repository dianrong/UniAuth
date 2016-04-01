package com.dianrong.common.uniauth.cas.helper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianrong.common.uniauth.cas.exp.ResetPasswordException;
import com.dianrong.common.uniauth.cas.model.CasCfgCacheModel;
import com.dianrong.common.uniauth.cas.service.CfgService;
import com.dianrong.common.uniauth.cas.util.SpringContextHolder;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;

/**.
 * cas的配置资源刷新辅助类
 * @author wanglin
 */
public final class CasCfgResourceRefreshHelper {
	/**.
	 * 日志对象
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**.
	 * singleton
	 */
	public static CasCfgResourceRefreshHelper instance = new CasCfgResourceRefreshHelper();
	
	/**.
	 * 整个jvm唯一的一份缓存数据对象
	 */
	private volatile CasCfgCacheModel casCfgCache;
	
	/**.
	 * 调用远程服务的service
	 */
	private CfgService cfgService;

	/**.
	 * 私有构造函数
	 */
	private CasCfgResourceRefreshHelper(){
		this.cfgService =  SpringContextHolder.getBean("cfgService");
	}
	
	/**.
	 * 刷新缓存//从远端获取最新的数据
	 * @throws ResetPasswordException 
	 */
	public void refreshCache() throws ResetPasswordException{
			List<ConfigDto> infoList = cfgService.queryConfigDtoByCfgKeys(getCfgKeyList());
			CasCfgCacheModel cacheObj = constructCacheModel(infoList);
			//刷新缓存
			this.casCfgCache = cacheObj;
	}
	
	/**.
	 * 组装缓存对象model
	 * @param infoList 获取的配置信息model 列表
	 * @return 缓存对象model
	 */
	private CasCfgCacheModel constructCacheModel(List<ConfigDto> infoList){
		if(infoList == null || infoList.isEmpty()) {
			logger.error("没有获取到cas的配置信息");
		}
		CasCfgCacheModel cacheModel = new CasCfgCacheModel(
				 getCfgModelFromList(infoList, AppConstants.CAS_CFG_KEY_TITLE),
				 getCfgModelFromList(infoList, AppConstants.CAS_CFG_KEY_ICON),
				 getCfgModelFromList(infoList, AppConstants.CAS_CFG_KEY_LOGO),
				 getCfgModelFromList(infoList, AppConstants.CAS_CFG_KEY_LOGIN_PAGE_IMG),
				 getCfgModelFromList(infoList, AppConstants.CAS_CFG_KEY_ALL_RIGHT),
				 getCfgModelFromList(infoList, AppConstants.CAS_CFG_KEY_BACKGROUND_COLOR)
				);
		return cacheModel;
	}
	
	/**.
	 * 从数据列表中获取缓存的对象
	 * @param infoList 数据数组
	 * @param cfgKey 对应的cfgKey
	 * @return 结果
	 */
	private ConfigDto getCfgModelFromList(List<ConfigDto> infoList, String cfgKey){
		for(ConfigDto tcfg: infoList){
			if(cfgKey.equals(tcfg.getCfgKey())){
				return tcfg;
			}
		}
		return null;
	}
	
	/**.
	 * 获取查询的cfgKey list
	 * @return list
	 */
	private List<String> getCfgKeyList(){
		List<String> cfgKeyList =new ArrayList<String>();
		cfgKeyList.add(AppConstants.CAS_CFG_KEY_LOGO);
		cfgKeyList.add(AppConstants.CAS_CFG_KEY_ICON);
		cfgKeyList.add(AppConstants.CAS_CFG_KEY_TITLE);
		cfgKeyList.add(AppConstants.CAS_CFG_KEY_ALL_RIGHT);
		cfgKeyList.add(AppConstants.CAS_CFG_KEY_LOGIN_PAGE_IMG);
		cfgKeyList.add(AppConstants.CAS_CFG_KEY_BACKGROUND_COLOR);
		return cfgKeyList;
	}
	
	/**.
	 * 获取缓存
	 * @return 结果
	 */
	public CasCfgCacheModel getCache(){
		return this.casCfgCache;
	}
	
	/**.
	 * 刷新缓存并返回缓存对象
	 * @return 缓存对象
	 * @throws ResetPasswordException 
	 */
	public CasCfgCacheModel refreshCacheAndGet() throws ResetPasswordException{
		refreshCache();
		return this.getCache();
	}
}
