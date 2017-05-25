package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.server.data.entity.Tenancy;
import com.dianrong.common.uniauth.server.data.entity.TenancyExample;
import com.dianrong.common.uniauth.server.data.mapper.TenancyMapper;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.service.cache.TenancyCache;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.UniBundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TenancyService {
  @Autowired
  private TenancyMapper tenancyMapper;

  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;

  @Autowired
  private TenancyCache tenancyCache;

  /**
   * 根据条件获取租户信息.
   */
  public List<TenancyDto> getAllTenancy(Long id, String code, Byte status, String name,
      String contactName, String phone, String description) {
    TenancyExample example = new TenancyExample();
    TenancyExample.Criteria criteria = example.createCriteria();
    if (id != null) {
      criteria.andIdEqualTo(id);
    }
    if (code != null) {
      criteria.andCodeLike("%" + code + "%");
    }
    if (status != null) {
      criteria.andStatusEqualTo(status);
    }
    if (name != null) {
      criteria.andNameLike("%" + name + "%");
    }
    if (contactName != null) {
      criteria.andContactNameLike("%" + contactName + "%");
    }
    if (phone != null) {
      criteria.andPhoneLike("%" + phone + "%");
    }
    if (description != null) {
      criteria.andPhoneLike("%" + phone + "%");
    }
    List<Tenancy> tenancyList = tenancyMapper.selectByExample(example);
    List<TenancyDto> tenancyDtoList = new ArrayList<TenancyDto>();
    if (tenancyList != null) {
      for (Tenancy tenancy : tenancyList) {
        tenancyDtoList.add(BeanConverter.convert(tenancy));
      }
    }
    return tenancyDtoList;
  }

  /**
   * 获取一个可用的租户id. 如果不能获取, 返回一个默认的非租户相关的租户id -1
   * 
   * @return tenancyId for current request
   * @see AppConstants.TENANCY_UNRELATED_TENANCY_ID
   */
  public Long getOneCanUsedTenancyId() {
    return getTenancyId(false);
  }

  /**
   * . 获取一个租户id，并且必须是有效值
   * 
   * @return tenancyId for current thread
   */
  public Long getTenancyIdWithCheck() {
    return getTenancyId(true);
  }

  /**
   * 根据租户编码获取一个启用的租户.
   * 
   * @param tenancyCode 租户编码
   * @return 租户信息
   */
  public TenancyDto getEnableTenancyByCode(String tenancyCode) {
    return tenancyCache.getEnableTenancyByCode(tenancyCode);
  }

  /**
   * . 获取一个可用的租户id 优先级为：tenancyId -> tenancyCode
   * 
   * @return tenancyId for current thread
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
