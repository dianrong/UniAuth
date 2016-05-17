package com.dianrong.common.uniauth.server.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendDto;
import com.dianrong.common.uniauth.common.bean.request.UserExtendPageParam;
import com.dianrong.common.uniauth.common.bean.request.UserExtendParam;
import com.dianrong.common.uniauth.server.service.UserExtendService;
import com.dianrong.common.uniauth.sharerw.interfaces.IUserExtendRWResource;

/**
 * @author wenlongchen
 * @since May 16, 2016
 */
@RestController
public class UserExtendResource implements IUserExtendRWResource {
    
    @Autowired
    private UserExtendService userExtendService;

    @Override
    public Response<PageDto<UserExtendDto>> searchUserExtend(UserExtendPageParam pageParam) {
        PageDto<UserExtendDto> pageDto=userExtendService.search(pageParam.getCode(),pageParam.getPageNumber(),pageParam.getPageSize());
        return Response.success(pageDto);
    }

    @Override
    public Response<UserExtendDto> addUserExtend(UserExtendParam userExtendParam) {
        UserExtendDto userExtendDto=userExtendService.add(userExtendParam.getCode(),userExtendParam.getDescription());
        return Response.success(userExtendDto);
    }

    @Override
    public Response<Integer> updateUserExtend(UserExtendParam userExtendParam) {
        int count=userExtendService.updateByKey(userExtendParam.getId(),
                userExtendParam.getCode(),userExtendParam.getDescription());
        return Response.success(count);
    }

    
}

