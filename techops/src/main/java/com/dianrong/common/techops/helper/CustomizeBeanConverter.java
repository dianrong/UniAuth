package com.dianrong.common.techops.helper;

import com.dianrong.common.techops.bean.Node;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.CollectionUtils;

/**
 * Created by Arc on 2/3/2016.
 */
public class CustomizeBeanConverter {

  /**
   * 转化Dto的工具方法.
   */
  public static RoleDto convert(RoleDto roleDto) {
    if (roleDto == null) {
      return null;
    } else {
      RoleDto returnRoleDto = new RoleDto();
      returnRoleDto.setDescription(roleDto.getDescription()).setId(roleDto.getId())
          .setStatus(roleDto.getStatus()).setName(roleDto.getName())
          .setRoleCode(roleDto.getRoleCode());
      return returnRoleDto;
    }
  }

  /**
   * 转化Dto的工具方法.
   */
  public static List<Node> convert(List<GroupDto> groupDtoList, Boolean roleCheckOrTagCheck) {
    if (groupDtoList == null) {
      return null;
    } else {
      List<Node> nodes = new ArrayList<>();
      for (GroupDto groupDto : groupDtoList) {
        Node groupNode = new Node();
        groupNode.setId(groupDto.getId().toString());
        groupNode.setLabel(groupDto.getName());
        groupNode.setCode(groupDto.getCode());
        groupNode.setType(AppConstants.NODE_TYPE_GROUP);
        if (roleCheckOrTagCheck == null || roleCheckOrTagCheck) {
          groupNode.setChecked(groupDto.getRoleChecked());
        } else {
          groupNode.setChecked(groupDto.getTagChecked());
        }
        groupNode.setOwnerMarkup(groupDto.getOwnerMarkup());
        groupNode.setIsRootGrp(groupDto.getIsRootGrp());
        nodes.add(groupNode);
        List<UserDto> userDtos = groupDto.getUsers();
        List<GroupDto> groupDtos = groupDto.getGroups();
        if (!CollectionUtils.isEmpty(userDtos) || !CollectionUtils.isEmpty(groupDtos)) {
          List<Node> subNodes = new ArrayList<>();
          groupNode.setChildren(subNodes);
          recursiveConvertGroupDtoToNode(groupDto, subNodes, roleCheckOrTagCheck);
        }
      }
      if (!CollectionUtils.isEmpty(nodes)) {
        return nodes;
      } else {
        return null;
      }
    }
  }

  /**
   * 转化Dto的工具方法.
   */
  public static void recursiveConvertGroupDtoToNode(GroupDto groupDto, List<Node> subNodes,
      Boolean roleCheckOrTagCheck) {

    List<UserDto> subUserNodes = groupDto.getUsers();
    if (!CollectionUtils.isEmpty(subUserNodes)) {
      for (UserDto userDto : subUserNodes) {
        Node subUserNode = new Node();
        subUserNode.setId(userDto.getId().toString());
        subUserNode.setLabel(userDto.getAccount());
        if (roleCheckOrTagCheck == null || roleCheckOrTagCheck) {
          subUserNode.setChecked(userDto.getRoleChecked());
        } else {
          subUserNode.setChecked(userDto.getTagChecked());
        }
        Byte userGroupType = userDto.getUserGroupType();
        if (AppConstants.ZERO_BYTE.equals(userGroupType)) {
          subUserNode.setType(AppConstants.NODE_TYPE_MEMBER_USER);
        } else if (AppConstants.ONE_BYTE.equals(userGroupType)) {
          subUserNode.setType(AppConstants.NODE_TYPE_OWNER_USER);
        }
        subNodes.add(subUserNode);
      }
    }
    List<GroupDto> subGroupDtoNodes = groupDto.getGroups();
    if (!CollectionUtils.isEmpty(subGroupDtoNodes)) {
      for (GroupDto subGroupDtoNode : subGroupDtoNodes) {
        Node groupNode = new Node();
        groupNode.setId(subGroupDtoNode.getId().toString());
        groupNode.setCode(subGroupDtoNode.getCode());
        groupNode.setLabel(subGroupDtoNode.getName());
        groupNode.setType(AppConstants.NODE_TYPE_GROUP);
        if (roleCheckOrTagCheck == null || roleCheckOrTagCheck) {
          groupNode.setChecked(subGroupDtoNode.getRoleChecked());
        } else {
          groupNode.setChecked(subGroupDtoNode.getTagChecked());
        }
        groupNode.setOwnerMarkup(subGroupDtoNode.getOwnerMarkup());
        subNodes.add(groupNode);

        List<UserDto> subSubUserDtoNodes = subGroupDtoNode.getUsers();
        List<GroupDto> subSubGroupDtoNodes = subGroupDtoNode.getGroups();
        if (!CollectionUtils.isEmpty(subSubGroupDtoNodes)
            || !CollectionUtils.isEmpty(subSubUserDtoNodes)) {
          List<Node> subSubNodes = new ArrayList<>();
          groupNode.setChildren(subSubNodes);
          recursiveConvertGroupDtoToNode(subGroupDtoNode, subSubNodes, roleCheckOrTagCheck);
        }
      }
    }
  }
}
