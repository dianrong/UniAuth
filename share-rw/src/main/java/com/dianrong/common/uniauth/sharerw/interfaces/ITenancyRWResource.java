package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.bean.request.TenancyParam;
import com.dianrong.common.uniauth.common.interfaces.read.ITenancyResource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

public interface ITenancyRWResource extends ITenancyResource {

  @POST
  @Path("add-tenancy")
  // scenario: add new tenancy
  Response<TenancyDto> addTenancy(TenancyParam tenancyParam);

  @POST
  @Path("update-tenancy")
  // scenario: update tenancy
  Response<TenancyDto> updateTenancy(TenancyParam tenancyParam);
}
