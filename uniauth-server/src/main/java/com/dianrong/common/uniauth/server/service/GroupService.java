package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.server.data.entity.*;
import com.dianrong.common.uniauth.server.data.mapper.*;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.AppConstants;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.UniBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by Arc on 14/1/16.
 */
@Service
public class GroupService {
    @Autowired
    private GrpMapper grpMapper;
    @Autowired
    private GrpPathMapper grpPathMapper;
    @Autowired
    private GrpRoleMapper grpRoleMapper;
    @Autowired
    private UserGrpMapper userGrpMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public GroupDto createDescendantGroup(GroupParam groupParam) {
        Grp grp = BeanConverter.convert(groupParam);
        Date now = new Date();
        grp.setStatus((byte)0);
        grp.setCreateDate(now);
        grp.setLastUpdate(now);
        grpMapper.insert(grp);
        GrpPath grpPath = new GrpPath();
        grpPath.setDeepth((byte)0);
        grpPath.setDescendant(grp.getId());
        grpPath.setAncestor(groupParam.getTargetGroupId());
        grpPathMapper.insertNewNode(grpPath);
        return BeanConverter.convert(grp);
    }

    @Transactional
    public void deleteGroup(Integer groupId) {
        GrpPathExample grpPathAncestorExample = new GrpPathExample();
        grpPathAncestorExample.createCriteria().andAncestorEqualTo(groupId);
        int desOfDes = grpPathMapper.countByExample(grpPathAncestorExample);
        if(desOfDes > 1) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("group.parameter.delgroup"));
        }
        // cascading delete the users in group and the roles on group.
        UserGrpExample userGrpExample = new UserGrpExample();
        userGrpExample.createCriteria().andGrpIdEqualTo(groupId);
        userGrpMapper.deleteByExample(userGrpExample);
        GrpRoleExample grpRoleExample = new GrpRoleExample();
        grpRoleExample.createCriteria().andGrpIdEqualTo(groupId);
        grpRoleMapper.deleteByExample(grpRoleExample);
        GrpPathExample grpPathDescendantExample = new GrpPathExample();
        grpPathDescendantExample.createCriteria().andDescendantEqualTo(groupId);
        grpPathMapper.deleteByExample(grpPathDescendantExample);
        grpMapper.deleteByPrimaryKey(groupId);
    }

    @Transactional
    public GroupDto updateGroup(GroupParam groupParam) {
        Grp grp = BeanConverter.convert(groupParam);
        grp.setLastUpdate(new Date());
        grpMapper.updateByPrimaryKeySelective(grp);
        return BeanConverter.convert(grp);
    }

    @Transactional
    public void addUsersIntoGroup(UserListParam userListParam) {
        Integer groupId = userListParam.getGroupId();
        List<Long> userIds = userListParam.getUserIds();
        if(groupId == null || CollectionUtils.isEmpty(userIds)) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "groupId, userIds"));
        }
        Boolean normalMember = userListParam.getNormalMember();
        UserGrpExample userGrpExample = new UserGrpExample();
        List<UserGrp> userGrps = null;
        if(normalMember == null || normalMember) {
            userGrpExample.createCriteria().andGrpIdEqualTo(groupId).andTypeEqualTo((byte)0);
            userGrps = userGrpMapper.selectByExample(userGrpExample);
        } else {
            userGrpExample.createCriteria().andGrpIdEqualTo(groupId).andTypeEqualTo((byte)1);
            userGrps = userGrpMapper.selectByExample(userGrpExample);
        }
        Set<Long> userIdSet = new HashSet<>();
        if(!CollectionUtils.isEmpty(userGrps)) {
            for (UserGrp userGrp : userGrps) {
                userIdSet.add(userGrp.getUserId());
            }
        }
        for(Long userId : userIds) {
            if(!userIdSet.contains(userId)) {
                UserGrp userGrp = new UserGrp();
                userGrp.setGrpId(groupId);
                userGrp.setUserId(userId);
                if(normalMember == null || normalMember) {
                    userGrp.setType((byte)0);
                } else {
                    userGrp.setType((byte)1);
                }
                userGrpMapper.insert(userGrp);
            }
        }
    }

    @Transactional
    public void removeUsersFromGroup(UserListParam userListParam) {
        Integer groupId = userListParam.getGroupId();
        List<Long> userIds = userListParam.getUserIds();
        if(groupId == null || CollectionUtils.isEmpty(userIds)) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "groupId, userIds"));
        }
        Boolean normalMember = userListParam.getNormalMember();
        UserGrpExample userGrpExample = new UserGrpExample();
        List<UserGrp> userGrps = null;
        if(normalMember == null || normalMember) {
            userGrpExample.createCriteria().andGrpIdEqualTo(groupId).andTypeEqualTo((byte)0);
            userGrps = userGrpMapper.selectByExample(userGrpExample);
        } else {
            userGrpExample.createCriteria().andGrpIdEqualTo(groupId).andTypeEqualTo((byte)1);
            userGrps = userGrpMapper.selectByExample(userGrpExample);
        }
        Set<Long> userIdSet = new HashSet<>();
        if(!CollectionUtils.isEmpty(userGrps)) {
            for (UserGrp userGrp : userGrps) {
                userIdSet.add(userGrp.getUserId());
            }
        }
        for(Long userId : userIds) {
            if(userIdSet.contains(userId)) {
                UserGrpExample userGrpExample1 = new UserGrpExample();
                UserGrpExample.Criteria criteria = userGrpExample1.createCriteria().andGrpIdEqualTo(groupId).andUserIdEqualTo(userId);
                if(normalMember == null || normalMember) {
                    criteria.andTypeEqualTo((byte)0);
                } else {
                    criteria.andTypeEqualTo((byte)1);
                }
                userGrpMapper.deleteByExample(userGrpExample1);
            }
        }
    }

    @Transactional
    public void saveRolesToGroup(GroupParam groupParam) {
        Integer groupId = groupParam.getId();
        List<Integer> roleIds = groupParam.getRoleIds();
        if(groupId == null || CollectionUtils.isEmpty(roleIds)) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "groupId, roleIds"));
        }
        GrpRoleExample grpRoleExample = new GrpRoleExample();
        grpRoleExample.createCriteria().andGrpIdEqualTo(groupId);
        List<GrpRoleKey> grpRoleKeys = grpRoleMapper.selectByExample(grpRoleExample);
        Set<Integer> roleIdSet = new HashSet<>();
        if(!CollectionUtils.isEmpty(grpRoleKeys)) {
            for(GrpRoleKey grpRoleKey : grpRoleKeys) {
                roleIdSet.add(grpRoleKey.getRoleId());
            }
        }
        for(Integer roleId : roleIds) {
            if(!roleIdSet.contains(roleId)) {
                GrpRoleKey grpRoleKey = new GrpRoleKey();
                grpRoleKey.setGrpId(groupId);
                grpRoleKey.setRoleId(roleId);
                grpRoleMapper.insert(grpRoleKey);
            }
        }
    }

    public List<RoleDto> getAllRolesToGroup(Integer groupId, Integer domainId) {
        if(groupId == null || domainId == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "groupId, domainId"));
        }
        if(grpMapper.selectByPrimaryKey(groupId) == null) {
            return null;
        }
        GrpRoleExample grpRoleExample = new GrpRoleExample();
        grpRoleExample.createCriteria().andGrpIdEqualTo(groupId);
        List<GrpRoleKey> grpRoleKeys = grpRoleMapper.selectByExample(grpRoleExample);
        Set<Integer> checkedRoleIds = new HashSet<>();
        if(!CollectionUtils.isEmpty(grpRoleKeys)) {
            for(GrpRoleKey grpRoleKey : grpRoleKeys) {
                checkedRoleIds.add(grpRoleKey.getRoleId());
            }
        }
        RoleExample roleExample = new RoleExample();
        roleExample.createCriteria().andDomainIdEqualTo(domainId);
        List<Role> roles = roleMapper.selectByExample(roleExample);
        if(!CollectionUtils.isEmpty(roles)) {
            List<RoleDto> roleDtos = new ArrayList<>();
            for(Role role : roles) {
                if(role.getStatus().equals(AppConstants.ONE_Byte)) {
                    continue;
                }
                RoleDto roleDto = BeanConverter.convert(role);
                if(checkedRoleIds.contains(roleDto.getId())) {
                    roleDto.setChecked(Boolean.TRUE);
                } else {
                    roleDto.setChecked(Boolean.FALSE);
                }
                roleDtos.add(roleDto);
            }
            return roleDtos;
        } else {
            return null;
        }
    }

    public List<UserDto> getGroupOwners(Integer groupId) {
        List<User> users = userMapper.getGroupOwners(groupId);
        if(!CollectionUtils.isEmpty(users)) {
            List<UserDto> userDtos = new ArrayList<>();
            for(User user : users) {
                if(user.getStatus().equals(AppConstants.ONE_Byte)) {
                    continue;
                }
                UserDto userDto = BeanConverter.convert(user);
                userDtos.add(userDto);
            }
            return userDtos;
        } else {
            return null;
        }
    }

    public GroupDto getGroupTree(Integer groupId, Boolean onlyShowGroup) {
        return null;
    }
}
