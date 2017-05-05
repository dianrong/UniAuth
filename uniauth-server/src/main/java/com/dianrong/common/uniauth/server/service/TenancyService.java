package com.dianrong.common.uniauth.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.data.entity.Tenancy;
import com.dianrong.common.uniauth.server.data.entity.TenancyExample;
import com.dianrong.common.uniauth.server.data.mapper.TenancyMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;

@Service
public class TenancyService {
    @Autowired
    private TenancyMapper tenancyMapper;

    @Resource(name = "uniauthConfig")
    private Map<String, String> allZkNodeMap;

    public List<TenancyDto> getAllTenancy(Long id, String code, Byte status, String name, String contactName, String phone, String description) {
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
     * . 根据tenancyCode 查询 可用的租户信息
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
