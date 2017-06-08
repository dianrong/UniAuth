package com.dianrong.common.uniauth.server.service.cache;

import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Tenancy;
import com.dianrong.common.uniauth.server.data.entity.TenancyExample;
import com.dianrong.common.uniauth.server.data.mapper.TenancyMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.util.BeanConverter;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = {"tenancy"})
public class TenancyCache {

  @Autowired
  private TenancyMapper tenancyMapper;

  @Resource(name = "tenancyDataFilter")
  private DataFilter dataFilter;

  /**
   * 根据tenancyCode 查询 可用的租户信息.
   * 
   * @param tenancyCode tenancyCode, not null
   */
  @Cacheable(key = "#tenancyCode.toUpperCase()")
  public TenancyDto getEnableTenancyByCode(String tenancyCode) {
    TenancyExample example = new TenancyExample();
    TenancyExample.Criteria criteria = example.createCriteria();
    criteria.andCodeEqualTo(tenancyCode).andStatusEqualTo(AppConstants.STATUS_ENABLED);
    List<Tenancy> tenancyList = tenancyMapper.selectByExample(example);
    if (tenancyList != null && !tenancyList.isEmpty()) {
      return BeanConverter.convert(tenancyList.get(0));
    }
    return null;
  }

  /**
   * 更新租户的信息.
   * 
   * @param originalCode 用于更新缓存.
   */

  @Caching(evict = {@CacheEvict(key = "#originalCode.toUpperCase()"), @CacheEvict(
      key = "#code.toUpperCase()", condition = "#code != null and #code.trim().length() > 0")})
  public TenancyDto updateTenancy(String originalCode, Long id, String code, String name,
      String contactName, String phone, String description, Byte status) {
    Tenancy updateTenancy = new Tenancy();
    updateTenancy.setId(id);
    updateTenancy.setCode(code);
    updateTenancy.setName(name);
    updateTenancy.setContactName(contactName);
    updateTenancy.setPhone(phone);
    updateTenancy.setDescription(description);
    updateTenancy.setStatus(status);
    updateTenancy.setLastUpdate(new Date());
    tenancyMapper.updateByPrimaryKeySelective(updateTenancy);
    return BeanConverter.convert(updateTenancy);
  }
}
