package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.TagTypeDto;
import com.dianrong.common.uniauth.common.bean.request.TagQuery;
import com.dianrong.common.uniauth.common.bean.request.TagTypeQuery;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Arc on 7/4/2016.
 */
@Path("tag")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface ITagResource {

  @POST
  @Path("query")
  Response<PageDto<TagDto>> searchTags(TagQuery tagQuery);

  @POST
  @Path("tag-types")
  Response<List<TagTypeDto>> searchTagTypes(TagTypeQuery tagTypeQuery);
}
