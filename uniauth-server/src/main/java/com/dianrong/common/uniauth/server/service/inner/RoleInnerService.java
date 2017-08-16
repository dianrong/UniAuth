package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.GrpRoleExample;
import com.dianrong.common.uniauth.server.data.entity.GrpRoleKey;
import com.dianrong.common.uniauth.server.data.entity.Role;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.data.entity.RoleExample;
import com.dianrong.common.uniauth.server.data.entity.UserRoleExample;
import com.dianrong.common.uniauth.server.data.entity.UserRoleKey;
import com.dianrong.common.uniauth.server.data.mapper.GrpRoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserRoleMapper;
import com.dianrong.common.uniauth.server.service.common.CommonService;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色操作的内部服务.
 */
@Service
public class RoleInnerService extends TenancyBasedService {

  @Autowired
  private RoleMapper roleMapper;

  @Autowired
  private GrpRoleMapper grpRoleMapper;

  @Autowired
  private UserRoleMapper userRoleMapper;

  @Autowired
  private CommonService commonService;

  /**
   * 获取组与角色的映射信息.
   * 
   * @param grpIds 查询组的范围.
   * @param domainId 所在的域id.
   */
  public Map<Integer, List<RoleDto>> queryGrpRole(List<Integer> grpIds, Integer domainId) {
    CheckEmpty.checkEmpty(domainId, "domainId");
    Map<Integer, List<RoleDto>> grpRoleMap = Maps.newHashMap();
    if (ObjectUtil.collectionIsEmptyOrNull(grpIds)) {
      return grpRoleMap;
    }
    GrpRoleExample grpRoleExample = new GrpRoleExample();
    GrpRoleExample.Criteria criteria = grpRoleExample.createCriteria();
    criteria.andGrpIdIn(grpIds);
    List<GrpRoleKey> grpRoleList = grpRoleMapper.selectByExample(grpRoleExample);
    if (ObjectUtil.collectionIsEmptyOrNull(grpRoleList)) {
      return grpRoleMap;
    }
    List<Integer> roleIds = Lists.newArrayList();
    for (GrpRoleKey gr : grpRoleList) {
      roleIds.add(gr.getRoleId());
    }

    RoleExample roleExample = new RoleExample();
    RoleExample.Criteria roleCriteria = roleExample.createCriteria();
    roleCriteria.andIdIn(roleIds).andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andDomainIdEqualTo(domainId);
    List<Role> roles = roleMapper.selectByExample(roleExample);
    if (ObjectUtil.collectionIsEmptyOrNull(roles)) {
      return grpRoleMap;
    }
    Map<Integer, Role> roleMap = Maps.newHashMap();
    for (Role role : roles) {
      roleMap.put(role.getId(), role);
    }

    Map<Integer, RoleCode> roleCodeMap = commonService.getRoleCodeMap();
    // 构建组与角色的映射
    for (GrpRoleKey grk : grpRoleList) {
      Integer grpId = grk.getGrpId();
      List<RoleDto> roleDtos = grpRoleMap.get(grpId);
      if (roleDtos == null) {
        roleDtos = Lists.newArrayList();
        grpRoleMap.put(grpId, roleDtos);
      }
      RoleDto roleDto = BeanConverter.convert(roleMap.get(grk.getRoleId()));
      if (roleDto == null) {
        continue;
      }
      roleDto.setRoleCode(roleCodeMap.get(roleDto.getRoleCodeId()).getCode());
      roleDtos.add(roleDto);
    }

    return grpRoleMap;
  }

  /**
   * 获取用户与角色的映射信息.
   * 
   * @param userIds 查询用户的范围.
   * @param domainId 所在的域id.
   */
  public Map<Long, List<RoleDto>> queryUserRole(List<Long> userIds, Integer domainId) {
    CheckEmpty.checkEmpty(domainId, "domainId");
    Map<Long, List<RoleDto>> userRoleMap = Maps.newHashMap();
    if (ObjectUtil.collectionIsEmptyOrNull(userIds)) {
      return userRoleMap;
    }
    UserRoleExample userRoleExample = new UserRoleExample();
    UserRoleExample.Criteria criteria = userRoleExample.createCriteria();
    criteria.andUserIdIn(userIds);
    List<UserRoleKey> userRoleList = userRoleMapper.selectByExample(userRoleExample);
    if (ObjectUtil.collectionIsEmptyOrNull(userRoleList)) {
      return userRoleMap;
    }
    List<Integer> roleIds = Lists.newArrayList();
    for (UserRoleKey gr : userRoleList) {
      roleIds.add(gr.getRoleId());
    }

    RoleExample roleExample = new RoleExample();
    RoleExample.Criteria roleCriteria = roleExample.createCriteria();
    roleCriteria.andIdIn(roleIds).andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andDomainIdEqualTo(domainId);
    List<Role> roles = roleMapper.selectByExample(roleExample);
    if (ObjectUtil.collectionIsEmptyOrNull(roles)) {
      return userRoleMap;
    }
    Map<Integer, Role> roleMap = Maps.newHashMap();
    for (Role role : roles) {
      roleMap.put(role.getId(), role);
    }

    Map<Integer, RoleCode> roleCodeMap = commonService.getRoleCodeMap();
    // 构建组与角色的映射
    for (UserRoleKey grk : userRoleList) {
      Long userId = grk.getUserId();
      List<RoleDto> roleDtos = userRoleMap.get(userId);
      if (roleDtos == null) {
        roleDtos = Lists.newArrayList();
        userRoleMap.put(userId, roleDtos);
      }
      RoleDto roleDto = BeanConverter.convert(roleMap.get(grk.getRoleId()));
      if (roleDto == null) {
        continue;
      }
      roleDto.setRoleCode(roleCodeMap.get(roleDto.getRoleCodeId()).getCode());
      roleDtos.add(roleDto);
    }

    return userRoleMap;
  }
}
