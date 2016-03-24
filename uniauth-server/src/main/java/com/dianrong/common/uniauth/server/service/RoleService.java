package com.dianrong.common.uniauth.server.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.GrpRoleExample;
import com.dianrong.common.uniauth.server.data.entity.GrpRoleKey;
import com.dianrong.common.uniauth.server.data.entity.PermType;
import com.dianrong.common.uniauth.server.data.entity.PermTypeExample;
import com.dianrong.common.uniauth.server.data.entity.Permission;
import com.dianrong.common.uniauth.server.data.entity.PermissionExample;
import com.dianrong.common.uniauth.server.data.entity.Role;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.data.entity.RoleCodeExample;
import com.dianrong.common.uniauth.server.data.entity.RoleExample;
import com.dianrong.common.uniauth.server.data.entity.RolePermissionExample;
import com.dianrong.common.uniauth.server.data.entity.RolePermissionKey;
import com.dianrong.common.uniauth.server.data.entity.UserRoleExample;
import com.dianrong.common.uniauth.server.data.entity.UserRoleKey;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpRoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.PermTypeMapper;
import com.dianrong.common.uniauth.server.data.mapper.PermissionMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleCodeMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.RolePermissionMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserRoleMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;
import com.dianrong.common.uniauth.server.util.UniBundle;

/**
 * Created by Arc on 15/1/16.
 */
@Service
public class RoleService {

    @Autowired
    private RoleCodeMapper roleCodeMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private DomainMapper domainMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private PermTypeMapper permTypeMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private GrpRoleMapper grpRoleMapper;

    /**.
	 * 进行角色数据过滤的filter
	 */
	@Resource(name="roleDataFilter")
	private DataFilter dataFilter;
	
	/**.
	 * 进行域名数据过滤的filter
	 */
	@Resource(name="domainDataFilter")
	private DataFilter domainDataFilter;
    
    public List<RoleCodeDto> getAllRoleCodes() {
        RoleCodeExample example = new RoleCodeExample();
        List<RoleCode> roleCodeList = roleCodeMapper.selectByExample(example);
        List<RoleCodeDto> roleCodeDtoList = new ArrayList<RoleCodeDto>();
        if(roleCodeList != null) {
            for(RoleCode roleCode : roleCodeList) {
                roleCodeDtoList.add(BeanConverter.convert(roleCode));
            }
        }
        return roleCodeDtoList;
    }

    public RoleDto addNewRole(Integer domainId, Integer roleCodeId, String name, String description) {
        CheckEmpty.checkEmpty(domainId, "domainId");
        CheckEmpty.checkEmpty(roleCodeId, "roleCodeId");
        CheckEmpty.checkEmpty(name, "name");

        //domainid必须是有效的
        domainDataFilter.dataFilter(FieldType.FIELD_TYPE_ID, domainId, FilterType.FILTER_TYPE_NO_DATA);
        
//        if(domainMapper.selectByPrimaryKey(domainId) == null) {
//            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", domainId, Domain.class.getSimpleName()));
//        }

        Role role = new Role();
        role.setDomainId(domainId);
        role.setName(name);
        role.setRoleCodeId(roleCodeId);
        role.setStatus(AppConstants.ZERO_Byte);
        role.setDescription(description);
        roleMapper.insert(role);
        return BeanConverter.convert(role);
    }

    public void updateRole(Integer roleId, Integer roleCodeId, String name, String description, Byte status) {
        CheckEmpty.checkEmpty(roleId, "roleId");
//        Role role = roleMapper.selectByPrimaryKey(roleId);
        Role role = roleMapper.selectByIdWithStatusEffective(roleId);
        
        if(role == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", roleId, Role.class.getSimpleName()));
        }

        CheckEmpty.checkEmpty(roleCodeId, "roleCodeId");
        if(roleCodeMapper.selectByPrimaryKey(roleCodeId) == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", roleCodeId, RoleCode.class.getSimpleName()));
        }

        ParamCheck.checkStatus(status);
        role.setStatus(status);
        role.setDescription(description);
        role.setName(name);
        role.setRoleCodeId(roleCodeId);

        roleMapper.updateByPrimaryKey(role);
    }
    @Transactional
    public void replaceGroupsAndUsersToRole(Integer roleId, List<Integer> grpIdsDup, List<Long> userIdsDup) {
        CheckEmpty.checkEmpty(roleId, "roleId");
        // remove duplicate values;
        List<Integer> grpIds = new ArrayList<>(new HashSet<>(grpIdsDup));
        List<Long> userIds = new ArrayList<>(new HashSet<>(userIdsDup));

        GrpRoleExample grpRoleExample = new GrpRoleExample();
        grpRoleExample.createCriteria().andRoleIdEqualTo(roleId);
        if(CollectionUtils.isEmpty(grpIds)) {
            grpRoleMapper.deleteByExample(grpRoleExample);
        } else {
            List<GrpRoleKey> grpRoleKeys = grpRoleMapper.selectByExample(grpRoleExample);
            if (!CollectionUtils.isEmpty(grpRoleKeys)) {
                ArrayList<Integer> dbGrpIds = new ArrayList<>();
                for (GrpRoleKey grpRoleKey : grpRoleKeys) {
                    dbGrpIds.add(grpRoleKey.getGrpId());
                }
                ArrayList<Integer> intersections = ((ArrayList<Integer>) dbGrpIds.clone());
                intersections.retainAll(grpIds);
                List<Integer> grpIdsNeedAddToDB = new ArrayList<>();
                List<Integer> grpIdsNeedDeleteFromDB = new ArrayList<>();
                for (Integer grpId : grpIds) {
                    if (!intersections.contains(grpId)) {
                        grpIdsNeedAddToDB.add(grpId);
                    }
                }
                for (Integer dbGrpId : dbGrpIds) {
                    if (!intersections.contains(dbGrpId)) {
                        grpIdsNeedDeleteFromDB.add(dbGrpId);
                    }
                }

                if (!CollectionUtils.isEmpty(grpIdsNeedAddToDB)) {
                    for (Integer grpIdNeedAddToDB : grpIdsNeedAddToDB) {
                        GrpRoleKey grpRoleKey = new GrpRoleKey();
                        grpRoleKey.setRoleId(roleId);
                        grpRoleKey.setGrpId(grpIdNeedAddToDB);
                        grpRoleMapper.insert(grpRoleKey);
                    }
                }
                if (!CollectionUtils.isEmpty(grpIdsNeedDeleteFromDB)) {
                    GrpRoleExample grpRoleDeleteExample = new GrpRoleExample();
                    grpRoleDeleteExample.createCriteria().andRoleIdEqualTo(roleId).andGrpIdIn(grpIdsNeedDeleteFromDB);
                    grpRoleMapper.deleteByExample(grpRoleDeleteExample);
                }
            } else {
                for (Integer grpId : grpIds) {
                    GrpRoleKey grpRoleKey = new GrpRoleKey();
                    grpRoleKey.setRoleId(roleId);
                    grpRoleKey.setGrpId(grpId);
                    grpRoleMapper.insert(grpRoleKey);
                }
            }
        }

        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andRoleIdEqualTo(roleId);
        if(CollectionUtils.isEmpty(userIds)) {
            userRoleMapper.deleteByExample(userRoleExample);
        } else {
            List<UserRoleKey> userRoleKeys = userRoleMapper.selectByExample(userRoleExample);
            if (!CollectionUtils.isEmpty(userRoleKeys)) {
                ArrayList<Long> dbUserIds = new ArrayList<>();
                for (UserRoleKey userRoleKey : userRoleKeys) {
                    dbUserIds.add(userRoleKey.getUserId());
                }
                ArrayList<Long> intersections = ((ArrayList<Long>) dbUserIds.clone());
                intersections.retainAll(userIds);
                List<Long> userIdsNeedAddToDB = new ArrayList<>();
                List<Long> userIdsNeedDeleteFromDB = new ArrayList<>();
                for (Long userId : userIds) {
                    if (!intersections.contains(userId)) {
                        userIdsNeedAddToDB.add(userId);
                    }
                }
                for (Long dbUserId : dbUserIds) {
                    if (!intersections.contains(dbUserId)) {
                        userIdsNeedDeleteFromDB.add(dbUserId);
                    }
                }

                if (!CollectionUtils.isEmpty(userIdsNeedAddToDB)) {
                    for (Long userIdNeedAddToDB : userIdsNeedAddToDB) {
                        UserRoleKey userRoleKey = new UserRoleKey();
                        userRoleKey.setRoleId(roleId);
                        userRoleKey.setUserId(userIdNeedAddToDB);
                        userRoleMapper.insert(userRoleKey);
                    }
                }
                if (!CollectionUtils.isEmpty(userIdsNeedDeleteFromDB)) {
                    UserRoleExample userRoleDeleteExample = new UserRoleExample();
                    userRoleDeleteExample.createCriteria().andRoleIdEqualTo(roleId).andUserIdIn(userIdsNeedDeleteFromDB);
                    userRoleMapper.deleteByExample(userRoleDeleteExample);
                }
            } else {
                for (Long userId : userIds) {
                    UserRoleKey userRoleKey = new UserRoleKey();
                    userRoleKey.setRoleId(roleId);
                    userRoleKey.setUserId(userId);
                    userRoleMapper.insert(userRoleKey);
                }
            }
        }
    }

    @Transactional
    public void replacePermsToRole(Integer roleId, List<Integer> permIds) {
        CheckEmpty.checkEmpty(roleId, "roleId");
        RolePermissionExample rolePermissionExample = new RolePermissionExample();
        rolePermissionExample.createCriteria().andRoleIdEqualTo(roleId);
        if(CollectionUtils.isEmpty(permIds)) {
            rolePermissionMapper.deleteByExample(rolePermissionExample);
            return;
        }
        List<RolePermissionKey> rolePermissionKeys = rolePermissionMapper.selectByExample(rolePermissionExample);
        if(!CollectionUtils.isEmpty(rolePermissionKeys)) {
            ArrayList<Integer> dbPermIds = new ArrayList<>();
            for(RolePermissionKey rolePermissionKey : rolePermissionKeys) {
                dbPermIds.add(rolePermissionKey.getPermissionId());
            }
            ArrayList<Integer> intersections = ((ArrayList<Integer>)dbPermIds.clone());
            intersections.retainAll(permIds);
            List<Integer> permIdsNeedAddToDB = new ArrayList<>();
            List<Integer> permIdsNeedDeleteFromDB = new ArrayList<>();
            for(Integer permId : permIds) {
                if(!intersections.contains(permId)) {
                    permIdsNeedAddToDB.add(permId);
                }
            }
            for(Integer dbPermId : dbPermIds) {
                if(!intersections.contains(dbPermId)) {
                    permIdsNeedDeleteFromDB.add(dbPermId);
                }
            }

            if(!CollectionUtils.isEmpty(permIdsNeedAddToDB)) {
                for(Integer permIdNeedAddToDB : permIdsNeedAddToDB) {
                    RolePermissionKey rolePermissionKey = new RolePermissionKey();
                    rolePermissionKey.setRoleId(roleId);
                    rolePermissionKey.setPermissionId(permIdNeedAddToDB);
                    rolePermissionMapper.insert(rolePermissionKey);
                }
            }
            if(!CollectionUtils.isEmpty(permIdsNeedDeleteFromDB)) {
                RolePermissionExample rolePermDeleteExample = new RolePermissionExample();
                rolePermDeleteExample.createCriteria().andRoleIdEqualTo(roleId).andPermissionIdIn(permIdsNeedDeleteFromDB);
                rolePermissionMapper.deleteByExample(rolePermDeleteExample);
            }
        } else {
            for(Integer permId : permIds) {
                RolePermissionKey rolePermissionKey = new RolePermissionKey();
                rolePermissionKey.setRoleId(roleId);
                rolePermissionKey.setPermissionId(permId);
                rolePermissionMapper.insert(rolePermissionKey);
            }
        }

    }

    @Transactional
    public void savePermsToRole(Integer roleId, List<Integer> permIds) {
        CheckEmpty.checkEmpty(roleId, "roleId");
        CheckEmpty.checkEmpty(permIds, "permIds");

        RolePermissionExample rolePermissionExample = new RolePermissionExample();
        rolePermissionExample.createCriteria().andRoleIdEqualTo(roleId);
        List<RolePermissionKey> rolePermissionKeys = rolePermissionMapper.selectByExample(rolePermissionExample);
        Set<Integer> permIdSet = new TreeSet<>();
        if(!CollectionUtils.isEmpty(rolePermissionKeys)) {
            for(RolePermissionKey rolePermissionKey : rolePermissionKeys) {
                permIdSet.add(rolePermissionKey.getPermissionId());
            }
        }
        for(Integer permId : permIds) {
            if(!permIdSet.contains(roleId)) {
                RolePermissionKey rolePermissionKey = new RolePermissionKey();
                rolePermissionKey.setPermissionId(permId);
                rolePermissionKey.setRoleId(roleId);
                rolePermissionMapper.insert(rolePermissionKey);
            }
        }
    }

    public PageDto<RoleDto> searchRole(List<Integer> roleIds, Integer roleId, Integer domainId, String roleName, Integer roleCodeId, Byte status, Integer pageNumber, Integer pageSize) {
        CheckEmpty.checkEmpty(pageNumber, "pageNumber");
        CheckEmpty.checkEmpty(pageSize, "pageSize");
        RoleExample roleExample = new RoleExample();
        roleExample.setPageOffSet(pageNumber*pageSize);
        roleExample.setPageSize(pageSize);
        roleExample.setOrderByClause("status asc");
        RoleExample.Criteria criteria = roleExample.createCriteria();
        if(!CollectionUtils.isEmpty(roleIds)) {
            criteria.andIdIn(roleIds);
        }
        if(roleId != null) {
            criteria.andIdEqualTo(roleId);
        }
        if(domainId != null) {
            criteria.andDomainIdEqualTo(domainId);
        }
        if(roleName != null) {
            criteria.andNameLike("%" + roleName + "%");
        }
        if(roleCodeId != null) {
            criteria.andRoleCodeIdEqualTo(roleCodeId);
        }
        if(status != null) {
            ParamCheck.checkStatus(status);
            criteria.andStatusEqualTo(status);
        }
        List<Role> roles = roleMapper.selectByExample(roleExample);

        if(!CollectionUtils.isEmpty(roles)) {
            int count = roleMapper.countByExample(roleExample);

            List<RoleDto> roleDtos = new ArrayList<>();
            List<RoleCode> roleCodes = roleCodeMapper.selectByExample(new RoleCodeExample());
            // build roleCode index.
            Map<Integer, String> roleCodeIdNamePairs = new TreeMap<>();
            for(RoleCode roleCode : roleCodes) {
                roleCodeIdNamePairs.put(roleCode.getId(), roleCode.getCode());
            }
            for(Role role : roles) {
                RoleDto roleDto = BeanConverter.convert(role).setRoleCode(roleCodeIdNamePairs.get(role.getRoleCodeId()));
                roleDtos.add(roleDto);
            }
            return new PageDto<>(pageNumber,pageSize, count, roleDtos);
        } else {
            return null;
        }
    }

    public List<PermissionDto> getAllPermsToRole(Integer domainId, Integer roleId) {
        CheckEmpty.checkEmpty(domainId, "domainId");
        CheckEmpty.checkEmpty(roleId, "roleId");
        // 1. get all permissions under the domain
        PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andDomainIdEqualTo(domainId).andStatusEqualTo(AppConstants.ZERO_Byte);
        List<Permission> permissions = permissionMapper.selectByExample(permissionExample);
        if(CollectionUtils.isEmpty(permissions)) {
            return null;
        }
        RolePermissionExample rolePermissionExample = new RolePermissionExample();
        rolePermissionExample.createCriteria().andRoleIdEqualTo(roleId);
        List<RolePermissionKey> rolePermissionKeys = rolePermissionMapper.selectByExample(rolePermissionExample);

        // 2. get the checked permIds for the role
        Set<Integer> permIds = null;
        if(!CollectionUtils.isEmpty(rolePermissionKeys)) {
            permIds = new TreeSet<>();
            for(RolePermissionKey rolePermissionKey : rolePermissionKeys) {
                permIds.add(rolePermissionKey.getPermissionId());
            }
        }
        List<PermType> permTypes = permTypeMapper.selectByExample(new PermTypeExample());
        // build permType index.
        Map<Integer, String> permTypeIdTypePairs = new TreeMap<>();
        for(PermType permType : permTypes) {
            permTypeIdTypePairs.put(permType.getId(), permType.getType());
        }

        // 3. construct all permissions linked with the role & mark the permission checked on the role or not
        List<PermissionDto> permissionDtos = new ArrayList<>();
        for(Permission permission:permissions) {
            PermissionDto permissionDto = BeanConverter.convert(permission);
            permissionDto.setPermType(permTypeIdTypePairs.get(permission.getPermTypeId()));
            if(permIds != null && permIds.contains(permission.getId())) {
                permissionDto.setChecked(Boolean.TRUE);
            } else {
                permissionDto.setChecked(Boolean.FALSE);
            }
            permissionDtos.add(permissionDto);
        }
        return permissionDtos;
    }
    
    /**.
	    * 根据id获取有效角色的数量
	    * @param id
	    * @return
	    */
	  public  int countRoleByIdWithStatusEffective(Long id){
		  return roleMapper.countRoleByIdWithStatusEffective(id);
	  }
}
