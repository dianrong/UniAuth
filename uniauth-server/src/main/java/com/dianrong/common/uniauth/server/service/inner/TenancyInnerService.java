package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.service.cache.TenancyCache;
import com.dianrong.common.uniauth.server.util.UniBundle;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TenancyInnerService {

  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;

  @Autowired
  private TenancyCache tenancyCache;

  @Resource(name = "tenancyDataFilter")
  private DataFilter dataFilter;

  /**
   * 获取一个可用的租户id.<br>
   * 如果不能获取, 返回一个默认的非租户相关的租户id -1
   *
   * @return tenancyId for current request
   * @see AppConstants.TENANCY_UNRELATED_TENANCY_ID
   */
  public Long getOneCanUsedTenancyId() {
    return getTenancyId(false);
  }

  /**
   * 获取一个租户id，并且必须是有效值.
   *
   * @return tenancyId for current thread
   */
  public Long getTenancyIdWithCheck() {
    return getTenancyId(true);
  }

  /**
   * 获取一个可用的租户Identity 优先级为：tenancyId > tenancyCode.
   *
   * @return tenancyId for current request
   */
  private Long getTenancyId(boolean check) {
    Long tenancyId = (Long) CxfHeaderHolder.TENANCYID.get();
    if (tenancyId != null) {
      return tenancyId;
    }
    String tenancyCode = (String) CxfHeaderHolder.TENANCYCODE.get();
    if (StringUtils.hasText(tenancyCode)) {
      TenancyDto dto = tenancyCache.getEnableTenancyByCode(tenancyCode);
      if (dto != null) {
        return dto.getId();
      }
    }
    if (check) {
      if (checkTenancyIdentity()) {
        throw new AppException(InfoName.TENANCY_IDENTITY_REQUIRED,
            UniBundle.getMsg("common.parameter.tenancyidentity.required"));
      } else {
        return tenancyCache.getEnableTenancyByCode(AppConstants.DEFAULT_TANANCY_CODE).getId();
      }
    }
    return AppConstants.TENANCY_UNRELATED_TENANCY_ID;
  }

  /**
   * A switch to control check tenancyIdentity forcibly.
   *
   * @return true or false
   */
  private boolean checkTenancyIdentity() {
    return "true".equalsIgnoreCase(allZkNodeMap.get(AppConstants.CHECK_TENANCY_IDENTITY_FORCIBLY));
  }
}
