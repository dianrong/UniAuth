package com.dianrong.common.uniauth.cas.model;

import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.io.Serializable;
import java.util.List;
import org.springframework.util.Assert;

/**
 * 用于缓存Cas的个性化的一些设置参数.
 *
 * @author wanglin
 */
public class CasCfgCacheModel implements Serializable {

  private static final long serialVersionUID = -5416502260004426583L;

  private final ConfigDto pageTitle;

  private final ConfigDto pageIcon;

  private final ConfigDto logo;

  private final ConfigDto bottomAllRightText;

  private final ConfigDto backgroundColorText;

  /**
   * 登陆首页的图片滚动定制化.
   */
  private final List<CasLoginAdConfigModel> loginPageAd;

  /**
   * 该资源对象生成的时间戳.
   */
  private long getResourceMilles;

  /**
   * 构造函数.
   *
   * @param pageTitle 页面 title
   * @param pageIcon 页面的icon
   * @param bottomAllRightText 页面的allright字符串
   * @param backgroundColorText 背景颜色字符串
   */
  public CasCfgCacheModel(ConfigDto pageTitle, ConfigDto pageIcon, ConfigDto logo,
      ConfigDto bottomAllRightText, ConfigDto backgroundColorText,
      List<CasLoginAdConfigModel> loginPageAd) {
    this.pageTitle = pageTitle;
    this.pageIcon = pageIcon;
    this.logo = logo;
    this.bottomAllRightText = bottomAllRightText;
    this.backgroundColorText = backgroundColorText;
    this.loginPageAd = loginPageAd;
    this.getResourceMilles = System.currentTimeMillis();
  }


  /**
   * 根据Cfg key 获取对应的dto list.
   * @param cfgKey cfg key
   */
  public List<CasLoginAdConfigModel> getDtoListByCfgKey(String cfgKey) {
    if (StringUtil.strIsNullOrEmpty(cfgKey)) {
      throw new NullPointerException();
    }
    switch (cfgKey.trim()) {
      case AppConstants.CAS_CFG_KEY_LOGIN_AD_IMG:
        return this.loginPageAd;
      default:
        return null;
    }
  }

  /**
   * 根据cfg key 获取对应的dto.
   * @param cfgKey cfg key
   */
  public ConfigDto getDtoByCfgKey(String cfgKey) {
    if (StringUtil.strIsNullOrEmpty(cfgKey)) {
      throw new NullPointerException();
    }
    switch (cfgKey.trim()) {
      case AppConstants.CAS_CFG_KEY_LOGO:
        return this.logo;
      case AppConstants.CAS_CFG_KEY_ICON:
        return this.pageIcon;
      case AppConstants.CAS_CFG_KEY_TITLE:
        return this.pageTitle;
      case AppConstants.CAS_CFG_KEY_ALL_RIGHT:
        return this.bottomAllRightText;
      case AppConstants.CAS_CFG_KEY_BACKGROUND_COLOR:
        return this.backgroundColorText;
      default:
        return null;
    }
  }

  /**
   * Return the pageTitle.
   */
  public ConfigDto getPageTitle() {
    return pageTitle;
  }

  /**
   * Return the pageIcon.
   */
  public ConfigDto getPageIcon() {
    return pageIcon;
  }

  /**
   * Return the bottomAllRightText.
   */
  public ConfigDto getBottomAllRightText() {
    return bottomAllRightText;
  }

  /**
   * Return the backgroundColorText.
   */
  public ConfigDto getBackgroundColorText() {
    return backgroundColorText;
  }

  /**
   * Return the getResourceMilles.
   */
  public long getGetResourceMilles() {
    return getResourceMilles;
  }

  /**
   * Return the logo.
   */
  public ConfigDto getLogo() {
    return logo;
  }


  public List<CasLoginAdConfigModel> getLoginPageAd() {
    return loginPageAd;
  }

  /**
   * 根据模板生成一个新的CasCfgCacheModel.
   */
  public static CasCfgCacheModel buildNewInstance(CasCfgCacheModel model) {
    Assert.notNull(model, "buildNewInstance, CasCfgCacheModel can not be null");
    CasCfgCacheModel newModel = new CasCfgCacheModel(model.getPageTitle(), model.getPageIcon(),
        model.getLogo(), model.getBottomAllRightText(), model.getBackgroundColorText(),
        model.getLoginPageAd());
    return newModel;
  }
}
