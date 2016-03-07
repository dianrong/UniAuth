package com.dianrong.common.techops.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.bean.request.RoleQuery;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;

/**
 * Created by Arc on 7/3/2016.
 */
@RestController
@RequestMapping("role")
public class RoleAction {

    @Resource
    private UARWFacade uARWFacade;

    @RequestMapping(value = "/query" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public Response<PageDto<RoleDto>> searchUser(@RequestBody RoleQuery roleQuery) {
        return uARWFacade.getRoleRWResource().searchRole(roleQuery);
    }

    @RequestMapping(value = "/codes" , method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<RoleCodeDto>> getAllRoleCodes() {
        return uARWFacade.getRoleRWResource().getAllRoleCodes();
    }

    @RequestMapping(value = "/add" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("principal.permMap['DOMAIN'] != null and (principal.permMap['DOMAIN'].contains('techops') or principal.permMap['DOMAIN_ID'].contains(#roleParam.domainId))")
    public Response<RoleDto>  addNewRole(@RequestBody RoleParam roleParam) {
        return uARWFacade.getRoleRWResource().addNewRole(roleParam);
    }

}
