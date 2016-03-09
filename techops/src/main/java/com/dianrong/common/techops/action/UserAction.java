package com.dianrong.common.techops.action;

import com.dianrong.common.techops.bean.LoginUser;
import com.dianrong.common.techops.service.TechOpsService;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;
import com.dianrong.common.uniauth.common.enm.UserActionEnum;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Arc on 16/2/16.
 */
@RestController
@RequestMapping("user")
public class UserAction {

    @Value("#{uniauthConfig['cas_server']}")
    private String casServerURL;

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
    public Response<Void> addUser(@RequestBody UserParam userParam) {
        Response<UserDto> userDtoResponse = uARWFacade.getUserRWResource().addNewUser(userParam);
        UserDto userDto = userDtoResponse.getData();
        StringBuffer buffer = new StringBuffer();
        buffer.append("====================================================<br />");
        buffer.append("            ");
        buffer.append("     系统管理员为您创建了点融系统账户<br />");
        buffer.append("            ");
        buffer.append(" 您的点融登录账号为: " + userDto.getEmail() + "        <br />");
        buffer.append("            ");
        buffer.append(" 您的点融账户密码为: " + userDto.getPassword() + "        <br />");
        if(casServerURL != null) {
            buffer.append("            ");
            buffer.append(" 请到: " + casServerURL + " 登录您想要登录的系统.       <br />");
        }
        buffer.append("====================================================<br />");
        techOpsService.sendEmail(userDto.getEmail(), buffer);

        return Response.success();
    }

    @RequestMapping(value = "/enable-disable" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public Response<Void> enableDisableUser(@RequestBody UserParam userParam) {
        UserParam param = new UserParam();
        param.setId(userParam.getId());
        param.setStatus(userParam.getStatus());
        param.setUserActionEnum(UserActionEnum.STATUS_CHANGE);
        uARWFacade.getUserRWResource().updateUser(param);
        return Response.success();
    }

    @RequestMapping(value = "/unlock" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public Response<Void> unlock(@RequestBody UserParam userParam) {
        userParam.setUserActionEnum(UserActionEnum.UNLOCK);
        uARWFacade.getUserRWResource().updateUser(userParam);
        return Response.success();
    }

    @RequestMapping(value = "/resetpassword" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public Response<Void> resetPassword(@RequestBody UserParam userParam) {
        userParam.setUserActionEnum(UserActionEnum.RESET_PASSWORD);
        Response<UserDto> userDtoResponse = uARWFacade.getUserRWResource().updateUser(userParam);

        UserDto userDto = userDtoResponse.getData();
        StringBuffer buffer = new StringBuffer();
        buffer.append("====================================================<br />");
        buffer.append("            ");
        buffer.append("      系统管理员重置了您的点融系统账户密码<br />");
        buffer.append("            ");
        buffer.append(" 您的点融登录账号为: " + userDto.getEmail() + "        <br />");
        buffer.append("            ");
        buffer.append(" 您的点融账户密码为: " + userDto.getPassword() + "        <br />");
        if(casServerURL != null) {
            buffer.append("            ");
            buffer.append(" 请到: " + casServerURL + " 登录您想要登录的系统.       <br />");
        }
        buffer.append("====================================================<br />");
        techOpsService.sendEmail(userDto.getEmail(), buffer);

        return Response.success();
    }

    @RequestMapping(value = "/modify" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public Response<Void> updateUser(@RequestBody UserParam userParam) {
        userParam.setUserActionEnum(UserActionEnum.UPDATE_INFO);
        uARWFacade.getUserRWResource().updateUser(userParam);
        return Response.success();
    }

    @RequestMapping(value = "/current" , method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<LoginUser> getCurrentUserInfo() {
        LoginUser loginUser = techOpsService.getLoginUser();
    	return Response.success(loginUser);
    }
}
