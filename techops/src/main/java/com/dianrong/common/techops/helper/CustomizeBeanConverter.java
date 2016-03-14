package com.dianrong.common.techops.helper;

import com.dianrong.common.techops.bean.Node;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arc on 2/3/2016.
 */
public class CustomizeBeanConverter {

    public static RoleDto convert(RoleDto roleDto) {
        if(roleDto == null) {
            return null;
        } else {
            RoleDto returnRoleDto = new RoleDto();
            returnRoleDto.setDescription(roleDto.getDescription()).
                    setId(roleDto.getId()).
                    setStatus(roleDto.getStatus()).
                    setName(roleDto.getName()).
                    setRoleCode(roleDto.getRoleCode());
            return returnRoleDto;
        }
    }

    public static List<Node> convert(List<GroupDto> groupDtoList) {
        if(groupDtoList == null) {
            return null;
        } else {
            List<Node> nodes = new ArrayList<>();
            for(GroupDto groupDto : groupDtoList) {
                Node groupNode = new Node();
                groupNode.setId(groupDto.getId().toString());
                groupNode.setLabel(groupDto.getName());
                groupNode.setCode(groupDto.getCode());
                groupNode.setType(AppConstants.NODE_TYPE_GROUP);
                groupNode.setChecked(groupDto.getRoleChecked());
                groupNode.setOwnerMarkup(groupDto.getOwnerMarkup());
                nodes.add(groupNode);
                List<UserDto> userDtos = groupDto.getUsers();
                List<GroupDto> groupDtos = groupDto.getGroups();
                if(!CollectionUtils.isEmpty(userDtos) || !CollectionUtils.isEmpty(groupDtos)) {
                    List<Node> subNodes = new ArrayList<>();
                    groupNode.setChildren(subNodes);
                    recursiveConvertGroupDtoToNode(groupDto, subNodes);
                }
            }
            if(!CollectionUtils.isEmpty(nodes)) {
                return nodes;
            } else {
                return null;
            }
        }
    }

    public static void recursiveConvertGroupDtoToNode(GroupDto groupDto, List<Node> subNodes) {

        List<UserDto> subUserNodes = groupDto.getUsers();
        if(!CollectionUtils.isEmpty(subUserNodes)) {
            for(UserDto userDto : subUserNodes) {
                Node subUserNode = new Node();
                subUserNode.setId(userDto.getId().toString());
                subUserNode.setLabel(userDto.getEmail());
                subUserNode.setChecked(userDto.getRoleChecked());
                Byte userGroupType = userDto.getUserGroupType();
                if(AppConstants.ZERO_Byte.equals(userGroupType)) {
                    subUserNode.setType(AppConstants.NODE_TYPE_MEMBER_USER);
                } else if(AppConstants.ONE_Byte.equals(userGroupType)){
                    subUserNode.setType(AppConstants.NODE_TYPE_OWNER_USER);
                }
                subNodes.add(subUserNode);
            }
        }
        List<GroupDto> subGroupDtoNodes = groupDto.getGroups();
        if(!CollectionUtils.isEmpty(subGroupDtoNodes)) {
            for(GroupDto subGroupDtoNode : subGroupDtoNodes) {
                Node groupNode = new Node();
                groupNode.setId(subGroupDtoNode.getId().toString());
                groupNode.setCode(subGroupDtoNode.getCode());
                groupNode.setLabel(subGroupDtoNode.getName());
                groupNode.setType(AppConstants.NODE_TYPE_GROUP);
                groupNode.setChecked(subGroupDtoNode.getRoleChecked());
                groupNode.setOwnerMarkup(subGroupDtoNode.getOwnerMarkup());
                subNodes.add(groupNode);

                List<UserDto> subSubUserDtoNodes = subGroupDtoNode.getUsers();
                List<GroupDto> subSubGroupDtoNodes = subGroupDtoNode.getGroups();
                if(!CollectionUtils.isEmpty(subSubGroupDtoNodes) || !CollectionUtils.isEmpty(subSubUserDtoNodes)) {
                    List<Node> subSubNodes = new ArrayList<>();
                    groupNode.setChildren(subSubNodes);
                    recursiveConvertGroupDtoToNode(subGroupDtoNode, subSubNodes);
                }
            }
        }
    }
}
