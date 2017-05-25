package com.dianrong.common.techops.action;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.bean.request.RoleQuery;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Arc on 7/3/2016.
 */
@RestController
@RequestMapping("role")
public class RoleAction {

  @Resource
  private UARWFacade uarwFacade;

  @RequestMapping(value = "/query", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) or "
      + "(hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#roleQuery.domainId))")
  public Response<PageDto<RoleDto>> searchRole(@RequestBody RoleQuery roleQuery) {
    return uarwFacade.getRoleRWResource().searchRole(roleQuery);
  }

  @RequestMapping(value = "/codes", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<List<RoleCodeDto>> getAllRoleCodes() {
    return uarwFacade.getRoleRWResource().getAllRoleCodes();
  }

  @RequestMapping(value = "/add", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) "
      + "or (hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#roleParam.domainId))")
  public Response<RoleDto> addNewRole(@RequestBody RoleParam roleParam) {
    return uarwFacade.getRoleRWResource().addNewRole(roleParam);
  }

  @RequestMapping(value = "/update", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) "
      + "or (hasRole('ROLE_ADMIN') and hasPermission(#roleParam, 'PERM_ROLEID_CHECK'))")
  public Response<Void> updateRole(@RequestBody RoleParam roleParam) {
    return uarwFacade.getRoleRWResource().updateRole(roleParam);
  }

  @RequestMapping(value = "/replace-perms-to-role", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) "
      + "or (hasRole('ROLE_ADMIN') and hasPermission(#roleParam, 'PERM_ROLEID_CHECK') "
      + "and hasPermission(#roleParam, 'PERM_PERMIDS_CHECK'))")
  public Response<Void> replacePermsToRole(@RequestBody RoleParam roleParam) {
    return uarwFacade.getRoleRWResource().replacePermsToRole(roleParam);
  }

  @RequestMapping(value = "/query-perms-with-checked-info", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) "
      + "or (hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#roleParam.domainId) "
      + "and hasPermission(#roleParam, 'PERM_ROLEID_CHECK'))")
  public Response<List<PermissionDto>> queryPermsWithCheckedInfo(@RequestBody RoleParam roleParam) {
    return uarwFacade.getRoleRWResource().getAllPermsToRole(roleParam);
  }

  @RequestMapping(value = "/replace-grps-users-to-role", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) "
      + "or (hasRole('ROLE_ADMIN') and hasPermission(#roleParam, 'PERM_ROLEID_CHECK'))")
  public Response<Void> replaceGroupsAndUsersToRole(@RequestBody RoleParam roleParam) {
    return uarwFacade.getRoleRWResource().replaceGroupsAndUsersToRole(roleParam);
  }

  /**
   * 根据角色获取用户.
   */
  @RequestMapping(value = "/query-role-user", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
  public Response<List<UserDto>> getRoleUser(@RequestBody RoleParam roleParam) {
    UserParam param = new UserParam();
    List<Integer> roleIds = new ArrayList<Integer>();
    roleIds.add(roleParam.getId());
    param.setRoleIds(roleIds);
    return uarwFacade.getUserRWResource().searchUserByRoleId(param);
  }
}
