package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("domain")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IDomainResource {

  @POST
  @Path("login/alldomains")
  // scenario: domain drop down list for login
  Response<List<DomainDto>> getAllLoginDomains(DomainParam domainParam);

  @POST
  @Path("domaininfo")
  // scenario: get domain info and it's related stakeholders
  Response<DomainDto> getDomainInfo(PrimaryKeyParam primaryKeyParam);

  @POST
  @Path("query")
  Response<PageDto<DomainDto>> searchDomain(DomainParam domainParam);

  @POST
  @Path("stakeholder/query")
  Response<List<StakeholderDto>> getAllStakeholdersInDomain(DomainParam domainParam);
}
