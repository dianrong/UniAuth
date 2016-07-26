package com.dianrong.common.uniauth.server.resource;

import java.util.ArrayList;
import java.util.List;

import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.*;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Grp;
import com.dianrong.common.uniauth.server.service.GroupService;
import com.dianrong.common.uniauth.sharerw.interfaces.IGroupRWResource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;

@RestController
public class GroupResource implements IGroupRWResource {
	
	private static final Logger logger = Logger.getLogger(GroupResource.class);

	@Autowired
	private GroupService groupService;

	@Override
	public Response<PageDto<GroupDto>> queryGroup(GroupQuery groupQuery) {
		PageDto<GroupDto> groupDtoPageDto = groupService.searchGroup(groupQuery.getUserGroupType(), groupQuery.getUserId(),
				groupQuery.getId(), groupQuery.getGroupIds(),groupQuery.getName(),groupQuery.getCode(), groupQuery.getDescription(),groupQuery.getStatus(),
				groupQuery.getTagId(),groupQuery.getNeedTag(), groupQuery.getNeedUser(), groupQuery.getPageNumber(),
				groupQuery.getPageSize());
		return Response.success(groupDtoPageDto);
	}

	@Override
	public Response<GroupDto> getGroupTree(GroupParam groupParam) {
		GroupDto grpDto = groupService.getGroupTree(groupParam.getId(), groupParam.getCode(),
				groupParam.getOnlyShowGroup(), groupParam.getUserGroupType(), groupParam.getRoleId(), groupParam.getTagId(),
				groupParam.getNeedOwnerMarkup(), groupParam.getOpUserId());
		return Response.success(grpDto);
	}

	@Override
	public Response<Void> addUsersIntoGroup(UserListParam userListParam) {
		groupService.addUsersIntoGroup(userListParam.getGroupId(),userListParam.getUserIds(),userListParam.getNormalMember());
		return Response.success();
	}

	@Override
	public Response<Void> removeUsersFromGroup(UserListParam userListParam) {
		groupService.removeUsersFromGroup(userListParam.getUserIdGroupIdPairs(),userListParam.getNormalMember());
		return Response.success();
	}

	@Override
	public Response<GroupDto> addNewGroupIntoGroup(GroupParam groupParam) {
		GroupDto groupDto = groupService.createDescendantGroup(groupParam);
		return Response.success(groupDto);
	}

	@Override
	public Response<GroupDto> updateGroup(GroupParam groupParam) {
		GroupDto groupDto = groupService.updateGroup(groupParam.getId(),groupParam.getCode(),
				groupParam.getName(),groupParam.getStatus(),groupParam.getDescription());
		return Response.success(groupDto);
	}
	
	@Override
	public Response<GroupDto> deleteGroup(GroupParam groupParam) {
		//将当前节点设置为失效
		Response<GroupDto> resp = updateGroup(groupParam);
		
		//找出子节点，然后设置为失效
		try{
			List<Grp> children = groupService.queryGroupByAncestor(groupParam.getId());
			if(children != null && !children.isEmpty()){
				for(Grp grp : children){
					if(grp.getId().equals(groupParam.getId())){
						continue;
					}
					groupService.updateGroup(grp.getId(), grp.getCode(), grp.getName(), AppConstants.ONE_Byte, grp.getDescription());
				}
			}
		}catch(Exception e){
			logger.error("delete child group error ,groupid:"+groupParam.getId(), e);
		}
		
		return resp;
	}

	@Override
	public Response<List<UserDto>> getGroupOwners(PrimaryKeyParam primaryKeyParam) {
		List<UserDto> userDtos = groupService.getGroupOwners(primaryKeyParam.getId());
		return Response.success(userDtos);
	}

	@Override
	public Response<List<RoleDto>> getAllRolesToGroupAndDomain(GroupParam groupParam) {
		List<RoleDto> roleDtos = groupService.getAllRolesToGroupAndDomain(groupParam.getId(),groupParam.getDomainId());
		return Response.success(roleDtos);
	}

	@Override
	public Response<Void> saveRolesToGroup(GroupParam groupParam) {
		groupService.saveRolesToGroup(groupParam.getId(), groupParam.getRoleIds());
		return Response.success();
	}

	@Override
	public Response<Void> checkOwner(GroupParam groupParam) {
		Long opUserId = groupParam.getOpUserId();
		List<Integer> grpIds = groupParam.getTargetGroupIds();
		if(CollectionUtils.isEmpty(grpIds) && groupParam.getTargetGroupId() != null) {
			grpIds = new ArrayList<>();
			grpIds.add(groupParam.getTargetGroupId());
		} else if(!CollectionUtils.isEmpty(grpIds) && groupParam.getTargetGroupId() != null) {
			grpIds.add(groupParam.getTargetGroupId());
		}
		groupService.checkOwner(opUserId, grpIds);
		return Response.success();
	}

	@Override
	public Response<Void> replaceRolesToGroup(GroupParam groupParam) {
		groupService.replaceRolesToGroupUnderDomain(groupParam.getId(), groupParam.getRoleIds(), groupParam.getDomainId());
		return Response.success();
	}
	
	@Override
	public Response<List<TagDto>> queryTagsWithChecked(GroupParam groupParam) {
		List<TagDto> tagDtos = groupService.searchTagsWithrChecked(groupParam.getId(), groupParam.getDomainId());
		return Response.success(tagDtos);
	}
	
	@Override
	public Response<Void> replaceTagsToGrp(GroupParam groupParam) {
		groupService.replaceTagsToGroup(groupParam.getId(), groupParam.getTagIds());
		return Response.success();
	}

}
