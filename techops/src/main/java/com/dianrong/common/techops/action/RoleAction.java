package com.dianrong.common.techops.action;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.bean.request.RoleQuery;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Arc on 7/3/2016.
 */
@RestController
@RequestMapping("role")
public class RoleAction {

    @Resource
    private UARWFacade uARWFacade;

    @RequestMapping(value = "/query" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops')) or " +
            "(hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#roleQuery.domainId))")
    public Response<PageDto<RoleDto>> searchRole(@RequestBody RoleQuery roleQuery) {
        return uARWFacade.getRoleRWResource().searchRole(roleQuery);
    }

    @RequestMapping(value = "/codes" , method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<RoleCodeDto>> getAllRoleCodes() {
        return uARWFacade.getRoleRWResource().getAllRoleCodes();
    }

    @RequestMapping(value = "/add" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops')) "
            + "or (hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#roleParam.domainId))")
    public Response<RoleDto>  addNewRole(@RequestBody RoleParam roleParam) {
        return uARWFacade.getRoleRWResource().addNewRole(roleParam);
    }

    @RequestMapping(value = "/update" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops')) "
            + "or (hasRole('ROLE_ADMIN') and hasPermission(#roleParam, 'PERM_ROLEID_CHECK'))")
    public Response<Void> updateRole(@RequestBody RoleParam roleParam) {
        return uARWFacade.getRoleRWResource().updateRole(roleParam);
    }

    @RequestMapping(value = "/replace-perms-to-role" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops')) "
            + "or (hasRole('ROLE_ADMIN') and hasPermission(#roleParam, 'PERM_ROLEID_CHECK') and hasPermission(#roleParam, 'PERM_PERMIDS_CHECK'))")
    public Response<Void> replacePermsToRole(@RequestBody RoleParam roleParam) {
        return uARWFacade.getRoleRWResource().replacePermsToRole(roleParam);
    }

    @RequestMapping(value = "/query-perms-with-checked-info" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops')) "
            + "or (hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#roleParam.domainId) and hasPermission(#roleParam, 'PERM_ROLEID_CHECK'))")
    public Response<List<PermissionDto>> queryPermsWithCheckedInfo(@RequestBody RoleParam roleParam) {
        return uARWFacade.getRoleRWResource().getAllPermsToRole(roleParam);
    }

    @RequestMapping(value = "/replace-grps-users-to-role" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null and principal.permMap['DOMAIN'].contains('techops')) "
            + "or (hasRole('ROLE_ADMIN') and hasPermission(#roleParam, 'PERM_ROLEID_CHECK'))")
    public Response<Void> replaceGroupsAndUsersToRole(@RequestBody RoleParam roleParam) {
        return uARWFacade.getRoleRWResource().replaceGroupsAndUsersToRole(roleParam);
    }
}
