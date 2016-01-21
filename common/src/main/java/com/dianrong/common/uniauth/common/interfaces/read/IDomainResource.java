package com.dianrong.common.uniauth.common.interfaces.read;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;

@Path("domain")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IDomainResource {

    @GET
    @Path("login/alldomains")
    //scenario: domain drop down list for login
    Response<List<DomainDto>> getAllLoginDomains();
    
    @POST
    @Path("domaininfo")
    //scenario: get domain info and it's related stakeholders
    Response<DomainDto> getDomainInfo(PrimaryKeyParam primaryKeyParam);

}
