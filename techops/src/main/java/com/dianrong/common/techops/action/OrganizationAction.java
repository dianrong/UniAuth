package com.dianrong.common.techops.action;

import com.dianrong.common.techops.bean.Node;
import com.dianrong.common.techops.helper.CustomizeBeanConverter;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.OrganizationDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.OrganizationParam;
import com.dianrong.common.uniauth.common.bean.request.OrganizationQuery;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 组织关系处理Action.
 */
@RestController @RequestMapping("organization") public class OrganizationAction {

  @Resource private UARWFacade uarwFacade;

  /**
   * 获取组织关系信息,以Tree的形式返回.
   */
  @RequestMapping(value = "/tree", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
          + "and principal.permMap['DOMAIN'] != null "
          + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<?> getOrganizationTree(@RequestBody OrganizationParam organizationParam) {
    Response<OrganizationDto> organizationDtoResponse =
        uarwFacade.getOrganizationRWResource().getOrganizationTree(organizationParam);
    if (!CollectionUtils.isEmpty(organizationDtoResponse.getInfo())) {
      return organizationDtoResponse;
    }
    OrganizationDto organizationDto = organizationDtoResponse.getData();
    if (organizationDto != null) {
      List<Node> nodes;
      if (organizationParam.getOnlyNeedGrpInfo() != null && organizationParam
          .getOnlyNeedGrpInfo()) {
        nodes = CustomizeBeanConverter.convert(Arrays.asList((GroupDto) organizationDto), false);
      } else {
        // 是 owner 才能查看到的组集合
        nodes = CustomizeBeanConverter.convert(Arrays.asList((GroupDto) organizationDto), null);
        if (!CollectionUtils.isEmpty(nodes)) {
          Iterator<Node> iterator = nodes.iterator();
          while (iterator.hasNext()) {
            Node n = iterator.next();
            if (!isOwnerThisNode(n)) {
              iterator.remove();
            }
          }
        }
      }
      return Response.success(nodes);
    }
    return Response.success();
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
      Iterator<Node> iterator = node.getChildren().iterator();
      while (iterator.hasNext()) {
        Node child = iterator.next();
        if (isOwnerThisNode(child)) {
          owner = true;
        } else {
          iterator.remove();
        }
      }
      return owner;
    }
    return false;
  }

  @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
          + "and principal.permMap['DOMAIN'] != null "
          + "and principal.permMap['DOMAIN'].contains('techops') ")
  public Response<Node> getOrganizationDetailsById(@RequestParam("id") Integer organizationId) {
    OrganizationQuery organizationQuery = new OrganizationQuery();
    organizationQuery.setId(organizationId);
    Response<PageDto<OrganizationDto>> pageDtoResponse =
        uarwFacade.getOrganizationRWResource().queryOrganization(organizationQuery);
    OrganizationDto organizationDto = pageDtoResponse.getData().getData().get(0);
    Node node = new Node();
    node.setId(organizationDto.getId().toString());
    node.setCode(organizationDto.getCode());
    node.setType(AppConstants.NODE_TYPE_GROUP);
    node.setLabel(organizationDto.getName());
    node.setDescription(organizationDto.getDescription());
    return Response.success(node);
  }

  @RequestMapping(value = "/query", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
          + "and principal.permMap['DOMAIN'] != null "
          + "and principal.permMap['DOMAIN'].contains('techops') ")
  public Response<PageDto<OrganizationDto>> searchOrganization(
      @RequestBody OrganizationQuery organizationQuery) {
    return uarwFacade.getOrganizationRWResource().queryOrganization(organizationQuery);
  }

  @RequestMapping(value = "/modify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
          + "and principal.permMap['DOMAIN'] != null "
          + "and principal.permMap['DOMAIN'].contains('techops') "
          + "and hasPermission(#organizationParam,'PERM_ORGANIZATION_OWNER')")
  public Response<OrganizationDto> modifyOrganization(@RequestBody OrganizationParam organizationParam) {
    return uarwFacade.getOrganizationRWResource().updateOrganization(organizationParam);
  }

  @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
          + "and principal.permMap['DOMAIN'] != null "
          + "and principal.permMap['DOMAIN'].contains('techops') "
          + "and hasPermission(#organizationParam,'PERM_ORGANIZATION_OWNER')")
  public Response<OrganizationDto> addNewOrganizationIntoOrganization(
      @RequestBody OrganizationParam organizationParam) {
    return uarwFacade.getOrganizationRWResource()
        .addNewOrganizationIntoOrganization(organizationParam);
  }

  @RequestMapping(value = "/del", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
          + "and principal.permMap['DOMAIN'] != null "
          + "and principal.permMap['DOMAIN'].contains('techops') "
          + "and hasPermission(#organizationParam,'PERM_ORGANIZATION_OWNER')")
  public Response<OrganizationDto> deleteOrganization(@RequestBody OrganizationParam organizationParam) {
    return uarwFacade.getOrganizationRWResource().deleteOrganization(organizationParam);
  }

  @RequestMapping(value = "/add-user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
          + "and principal.permMap['DOMAIN'] != null "
          + "and principal.permMap['DOMAIN'].contains('techops') "
          + "and hasPermission(#userListParam,'PERM_ORGANIZATION_OWNER')")
  public Response<Void> addUsersIntoOrganization(@RequestBody UserListParam userListParam) {
    return uarwFacade.getOrganizationRWResource().addUsersIntoOrganization(userListParam);
  }

  @RequestMapping(value = "/delete-user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
          + "and principal.permMap['DOMAIN'] != null "
          + "and principal.permMap['DOMAIN'].contains('techops') "
          + "and hasPermission(#userListParam,'PERM_ORGANIZATION_OWNER')")
  public Response<Void> removeUsersFromOrganization(@RequestBody UserListParam userListParam) {
    return uarwFacade.getOrganizationRWResource().removeUsersFromOrganization(userListParam);
  }

  @RequestMapping(value = "/move-user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
          + "and principal.permMap['DOMAIN'] != null "
          + "and principal.permMap['DOMAIN'].contains('techops') "
          + "and hasPermission(#userListParam,'PERM_ORGANIZATION_OWNER')")
  public Response<Void> moveOrganizationUser(@RequestBody UserListParam userListParam) {
    return uarwFacade.getOrganizationRWResource().moveOrganizationUser(userListParam);
  }

  @RequestMapping(value = "/move", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize(
      "hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN') "
          + "and principal.permMap['DOMAIN'] != null "
          + "and principal.permMap['DOMAIN'].contains('techops') "
          + "and hasPermission(#userListParam,'PERM_ORGANIZATION_OWNER')")
  public Response<Void> moveOrganization(@RequestBody OrganizationParam organizationParam) {
    return uarwFacade.getOrganizationRWResource().moveOrganization(organizationParam);
  }
}
