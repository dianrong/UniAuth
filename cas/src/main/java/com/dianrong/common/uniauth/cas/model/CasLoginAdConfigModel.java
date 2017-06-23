package com.dianrong.common.uniauth.cas.model;

import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.security.InvalidParameterException;

/**
 * 专门用于Cas的首页登陆广告轮询的Config.
 *
 * @author wanglin
 */
public class CasLoginAdConfigModel extends ConfigDto {

  private static final long serialVersionUID = -3358258604250843024L;

  /**
   * . 广告跳转的url
   */
  private String hrefUrl;

  /**
   * . 字段判断是否有用正常合法的跳转url
   */
  private boolean hasValidHref;

  /**
   * . 设置url
   */
  public CasLoginAdConfigModel(ConfigDto imgConfig, String hrefUrl) {
    if (imgConfig == null) {
      throw new InvalidParameterException("imgConfig can not be null");
    }
    this.setCfgKey(imgConfig.getCfgKey()).setCfgType(imgConfig.getCfgType())
        .setCfgTypeId(imgConfig.getCfgTypeId()).setFile(imgConfig.getFile())
        .setId(imgConfig.getId())
        .setValue(imgConfig.getValue());

    this.hrefUrl = hrefUrl;

    if (!StringUtil.strIsNullOrEmpty(hrefUrl) && !hrefUrl.trim()
        .equals(AppConstants.CAS_CFG_HREF_DEFALT_VAL)) {
      this.hasValidHref = true;
    }
  }

  public String getHrefUrl() {
    return hrefUrl;
  }

  public boolean isHasValidHref() {
    return hasValidHref;
  }
}
