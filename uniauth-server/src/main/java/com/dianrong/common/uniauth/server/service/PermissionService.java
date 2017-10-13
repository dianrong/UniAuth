package com.dianrong.common.uniauth.server.service;

import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.dianrong.common.uniauth.common.bean.dto.*;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.*;
import com.dianrong.common.uniauth.server.data.entity.ext.UrlRoleMappingExt;
import com.dianrong.common.uniauth.server.data.mapper.*;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.service.common.CommonService;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;

@Service
public class PermissionService extends TenancyBasedService {

  @Autowired
  private PermTypeMapper permTypeMapper;
  @Autowired
  private PermissionMapper permissionMapper;
  @Autowired
  private RoleMapper roleMapper;
  @Autowired
  private RolePermissionMapper rolePermissionMapper;
  @Autowired
  private RoleCodeMapper roleCodeMapper;
  @Autowired
  private UserRoleMapper userRoleMapper;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private CommonService commonService;

  /**
   * . 进行权限数据过滤的filter
   */
  @Resource(name = "permissionDataFilter")
  private DataFilter dataFilter;

  /**
   * . 进行域名数据过滤的filter
   */
  @Resource(name = "domainDataFilter")
  private DataFilter domainDataFilter;

  /**
   * 获取所有的权限编码.
   */
  public List<PermTypeDto> getAllPermTypeCodes() {
    PermTypeExample example = new PermTypeExample();
    List<PermType> permTypeList = permTypeMapper.selectByExample(example);
    List<PermTypeDto> permTypeDtoList = new ArrayList<PermTypeDto>();
    if (permTypeList != null && !permTypeList.isEmpty()) {
      for (PermType pt : permTypeList) {
        permTypeDtoList.add(BeanConverter.convert(pt));
      }
    }

    return permTypeDtoList;
  }

  /**
   * 添加新的权限信息.
   */
  @Transactional
  public PermissionDto addNewPerm(PermissionParam permissionParam) {
    Integer domainId = permissionParam.getDomainId();
    Integer permTypeId = permissionParam.getPermTypeId();
    String value = permissionParam.getValue();
    CheckEmpty.checkEmpty(domainId, "域ID");
    CheckEmpty.checkEmpty(permTypeId, "权限类型ID");
    CheckEmpty.checkEmpty(value, "权限的值");

    // 域名id必须是有效的
    domainDataFilter.addFieldCheck(FilterType.EXIST, FieldType.FIELD_TYPE_ID, domainId);

    // 同一个域下面不能出现重复数据
    dataFilter.addFieldsCheck(FilterType.NON_EXIST,
        FilterData.buildFilterData(FieldType.FIELD_TYPE_VALUE, value),
        FilterData.buildFilterData(FieldType.FIELD_TYPE_PERM_TYPE_ID, permTypeId),
        FilterData.buildFilterData(FieldType.FIELD_TYPE_DOMAIN_ID, domainId));

    Permission permission = BeanConverter.convert(permissionParam, false);
    permission.setStatus(AppConstants.STATUS_ENABLED);
    permission.setTenancyId(tenancyService.getTenancyIdWithCheck());
    permissionMapper.insert(permission);

    PermissionDto permissionDto = BeanConverter.convert(permission);
    PermType permType = permTypeMapper.selectByPrimaryKey(permTypeId);
    permissionDto.setPermType(permType.getType());

    return permissionDto;
  }

  /**
   * 更新权限信息.
   */
  @Transactional
  public void updatePerm(PermissionParam permissionParam) {
    Integer domainId = permissionParam.getDomainId();
    Integer permTypeId = permissionParam.getPermTypeId();
    String value = permissionParam.getValue();
    CheckEmpty.checkEmpty(permissionParam.getId(), "主键ID");
    CheckEmpty.checkEmpty(domainId, "域ID");
    CheckEmpty.checkEmpty(permTypeId, "权限类型ID");
    CheckEmpty.checkEmpty(value, "权限的值");

    // 域名id必须是有效的
    domainDataFilter.addFieldCheck(FilterType.EXIST, FieldType.FIELD_TYPE_ID, domainId);

    Byte status = permissionParam.getStatus();
    // 启用或者启用状态的修改
    if ((status != null && status == AppConstants.STATUS_ENABLED) || status == null) {
      // 同一个域下面不能出现重复数据
      dataFilter.updateFieldsCheck(permissionParam.getId(),
          FilterData.buildFilterData(FieldType.FIELD_TYPE_VALUE, value),
          FilterData.buildFilterData(FieldType.FIELD_TYPE_PERM_TYPE_ID, permTypeId),
          FilterData.buildFilterData(FieldType.FIELD_TYPE_DOMAIN_ID, domainId));
    }

    Permission permission = BeanConverter.convert(permissionParam, true);
    permission.setTenancyId(tenancyService.getTenancyIdWithCheck());
    permissionMapper.updateByPrimaryKey(permission);
  }

  @Transactional
  public void deletePerm(PrimaryKeyParam primaryKeyParam) {
    CheckEmpty.checkParamId(primaryKeyParam, "权限ID");
    permissionMapper.deleteByPrimaryKey(primaryKeyParam.getId());
  }

  /**
   * 替换权限与角色的关联关系.
   */
  @Transactional
  public void replaceRolesToPerm(Integer permId, List<Integer> roleIds) {
    CheckEmpty.checkEmpty(permId, "permId");

    RolePermissionExample rolePermissionExample = new RolePermissionExample();
    rolePermissionExample.createCriteria().andPermissionIdEqualTo(permId);
    if (CollectionUtils.isEmpty(roleIds)) {
      rolePermissionMapper.deleteByExample(rolePermissionExample);
      return;
    }
    List<RolePermissionKey> rolePermissionKeys =
        rolePermissionMapper.selectByExample(rolePermissionExample);
    if (!CollectionUtils.isEmpty(rolePermissionKeys)) {
      ArrayList<Integer> dbRoleIds = new ArrayList<>();
      for (RolePermissionKey rolePermissionKey : rolePermissionKeys) {
        dbRoleIds.add(rolePermissionKey.getRoleId());
      }
      @SuppressWarnings("unchecked")
      ArrayList<Integer> intersections = ((ArrayList<Integer>) dbRoleIds.clone());
      intersections.retainAll(roleIds);
      List<Integer> roleIdsNeedAddToDb = new ArrayList<>();
      List<Integer> roleIdsNeedDeleteFromDb = new ArrayList<>();
      for (Integer roleId : roleIds) {
        if (!intersections.contains(roleId)) {
          roleIdsNeedAddToDb.add(roleId);
        }
      }
      for (Integer dbRoleId : dbRoleIds) {
        if (!intersections.contains(dbRoleId)) {
          roleIdsNeedDeleteFromDb.add(dbRoleId);
        }
      }

      if (!CollectionUtils.isEmpty(roleIdsNeedAddToDb)) {
        for (Integer roleIdNeedAddToDb : roleIdsNeedAddToDb) {
          RolePermissionKey rolePermissionKey = new RolePermissionKey();
          rolePermissionKey.setRoleId(roleIdNeedAddToDb);
          rolePermissionKey.setPermissionId(permId);
          rolePermissionMapper.insert(rolePermissionKey);
        }
      }
      if (!CollectionUtils.isEmpty(roleIdsNeedDeleteFromDb)) {
        RolePermissionExample rolePermDeleteExample = new RolePermissionExample();
        rolePermDeleteExample.createCriteria().andPermissionIdEqualTo(permId)
            .andRoleIdIn(roleIdsNeedDeleteFromDb);
        rolePermissionMapper.deleteByExample(rolePermDeleteExample);
      }
    } else {
      for (Integer roleId : roleIds) {
        RolePermissionKey rolePermissionKey = new RolePermissionKey();
        rolePermissionKey.setRoleId(roleId);
        rolePermissionKey.setPermissionId(permId);
        rolePermissionMapper.insert(rolePermissionKey);
      }
    }
  }

  /**
   * 根据域和权限获取角色信息.
   */
  public List<RoleDto> getAllRolesToPermUnderADomain(Integer domainId, Integer permissionId) {
    CheckEmpty.checkEmpty(domainId, "域ID");
    CheckEmpty.checkEmpty(permissionId, "权限ID");
    // 1. get all roles under the domain
    RoleExample roleExample = new RoleExample();
    roleExample.createCriteria().andDomainIdEqualTo(domainId)
        .andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<Role> roles = roleMapper.selectByExample(roleExample);
    if (CollectionUtils.isEmpty(roles)) {
      return null;
    }
    RolePermissionExample rolePermissionExample = new RolePermissionExample();
    rolePermissionExample.createCriteria().andPermissionIdEqualTo(permissionId);
    List<RolePermissionKey> rolePermissionKeys =
        rolePermissionMapper.selectByExample(rolePermissionExample);

    // 2. get the checked roleIds for the perm
    Set<Integer> roleIds = null;
    if (!CollectionUtils.isEmpty(rolePermissionKeys)) {
      roleIds = new TreeSet<>();
      for (RolePermissionKey rolePermissionKey : rolePermissionKeys) {
        roleIds.add(rolePermissionKey.getRoleId());
      }
    }
    List<RoleCode> roleCodes = roleCodeMapper.selectByExample(new RoleCodeExample());
    // build permType index.
    Map<Integer, String> roleCodeIdCodePairs = new TreeMap<>();
    for (RoleCode roleCode : roleCodes) {
      roleCodeIdCodePairs.put(roleCode.getId(), roleCode.getCode());
    }

    // 3. construct all roles linked with the perm & mark the role checked on the perm or not
    List<RoleDto> roleDtos = new ArrayList<>();
    for (Role role : roles) {
      RoleDto roleDto = BeanConverter.convert(role);
      roleDto.setRoleCode(roleCodeIdCodePairs.get(role.getRoleCodeId()));
      if (roleIds != null && roleIds.contains(role.getId())) {
        roleDto.setChecked(Boolean.TRUE);
      } else {
        roleDto.setChecked(Boolean.FALSE);
      }
      roleDtos.add(roleDto);
    }
    return roleDtos;
  }

  /**
   * 保存角色到权限信息.
   */
  @Transactional
  public void saveRolesToPerm(PermissionParam permissionParam) {
    Integer domainId = permissionParam.getDomainId();
    Integer permissionId = permissionParam.getId();
    CheckEmpty.checkEmpty(domainId, "域ID");
    CheckEmpty.checkEmpty(permissionId, "权限ID");

    RolePermissionExample example = new RolePermissionExample();
    example.createCriteria().andPermissionIdEqualTo(permissionId);

    rolePermissionMapper.deleteByExample(example);

    List<Integer> roleIds = permissionParam.getRoleIds();
    if (roleIds != null && !roleIds.isEmpty()) {
      for (Integer roleId : roleIds) {
        RolePermissionKey rolePermissionKey = new RolePermissionKey();
        rolePermissionKey.setRoleId(roleId);
        rolePermissionKey.setPermissionId(permissionId);
        rolePermissionMapper.insert(rolePermissionKey);
      }
    }
  }

  /**
   * 根据条件查询权限信息列表.
   */
  public PageDto<PermissionDto> searchPerm(List<Integer> permIds, Integer permId, Integer domainId,
      Byte status, String valueExt, String value, Integer permTypeId, Integer pageNumber,
      Integer pageSize) {
    CheckEmpty.checkEmpty(pageNumber, "pageNumber");
    CheckEmpty.checkEmpty(pageSize, "pageSize");
    PermissionExample permissionExample = new PermissionExample();
    permissionExample.setPageOffSet(pageNumber * pageSize);
    permissionExample.setPageSize(pageSize);
    permissionExample.setOrderByClause("status asc");
    PermissionExample.Criteria criteria = permissionExample.createCriteria();
    if (!CollectionUtils.isEmpty(permIds)) {
      criteria.andIdIn(permIds);
    }
    if (permId != null) {
      criteria.andIdEqualTo(permId);
    }
    if (domainId != null) {
      criteria.andDomainIdEqualTo(domainId);
    }
    if (status != null) {
      criteria.andStatusEqualTo(status);
    }
    if (!StringUtils.isEmpty(value)) {
      criteria.andValueLike("%" + value + "%");
    }
    if (!StringUtils.isEmpty(valueExt)) {
      criteria.andValueLike("%" + valueExt + "%");
    }
    if (permTypeId != null) {
      criteria.andPermTypeIdEqualTo(permTypeId);
    }
    criteria.andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    Integer totalCount = permissionMapper.countByExample(permissionExample);
    ParamCheck.checkPageParams(pageNumber, pageSize, totalCount);
    List<Permission> permissionList = permissionMapper.selectByExample(permissionExample);

    Map<Integer, PermType> permTypeMap = commonService.getPermTypeMap();

    if (permissionList != null && !permissionList.isEmpty()) {
      List<PermissionDto> permissionDtoList = new ArrayList<PermissionDto>();
      for (Permission permission : permissionList) {
        PermissionDto permissionDto = BeanConverter.convert(permission);
        Integer permissionDtoPermTypeId = permissionDto.getPermTypeId();
        PermType permType = permTypeMap.get(permissionDtoPermTypeId);

        permissionDto.setPermType(permType.getType());
        permissionDtoList.add(permissionDto);
      }
      return new PageDto<>(pageNumber, pageSize, totalCount, permissionDtoList);
    }
    return null;
  }

  /**
   * 获取UrlRoleMapping.
   */
  public List<UrlRoleMappingDto> getUrlRoleMapping(DomainParam domainParam) {
    String domainCode = domainParam.getCode();
    CheckEmpty.checkEmpty(domainCode, "域编码");

    Map<String, Object> values = new HashMap<String, Object>();
    values.put("domainCode", domainCode);
    if (domainParam.getIncludeTenancyIds() != null
        && !domainParam.getIncludeTenancyIds().isEmpty()) {
      values.put("tenancyIds", domainParam.getIncludeTenancyIds());
    }
    List<UrlRoleMappingExt> urlRoleMappingExtList = permissionMapper.selectUrlRoleMapping(values);
    List<UrlRoleMappingDto> urlRoleMappingDtoList = new ArrayList<UrlRoleMappingDto>();
    if (urlRoleMappingExtList != null) {
      for (UrlRoleMappingExt urlRoleMappingExt : urlRoleMappingExtList) {
        urlRoleMappingDtoList.add(BeanConverter.convert(urlRoleMappingExt));
      }
    }
    return urlRoleMappingDtoList;
  }

  /**
   * 根据权限ID查询关联的用户信息.
   * 
   * @param permIds 权限id集合.
   * @param includePermRole 返回结果是否包含权限和角色关联关系信息.
   * @param includeRoleUser 返回结果是否包含角色和用户的关联关系信息.
   * @param includeDisableUser 是否将禁用用户信息也查询返回.
   */
  public List<PermissionDto> searchUsersByPerms(List<Integer> permIds, Boolean includePermRole,
      Boolean includeRoleUser, Boolean includeDisableUser) {
    if (ObjectUtils.isEmpty(permIds)) {
      return Collections.emptyList();
    }
    // 将权限信息查询出来
    PermissionExample permissionExample = new PermissionExample();
    PermissionExample.Criteria pCriteria = permissionExample.createCriteria();
    pCriteria.andIdIn(permIds);
    List<Permission> perms = permissionMapper.selectByExample(permissionExample);
    if (ObjectUtils.isEmpty(perms)) {
      return Collections.emptyList();
    }
    Map<Integer, PermissionDto> permMap = new HashMap<>(perms.size());
    List<PermissionDto> permResult = new ArrayList<>(perms.size());
    for (Permission perm : perms) {
      PermissionDto permDto = BeanConverter.convert(perm);
      permMap.put(perm.getId(), permDto);
      permResult.add(permDto);
    }
    RolePermissionExample rolePermissionExample = new RolePermissionExample();
    RolePermissionExample.Criteria rpCriteria = rolePermissionExample.createCriteria();
    rpCriteria.andPermissionIdIn(permIds);
    List<RolePermissionKey> rpKeyList = rolePermissionMapper.selectByExample(rolePermissionExample);
    if (ObjectUtils.isEmpty(rpKeyList)) {
      return permResult;
    }

    // 权限和角色的关联关系
    Map<Integer, List<Integer>> permRolesMap = new HashMap<>(rpKeyList.size());
    Set<Integer> allRoleIds = new HashSet<>(rpKeyList.size());
    for (RolePermissionKey rpk : rpKeyList) {
      List<Integer> roleIds = permRolesMap.get(rpk.getPermissionId());
      if (roleIds == null) {
        roleIds = new ArrayList<>();
        permRolesMap.put(rpk.getPermissionId(), roleIds);
      }
      roleIds.add(rpk.getRoleId());
      allRoleIds.add(rpk.getRoleId());
    }

    // 获取所有的关联的角色信息
    RoleExample roleExample = new RoleExample();
    RoleExample.Criteria roleExampleCriteria = roleExample.createCriteria();
    roleExampleCriteria.andIdIn(new ArrayList<Integer>(allRoleIds))
        .andStatusEqualTo(AppConstants.STATUS_ENABLED);
    List<Role> roleList = roleMapper.selectByExample(roleExample);
    Map<Integer, RoleDto> roleDtoMap;
    List<Integer> enableRoleIds;
    if (!ObjectUtils.isEmpty(roleList)) {
      roleDtoMap = new HashMap<>(roleList.size());
      enableRoleIds = new ArrayList<>(roleList.size());
      for (Role role : roleList) {
        roleDtoMap.put(role.getId(), BeanConverter.convert(role));
        enableRoleIds.add(role.getId());
      }
    } else {
      roleDtoMap = Collections.emptyMap();
      enableRoleIds = Collections.emptyList();
    }

    // 获取角色关联的用户
    Map<Long, UserDto> userDtoMap = new HashMap<>();
    Map<Integer, List<Long>> roleUserListMap = new HashMap<>(enableRoleIds.size());
    if (!enableRoleIds.isEmpty()) {
      UserRoleExample userRoleExample = new UserRoleExample();
      UserRoleExample.Criteria urCriteria = userRoleExample.createCriteria();
      urCriteria.andRoleIdIn(enableRoleIds);
      List<UserRoleKey> userRoleKeyList = userRoleMapper.selectByExample(userRoleExample);
      if (!ObjectUtils.isEmpty(userRoleKeyList)) {
        Set<Long> userIds = new HashSet<>(userRoleKeyList.size());
        for (UserRoleKey urk : userRoleKeyList) {
          List<Long> roleUserIds = roleUserListMap.get(urk.getRoleId());
          if (roleUserIds == null) {
            roleUserIds = new ArrayList<>();
            roleUserListMap.put(urk.getRoleId(), roleUserIds);
          }
          roleUserIds.add(urk.getUserId());
          userIds.add(urk.getUserId());
        }
        // 获取用户的详细信息
        UserExample userExample = new UserExample();
        UserExample.Criteria uCriteria = userExample.createCriteria();
        uCriteria.andIdIn(new ArrayList<Long>(userIds));
        if (!(includeDisableUser != null && includeDisableUser)) {
          uCriteria.andStatusEqualTo(AppConstants.STATUS_ENABLED);
        }
        List<User> userList = userMapper.selectByExample(userExample);
        if (!ObjectUtils.isEmpty(userList)) {
          for (User user : userList) {
            userDtoMap.put(user.getId(), BeanConverter.convert(user));
          }
        }
      }
    }

    // 组装结果
    boolean includeRoles = (includePermRole != null && includePermRole);
    boolean includeRoleUsers = (includeRoleUser != null && includeRoleUser);
    for (PermissionDto permDto : permResult) {
      Set<UserDto> userDtoSet = new HashSet<>();
      List<Integer> roleIds = permRolesMap.get(permDto.getId());
      Set<RoleDto> roleDtoSet = new HashSet<>();
      Map<Integer, List<UserDto>> roleUsersMap = new HashMap<>();
      if (roleIds != null) {
        for (Integer roleId : roleIds) {
          RoleDto roleDto = roleDtoMap.get(roleId);
          if (roleDto != null) {
            roleDtoSet.add(roleDto);
          }
          List<UserDto> userDtoPermRole = new ArrayList<>();
          // 角色和用户的关联信息组装
          List<Long> userIds = roleUserListMap.get(roleId);
          if (userIds != null) {
            for (Long userId : userIds) {
              UserDto userDto = userDtoMap.get(userId);
              if (userDto != null) {
                userDtoSet.add(userDto);
                userDtoPermRole.add(userDto);
              }
            }
          }
          roleUsersMap.put(roleId, userDtoPermRole);
        }
      }
      permDto.setRelatedUsers(new ArrayList<UserDto>(userDtoSet));
      if (includeRoles) {
        permDto.setRelatedRoles(new ArrayList<RoleDto>(roleDtoSet));
      }
      if (includeRoleUsers) {
        permDto.setRoleUserListMap(roleUsersMap);
      }
    }
    return permResult;
  }
}
