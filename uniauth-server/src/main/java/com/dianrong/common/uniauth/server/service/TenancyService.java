package com.dianrong.common.uniauth.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.server.data.entity.Tenancy;
import com.dianrong.common.uniauth.server.data.entity.TenancyExample;
import com.dianrong.common.uniauth.server.data.mapper.TenancyMapper;
import com.dianrong.common.uniauth.server.exp.ParameterRequiredException;
import com.dianrong.common.uniauth.server.util.BeanConverter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TenancyService {
	@Autowired
	private TenancyMapper tenancyMapper;
	
	@Resource(name = "uniauthConfig")
    private Map<String, String> allZkNodeMap;

	/**
	 * . 默认使用的tenancy
	 */
	private volatile TenancyDto defaultTenancy;
	private  Object lock = new Object();

	public List<TenancyDto> getAllTenancy(Long id, String code, Byte status, String name, String contactName,
			String phone, String description) {
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

	// 初期的租户需要一个默认的租户，即点融网
	public TenancyDto getDefaultTenancy() {
		if (this.defaultTenancy == null) {
			synchronized (lock) {
				if (this.defaultTenancy == null) {
					this.defaultTenancy = _getDefaultTenancy();
				}
			}
		}
		return this.defaultTenancy;
	}

	/**.
	 * 获取一个可用的租户id
	 * @return tenancyId for current thread
	 */
	public Long getOneCanUsedTenancyId() {
		return getTenancyId(false);
	}
	
	/**.
     * 获取一个租户id，并且必须是有效值
     * @return tenancyId for current thread
     */
    public Long getTenancyIdWithCheck() {
        return getTenancyId(true);
    }
	
	   /**.
     * 获取一个可用的租户id
     * 优先级为：tenancyId -> tenancyCode
     * @return tenancyId for current thread
     */
    private Long getTenancyId(boolean check) {
        Long _id = (Long)CxfHeaderHolder.TENANCYID.get();
        if (_id != null) {
            return _id;
        }
        String tenancyCode = (String)CxfHeaderHolder.TENANCYCODE.get();
        TenancyDto dto = getEnableTenancyByCode(tenancyCode);
        if (dto != null) {
            return dto.getId();
        }
        if (check) {
            if (checkTenancyIdentity()) {
                throw new ParameterRequiredException("tenancyId or tenancyCode is required");
            } else {
                return getDefaultTenancy().getId();
            }
        }
        return AppConstants.TENANCY_UNRELATED_TENANCY_ID;
    }

	/**.
	 * 根据tenancyCode 查询 可用的租户信息
	 * @param tenancyCode tenancyCode, not null
	 * @return  
	 */
	public TenancyDto getEnableTenancyByCode(String tenancyCode) {
		if (!StringUtils.hasText(tenancyCode)) {
			return null;
		}
		TenancyExample example = new TenancyExample();
		TenancyExample.Criteria criteria = example.createCriteria();
		criteria.andCodeEqualTo(tenancyCode).andStatusEqualTo(AppConstants.STATUS_ENABLED);
		List<Tenancy> tenancyList = tenancyMapper.selectByExample(example);
		if (tenancyList != null && !tenancyList.isEmpty()) {
			return BeanConverter.convert(tenancyList.get(0));
		}
		return null;
	}

	private TenancyDto _getDefaultTenancy() {
		TenancyExample example = new TenancyExample();
		TenancyExample.Criteria criteria = example.createCriteria();
		criteria.andCodeEqualTo(AppConstants.DEFAULT_TANANCY_CODE).andStatusEqualTo(AppConstants.STATUS_ENABLED);
		List<Tenancy> tenancyList = tenancyMapper.selectByExample(example);
		if (tenancyList != null && !tenancyList.isEmpty()) {
			return BeanConverter.convert(tenancyList.get(0));
		}
		log.error("can not find default tenancy, the default tenancy code is " + AppConstants.DEFAULT_TANANCY_CODE);
		return null;
	}
	
	/**
	 * a switch to control check tenancyIdentity forcibly
	 * @return true or false
	 */
	private boolean checkTenancyIdentity(){
	    return "true".equalsIgnoreCase(allZkNodeMap.get(AppConstants.CHECK_TENANCY_IDENTITY_FORCIBLY));
	}
}