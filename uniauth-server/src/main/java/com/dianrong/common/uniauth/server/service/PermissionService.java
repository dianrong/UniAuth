package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermTypeDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UrlRoleMappingDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
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
import com.dianrong.common.uniauth.server.data.entity.ext.UrlRoleMappingExt;
import com.dianrong.common.uniauth.server.data.mapper.PermTypeMapper;
import com.dianrong.common.uniauth.server.data.mapper.PermissionMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleCodeMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.RolePermissionMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.service.common.CommonService;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    domainDataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID,
        domainId);

    // 同一个域下面不能出现重复数据
    dataFilter.addFieldsCheck(FilterType.EXSIT_DATA,
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
    domainDataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID,
        domainId);

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
}
