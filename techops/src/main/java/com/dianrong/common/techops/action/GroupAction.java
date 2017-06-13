package com.dianrong.common.techops.action;

import com.dianrong.common.techops.bean.Node;
import com.dianrong.common.techops.helper.CustomizeBeanConverter;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.GroupQuery;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Arc on 29/2/2016.
 */
@RestController
@RequestMapping("group")
public class GroupAction {

  @Resource
  private UARWFacade uarwFacade;

  /**
   * 获取组信息,以Tree的形式返回.
   */
  @RequestMapping(value = "/tree", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
  public Response<?> getGroupTree(@RequestBody GroupParam groupParam) {
    Response<GroupDto> groupDtoResponse = uarwFacade.getGroupRWResource().getGroupTree(groupParam);
    if (!CollectionUtils.isEmpty(groupDtoResponse.getInfo())) {
      return groupDtoResponse;
    }
    GroupDto groupDto = groupDtoResponse.getData();
    if (groupDto != null) {
      List<Node> nodes;
      if (groupParam.getOnlyNeedGrpInfo() != null && groupParam.getOnlyNeedGrpInfo()) {
        // 根据groupParam.roleId来判断 逻辑上的处理为：如果传了RoleId 就当成在查询角色与用户关联关系
        nodes =
            CustomizeBeanConverter.convert(Arrays.asList(groupDto), groupParam.getRoleId() != null);
      } else if (groupParam.getRoleId() != null) {
        nodes = CustomizeBeanConverter.convert(Arrays.asList(groupDto), Boolean.TRUE);
        PrimaryKeyParam primaryKeyParam = new PrimaryKeyParam();
        primaryKeyParam.setId(groupParam.getRoleId());
        Response<List<UserDto>> usersWithRoleCheck =
            uarwFacade.getUserRWResource().searchUsersWithRoleCheck(primaryKeyParam);
        if (!CollectionUtils.isEmpty(usersWithRoleCheck.getInfo())) {
          return usersWithRoleCheck;
        }
        List<UserDto> userDtos = usersWithRoleCheck.getData();
        if (!CollectionUtils.isEmpty(userDtos)) {
          for (UserDto userDto : userDtos) {
            Node userNode = new Node();
            userNode.setId(userDto.getId().toString());
            userNode.setLabel(userDto.getAccount());
            userNode.setChecked(userDto.getRoleChecked());
            userNode.setType(AppConstants.NODE_TYPE_MEMBER_USER);
            nodes.add(userNode);
          }
        }
      } else if (groupParam.getTagId() != null) {
        nodes = CustomizeBeanConverter.convert(Arrays.asList(groupDto), Boolean.FALSE);
        PrimaryKeyParam primaryKeyParam = new PrimaryKeyParam();
        primaryKeyParam.setId(groupParam.getTagId());
        Response<List<UserDto>> usersWithTagCheck =
            uarwFacade.getUserRWResource().searchUsersWithTagCheck(primaryKeyParam);
        if (!CollectionUtils.isEmpty(usersWithTagCheck.getInfo())) {
          return usersWithTagCheck;
        }
        List<UserDto> userDtos = usersWithTagCheck.getData();
        if (!CollectionUtils.isEmpty(userDtos)) {
          for (UserDto userDto : userDtos) {
            Node userNode = new Node();
            userNode.setId(userDto.getId().toString());
            userNode.setLabel(userDto.getAccount());
            userNode.setChecked(userDto.getTagChecked());
            userNode.setType(AppConstants.NODE_TYPE_MEMBER_USER);
            nodes.add(userNode);
          }
        }
      } else {
        // 是 owner 才能查看到的组集合
        nodes = CustomizeBeanConverter.convert(Arrays.asList(groupDto), null);
        filterNodes(nodes);
      }
      return Response.success(nodes);
    } else {
      return Response.success(null);
    }
  }

  private void filterNodes(List<Node> nodes) {
    if (nodes == null) {
      return;
    }
    Iterator<Node> iter = nodes.iterator();
    while (iter.hasNext()) {
      Node n = iter.next();
      if (!isOwnerThisNode(n)) {
        iter.remove();
      }
    }
  }

  /**
   * 是否是这个节点的owner.
   */
  private boolean isOwnerThisNode(Node node) {
    if (node.getOwnerMarkup() != null && node.getOwnerMarkup()) {
      return true;
    }
    if (node.getChildren() != null) {
      boolean owner = false;
      Iterator<Node> childIter = node.getChildren().iterator();
      while (childIter.hasNext()) {
        Node child = childIter.next();
        if (isOwnerThisNode(child)) {
          owner = true;
        } else {
          childIter.remove();
        }
      }
      return owner;
    }
    return false;
  }

  // perm double checked
  @RequestMapping(value = "/add", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
      + "and hasPermission(#groupParam,'PERM_GROUP_OWNER')")
  public Response<GroupDto> addNewGroupIntoGroup(@RequestBody GroupParam groupParam) {
    Response<GroupDto> groupDto = uarwFacade.getGroupRWResource().addNewGroupIntoGroup(groupParam);
    return groupDto;
  }

  /**
   * 获取组的详细信息.
   */
  // perm double checked
  @RequestMapping(value = "/get", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
  public Response<Node> getGroupDetailsById(@RequestParam("id") Integer grpId) {
    GroupQuery groupQuery = new GroupQuery();
    groupQuery.setId(grpId);
    Response<PageDto<GroupDto>> pageDtoResponse =
        uarwFacade.getGroupRWResource().queryGroup(groupQuery);
    GroupDto groupDto = pageDtoResponse.getData().getData().get(0);
    Node node = new Node();
    node.setId(groupDto.getId().toString());
    node.setCode(groupDto.getCode());
    node.setType(AppConstants.NODE_TYPE_GROUP);
    node.setLabel(groupDto.getName());
    node.setDescription(groupDto.getDescription());
    return Response.success(node);
  }

  /**
   * 根据条件查询组信息.
   */
  // perm double checked
  @RequestMapping(value = "/query", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')")
  public Response<PageDto<GroupDto>> searchGroup(@RequestBody GroupQuery groupQuery) {
    Response<PageDto<GroupDto>> pageDtoResponse =
        uarwFacade.getGroupRWResource().queryGroup(groupQuery);
    return pageDtoResponse;
  }

  /**
   * 更新组信息.
   */
  // perm double checked
  @RequestMapping(value = "/modify", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
      + "and hasPermission(#groupParam,'PERM_GROUP_OWNER')")
  public Response<GroupDto> modifyGroup(@RequestBody GroupParam groupParam) {
    Response<GroupDto> groupDto = uarwFacade.getGroupRWResource().updateGroup(groupParam);
    return groupDto;
  }

  @RequestMapping(value = "/del", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
      + "and hasPermission(#groupParam,'PERM_GROUP_OWNER')")
  public Response<GroupDto> deleteGroup(@RequestBody GroupParam groupParam) {
    Response<GroupDto> groupDto = uarwFacade.getGroupRWResource().deleteGroup(groupParam);
    return groupDto;
  }

  // perm double checked
  @RequestMapping(value = "/add-user", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
      + "and hasPermission(#userListParam,'PERM_GROUP_OWNER')")
  public Response<Void> addUserToGroup(@RequestBody UserListParam userListParam) {
    Response<Void> response = uarwFacade.getGroupRWResource().addUsersIntoGroup(userListParam);
    return response;
  }

  @RequestMapping(value = "/delete-user", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
      + "and hasPermission(#userListParam,'PERM_GROUP_OWNER')")
  public Response<Void> removeUserFromGroup(@RequestBody UserListParam userListParam) {
    Response<Void> response = uarwFacade.getGroupRWResource().removeUsersFromGroup(userListParam);
    return response;
  }

  @RequestMapping(value = "/move-user", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
      + "and hasPermission(#userListParam,'PERM_GROUP_OWNER')")
  public Response<Void> moveUser(@RequestBody UserListParam userListParam) {
    Response<Void> response = uarwFacade.getGroupRWResource().moveGroupUser(userListParam);
    return response;
  }

  // perm double checked
  @RequestMapping(value = "/group-roles", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) "
      + "or (hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#groupParam.domainId))")
  public Response<List<RoleDto>> getAllRolesToGroupAndDomain(@RequestBody GroupParam groupParam) {
    return uarwFacade.getGroupRWResource().getAllRolesToGroupAndDomain(groupParam);
  }

  // perm double checked
  @RequestMapping(value = "/replace-roles-to-group", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) "
      + "or (hasRole('ROLE_ADMIN') and hasPermission(#groupParam, 'PERM_ROLEIDS_CHECK'))")
  public Response<Void> replaceRolesToGroup(@RequestBody GroupParam groupParam) {
    return uarwFacade.getGroupRWResource().replaceRolesToGroup(groupParam);
  }

  // perm double checked
  @RequestMapping(value = "/group-tags", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) "
      + "or (hasRole('ROLE_ADMIN') and principal.domainIdSet.contains(#groupParam.domainId))")
  public Response<List<TagDto>> getAllTagsToGroup(@RequestBody GroupParam groupParam) {
    return uarwFacade.getGroupRWResource().queryTagsWithChecked(groupParam);
  }

  // perm double checked
  @RequestMapping(value = "/replace-tags-to-group", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')) "
      + "or (hasRole('ROLE_ADMIN') and hasPermission(#groupParam, 'PERM_ROLEIDS_CHECK'))")
  public Response<Void> replaceTagsToGroup(@RequestBody GroupParam groupParam) {
    return uarwFacade.getGroupRWResource().replaceTagsToGrp(groupParam);
  }

  @RequestMapping(value = "/move", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
      + "and hasPermission(#groupParam,'PERM_GROUP_OWNER')")
  public Response<Void> moveGroup(@RequestBody GroupParam groupParam) {
    return uarwFacade.getGroupRWResource().moveGroup(groupParam);
  }
}
