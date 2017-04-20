package com.dianrong.common.techops.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.techops.bean.LoginUser;
import com.dianrong.common.techops.service.NotificationService;
import com.dianrong.common.techops.service.TechOpsService;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
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
    /**
     *  发送消息的服务
     */
    @Autowired
    private NotificationService notificationService;

    @Resource
    private UARWFacade uARWFacade;
    
    @Resource
    private TechOpsService techOpsService;

    // perm double checked
    @RequestMapping(value = "/query" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public Response<PageDto<UserDto>> searchUser(@RequestBody UserQuery userQuery) {
        return uARWFacade.getUserRWResource().searchUser(userQuery);
    }

    // perm double checked
    @RequestMapping(value = "/add" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public Response<?> addUser(@RequestBody UserParam userParam) {
        Response<UserDto> userDtoResponse = uARWFacade.getUserRWResource().addNewUser(userParam);
        if(!CollectionUtils.isEmpty(userDtoResponse.getInfo())) {
            return userDtoResponse;
        }
        UserDto userDto = userDtoResponse.getData();
        notificationService.addUserNotification(userDto);
        return Response.success();
    }

    // perm double checked
    @RequestMapping(value = "/enable-disable" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') or (hasRole('ROLE_ADMIN') and hasPermission(#userParam, 'PERM_USERID_CHECK'))")
    public Response<?> enableDisableUser(@RequestBody UserParam userParam) {
        UserParam param = new UserParam();
        param.setId(userParam.getId());
        param.setStatus(userParam.getStatus());
        param.setUserActionEnum(UserActionEnum.STATUS_CHANGE);
        Response<UserDto> userDtoResponse = uARWFacade.getUserRWResource().updateUser(param);
        if(!CollectionUtils.isEmpty(userDtoResponse.getInfo())) {
            return userDtoResponse;
        }
        return Response.success();
    }

    // perm double checked
    @RequestMapping(value = "/unlock" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public Response<?> unlock(@RequestBody UserParam userParam) {
        userParam.setUserActionEnum(UserActionEnum.UNLOCK);
        Response<UserDto> userDtoResponse = uARWFacade.getUserRWResource().updateUser(userParam);
        if(!CollectionUtils.isEmpty(userDtoResponse.getInfo())) {
            return userDtoResponse;
        }
        return Response.success();
    }

    // perm double checked
    @RequestMapping(value = "/resetpassword" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops')")
    public Response<?> resetPassword(@RequestBody UserParam userParam) {
        userParam.setUserActionEnum(UserActionEnum.RESET_PASSWORD);
        // 管理员设置密码不需要检查
        userParam.setIgnorePwdStrategyCheck(Boolean.TRUE);
        Response<UserDto> userDtoResponse = uARWFacade.getUserRWResource().updateUser(userParam);
        if(!CollectionUtils.isEmpty(userDtoResponse.getInfo())) {
            return userDtoResponse;
        }
        UserDto userDto = userDtoResponse.getData();
        notificationService.updateUserPwdNotification(userDto);
        return Response.success();
    }

    // perm double checked
    @RequestMapping(value = "/modify" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops')")
    public Response<?> updateUser(@RequestBody UserParam userParam) {
        userParam.setUserActionEnum(UserActionEnum.UPDATE_INFO);
        Response<UserDto> userDtoResponse = uARWFacade.getUserRWResource().updateUser(userParam);
        if(!CollectionUtils.isEmpty(userDtoResponse.getInfo())) {
            return userDtoResponse;
        }
        return Response.success();
    }

    @RequestMapping(value = "/current" , method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<LoginUser> getCurrentUserInfo() {
        LoginUser loginUser = techOpsService.getLoginUser();
    	return Response.success(loginUser);
    }

    // perm double checked
    @RequestMapping(value = "/user-roles" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops')) "
            + "or (hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#userParam.domainId))")
    public Response<List<RoleDto>> getUserRolesWithCheckedInfoByDomain(@RequestBody UserParam userParam) {
        return uARWFacade.getUserRWResource().getAllRolesToUserAndDomain(userParam);
    }

    // perm double checked
    @RequestMapping(value = "/replace-roles-to-user" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops')) "
            + "or (hasRole('ROLE_ADMIN') and hasPermission(#userParam, 'PERM_ROLEIDS_CHECK'))")
    public Response<Void> replaceRolesToUser(@RequestBody UserParam userParam) {
        return uARWFacade.getUserRWResource().replaceRolesToUser(userParam);
    }
    
    // perm double checked
    @RequestMapping(value = "/user-tags" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops')) "
            + "or (hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#userParam.domainId))")
    public Response<List<TagDto>> getTagsWithUserCheckedInfo(@RequestBody UserParam userParam) {
        return uARWFacade.getUserRWResource().getTagsWithUserCheckedInfo(userParam);
    }
    
 // perm double checked
    @RequestMapping(value = "/replace-tags-to-user" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops')) "
            + "or (hasRole('ROLE_ADMIN') and hasPermission(#userParam, 'PERM_ROLEIDS_CHECK'))")
    public Response<Void> replaceTagsToUser(@RequestBody UserParam userParam) {
        return uARWFacade.getUserRWResource().replaceTagsToUser(userParam);
    }
}
