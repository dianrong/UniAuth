package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendValDto;
import com.dianrong.common.uniauth.common.bean.request.UserExtendValParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.interfaces.readwrite.IUserExtendValRWResource;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.service.UserExtendValService;
import com.dianrong.common.uniauth.server.service.UserService;

/**
 * @author wenlongchen
 * @since May 16, 2016
 */
@RestController
public class UserExtendValResource implements IUserExtendValRWResource {

    @Autowired
    private UserExtendValService userExtendValService;
    
    @Autowired
    private UserService userService;
    
    @Override
    public Response<List<UserExtendValDto>> searchByUserId(UserExtendValParam userExtendValParam) {
        List<UserExtendValDto> userExtendValDtos=userExtendValService.searchByUserId(userExtendValParam.getUserId(),userExtendValParam.getStatus());
        return Response.success(userExtendValDtos);
    }

    @Override
    public Response<UserExtendValDto> add(UserExtendValParam userExtendValParam) {
        UserExtendValDto userExtendValDto=userExtendValService.add(userExtendValParam.getUserId(), 
                userExtendValParam.getExtendId(),
                userExtendValParam.getValue(),
                userExtendValParam.getStatus());
        return Response.success(userExtendValDto);
    }

    @Override
    public Response<Integer> delById(UserExtendValParam userExtendValParam) {
        int count=userExtendValService.delById(userExtendValParam.getId());
        return Response.success(count);
    }

    @Override
    public Response<Integer> updateById(UserExtendValParam userExtendValParam) {
        int count=userExtendValService.updateById(userExtendValParam.getId(),
                userExtendValParam.getUserId(), 
                userExtendValParam.getExtendId(),
                userExtendValParam.getValue(),
                userExtendValParam.getStatus());
        return Response.success(count);
    }

    @Override
    public Response<PageDto<UserExtendValDto>> searchByUserIdAndCode(UserExtendValParam userExtendValParam) {
        PageDto<UserExtendValDto> pageDto=userExtendValService.searchByUserIdAndCode(
                userExtendValParam.getUserId(),
                userExtendValParam.getExtendCode(),
                userExtendValParam.getPageNumber(),
                userExtendValParam.getPageSize(),
                userExtendValParam.isQueryOnlyUsed());
        return Response.success(pageDto);
    }

    @Override
    public Response<List<UserExtendValDto>> searchByUserIdentity(UserExtendValParam userExtendValParam) {
        User user = userService.getUserByAccount(userExtendValParam.getIdentity(), userExtendValParam.getTenancyCode(), userExtendValParam.getTenancyId(),
            true, AppConstants.STATUS_ENABLED);
        List<UserExtendValDto> userExtendValDtos = userExtendValService.searchByUserId(user.getId(), userExtendValParam.getStatus());
        return Response.success(userExtendValDtos);
    }
    
}

