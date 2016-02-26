package com.dianrong.common.uniauth.server.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.entity.DomainExample;
import com.dianrong.common.uniauth.server.data.entity.Stakeholder;
import com.dianrong.common.uniauth.server.data.entity.StakeholderExample;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.data.mapper.StakeholderMapper;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;

@Service
public class DomainService {

	@Autowired
	private DomainMapper domainMapper;
	@Autowired
	private StakeholderMapper stakeholderMapper;
	
	@Transactional
	public DomainDto addNewDomain(DomainParam domainParam) {
		String domainCode = domainParam.getCode();
		CheckEmpty.checkEmpty(domainCode, "域编码");
		DomainExample example = new DomainExample();
		example.createCriteria().andCodeEqualTo(domainCode);
		List<Domain> domainList = domainMapper.selectByExample(example);
		if(domainList == null || domainList.isEmpty()){
			Domain param = BeanConverter.convert(domainParam);
			domainMapper.insert(param);
			return BeanConverter.convert(param);
		}
		else{
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.entity.code.duplicate", domainCode, "域"));
		}
	}

	public List<DomainDto> getAllLoginDomains(DomainParam domainParam) {
		List<String> domainCodeList = domainParam.getDomainCodeList();
		CheckEmpty.checkEmpty(domainCodeList, "请求的域编码列表");
		DomainExample example = new DomainExample();
		example.createCriteria().andStatusEqualTo(AppConstants.ZERO_Byte).andCodeIn(domainCodeList);
		
		List<Domain> domainList = domainMapper.selectByExample(example);
		List<DomainDto> domainDtoList = new ArrayList<DomainDto>();
		if(domainList != null){
			for(Domain domain : domainList){
				domainDtoList.add(BeanConverter.convert(domain));
			}
		}
		
		return domainDtoList;
	}

	public DomainDto getDomainInfo(PrimaryKeyParam primaryKeyParam) {
		CheckEmpty.checkParamId(primaryKeyParam, "域ID");
		Integer domainId = primaryKeyParam.getId();
		Domain domain = checkDomain(domainId);
		
		StakeholderExample stakeholderExample = new StakeholderExample();
		stakeholderExample.createCriteria().andDomainIdEqualTo(domainId);
		List<Stakeholder> stakeHolderList = stakeholderMapper.selectByExample(stakeholderExample);
		List<StakeholderDto> stakeholderDtoList = new ArrayList<StakeholderDto>();
		for(Stakeholder stakeholder : stakeHolderList){
			stakeholderDtoList.add(BeanConverter.convert(stakeholder));
		}
		
		DomainDto domainDto = BeanConverter.convert(domain);
		domainDto.setStakeholderList(stakeholderDtoList);
		
		return domainDto;
	}

	@Transactional
	public void updateDomain(DomainParam domainParam) {
		if(domainParam == null || domainParam.getId() == null){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", "域ID"));
		}
		Domain param = BeanConverter.convert(domainParam);
		domainMapper.updateByPrimaryKey(param);
	}

	@Transactional
	public StakeholderDto addNewStakeholder(StakeholderParam stakeholderParam) {
		Integer domainId = stakeholderParam.getDomainId();
		checkDomain(domainId);
		Stakeholder stakeholder = BeanConverter.convert(stakeholderParam,false);
		stakeholderMapper.insert(stakeholder);
		StakeholderDto stakeholderDto = BeanConverter.convert(stakeholder);
		
		return stakeholderDto;
	}

	@Transactional
	public void updateStakeholder(StakeholderParam stakeholderParam) {
		if(stakeholderParam == null || stakeholderParam.getId() == null){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", "域相关人ID"));
		}
		
		Stakeholder param = BeanConverter.convert(stakeholderParam,true);
		stakeholderMapper.updateByPrimaryKey(param);
	}

	@Transactional
	public void deleteStakeholder(PrimaryKeyParam primaryKeyParam) {
		CheckEmpty.checkParamId(primaryKeyParam, "域相关人ID");
		stakeholderMapper.deleteByPrimaryKey(primaryKeyParam.getId());
	}
	
	private Domain checkDomain(Integer domainId){
		Domain domain = domainMapper.selectByPrimaryKey(domainId);
		if(domain == null){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.entity.notfound", String.valueOf(domainId), "域"));
		}
		if(domain.getStatus() == 1){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.entity.status.isone", String.valueOf(domainId), "域"));
		}
		return domain;
	}

}
