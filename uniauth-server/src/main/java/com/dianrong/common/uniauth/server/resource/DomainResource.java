package com.dianrong.common.uniauth.server.resource;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.server.service.DomainService;
import com.dianrong.common.uniauth.sharerw.interfaces.IDomainRWResource;
import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@Api("域操作接口")
@RestController
public class DomainResource implements IDomainRWResource {

  @Autowired
  private DomainService domainService;

  @Override
  @Timed
  public Response<List<StakeholderDto>> getAllStakeholdersInDomain(DomainParam domainParam) {
    List<StakeholderDto> stakeholderDtos =
        domainService.getAllStakeholdersInDomain(domainParam.getId());
    return Response.success(stakeholderDtos);
  }

  @Override
  @Timed
  public Response<PageDto<DomainDto>> searchDomain(DomainParam domainParam) {
    PageDto<DomainDto> pageDto =
        domainService.searchDomain(domainParam.getDomainIds(), domainParam.getId(),
            domainParam.getCode(), domainParam.getDisplayName(), domainParam.getStatus(),
            domainParam.getDescription(), domainParam.getPageNumber(), domainParam.getPageSize());
    return Response.success(pageDto);
  }

  @Override
  @Timed
  public Response<List<DomainDto>> getAllLoginDomains(DomainParam domainParam) {
    List<DomainDto> domainDtoList = domainService.getAllLoginDomains(domainParam);
    return new Response<List<DomainDto>>(domainDtoList);
  }

  @Override
  @Timed
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
