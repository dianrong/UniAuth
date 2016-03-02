package com.dianrong.common.techops.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.dianrong.common.techops.bean.LoginUser;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.techops.service.TechOpsService;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;
import com.dianrong.common.uniauth.common.enm.UserActionEnum;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
/**
 * Created by Arc on 16/2/16.
 */
@RestController
@RequestMapping("user")
public class UserAction {

    @Resource
    private UARWFacade uARWFacade;
    
    @Resource
    private TechOpsService techOpsService;

    @RequestMapping(value = "/query" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public Response<PageDto<UserDto>> searchUser(@RequestBody UserQuery userQuery) {
        return uARWFacade.getUserRWResource().searchUser(userQuery);
    }

    @RequestMapping(value = "/add" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public Response<UserDto> addUser(@RequestBody UserParam userParam) {
        return uARWFacade.getUserRWResource().addNewUser(userParam);
    }

    @RequestMapping(value = "/enable-disable" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public Response<Void> enableDisableUser(@RequestBody UserParam userParam) {
        UserParam param = new UserParam();
        param.setId(userParam.getId());
        param.setStatus(userParam.getStatus());
        param.setUserActionEnum(UserActionEnum.STATUS_CHANGE);
        return uARWFacade.getUserRWResource().updateUser(param);
    }

    @RequestMapping(value = "/unlock" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public Response<Void> unlock(@RequestBody UserParam userParam) {
        userParam.setUserActionEnum(UserActionEnum.UNLOCK);
        return uARWFacade.getUserRWResource().updateUser(userParam);
    }

    @RequestMapping(value = "/resetpassword" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public Response<Void> resetPassword(@RequestBody UserParam userParam) {
        userParam.setUserActionEnum(UserActionEnum.RESET_PASSWORD);
        return uARWFacade.getUserRWResource().updateUser(userParam);
    }

    @RequestMapping(value = "/modify" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public Response<Void> updateUser(@RequestBody UserParam userParam) {
        userParam.setUserActionEnum(UserActionEnum.UPDATE_INFO);
        return uARWFacade.getUserRWResource().updateUser(userParam);
    }

    @RequestMapping(value = "/current" , method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<LoginUser> getCurrentUserInfo(HttpServletRequest request, String email) {
        LoginUser loginUser = techOpsService.getLoginUser();
    	return Response.success(loginUser);
    }
}
