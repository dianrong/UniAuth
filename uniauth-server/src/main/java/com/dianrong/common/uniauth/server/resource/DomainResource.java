package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import com.dianrong.uniauth.rw.interfaces.IDomainRWResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.server.service.DomainService;

@RestController
public class DomainResource implements IDomainRWResource {

	@Autowired
	private DomainService domainService;
	
	@Override
	public Response<List<DomainDto>> getAllLoginDomains() {
		List<DomainDto> domainDtoList = domainService.getAllLoginDomains();
		return new Response<List<DomainDto>>(domainDtoList);
	}

	@Override
	public Response<DomainDto> getDomainInfo(PrimaryKeyParam primaryKeyParam) {
		DomainDto domainDto = domainService.getDomainInfo(primaryKeyParam);
		return new Response<DomainDto>(domainDto);
	}

	@Override
	public Response<DomainDto> addNewDomain(DomainParam domainParam) {
		DomainDto domainDto = domainService.addNewDomain(domainParam);
		return new Response<DomainDto>(domainDto);
	}

	@Override
	public Response<Void> updateDomain(DomainParam domainParam) {
		domainService.updateDomain(domainParam);
		return Response.success();
	}

	@Override
	public Response<StakeholderDto> addNewStakeholder(StakeholderParam stakeholderParam) {
		StakeholderDto stakeholderDto = domainService.addNewStakeholder(stakeholderParam);
		return new Response<StakeholderDto>(stakeholderDto);
	}

	@Override
	public Response<Void> updateStakeholder(StakeholderParam stakeholderParam) {
		domainService.updateStakeholder(stakeholderParam);
		return Response.success();
	}

	@Override
	public Response<Void> deleteStakeholder(PrimaryKeyParam primaryKeyParam) {
		domainService.deleteStakeholder(primaryKeyParam);
		return Response.success();
	}
}
