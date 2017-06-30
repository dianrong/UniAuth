package com.dianrong.common.uniauth.server.resource;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.TagTypeDto;
import com.dianrong.common.uniauth.common.bean.request.TagParam;
import com.dianrong.common.uniauth.common.bean.request.TagQuery;
import com.dianrong.common.uniauth.common.bean.request.TagTypeParam;
import com.dianrong.common.uniauth.common.bean.request.TagTypeQuery;
import com.dianrong.common.uniauth.server.service.TagService;
import com.dianrong.common.uniauth.sharerw.interfaces.ITagRWResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Arc on 7/4/2016.
 */
@Api("标签操作接口")
@RestController
public class TagResource implements ITagRWResource {

  @Autowired
  private TagService tagService;

  @Override
  @Timed
  public Response<PageDto<TagDto>> searchTags(TagQuery tagQuery) {
    PageDto<TagDto> tagDtoPageDto = tagService.searchTags(tagQuery.getId(), tagQuery.getTagIds(),
        tagQuery.getCode(), tagQuery.getFuzzyCode(), tagQuery.getStatus(), tagQuery.getTagTypeId(),
        tagQuery.getUserId(), tagQuery.getDomainId(), tagQuery.getDomainCode(),
        tagQuery.getDomainIds(), tagQuery.getGroupId(), tagQuery.getNeedDomainInfo(),
        tagQuery.getPageNumber(), tagQuery.getPageSize());
    return Response.success(tagDtoPageDto);
  }

  @Override
  public Response<TagDto> addNewTag(TagParam tagParam) {
    TagDto tagDto = tagService.addNewTag(tagParam.getCode(), tagParam.getTagTypeId(),
        tagParam.getDescription());
    return Response.success(tagDto);
  }

  @Override
  public Response<TagDto> updateTag(TagParam tagParam) {
    TagDto tagDto = tagService.updateTag(tagParam.getId(), tagParam.getCode(), tagParam.getStatus(),
        tagParam.getTagTypeId(), tagParam.getDescription());
    return Response.success(tagDto);
  }

  @Override
  @Timed
  public Response<List<TagTypeDto>> searchTagTypes(TagTypeQuery tagTypeQuery) {
    List<TagTypeDto> tagTypeDtos = tagService.searchTagTypes(tagTypeQuery.getId(),
        tagTypeQuery.getDomainIds(), tagTypeQuery.getDomainId(), tagTypeQuery.getCode());
    return Response.success(tagTypeDtos);
  }

  @Override
  public Response<TagTypeDto> addNewTagType(TagTypeParam tagTypeParam) {
    TagTypeDto tagTypeDto =
        tagService.addNewTagType(tagTypeParam.getCode(), tagTypeParam.getDomainId());
    return Response.success(tagTypeDto);
  }

  @Override
  public Response<TagTypeDto> updateTagType(TagTypeParam tagTypeParam) {
    TagTypeDto tagTypeDto = tagService.updateTagType(tagTypeParam.getId(), tagTypeParam.getCode(),
        tagTypeParam.getDomainId());
    return Response.success(tagTypeDto);
  }

  @Override
  public Response<Void> deleteTagType(TagTypeParam tagTypeParam) {
    tagService.deleteTagType(tagTypeParam.getId());
    return Response.success();
  }

  @Override
  public Response<Void> replaceGroupsAndUsersToTag(TagParam tagParam) {
    tagService.replaceGroupsAndUsersToTag(tagParam.getId(), tagParam.getGrpIds(),
        tagParam.getUserIds(), tagParam.getNeedProcessGoupIds(), tagParam.getNeedProcessUserIds());
    return Response.success();
  }

  @ApiOperation("批量关联用户和标签")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "tagId", value = "标签Id", dataType = "int", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "userIds", value = "需要添加角色用户id列表", dataType = "java.util.List",
          required = true, paramType = "query")})
  @Override
  public Response<Void> relateUsersAndTag(TagParam tagParam) {
    tagService.relateUsersAndTag(tagParam.getId(), tagParam.getUserIds());
    return Response.success();
  }
}
