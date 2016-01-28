package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.enm.RoleActionEnum;
import com.dianrong.common.uniauth.server.data.entity.*;
import com.dianrong.common.uniauth.server.data.mapper.*;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

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

        if(domainMapper.selectByPrimaryKey(domainId) == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", domainId, Domain.class.getSimpleName()));
        }

        Role role = new Role();
        role.setDomainId(domainId);
        role.setName(name);
        role.setRoleCodeId(roleCodeId);
        role.setStatus(AppConstants.ZERO_Byte);
        role.setDescription(description);
        roleMapper.insert(role);
        return BeanConverter.convert(role);
    }

    public void updateRole(RoleActionEnum roleActionEnum, Integer roleId, Integer roleCodeId, String name, String description, Byte status) {
        CheckEmpty.checkEmpty(roleActionEnum, "roleActionEnum");
        Role role = roleMapper.selectByPrimaryKey(roleId);
        if(role == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", roleId, Role.class.getSimpleName()));
        } else if(role.getStatus().equals(AppConstants.ONE_Byte) && !RoleActionEnum.STATUS_CHANGE.equals(roleActionEnum)) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.status.isone", roleId, Role.class.getSimpleName()));
        }
        switch (roleActionEnum) {
            case STATUS_CHANGE :
                ParamCheck.checkStatus(status);
                role.setStatus(status);
                break;
            case UPDATE_INFO:
                CheckEmpty.checkEmpty(roleCodeId, "roleCodeId");
                if(roleCodeMapper.selectByPrimaryKey(roleCodeId) == null) {
                    throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", roleCodeId, RoleCode.class.getSimpleName()));
                }
                role.setDescription(description);
                role.setName(name);
                role.setRoleCodeId(roleCodeId);
                break;
        }
        roleMapper.updateByPrimaryKey(role);
    }

    public void deleteRole(Integer roleId) {
        CheckEmpty.checkEmpty(roleId, "roleId");
        Role role = roleMapper.selectByPrimaryKey(roleId);
        if(role == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", roleId, Role.class.getSimpleName()));
        }
        // cascading delete the role-user, role-permission relationships
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andRoleIdEqualTo(roleId);
        userRoleMapper.deleteByExample(userRoleExample);
        RolePermissionExample rolePermissionExample = new RolePermissionExample();
        rolePermissionExample.createCriteria().andRoleIdEqualTo(roleId);
        rolePermissionMapper.deleteByExample(rolePermissionExample);
        roleMapper.deleteByPrimaryKey(roleId);
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

    public PageDto<RoleDto> searchRole(Integer domainId, String roleName, Integer roleCodeId, Byte status, Integer pageNumber, Integer pageSize) {
        CheckEmpty.checkEmpty(domainId, "domainId");
        CheckEmpty.checkEmpty(pageNumber, "pageNumber");
        CheckEmpty.checkEmpty(pageSize, "pageSize");
        if(domainMapper.selectByPrimaryKey(domainId) == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", domainId, Domain.class.getSimpleName()));
        }
        RoleExample roleExample = new RoleExample();
        roleExample.setPageOffSet(pageNumber*pageSize);
        roleExample.setPageSize(pageSize);
        roleExample.setOrderByClause("");
        RoleExample.Criteria criteria = roleExample.createCriteria().andDomainIdEqualTo(domainId);
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
        permissionExample.createCriteria().andDomainIdEqualTo(domainId);
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
}
