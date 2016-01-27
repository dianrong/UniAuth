package com.dianrong.common.uniauth.server.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.common.interfaces.rw.IDomainRWResource;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.entity.DomainExample;
import com.dianrong.common.uniauth.server.data.entity.Stakeholder;
import com.dianrong.common.uniauth.server.data.entity.StakeholderExample;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.data.mapper.StakeholderMapper;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.AppConstants;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;

@RestController
public class DomainResource implements IDomainRWResource {

	@Autowired
	private DomainMapper domainMapper;
	@Autowired
	private StakeholderMapper stakeholderMapper;
	
	@Override
	public Response<DomainDto> addNewDomain(DomainParam domainParam) {
		String domainCode = domainParam.getCode();
		CheckEmpty.checkEmpty(domainCode, "域编码");
		DomainExample example = new DomainExample();
		example.createCriteria().andCodeEqualTo(domainCode);
		List<Domain> domainList = domainMapper.selectByExample(example);
		if(domainList == null || domainList.isEmpty()){
			Domain param = BeanConverter.convert(domainParam);
			domainMapper.insert(param);
			return new Response<DomainDto>(BeanConverter.convert(param));
		}
		else{
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.entity.code.duplicate", domainCode, "域"));
		}
	}

	@Override
	public Response<List<DomainDto>> getAllLoginDomains() {
		DomainExample example = new DomainExample();
		example.createCriteria().andStatusEqualTo(AppConstants.ZERO_Byte);
		List<Domain> domainList = domainMapper.selectByExample(example);
		List<DomainDto> domainDtoList = new ArrayList<DomainDto>();
		if(domainList != null){
			for(Domain domain : domainList){
				domainDtoList.add(BeanConverter.convert(domain));
			}
		}
		
		return new Response<List<DomainDto>>(domainDtoList);
	}

	@Override
	public Response<DomainDto> getDomainInfo(PrimaryKeyParam primaryKeyParam) {
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
		
		return new Response<DomainDto>(domainDto);
	}

	@Override
	public Response<Void> updateDomain(DomainParam domainParam) {
		if(domainParam == null || domainParam.getId() == null){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", "域ID"));
		}
		Domain param = BeanConverter.convert(domainParam);
		domainMapper.updateByPrimaryKey(param);
		
		return Response.success();
	}

	@Override
	public Response<StakeholderDto> addNewStakeholder(StakeholderParam stakeholderParam) {
		Integer domainId = stakeholderParam.getDomainId();
		checkDomain(domainId);
		Stakeholder stakeholder = BeanConverter.convert(stakeholderParam,false);
		stakeholderMapper.insert(stakeholder);
		StakeholderDto stakeholderDto = BeanConverter.convert(stakeholder);
		
		return new Response<StakeholderDto>(stakeholderDto);
	}

	@Override
	public Response<Void> updateStakeholder(StakeholderParam stakeholderParam) {
		if(stakeholderParam == null || stakeholderParam.getId() == null){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", "域相关人ID"));
		}
		
		Stakeholder param = BeanConverter.convert(stakeholderParam,true);
		stakeholderMapper.updateByPrimaryKey(param);
		
		return Response.success(); 
	}

	@Override
	public Response<Void> deleteStakeholder(PrimaryKeyParam primaryKeyParam) {
		CheckEmpty.checkParamId(primaryKeyParam, "域相关人ID");
		stakeholderMapper.deleteByPrimaryKey(primaryKeyParam.getId());
		return  Response.success();
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
