package com.dianrong.common.uniauth.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Resource;

import com.dianrong.common.uniauth.server.data.entity.*;
import com.dianrong.common.uniauth.server.data.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Linkage;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.ext.UserExt;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;
import com.dianrong.common.uniauth.server.util.UniBundle;

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
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private GrpTagMapper grpTagMapper;
    @Autowired
    private UserTagMapper userTagMapper;

    /**.
	 * 进行组数据过滤的filter
	 */
	@Resource(name="groupDataFilter")
	private DataFilter dataFilter;

    public PageDto<GroupDto> searchGroup(Byte userGroupType, Long userId, Integer id, String name, String code,
                                         String description, Byte status, Integer tagId,
                                         Integer pageNumber, Integer pageSize) {

        if(id != null) {
            GroupDto groupDto = BeanConverter.convert(grpMapper.selectByPrimaryKey(id));
            if(groupDto != null) {
                return new PageDto<>(0, 1, 1, Arrays.asList(groupDto));
            } else {
                return null;
            }
        }

        if(code != null) {
            GrpExample grpExample = new GrpExample();
            grpExample.createCriteria().andCodeEqualTo(code);
            List<Grp> grps = grpMapper.selectByExample(grpExample);
            if(!CollectionUtils.isEmpty(grps)) {
                GroupDto groupDto = BeanConverter.convert(grps.get(0));
                return new PageDto<>(0, 1, 1, Arrays.asList(groupDto));
            } else {
                return null;
            }
        }

        if(pageNumber == null || pageSize == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "pageNumber, pageSize"));
        }

        GrpExample grpExample = new GrpExample();
        grpExample.setOrderByClause("create_date desc");
        grpExample.setPageOffSet(pageNumber * pageSize);
        grpExample.setPageSize(pageSize);
        GrpExample.Criteria criteria = grpExample.createCriteria();
        if(!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if(!StringUtils.isEmpty(description)) {
            criteria.andDescriptionLike("%" + description + "%");
        }
        if(status != null) {
            criteria.andStatusEqualTo(status);
        }
        // join user table to find group
        if(userId != null) {
            UserGrpExample userGrpExample = new UserGrpExample();
            UserGrpExample.Criteria userGrpExampleCriteria = userGrpExample.createCriteria();
            userGrpExampleCriteria.andUserIdEqualTo(userId);
            if(userGroupType != null ) {
                userGrpExampleCriteria.andTypeEqualTo(userGroupType);
            } else {
                userGrpExampleCriteria.andTypeEqualTo(AppConstants.ZERO_Byte);
            }
            List<UserGrpKey> userGrpKeys = userGrpMapper.selectByExample(userGrpExample);
            List<Integer> grpIds = new ArrayList<>();
            if(!CollectionUtils.isEmpty(userGrpKeys)) {
                for (UserGrpKey userGrpKey : userGrpKeys) {
                    grpIds.add(userGrpKey.getGrpId());
                }
            }
            if(grpIds.isEmpty()) {
                return null;
            } else {
                criteria.andIdIn(grpIds);
            }
        }
        if(tagId != null) {
            GrpTagExample grpTagExample = new GrpTagExample();
            grpTagExample.createCriteria().andTagIdEqualTo(tagId);
            List<GrpTagKey>  grpTagKeys = grpTagMapper.selectByExample(grpTagExample);
            if(!CollectionUtils.isEmpty(grpTagKeys)) {
                List<Integer> grpTagKeysGrpIds = new ArrayList<>();
                for (GrpTagKey grpTagKey : grpTagKeys) {
                    grpTagKeysGrpIds.add(grpTagKey.getGrpId());
                }
                criteria.andIdIn(grpTagKeysGrpIds);
            } else {
                return null;
            }
        }

        List<Grp> grps = grpMapper.selectByExample(grpExample);
        if(!CollectionUtils.isEmpty(grps)) {
            int count = grpMapper.countByExample(grpExample);
            List<GroupDto> groupDtos = new ArrayList<>();
            for(Grp grp : grps) {
                groupDtos.add(BeanConverter.convert(grp));
            }
            return new PageDto<>(pageNumber,pageSize,count,groupDtos);
        } else {
            return null;
        }

    }

    @Transactional
    public GroupDto createDescendantGroup(GroupParam groupParam) {
        Integer targetGroupId = groupParam.getTargetGroupId();
        String groupCode = groupParam.getCode();
        if(targetGroupId == null || groupCode == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "targetGroupId, groupCode"));
        }
        
        //父group需要存在
        dataFilter.dataFilter(FieldType.FIELD_TYPE_ID, targetGroupId, FilterType.FILTER_TYPE_NO_DATA);
        //子group不能存在
        dataFilter.dataFilter(FieldType.FIELD_TYPE_CODE, groupCode, FilterType.FILTER_TYPE_EXSIT_DATA);
//        GrpExample grpExample = new GrpExample();
//        grpExample.createCriteria().andCodeEqualTo(groupCode);
//        List<Grp> grps = grpMapper.selectByExample(grpExample);
//        if(!CollectionUtils.isEmpty(grps)) {
//            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("group.parameter.code", groupCode));
//        }
        Grp grp = BeanConverter.convert(groupParam);
        Date now = new Date();
        grp.setStatus(AppConstants.ZERO_Byte);
        grp.setCreateDate(now);
        grp.setLastUpdate(now);
        grpMapper.insert(grp);
        GrpPath grpPath = new GrpPath();
        grpPath.setDeepth(AppConstants.ZERO_Byte);
        grpPath.setDescendant(grp.getId());
        grpPath.setAncestor(targetGroupId);
        grpPathMapper.insertNewNode(grpPath);
        Integer count = grpMapper.selectNameCountBySameLayerGrpId(grp.getId());
        if(count !=null && count > 1) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("group.parameter.name", grp.getName()));
        }
        return BeanConverter.convert(grp);
    }

    @Transactional
    public GroupDto updateGroup(Integer groupId, String groupCode, String groupName, Byte status, String description) {
    	CheckEmpty.checkEmpty(groupId, "groupId");
        Grp grp = grpMapper.selectByPrimaryKey(groupId);
        CheckEmpty.checkEmpty(groupCode, "groupCode");
        if(status != null) {
            ParamCheck.checkStatus(status);
        } 
        if(grp == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", groupId, Grp.class.getSimpleName()));
        }
        
        //启用或者启用状态的修改
        if((status != null && status == AppConstants.ZERO_Byte) || (status == null && grp.getStatus() == AppConstants.ZERO_Byte)){
	        //判断是否存在重复的code
	        dataFilter.fileterFieldValueIsExsist(FieldType.FIELD_TYPE_CODE, groupId, groupCode);
        }
        
        grp.setName(groupName);
        grp.setStatus(status);
        grp.setDescription(description);
        grp.setCode(groupCode);
        grp.setLastUpdate(new Date());
        grpMapper.updateByPrimaryKeySelective(grp);
        Integer count  = grpMapper.selectNameCountBySameLayerGrpId(grp.getId());
        if(count !=null && count > 1) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("group.parameter.name", grp.getName()));
        }
        return BeanConverter.convert(grp);
    }

    @Transactional
    public void addUsersIntoGroup(Integer groupId, List<Long> userIds, Boolean normalMember) {
        if(groupId == null || CollectionUtils.isEmpty(userIds)) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "groupId, userIds"));
        }
        UserGrpExample userGrpExample = new UserGrpExample();
        List<UserGrpKey> userGrpKeys;
        if(normalMember == null || normalMember) {
            userGrpExample.createCriteria().andGrpIdEqualTo(groupId).andTypeEqualTo(AppConstants.ZERO_Byte);
            userGrpKeys = userGrpMapper.selectByExample(userGrpExample);
        } else {
            userGrpExample.createCriteria().andGrpIdEqualTo(groupId).andTypeEqualTo(AppConstants.ONE_Byte);
            userGrpKeys = userGrpMapper.selectByExample(userGrpExample);
        }
        Set<Long> userIdSet = new HashSet<>();
        if(!CollectionUtils.isEmpty(userGrpKeys)) {
            for (UserGrpKey userGrpKey : userGrpKeys) {
                userIdSet.add(userGrpKey.getUserId());
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
    public void removeUsersFromGroup(List<Linkage<Long, Integer>> userIdGrpIdPairs , Boolean normalMember) {

        if(CollectionUtils.isEmpty(userIdGrpIdPairs)) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "groupId, userId pair"));
        }

        for(Linkage<Long, Integer> linkage : userIdGrpIdPairs) {
            Long userId = linkage.getEntry1();
            Integer grpId = linkage.getEntry2();
            UserGrpExample userGrpExample = new UserGrpExample();
            userGrpExample.createCriteria().andGrpIdEqualTo(grpId).andUserIdEqualTo(userId);
            if(normalMember == null || normalMember) {
                userGrpExample.createCriteria().andTypeEqualTo(AppConstants.ZERO_byte);
            } else {
                userGrpExample.createCriteria().andTypeEqualTo(AppConstants.ONE_byte);
            }
            userGrpMapper.deleteByExample(userGrpExample);
        }

    }

    @Transactional
    public void saveRolesToGroup(Integer groupId, List<Integer> roleIds) {
        if(groupId == null || CollectionUtils.isEmpty(roleIds)) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.parameter.empty", "groupId, roleIds"));
        }
        GrpRoleExample grpRoleExample = new GrpRoleExample();
        grpRoleExample.createCriteria().andGrpIdEqualTo(groupId);
        List<GrpRoleKey> grpRoleKeys = grpRoleMapper.selectByExample(grpRoleExample);
        Set<Integer> roleIdSet = new TreeSet<>();
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

    public List<RoleDto> getAllRolesToGroupAndDomain(Integer groupId, Integer domainId) {
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
        roleExample.createCriteria().andDomainIdEqualTo(domainId).andStatusEqualTo(AppConstants.ZERO_Byte);
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

    public GroupDto getGroupTree(Integer groupId, String groupCode, Boolean onlyShowGroup, Byte userGroupType, Integer roleId,
                                 Integer tagId, Boolean needOwnerMarkup, Long ownerId) {
        Grp rootGrp;
        if(groupCode == null && (groupId == null || Integer.valueOf(-1).equals(groupId))) {
            GrpExample grpExample = new GrpExample();
            grpExample.createCriteria().andCodeEqualTo(AppConstants.GRP_ROOT).andStatusEqualTo(AppConstants.ZERO_Byte);
            rootGrp = grpMapper.selectByExample(grpExample).get(0);
        } else if(groupCode != null && groupId != null) {
            GrpExample grpExample = new GrpExample();
            grpExample.createCriteria().andCodeEqualTo(groupCode).andStatusEqualTo(AppConstants.ZERO_Byte);
            List<Grp> grps = grpMapper.selectByExample(grpExample);
            Grp grp = grpMapper.selectByPrimaryKey(groupId);
            if(grp == null) {
                throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", groupId, Grp.class.getSimpleName()));
            }
            if (CollectionUtils.isEmpty(grps)) {
                throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.code.notfound", groupCode, Grp.class.getSimpleName()));
            }
            if(!grp.getId().equals(grps.get(0).getId())) {
                throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("group.parameter.code.id.dif", groupCode, groupId));
            }
            rootGrp = grp;
        } else if(groupCode != null && groupId == null) {
            GrpExample grpExample = new GrpExample();
            grpExample.createCriteria().andCodeEqualTo(groupCode).andStatusEqualTo(AppConstants.ZERO_Byte);
            List<Grp> grps = grpMapper.selectByExample(grpExample);
            if (CollectionUtils.isEmpty(grps)) {
                throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.code.notfound", groupCode, Grp.class.getSimpleName()));
            }
            rootGrp = grps.get(0);
        } else {
            //else if(groupCode == null && groupId != null)
            rootGrp = grpMapper.selectByPrimaryKey(groupId);
            if(rootGrp == null || !AppConstants.ZERO_Byte.equals(rootGrp.getStatus())) {
                throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", groupId, Grp.class.getSimpleName()));
            }
        }
        Integer realGroupId = rootGrp.getId();
        List<Grp> grps = grpMapper.getGroupTree(realGroupId);
        if(!CollectionUtils.isEmpty(grps)) {
            Set<Integer> ownGrpIds = null;
            if(needOwnerMarkup != null && needOwnerMarkup && ownerId != null) {
                ownGrpIds = grpMapper.getOwnGrpIds(ownerId);
            }
            List<HashMap<String,Integer>> descendantAncestorPairs = grpMapper.getGroupTreeLinks(realGroupId);
            Map<Integer, GroupDto> idGroupDtoPair = new HashMap();
            for(Grp grp : grps) {
                GroupDto groupDto = new GroupDto().setId(grp.getId()).setCode(grp.getCode()).setName(grp.getName())
                        .setDescription(grp.getDescription());
                if(!CollectionUtils.isEmpty(ownGrpIds)) {
                    if(ownGrpIds.contains(grp.getId())) {
                        groupDto.setOwnerMarkup(Boolean.TRUE);
                    }
                }
                idGroupDtoPair.put(groupDto.getId(), groupDto);
            }

            // construct the tree
            if(!CollectionUtils.isEmpty(descendantAncestorPairs)) {
                for(HashMap<String,Integer> descendantAncestorPair : descendantAncestorPairs) {
                    Integer descendantId = descendantAncestorPair.get("descendant");
                    Integer ancestorId = descendantAncestorPair.get("ancestor");
                    GroupDto ancestorDto = idGroupDtoPair.get(ancestorId);
                    GroupDto descendantDto = idGroupDtoPair.get(descendantId);
                    if(ancestorDto != null) {
                        List<GroupDto> groupDtos = ancestorDto.getGroups();
                        if (groupDtos == null && descendantDto != null) {
                            groupDtos = new ArrayList<>();
                            ancestorDto.setGroups(groupDtos);
                        }
                        if(descendantDto != null) {
                            groupDtos.add(descendantDto);
                        }
                    }
                }
            }

            if(roleId != null) {
                //role checked on group
                GrpRoleExample grpRoleExample = new GrpRoleExample();
                grpRoleExample.createCriteria().andRoleIdEqualTo(roleId).andGrpIdIn(new ArrayList<Integer>(idGroupDtoPair.keySet()));
                List<GrpRoleKey> grpRoleKeys = grpRoleMapper.selectByExample(grpRoleExample);
                if(!CollectionUtils.isEmpty(grpRoleKeys)) {
                    for (GrpRoleKey grpRoleKey : grpRoleKeys) {
                        Integer checkedGroupId = grpRoleKey.getGrpId();
                        idGroupDtoPair.get(checkedGroupId).setRoleChecked(Boolean.TRUE);
                    }
                }
            }

            if(tagId != null) {
                GrpTagExample grpTagExample = new GrpTagExample();
                grpTagExample.createCriteria().andTagIdEqualTo(tagId).andGrpIdIn(new ArrayList<Integer>(idGroupDtoPair.keySet()));
                List<GrpTagKey> grpTagKeys = grpTagMapper.selectByExample(grpTagExample);
                if(!CollectionUtils.isEmpty(grpTagKeys)) {
                    for (GrpTagKey grpTagKey : grpTagKeys) {
                        Integer checkedGroupId = grpTagKey.getGrpId();
                        idGroupDtoPair.get(checkedGroupId).setTagChecked(Boolean.TRUE);
                    }
                }
            }

            if(onlyShowGroup != null && !onlyShowGroup) {
                CheckEmpty.checkEmpty(userGroupType, "users' type in the group");
                Map<String, Object> groupIdAndUserType = new HashMap<String, Object>();
                groupIdAndUserType.put("id", realGroupId);
                groupIdAndUserType.put("userGroupType", userGroupType);
                List<UserExt> userExts = userMapper.getUsersByParentGrpIdByUserType(groupIdAndUserType);
                // construct the users on the tree
                if(!CollectionUtils.isEmpty(userExts)) {
                    //role checked on users
                    Set<Long> userIdsOnRole = null;
                    if(roleId != null) {
                        UserRoleExample userRoleExample = new UserRoleExample();
                        userRoleExample.createCriteria().andRoleIdEqualTo(roleId);
                        List<UserRoleKey> userRoleKeys = userRoleMapper.selectByExample(userRoleExample);
                        if(!CollectionUtils.isEmpty(userRoleKeys)) {
                            userIdsOnRole = new TreeSet<>();
                            for (UserRoleKey userRoleKey : userRoleKeys) {
                                userIdsOnRole.add(userRoleKey.getUserId());
                            }
                        }
                    }
                    //tag checked on users
                    Set<Long> userIdsOnTag = null;
                    if(tagId != null) {
                        UserTagExample userTagExample = new UserTagExample();
                        userTagExample.createCriteria().andTagIdEqualTo(tagId);
                        List<UserTagKey> userTagKeys = userTagMapper.selectByExample(userTagExample);
                        if(!CollectionUtils.isEmpty(userTagKeys)) {
                            userIdsOnTag = new TreeSet<>();
                            for (UserTagKey userTagKey : userTagKeys) {
                                userIdsOnTag.add(userTagKey.getUserId());
                            }
                        }
                    }
                    for(UserExt userExt : userExts) {
                        UserDto userDto = new UserDto().setEmail(userExt.getEmail()).setId(userExt.getId()).setUserGroupType(userExt.getUserGroupType()).setName(userExt.getName());
                        GroupDto groupDto = idGroupDtoPair.get(userExt.getGroupId());
                        if(groupDto != null) {
                            List<UserDto> userDtos = groupDto.getUsers();
                            if (userDtos == null) {
                                userDtos = new ArrayList<>();
                                groupDto.setUsers(userDtos);
                            }
                            userDtos.add(userDto);
                            if (userIdsOnRole != null && userIdsOnRole.contains(userDto.getId())) {
                                userDto.setRoleChecked(Boolean.TRUE);
                            }
                            if(userIdsOnTag != null && userIdsOnTag.contains(userDto.getId())) {
                                userDto.setTagChecked(Boolean.TRUE);
                            }
                        }
                    }

                }
            }

            return idGroupDtoPair.get(realGroupId);
        } else {
            return null;
        }
    }
    
    public void checkOwner(Long opUserId, List<Integer> targetGroupIds) {
		CheckEmpty.checkEmpty(opUserId, "当前用户");
		CheckEmpty.checkEmpty(targetGroupIds, "添加的目标组(s)");

        for(Integer targetGroupId : targetGroupIds) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("userId", opUserId);
            paramMap.put("targetGroupId", targetGroupId);

            Integer ownerGroupCount = grpMapper.checkOwner(paramMap);
            if (ownerGroupCount == 0) {
                throw new AppException(InfoName.GRP_NOT_OWNER, UniBundle.getMsg("group.checkowner.not.owner", opUserId, targetGroupId));
            }
        }
    }

    @Transactional
    public void replaceRolesToGroupUnderDomain(Integer grpId, List<Integer> roleIds, Integer domainId) {

        CheckEmpty.checkEmpty(grpId, "grpId");
        CheckEmpty.checkEmpty(domainId, "domainId");
        //step 1. get roleIds in the specific domain.
        RoleExample roleExample = new RoleExample();
        roleExample.createCriteria().andDomainIdEqualTo(domainId);
        List<Role> roles = roleMapper.selectByExample(roleExample);
        List<Integer> roleIdsInDomain = new ArrayList<>();
        if(!CollectionUtils.isEmpty(roles)) {
            for(Role role:roles) {
                roleIdsInDomain.add(role.getId());
            }
        }
        // Not null, otherwise it is an invalid call.
        CheckEmpty.checkEmpty(roleIdsInDomain, "roleIdsInDomain");

        GrpRoleExample grpRoleExample = new GrpRoleExample();
        GrpRoleExample.Criteria criteria = grpRoleExample.createCriteria();
        criteria.andGrpIdEqualTo(grpId).andRoleIdIn(roleIdsInDomain);
        // no roleIds means delete all the links of the role to group under the domain
        if(CollectionUtils.isEmpty(roleIds)) {
            grpRoleMapper.deleteByExample(grpRoleExample);
            return;
        }
        // if the input roleIds is not under the domain, then it is an invalid call
        if(!roleIdsInDomain.containsAll(roleIds)){
            throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.ids.invalid", roleIds));
        }

        // step 2. get the roleIds in the specific domain and linked to group
        List<GrpRoleKey> grpRoleKeys = grpRoleMapper.selectByExample(grpRoleExample);
        if(!CollectionUtils.isEmpty(grpRoleKeys)) {
            ArrayList<Integer> dbRoleIds = new ArrayList<>();
            for(GrpRoleKey grpRoleKey : grpRoleKeys) {
                dbRoleIds.add(grpRoleKey.getRoleId());
            }
            ArrayList<Integer> intersections = ((ArrayList<Integer>)dbRoleIds.clone());
            //step 3. get the intersection of param roleIds and dbRoleIds.
            intersections.retainAll(roleIds);
            List<Integer> roleIdsNeedAddToDB = new ArrayList<>();
            List<Integer> roleIdsNeedDeleteFromDB = new ArrayList<>();
            //step 4. get the roleIds need to add
            for(Integer roleId : roleIds) {
                if(!intersections.contains(roleId)) {
                    roleIdsNeedAddToDB.add(roleId);
                }
            }
            //step 5. get the roleIds need to delete
            for(Integer dbRoleId : dbRoleIds) {
                if(!intersections.contains(dbRoleId)) {
                    roleIdsNeedDeleteFromDB.add(dbRoleId);
                }
            }
            //step 6. add and delete them.
            if(!CollectionUtils.isEmpty(roleIdsNeedAddToDB)) {
                for(Integer roleIdNeedAddToDB : roleIdsNeedAddToDB) {
                    GrpRoleKey grpRoleKey = new GrpRoleKey();
                    grpRoleKey.setRoleId(roleIdNeedAddToDB);
                    grpRoleKey.setGrpId(grpId);
                    grpRoleMapper.insert(grpRoleKey);
                }
            }
            if(!CollectionUtils.isEmpty(roleIdsNeedDeleteFromDB)) {
                GrpRoleExample grpRoleDeleteExample = new GrpRoleExample();
                grpRoleDeleteExample.createCriteria().andGrpIdEqualTo(grpId).andRoleIdIn(roleIdsNeedDeleteFromDB);
                grpRoleMapper.deleteByExample(grpRoleDeleteExample);
            }
        } else {
            for(Integer roleId : roleIds) {
                GrpRoleKey grpRoleKey = new GrpRoleKey();
                grpRoleKey.setRoleId(roleId);
                grpRoleKey.setGrpId(grpId);
                grpRoleMapper.insert(grpRoleKey);
            }
        }
    }
    
    /**.
	    * 根据id获取有效组的数量
	    * @param id
	    * @return
	    */
	  public  int countGroupByIdWithStatusEffective(Long id){
		  return grpMapper.countGroupByIdWithStatusEffective(id);
	  }
	    
	    /**.
	     * 根据code获取有效组的数量
	     * @param code code
	     * @return 数量
	     */
	    public int countGroupByCodeWithStatusEffective( String code){
	    	return grpMapper.countGroupByCodeWithStatusEffective(code);
	    }
	    
	    /**.
	     * 根据id获取组的信息
	     * @param id id
	     * @return 信息
	     */
	    public GroupDto selectByIdWithStatusEffective( Integer id){
	    	return BeanConverter.convert(grpMapper.selectByIdWithStatusEffective(id));
	    }
}
