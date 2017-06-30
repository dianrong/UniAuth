package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.TagTypeDto;
import com.dianrong.common.uniauth.common.bean.request.TagParam;
import com.dianrong.common.uniauth.common.bean.request.TagTypeParam;
import com.dianrong.common.uniauth.common.interfaces.read.ITagResource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by Arc on 7/4/2016.
 */
public interface ITagRWResource extends ITagResource {

  @POST
  @Path("add")
  Response<TagDto> addNewTag(TagParam tagParam);

  @POST
  @Path("update")
  Response<TagDto> updateTag(TagParam tagParam);

  @POST
  @Path("add-new-tag-type")
  Response<TagTypeDto> addNewTagType(TagTypeParam tagTypeParam);

  @POST
  @Path("update-tag-type")
  Response<TagTypeDto> updateTagType(TagTypeParam tagTypeParam);

  @POST
  @Path("delete-tag-type")
  Response<Void> deleteTagType(TagTypeParam tagTypeParam);

  @POST
  @Path("replace-grps-users-to-tag")
  Response<Void> replaceGroupsAndUsersToTag(TagParam tagParam);
  
  @POST
  @Path("relate-users-and-tag")
  Response<Void> relateUsersAndTag(TagParam tagParam);
}
