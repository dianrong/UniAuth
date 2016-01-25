package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.common.interfaces.read.IDomainResource;
import com.dianrong.common.uniauth.common.interfaces.rw.IDomainRWResource;

@RestController
public class DomainResource implements IDomainRWResource {

	@Override
	public Response<List<DomainDto>> getAllLoginDomains() {
		// TODO Auto-generated method stub
		return null;
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
