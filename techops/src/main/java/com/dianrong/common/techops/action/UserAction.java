package com.dianrong.common.techops.action;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Arc on 16/2/16.
 */
@RestController
@RequestMapping("user")
public class UserAction {

    @Resource
    private UARWFacade uARWFacade;

    @RequestMapping(value = "/query" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<PageDto<UserDto>> searchUser(@RequestBody UserQuery userQuery) {
        return uARWFacade.getUserRWResource().searchUser(userQuery);
    }

    @RequestMapping(value = "/techops/domain" , method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<DomainDto>> getSwitchableDomains(String email) {
//        LoginParam loginParam = new LoginParam();
//        loginParam.setAccount(email);
//        Response<UserDetailDto> userDetailDtoResponse = uARWFacade.getUserRWResource().getUserDetailInfo(loginParam);
        return uARWFacade.getDomainRWResource().getAllLoginDomains();
    }
}
