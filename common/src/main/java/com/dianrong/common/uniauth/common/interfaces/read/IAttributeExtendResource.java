package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.AttributeExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.AttributeExtendPageParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 扩展属性读接口.
 */
@Path("attribute-extend")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IAttributeExtendResource {

  @POST
  @Path("search-attribute-extend")
  Response<PageDto<AttributeExtendDto>> searchAttributeExtend(AttributeExtendPageParam pageParam);
}

