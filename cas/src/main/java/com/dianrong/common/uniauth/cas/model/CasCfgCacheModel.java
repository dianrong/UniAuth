package com.dianrong.common.uniauth.cas.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianrong.common.uniauth.cas.util.UniBundle;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;

/**.
 * 用于缓存cas的个性化的一些设置参数
 * @author wanglin
 */
public class CasCfgCacheModel {
	/**.
	 * 日志对象
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final ConfigDto pageTitle;
	
	private final ConfigDto pageIcon;
	
	private final ConfigDto logo;
	
	private final ConfigDto loginPageImage;
	
	private final ConfigDto bottomAllRightText;
	
	private final ConfigDto backgroundColorText;
	
	/**.
	 * 该资源对象生成的时间戳
	 */
	private long getResourceMilles;
	
	/**.
	 * 构造函数
	 * @param pageTitle 页面 title
	 * @param pageIcon 页面的icon
	 * @param topImage 页面top的图片
	 * @param loginPageImage 登陆页面的图片
	 * @param bottomAllRightText 页面的allright字符串
	 * @param backgroundColorText 背景颜色字符串
	 */
	public CasCfgCacheModel(ConfigDto pageTitle, ConfigDto pageIcon, ConfigDto logo, ConfigDto loginPageImage, ConfigDto bottomAllRightText, ConfigDto backgroundColorText){
		if(pageTitle == null){
			logger.warn("page title text is null");
			pageTitle = new ConfigDto();
			pageTitle.setValue(UniBundle.getMsg("cas.cfg.cache.default.pagetitle"));
		}
		this.pageTitle = pageTitle;
		
		if(pageIcon == null) {
			logger.warn("page icon resourcess is null");
			throw new NullPointerException("page icon resourcess can not be null");
		}
		this.pageIcon = pageIcon;
		
		if(logo == null) {
			logger.warn("logo image resourcess is null");
			throw new NullPointerException("logo image resourcess can not be null");
		}
		this.logo = logo;
		
		if(loginPageImage== null){
			logger.warn("login page image resourcess is null");
			throw new NullPointerException("login page image resourcess can not be null");
		}
		this.loginPageImage = loginPageImage;
		
		if(bottomAllRightText == null){
			logger.warn("all right text is null");
			bottomAllRightText = new ConfigDto();
			bottomAllRightText.setValue(UniBundle.getMsg("cas.cfg.cache.default.allright"));
		}
		this.bottomAllRightText = bottomAllRightText;
		
		//背景色
		if(backgroundColorText == null){
			logger.warn("background color value is null");
			backgroundColorText = new ConfigDto();
			backgroundColorText.setValue("#153e50");
		}
		this.backgroundColorText = backgroundColorText;
		
		this.getResourceMilles = System.currentTimeMillis();
	}
	
	/**.
	 * 单独给三个图片的构造函数
	 * @param pageIcon 首页的icon
	 * @param topImage top页的图片
	 * @param loginPageImage 登陆页的图片
	 */
	public CasCfgCacheModel(ConfigDto pageIcon, ConfigDto topImage, ConfigDto loginPageImage){
		this(null, pageIcon, topImage, loginPageImage, null, null);
	}

	/**
	 * @return the pageTitle
	 */
	public ConfigDto getPageTitle() {
		return pageTitle;
	}

	/**
	 * @return the pageIcon
	 */
	public ConfigDto getPageIcon() {
		return pageIcon;
	}

	/**
	 * @return the loginPageImage
	 */
	public ConfigDto getLoginPageImage() {
		return loginPageImage;
	}

	/**
	 * @return the bottomAllRightText
	 */
	public ConfigDto getBottomAllRightText() {
		return bottomAllRightText;
	}

	/**
	 * @return the backgroundColorText
	 */
	public ConfigDto getBackgroundColorText() {
		return backgroundColorText;
	}

	/**
	 * @return the getResourceMilles
	 */
	public long getGetResourceMilles() {
		return getResourceMilles;
	}

	/**.
	 *  @return the logo
	 */
	public ConfigDto getLogo() {
		return logo;
	}
}
