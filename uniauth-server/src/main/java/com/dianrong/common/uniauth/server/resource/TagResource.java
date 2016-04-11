package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.TagTypeDto;
import com.dianrong.common.uniauth.common.bean.request.TagQuery;
import com.dianrong.common.uniauth.common.bean.request.TagTypeParam;
import com.dianrong.common.uniauth.common.bean.request.TagTypeQuery;
import com.dianrong.common.uniauth.server.service.TagService;
import com.dianrong.common.uniauth.sharerw.interfaces.ITagRWResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Arc on 7/4/2016.
 */
@RestController
public class TagResource implements ITagRWResource {

    @Autowired
    private TagService tagService;

    @Override
    public Response<PageDto<TagDto>> searchTags(TagQuery tagQuery) {
        PageDto<TagDto> tagDtoPageDto = tagService.searchTags(tagQuery.getId(),tagQuery.getTagIds(),tagQuery.getCode(),tagQuery.getStatus(),
                tagQuery.getTagTypeId(),tagQuery.getUserId(),tagQuery.getDomainId(),tagQuery.getDomainIds(), tagQuery.getGroupId(),
                tagQuery.getPageNumber(),tagQuery.getPageSize());
        return Response.success(tagDtoPageDto);
    }

    @Override
    public Response<List<TagTypeDto>> getTagTypes(TagTypeQuery tagTypeQuery) {
        List<TagTypeDto> tagTypeDtos = tagService.getTagTypes(tagTypeQuery.getDomainId());
        return Response.success(tagTypeDtos);
    }

    @Override
    public Response<TagTypeDto> addNewTagType(TagTypeParam tagTypeParam) {
        TagTypeDto tagTypeDto = tagService.addNewTagType(tagTypeParam.getCode(),tagTypeParam.getDomainId());
        return Response.success(tagTypeDto);
    }

    @Override
    public Response<TagTypeDto> updateTagType(TagTypeParam tagTypeParam) {
        TagTypeDto tagTypeDto = tagService.updateTagType(tagTypeParam.getId(),tagTypeParam.getCode(),tagTypeParam.getDomainId());
        return Response.success(tagTypeDto);
    }

    @Override
    public Response<Void> deleteTagType(TagTypeParam tagTypeParam) {
        tagService.deleteTagType(tagTypeParam.getId());
        return Response.success();
    }
}
