package com.dianrong.common.uniauth.server.service.cache;

import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.data.entity.Tenancy;
import com.dianrong.common.uniauth.server.data.entity.TenancyExample;
import com.dianrong.common.uniauth.server.data.mapper.TenancyMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class TenancyCache {

  @Autowired
  private TenancyMapper tenancyMapper;

  /**
   * 根据tenancyCode 查询 可用的租户信息.
   * 
   * @param tenancyCode tenancyCode, not null
   * @return 根据tenancyCode查找的租户信息
   */
  @Cacheable(value = "tenancy", key = "'tenancy:' + #tenancyCode")
  public TenancyDto getEnableTenancyByCode(String tenancyCode) {
    Assert.notNull(tenancyCode);
    TenancyExample example = new TenancyExample();
    TenancyExample.Criteria criteria = example.createCriteria();
    criteria.andCodeEqualTo(tenancyCode).andStatusEqualTo(AppConstants.STATUS_ENABLED);
    List<Tenancy> tenancyList = tenancyMapper.selectByExample(example);
    if (tenancyList != null && !tenancyList.isEmpty()) {
      return BeanConverter.convert(tenancyList.get(0));
    }
    return null;
  }
}
