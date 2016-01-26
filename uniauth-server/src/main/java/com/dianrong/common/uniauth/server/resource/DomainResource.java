package com.dianrong.common.uniauth.server.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
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
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.UniBundle;

@RestController
public class DomainResource implements IDomainRWResource {

	@Autowired
	private DomainMapper domainMapper;
	@Autowired
	private StakeholderMapper stakeholderMapper;
	
	@Override
	public Response<List<DomainDto>> getAllLoginDomains() {
		DomainExample example = new DomainExample();
		example.createCriteria().andStatusEqualTo((byte)0);
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
		if(primaryKeyParam == null || primaryKeyParam.getId() == null){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", "域ID"));
		}
		Integer domainId = primaryKeyParam.getId();
		Domain domain = domainMapper.selectByPrimaryKey(domainId);
		if(domain.getStatus() == 1){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.entity.status.isone", String.valueOf(domainId), "域"));
		}
		
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
	public Response<StakeholderDto> addNewStakeholder(StakeholderParam stakeholderParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> updateStakeholder(StakeholderParam stakeholderParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> deleteStakeholder(PrimaryKeyParam primaryKeyParam) {
		// TODO Auto-generated method stub
		return null;
	}
}
