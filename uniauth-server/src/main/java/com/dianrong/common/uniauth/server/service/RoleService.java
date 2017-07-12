package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
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
import com.dianrong.common.uniauth.server.data.mapper.GrpRoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.PermTypeMapper;
import com.dianrong.common.uniauth.server.data.mapper.PermissionMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleCodeMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.RolePermissionMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserRoleMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.service.cache.CommonCache;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;
import com.dianrong.common.uniauth.server.util.UniBundle;
import com.google.common.collect.Lists;

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

/**
 * Created by Arc on 15/1/16.
 */
@Service
public class RoleService extends TenancyBasedService {

  @Autowired
  private RoleCodeMapper roleCodeMapper;
  @Autowired
  private RoleMapper roleMapper;
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

  @Autowired
  private DomainService domainService;

  /**
   * 进行角色数据过滤的filter.
   */
  @Resource(name = "roleDataFilter")
  private DataFilter dataFilter;

  /**
   * 进行域名数据过滤的filter.
   */
  @Resource(name = "domainDataFilter")
  private DataFilter domainDataFilter;

  @Autowired
  private CommonCache commonCache;

  /**
   * 获取所有的角色编码信息.
   */
  public List<RoleCodeDto> getAllRoleCodes() {
    return commonCache.getAllRoleCodes();
  }

  /**
   * 添加角色.
   */
  public RoleDto addNewRole(Integer domainId, Integer roleCodeId, String name, String description) {
    CheckEmpty.checkEmpty(domainId, "domainId");
    CheckEmpty.checkEmpty(roleCodeId, "roleCodeId");
    CheckEmpty.checkEmpty(name, "name");

    // domainid必须是有效的
    domainDataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID, domainId);
    // 不能存在domainid，roleCodeId，name完全一致的 role
    dataFilter.addFieldsCheck(FilterType.EXSIT_DATA,
        FilterData.buildFilterData(FieldType.FIELD_TYPE_DOMAIN_ID, domainId),
        FilterData.buildFilterData(FieldType.FIELD_TYPE_ROLE_CODE_ID, roleCodeId),
        FilterData.buildFilterData(FieldType.FIELD_TYPE_NAME, name));

    Role role = new Role();
    role.setDomainId(domainId);
    role.setName(name);
    role.setRoleCodeId(roleCodeId);
    role.setStatus(AppConstants.ZERO_BYTE);
    role.setDescription(description);
    role.setTenancyId(tenancyService.getTenancyIdWithCheck());
    roleMapper.insert(role);
    return BeanConverter.convert(role);
  }

  /**
   * 更新角色.
   */
  public void updateRole(Integer roleId, Integer roleCodeId, String name, String description,
      Byte status) {
    CheckEmpty.checkEmpty(roleId, "roleId");
    Role role = roleMapper.selectByPrimaryKey(roleId);

    if (role == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.entity.notfound", roleId, Role.class.getSimpleName()));
    }

    CheckEmpty.checkEmpty(roleCodeId, "roleCodeId");
    if (roleCodeMapper.selectByPrimaryKey(roleCodeId) == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.entity.notfound", roleCodeId, RoleCode.class.getSimpleName()));
    }

    ParamCheck.checkStatus(status);
    role.setStatus(status);
    role.setDescription(description);
    role.setName(name);
    role.setRoleCodeId(roleCodeId);

    // 不能存在domainid，roleCodeId，name完全一致的 role
    dataFilter.updateFieldsCheck(roleId,
        FilterData.buildFilterData(FieldType.FIELD_TYPE_DOMAIN_ID, role.getDomainId()),
        FilterData.buildFilterData(FieldType.FIELD_TYPE_ROLE_CODE_ID, role.getRoleCodeId()),
        FilterData.buildFilterData(FieldType.FIELD_TYPE_NAME, role.getName()));

    roleMapper.updateByPrimaryKey(role);
  }

  /**
   * 替换角色的组和用户信息.
   */
  @Transactional
  public void replaceGroupsAndUsersToRole(Integer roleId, List<Integer> grpIdsDup,
      List<Long> userIdsDup, Boolean needProcessGoupIds, Boolean needProcessUserIds) {
    CheckEmpty.checkEmpty(roleId, "roleId");
    List<Integer> grpIds = null;
    List<Long> userIds = null;
    if (needProcessGoupIds != null && needProcessGoupIds) {
      grpIds = new ArrayList<>(new HashSet<>(grpIdsDup));
    }
    if (needProcessUserIds != null && needProcessUserIds) {
      userIds = new ArrayList<>(new HashSet<>(userIdsDup));
    }
    if (grpIds != null) {
      GrpRoleExample grpRoleExample = new GrpRoleExample();
      grpRoleExample.createCriteria().andRoleIdEqualTo(roleId);
      if (grpIds.isEmpty()) {
        grpRoleMapper.deleteByExample(grpRoleExample);
      } else {
        List<GrpRoleKey> grpRoleKeys = grpRoleMapper.selectByExample(grpRoleExample);
        if (!CollectionUtils.isEmpty(grpRoleKeys)) {
          ArrayList<Integer> dbGrpIds = new ArrayList<>();
          for (GrpRoleKey grpRoleKey : grpRoleKeys) {
            dbGrpIds.add(grpRoleKey.getGrpId());
          }
          @SuppressWarnings("unchecked")
          ArrayList<Integer> intersections = ((ArrayList<Integer>) dbGrpIds.clone());
          intersections.retainAll(grpIds);
          List<Integer> grpIdsNeedAddToDb = new ArrayList<>();
          List<Integer> grpIdsNeedDeleteFromDb = new ArrayList<>();
          for (Integer grpId : grpIds) {
            if (!intersections.contains(grpId)) {
              grpIdsNeedAddToDb.add(grpId);
            }
          }
          for (Integer dbGrpId : dbGrpIds) {
            if (!intersections.contains(dbGrpId)) {
              grpIdsNeedDeleteFromDb.add(dbGrpId);
            }
          }

          if (!CollectionUtils.isEmpty(grpIdsNeedAddToDb)) {
            for (Integer grpIdNeedAddToDb : grpIdsNeedAddToDb) {
              GrpRoleKey grpRoleKey = new GrpRoleKey();
              grpRoleKey.setRoleId(roleId);
              grpRoleKey.setGrpId(grpIdNeedAddToDb);
              grpRoleMapper.insert(grpRoleKey);
            }
          }
          if (!CollectionUtils.isEmpty(grpIdsNeedDeleteFromDb)) {
            GrpRoleExample grpRoleDeleteExample = new GrpRoleExample();
            grpRoleDeleteExample.createCriteria().andRoleIdEqualTo(roleId)
                .andGrpIdIn(grpIdsNeedDeleteFromDb);
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
    }

    if (userIds != null) {
      UserRoleExample userRoleExample = new UserRoleExample();
      userRoleExample.createCriteria().andRoleIdEqualTo(roleId);
      if (userIds.isEmpty()) {
        userRoleMapper.deleteByExample(userRoleExample);
      } else {
        List<UserRoleKey> userRoleKeys = userRoleMapper.selectByExample(userRoleExample);
        if (!CollectionUtils.isEmpty(userRoleKeys)) {
          ArrayList<Long> dbUserIds = new ArrayList<>();
          for (UserRoleKey userRoleKey : userRoleKeys) {
            dbUserIds.add(userRoleKey.getUserId());
          }
          @SuppressWarnings("unchecked")
          ArrayList<Long> intersections = ((ArrayList<Long>) dbUserIds.clone());
          intersections.retainAll(userIds);
          List<Long> userIdsNeedAddToDb = new ArrayList<>();
          List<Long> userIdsNeedDeleteFromDb = new ArrayList<>();
          for (Long userId : userIds) {
            if (!intersections.contains(userId)) {
              userIdsNeedAddToDb.add(userId);
            }
          }
          for (Long dbUserId : dbUserIds) {
            if (!intersections.contains(dbUserId)) {
              userIdsNeedDeleteFromDb.add(dbUserId);
            }
          }

          if (!CollectionUtils.isEmpty(userIdsNeedAddToDb)) {
            for (Long userIdNeedAddToDb : userIdsNeedAddToDb) {
              UserRoleKey userRoleKey = new UserRoleKey();
              userRoleKey.setRoleId(roleId);
              userRoleKey.setUserId(userIdNeedAddToDb);
              userRoleMapper.insert(userRoleKey);
            }
          }
          if (!CollectionUtils.isEmpty(userIdsNeedDeleteFromDb)) {
            UserRoleExample userRoleDeleteExample = new UserRoleExample();
            userRoleDeleteExample.createCriteria().andRoleIdEqualTo(roleId)
                .andUserIdIn(userIdsNeedDeleteFromDb);
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
  }

  /**
   * 替换权限关联的角色.
   */
  @Transactional
  public void replacePermsToRole(Integer roleId, List<Integer> permIds) {
    CheckEmpty.checkEmpty(roleId, "roleId");
    RolePermissionExample rolePermissionExample = new RolePermissionExample();
    rolePermissionExample.createCriteria().andRoleIdEqualTo(roleId);
    if (CollectionUtils.isEmpty(permIds)) {
      rolePermissionMapper.deleteByExample(rolePermissionExample);
      return;
    }
    List<RolePermissionKey> rolePermissionKeys =
        rolePermissionMapper.selectByExample(rolePermissionExample);
    if (!CollectionUtils.isEmpty(rolePermissionKeys)) {
      ArrayList<Integer> dbPermIds = new ArrayList<>();
      for (RolePermissionKey rolePermissionKey : rolePermissionKeys) {
        dbPermIds.add(rolePermissionKey.getPermissionId());
      }
      @SuppressWarnings("unchecked")
      ArrayList<Integer> intersections = ((ArrayList<Integer>) dbPermIds.clone());
      intersections.retainAll(permIds);
      List<Integer> permIdsNeedAddToDb = new ArrayList<>();
      List<Integer> permIdsNeedDeleteFromDb = new ArrayList<>();
      for (Integer permId : permIds) {
        if (!intersections.contains(permId)) {
          permIdsNeedAddToDb.add(permId);
        }
      }
      for (Integer dbPermId : dbPermIds) {
        if (!intersections.contains(dbPermId)) {
          permIdsNeedDeleteFromDb.add(dbPermId);
        }
      }

      if (!CollectionUtils.isEmpty(permIdsNeedAddToDb)) {
        for (Integer permIdNeedAddToDb : permIdsNeedAddToDb) {
          RolePermissionKey rolePermissionKey = new RolePermissionKey();
          rolePermissionKey.setRoleId(roleId);
          rolePermissionKey.setPermissionId(permIdNeedAddToDb);
          rolePermissionMapper.insert(rolePermissionKey);
        }
      }
      if (!CollectionUtils.isEmpty(permIdsNeedDeleteFromDb)) {
        RolePermissionExample rolePermDeleteExample = new RolePermissionExample();
        rolePermDeleteExample.createCriteria().andRoleIdEqualTo(roleId)
            .andPermissionIdIn(permIdsNeedDeleteFromDb);
        rolePermissionMapper.deleteByExample(rolePermDeleteExample);
      }
    } else {
      for (Integer permId : permIds) {
        RolePermissionKey rolePermissionKey = new RolePermissionKey();
        rolePermissionKey.setRoleId(roleId);
        rolePermissionKey.setPermissionId(permId);
        rolePermissionMapper.insert(rolePermissionKey);
      }
    }
  }

  /**
   * 保存权限到角色.
   */
  @Transactional
  public void savePermsToRole(Integer roleId, List<Integer> permIds) {
    CheckEmpty.checkEmpty(roleId, "roleId");
    CheckEmpty.checkEmpty(permIds, "permIds");

    RolePermissionExample rolePermissionExample = new RolePermissionExample();
    rolePermissionExample.createCriteria().andRoleIdEqualTo(roleId);
    List<RolePermissionKey> rolePermissionKeys =
        rolePermissionMapper.selectByExample(rolePermissionExample);
    Set<Integer> permIdSet = new TreeSet<>();
    if (!CollectionUtils.isEmpty(rolePermissionKeys)) {
      for (RolePermissionKey rolePermissionKey : rolePermissionKeys) {
        permIdSet.add(rolePermissionKey.getPermissionId());
      }
    }
    for (Integer permId : permIds) {
      if (!permIdSet.contains(roleId)) {
        RolePermissionKey rolePermissionKey = new RolePermissionKey();
        rolePermissionKey.setPermissionId(permId);
        rolePermissionKey.setRoleId(roleId);
        rolePermissionMapper.insert(rolePermissionKey);
      }
    }
  }

  /**
   * 根据条件查询角色列表.
   */
  public PageDto<RoleDto> searchRole(List<Integer> roleIds, Integer roleId, Integer domainId,
      String roleName, Integer roleCodeId, Byte status, Boolean needDomainInfo, Integer pageNumber,
      Integer pageSize) {
    CheckEmpty.checkEmpty(pageNumber, "pageNumber");
    CheckEmpty.checkEmpty(pageSize, "pageSize");
    RoleExample roleExample = new RoleExample();
    roleExample.setPageOffSet(pageNumber * pageSize);
    roleExample.setPageSize(pageSize);
    roleExample.setOrderByClause("status asc");
    RoleExample.Criteria criteria = roleExample.createCriteria();
    if (!CollectionUtils.isEmpty(roleIds)) {
      criteria.andIdIn(roleIds);
    }
    if (roleId != null) {
      criteria.andIdEqualTo(roleId);
    }
    if (domainId != null) {
      criteria.andDomainIdEqualTo(domainId);
    }
    if (roleName != null) {
      criteria.andNameLike("%" + roleName + "%");
    }
    if (roleCodeId != null) {
      criteria.andRoleCodeIdEqualTo(roleCodeId);
    }
    if (status != null) {
      ParamCheck.checkStatus(status);
      criteria.andStatusEqualTo(status);
    }
    criteria.andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    int count = roleMapper.countByExample(roleExample);
    ParamCheck.checkPageParams(pageNumber, pageSize, count);
    List<Role> roles = roleMapper.selectByExample(roleExample);

    if (!CollectionUtils.isEmpty(roles)) {
      List<RoleDto> roleDtos = new ArrayList<>();
      List<RoleCode> roleCodes = roleCodeMapper.selectByExample(new RoleCodeExample());
      // build roleCode index.
      Map<Integer, String> roleCodeIdNamePairs = new TreeMap<>();
      for (RoleCode roleCode : roleCodes) {
        roleCodeIdNamePairs.put(roleCode.getId(), roleCode.getCode());
      }

      for (Role role : roles) {
        RoleDto roleDto =
            BeanConverter.convert(role).setRoleCode(roleCodeIdNamePairs.get(role.getRoleCodeId()));
        roleDtos.add(roleDto);
      }

      if (needDomainInfo != null && needDomainInfo) {
        List<Integer> domainIds = Lists.newArrayList();
        for (RoleDto roleDto : roleDtos) {
          domainIds.add(roleDto.getDomainId());
        }
        Map<Integer, DomainDto> domainMap = domainService.getDomainMapByDomainIds(domainIds);
        for (RoleDto roleDto : roleDtos) {
          roleDto.setDomain(domainMap.get(roleDto.getDomainId()));
        }
      }
      return new PageDto<>(pageNumber, pageSize, count, roleDtos);
    } else {
      return null;
    }
  }

  /**
   * 替换用户与角色的关联关系.
   * @param roleId 角色Id.
   * @param userIds 用户Id集合.
   */
  @Transactional
  public void relateUsersAndRole(Integer roleId, List<Long> userIds) {
    CheckEmpty.checkEmpty(roleId, "roleId");
    // roleId 必须要存在
    dataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID, roleId);
    UserRoleExample userRoleExample = new UserRoleExample();
    UserRoleExample.Criteria criteria = userRoleExample.createCriteria();
    criteria.andRoleIdEqualTo(roleId);
    List<UserRoleKey> userRoleKeys = userRoleMapper.selectByExample(userRoleExample);
    List<Long> existUserIds = Lists.newArrayList();
    if (!ObjectUtil.collectionIsEmptyOrNull(userRoleKeys)) {
      for (UserRoleKey urk : userRoleKeys) {
        existUserIds.add(urk.getUserId());
      }
    }
    List<Long> insertUserIds = userIds;
    insertUserIds.removeAll(existUserIds);
    for (Long userId : insertUserIds) {
      UserRoleKey record = new UserRoleKey();
      record.setRoleId(roleId);
      record.setUserId(userId);
      userRoleMapper.insert(record);
    }
  }

  /**
   * 查询某个域下,角色相关的所有权限.
   */
  public List<PermissionDto> getAllPermsToRole(Integer domainId, Integer roleId) {
    CheckEmpty.checkEmpty(domainId, "domainId");
    CheckEmpty.checkEmpty(roleId, "roleId");
    // 1. get all permissions under the domain
    PermissionExample permissionExample = new PermissionExample();
    permissionExample.createCriteria().andDomainIdEqualTo(domainId)
        .andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<Permission> permissions = permissionMapper.selectByExample(permissionExample);
    if (CollectionUtils.isEmpty(permissions)) {
      return null;
    }
    RolePermissionExample rolePermissionExample = new RolePermissionExample();
    rolePermissionExample.createCriteria().andRoleIdEqualTo(roleId);
    List<RolePermissionKey> rolePermissionKeys =
        rolePermissionMapper.selectByExample(rolePermissionExample);

    // 2. get the checked permIds for the role
    Set<Integer> permIds = null;
    if (!CollectionUtils.isEmpty(rolePermissionKeys)) {
      permIds = new TreeSet<>();
      for (RolePermissionKey rolePermissionKey : rolePermissionKeys) {
        permIds.add(rolePermissionKey.getPermissionId());
      }
    }
    List<PermType> permTypes = permTypeMapper.selectByExample(new PermTypeExample());
    // build permType index.
    Map<Integer, String> permTypeIdTypePairs = new TreeMap<>();
    for (PermType permType : permTypes) {
      permTypeIdTypePairs.put(permType.getId(), permType.getType());
    }

    // 3. construct all permissions linked with the role & mark the permission checked on the
    // role or not
    List<PermissionDto> permissionDtos = new ArrayList<>();
    for (Permission permission : permissions) {
      PermissionDto permissionDto = BeanConverter.convert(permission);
      permissionDto.setPermType(permTypeIdTypePairs.get(permission.getPermTypeId()));
      if (permIds != null && permIds.contains(permission.getId())) {
        permissionDto.setChecked(Boolean.TRUE);
      } else {
        permissionDto.setChecked(Boolean.FALSE);
      }
      permissionDtos.add(permissionDto);
    }
    return permissionDtos;
  }
}
