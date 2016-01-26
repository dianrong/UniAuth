package com.dianrong.common.uniauth.server.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.common.interfaces.rw.IDomainRWResource;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.entity.DomainExample;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;

@RestController
public class DomainResource implements IDomainRWResource {

	@Autowired
	private DomainMapper domainMapper;
	
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
		// TODO Auto-generated method stub
		return null;
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
