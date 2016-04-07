package com.dianrong.common.uniauth.cas.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianrong.common.uniauth.cas.exp.ResetPasswordException;
import com.dianrong.common.uniauth.cas.model.CasCfgCacheModel;
import com.dianrong.common.uniauth.cas.model.CasLoginAdConfigModel;
import com.dianrong.common.uniauth.cas.service.CfgService;
import com.dianrong.common.uniauth.cas.util.FileUtil;
import com.dianrong.common.uniauth.cas.util.SpringContextHolder;
import com.dianrong.common.uniauth.cas.util.UniBundle;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;

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
	 * 缓存的图片cfg key 列表
	 */
	private static List<String> imgCacheCfgKeyList = new ArrayList<String>(){
		private static final long serialVersionUID = -2609958017102569295L;

		{
			add(AppConstants.CAS_CFG_KEY_LOGO);
			add(AppConstants.CAS_CFG_KEY_ICON);
			add(AppConstants.CAS_CFG_KEY_LOGIN_AD_IMG);
		}
	};
	
	/**.
	 * 缓存的文字的cfg key 列表
	 */
	private static List<String> textCacheCfgKeyList = new ArrayList<String>(){
		private static final long serialVersionUID = -8558828787817302839L;

		{
			add(AppConstants.CAS_CFG_KEY_TITLE);
			add(AppConstants.CAS_CFG_KEY_ALL_RIGHT);
			add(AppConstants.CAS_CFG_KEY_BACKGROUND_COLOR);
		}
	};
	
	/**.
	 * 所有的缓存的cfg key 列表
	 */
	private static List<String> allCacheCfgKeyList = new ArrayList<String>(){
		private static final long serialVersionUID = 1824871250479641994L;

		{
			addAll(imgCacheCfgKeyList);
			addAll(textCacheCfgKeyList);
		}
	};
	
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
		
		//设置默认缓存
		try{
			this.casCfgCache =  new CasCfgCacheModel(
					 getConfigDto(AppConstants.CAS_CFG_KEY_TITLE, UniBundle.getMsg("cas.cfg.cache.default.pagetitle")),
					 getConfigDto(AppConstants.CAS_CFG_KEY_ICON, AppConstants.CAS_CFG_KEY_ICON, FileUtil.readFiles(getRelativePath("favicon.ico"))),
					 getConfigDto(AppConstants.CAS_CFG_KEY_ICON, AppConstants.CAS_CFG_KEY_ICON, FileUtil.readFiles(getRelativePath("images", "logo.png"))),
					 getConfigDto(AppConstants.CAS_CFG_KEY_TITLE, UniBundle.getMsg("cas.cfg.cache.default.allright")),
					 getConfigDto(AppConstants.CAS_CFG_KEY_TITLE, "#153e50"),
					 Arrays.asList(new CasLoginAdConfigModel(getConfigDto(AppConstants.CAS_CFG_KEY_LOGIN_AD_IMG+"_1", 
								AppConstants.CAS_CFG_KEY_LOGIN_AD_IMG, FileUtil.readFiles(getRelativePath("images", "spring_festival.jpg"))), 
								AppConstants.CAS_CFG_HREF_DEFALT_VAL))
					);
		}catch(Exception ex){
			logger.error("构造cas定制化数据缓存异常:"+ex.getMessage());
		}
	}
	
	/**.
	 * 刷新缓存//从远端获取最新的数据
	 * @throws ResetPasswordException 
	 */
	public void refreshCache() throws Exception{
			final List<String> tempAllImgList  = new ArrayList<String>(allCacheCfgKeyList);
			//去掉登陆首页的滚动img
			tempAllImgList.remove(AppConstants.CAS_CFG_KEY_LOGIN_AD_IMG);
			List<ConfigDto> standardCaches = cfgService.queryConfigDtoByCfgKeys(tempAllImgList);
			List<ConfigDto> loginImages = cfgService.queryConfigDtoByLikeCfgKeys(AppConstants.CAS_CFG_KEY_LOGIN_AD_IMG);
			CasCfgCacheModel cacheObj = constructCacheModel(standardCaches, loginImages);
			
			if(cacheObj != null){
				//刷新缓存
				this.casCfgCache = cacheObj;
			}
	}
	
	/**.
	 * 组装缓存对象model
	 * @param standardCaches 标准的属性
	 * @param loginImages 首页滚动的图表列表
	 * @return 缓存对象model
	 */
	private CasCfgCacheModel constructCacheModel(List<ConfigDto> standardCaches, List<ConfigDto> loginImages){
		if(standardCaches == null || standardCaches.isEmpty()) {
			logger.info("没有获取到cas的配置信息");
		}
		if(loginImages == null || loginImages.isEmpty()) {
			logger.info("没有获取到首页滚动的配置信息");
		}
		try{
		CasCfgCacheModel cacheModel = new CasCfgCacheModel(
				 getCfgModelFromList(standardCaches, AppConstants.CAS_CFG_KEY_TITLE, getConfigDto(AppConstants.CAS_CFG_KEY_TITLE, UniBundle.getMsg("cas.cfg.cache.default.pagetitle"))),
				 getCfgModelFromList(standardCaches, AppConstants.CAS_CFG_KEY_ICON, getConfigDto(AppConstants.CAS_CFG_KEY_ICON, AppConstants.CAS_CFG_KEY_ICON, FileUtil.readFiles(getRelativePath("favicon.ico")))),
				 getCfgModelFromList(standardCaches, AppConstants.CAS_CFG_KEY_LOGO, getConfigDto(AppConstants.CAS_CFG_KEY_ICON, AppConstants.CAS_CFG_KEY_ICON, FileUtil.readFiles(getRelativePath("images", "logo.png")))),
				 getCfgModelFromList(standardCaches, AppConstants.CAS_CFG_KEY_ALL_RIGHT, getConfigDto(AppConstants.CAS_CFG_KEY_TITLE, UniBundle.getMsg("cas.cfg.cache.default.allright"))),
				 getCfgModelFromList(standardCaches, AppConstants.CAS_CFG_KEY_BACKGROUND_COLOR, getConfigDto(AppConstants.CAS_CFG_KEY_TITLE, "#153e50")),
				 getLoginImges(loginImages)
				);
		return cacheModel;
		}catch(Exception ex){
			logger.error("构造cas定制化数据缓存异常:"+ex.getMessage());
		}
		return null;
	}
	
	/**.
	 * 获取首页滚动图片的列表
	 * @param loginImages 调服务查询到的数据
	 * @return 滚动图片列表
	 * @throws IOException 
	 */
	private List<CasLoginAdConfigModel> getLoginImges(List<ConfigDto> loginImages) throws IOException{
		List<CasLoginAdConfigModel> tdlist = getAdCfgModelList(loginImages, this.casCfgCache == null? null: this.casCfgCache.getLoginPageAd()) ;
		
		if(tdlist == null || tdlist.isEmpty()){
			tdlist =  Arrays.asList(new CasLoginAdConfigModel(getConfigDto(AppConstants.CAS_CFG_KEY_LOGIN_AD_IMG+"_1", 
					AppConstants.CAS_CFG_KEY_LOGIN_AD_IMG, FileUtil.readFiles(getRelativePath("images", "spring_festival.jpg"))), 
					AppConstants.CAS_CFG_HREF_DEFALT_VAL));
		} 
		//按照cfg-key自然排序
		Collections.sort(tdlist, new Comparator<CasLoginAdConfigModel>(){
			@Override
			public int compare(CasLoginAdConfigModel o1, CasLoginAdConfigModel o2) {
				if(o1 == null && o2 == null) {
					return 0;
				}
				if(o1 == null){
					return -1;
				}
				if(o2 == null) {
					return 1;
				}
				return o1.getCfgKey().compareTo(o2.getCfgKey());
			}
		});
		return tdlist;
	}
	
	/**.
	 * 获取字符串类型的配置对象
	 * @param cfgType 配置的类型
	 * @param textValue 配置的属性
	 * @return 结果
	 */
	private ConfigDto getConfigDto(String cfgKey, String value){
		return new ConfigDto().setCfgKey(cfgKey).setValue(value);
	}
	
	/**.
	 * 获取字符串类型的配置对象
	 * @param cfgType 配置的类型
	 * @param textValue 配置的属性
	 * @return 结果
	 */
	private ConfigDto getConfigDto(String cfgKey, String value, byte[] file){
		return new ConfigDto().setCfgKey(cfgKey).setValue(value).setFile(file);
	}
	
	/**.
	 * 获取一个与操作系统无关的文件路径
	 * @param filenames 路径上的文件夹名
	 * @return 结果
	 */
	private String getRelativePath(String... filenames){
		StringBuilder sb = new StringBuilder();
		if(filenames == null || filenames.length == 0){
			return "";
		}
		for(int i = 0; i < filenames.length; i++){
			if(sb.toString().length() > 0){
				sb.append(File.separator);
			}
			sb.append(filenames[i]);
		}
		return sb.toString();
	}
	
	/**.
	 * 从数据列表中获取缓存的对象
	 * @param infoList 数据数组
	 * @param cfgKey 对应的cfgKey
	 * @param defaultDto 默认结果
	 * @return 结果
	 */
	private ConfigDto getCfgModelFromList(List<ConfigDto> infoList, String cfgKey, ConfigDto defaultDto){
		if(infoList == null || infoList.isEmpty()) {
			return defaultDto;
		}
		for(ConfigDto tcfg: infoList){
			if(cfgKey.equals(tcfg.getCfgKey())){
				return tcfg;
			}
		}
		return defaultDto;
	}
	
	/**.
	 * 从数据列表中获取缓存的对象
	 * @param addConfigList 广告数据的配置信息数组
	 * @param defaultDtos 默认返回结果
	 * @return 结果
	 */
	private List<CasLoginAdConfigModel> getAdCfgModelList(List<ConfigDto> adConfigList, List<CasLoginAdConfigModel> defaultDtos){
		if(adConfigList == null || adConfigList.isEmpty()) {
			//使用默认的数据
			return defaultDtos;
		}
		
		//存储广告跳转url的map
		Map<String, ConfigDto> adHrefUrlConfigMap = new HashMap<String, ConfigDto>();
		for(ConfigDto cfd: adConfigList){
			if(cfd.getCfgTypeId() != null && cfd.getCfgTypeId() == AppConstants.CAS_CFG_TYPE_TEXT_ID && !StringUtil.strIsNullOrEmpty(cfd.getCfgKey())){
				adHrefUrlConfigMap.put(cfd.getCfgKey().trim(), cfd);
			}
		}
		
		//从列表中获取所有的轮询广告的图片
		List<CasLoginAdConfigModel> newAdCfgList = new ArrayList<CasLoginAdConfigModel>();
		for(ConfigDto cfd: adConfigList){
			if(cfd.getCfgTypeId() != null && cfd.getCfgTypeId() == AppConstants.CAS_CFG_TYPE_FILE_ID && !StringUtil.strIsNullOrEmpty(cfd.getCfgKey())){
				//获取对应的正常跳转href url
				String hrefCfgKey = cfd.getCfgKey().trim() + AppConstants.CAS_CFG_LOGIN_AD_HREF_SUFFIX;
				String hrefUrl = adHrefUrlConfigMap.get(hrefCfgKey) == null?AppConstants.CAS_CFG_HREF_DEFALT_VAL: adHrefUrlConfigMap.get(hrefCfgKey).getValue();
				newAdCfgList.add(new CasLoginAdConfigModel(cfd, hrefUrl));
			}
		}
		return newAdCfgList;
	}
	
	/**.
	 * 获取缓存
	 * @return 结果
	 */
	public CasCfgCacheModel getCache(){
		return this.casCfgCache;
	}
	
	/**.
	 * 从缓存中获取图片的dto
	 * @param cacheDtoType  缓存图片的类型
	 * @return 缓存的model
	 */
	public ConfigDto getImageCacheDto(String cacheDtoType){
		if(StringUtil.strIsNullOrEmpty(cacheDtoType)){
			return null;
		}
		ConfigDto dto = this.casCfgCache.getDtoByCfgKey(cacheDtoType);
		
		if(dto != null){
			return dto;
		}

		//从广告里面去查找
		List<CasLoginAdConfigModel>  loginAds = this.casCfgCache.getLoginPageAd();
		if(loginAds != null && !loginAds.isEmpty()){
			for(ConfigDto tloginimg : loginAds){
				if(cacheDtoType.equals(tloginimg.getCfgKey())){
					return tloginimg;
				}
			}
		}
		return null;
	}
	
	/**.
	 * 刷新缓存并返回缓存对象
	 * @return 缓存对象
	 * @throws ResetPasswordException 
	 */
	public CasCfgCacheModel refreshCacheAndGet() throws Exception{
		refreshCache();
		return this.getCache();
	}
}
