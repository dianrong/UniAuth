package com.dianrong.common.uniauth.server.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Tenancy;
import com.dianrong.common.uniauth.server.data.entity.TenancyExample;
import com.dianrong.common.uniauth.server.data.mapper.TenancyMapper;
import com.dianrong.common.uniauth.server.support.CxfHeaderHolder;
import com.dianrong.common.uniauth.server.util.BeanConverter;

@Service
public class TenancyService {
	private static Logger logger = LoggerFactory.getLogger(TenancyService.class);

	@Autowired
	private TenancyMapper tenancyMapper;

	/**
	 * . 默认使用的tenancy
	 */
	private volatile TenancyDto defaultTenancy;
	private Object lock = new Object();

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
	 * 优先级为：tenancyId -> tenancyCode - > defaultTenancyId
	 * @return
	 */
	public Long getOneCanUsedTenancyId() {
		Long _id = (Long)CxfHeaderHolder.TENANCYID.get();
		if (_id != null) {
			return _id;
		}
		String tenancyCode = (String)CxfHeaderHolder.TENANCYCODE.get();
		TenancyDto dto = getEnableTenancyByCode(tenancyCode);
		if (dto != null) {
			return dto.getId();
		}
		return getDefaultTenancy().getId();
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
		logger.error("can not find default tenancy, the default tenancy code is " + AppConstants.DEFAULT_TANANCY_CODE);
		return null;
	}
}