package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.request.TagQuery;
import com.dianrong.common.uniauth.server.service.TagService;
import com.dianrong.common.uniauth.sharerw.interfaces.ITagRWResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Arc on 7/4/2016.
 */
@RestController
public class TagResource implements ITagRWResource {
    @Autowired
    private TagService tagService;

    @Override
    public Response<PageDto<TagDto>> searchTags(TagQuery tagQuery) {

        return null;
    }
}
