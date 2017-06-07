package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.bean.request.TenancyParam;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("tenancy")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface ITenancyResource {

  @POST
  @Path("query")
  Response<List<TenancyDto>> searchTenancy(TenancyParam tenancyParam);

  @POST
  @Path("queryDefault")
  Response<TenancyDto> queryDefaultTenancy();

  @POST
  @Path("query-enable-tenancy-by-code")
  Response<TenancyDto> queryEnableTenancyByCode(TenancyParam param);
}
