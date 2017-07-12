package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.UserWorkRelationshipDto;
import com.dianrong.common.uniauth.common.bean.request.UserWorkRelationshipParam;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.entity.UserWorkRelationship;
import com.dianrong.common.uniauth.server.data.entity.UserWorkRelationshipExample;
import com.dianrong.common.uniauth.server.data.mapper.UserMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserWorkRelationshipMapper;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.service.inner.UserProfileInnerService;
import com.dianrong.common.uniauth.server.service.support.AtrributeDefine;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserWorkRelationshipService extends TenancyBasedService {

  @Autowired
  private UserWorkRelationshipMapper userWorkRelationshipMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private UserProfileInnerService userProfileInnerService;

  /**
   * 根据UserId去查询关联的UserDetail信息.
   * 
   * @param userId 用户id,不能为空.
   */
  public UserWorkRelationshipDto searchByUserId(Long userId) {
    CheckEmpty.checkEmpty(userId, "userId");
    UserWorkRelationshipExample example = new UserWorkRelationshipExample();
    UserWorkRelationshipExample.Criteria criteria = example.createCriteria();
    criteria.andUserIdEqualTo(userId);
    List<UserWorkRelationship> userWorkRelationships =
        userWorkRelationshipMapper.selectByExample(example);
    if (ObjectUtil.collectionIsEmptyOrNull(userWorkRelationships)) {
      return null;
    }
    return BeanConverter.convert(userWorkRelationships.get(0));
  }

  /**
   * 更新或者添加汇报线关系.
   */
  @Transactional
  public UserWorkRelationshipDto addOrUpdateUserWrokRelationship(UserWorkRelationshipParam param) {
    Long userId = param.getUserId();
    CheckEmpty.checkEmpty(userId, "userId");

    // Check 这个UserId是否正确
    User user = userMapper.selectByPrimaryKey(userId);
    if (user == null) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.parameter.wrong", userId));
    }

    UserWorkRelationshipExample example = new UserWorkRelationshipExample();
    UserWorkRelationshipExample.Criteria criteria = example.createCriteria();
    criteria.andUserIdEqualTo(userId);
    List<UserWorkRelationship> userWorkRelationbShips =
        userWorkRelationshipMapper.selectByExample(example);
    UserWorkRelationship userWorkRelationbShip = getUserWorkRelationshipFromParam(param);
    userWorkRelationbShip.setTenancyId(user.getTenancyId());
    if (ObjectUtil.collectionIsEmptyOrNull(userWorkRelationbShips)) {
      // Add new one
      userWorkRelationshipMapper.insert(userWorkRelationbShip);
    } else {
      // Update the exist one
      Long primaryId = userWorkRelationbShips.get(0).getId();
      userWorkRelationbShip.setId(primaryId);
      userWorkRelationshipMapper.updateByPrimaryKey(userWorkRelationbShip);
    }
    // 关联更新扩展属性值
    updateUserExtendVal(param, true);
    return BeanConverter.convert(userWorkRelationbShip);
  }

  /**
   * 只更新非空的属性.
   */
  public UserWorkRelationshipDto updateUserWrokRelationship(UserWorkRelationshipParam param) {
    Long userId = param.getUserId();
    CheckEmpty.checkEmpty(userId, "userId");

    // Check 这个UserId是否正确
    User user = userMapper.selectByPrimaryKey(userId);
    if (user == null) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.parameter.wrong", userId));
    }
    UserWorkRelationshipExample example = new UserWorkRelationshipExample();
    UserWorkRelationshipExample.Criteria criteria = example.createCriteria();
    criteria.andUserIdEqualTo(userId);
    UserWorkRelationship userWorkRelationbShip = getUserWorkRelationshipFromParam(param);
    userWorkRelationbShip.setTenancyId(user.getTenancyId());
    int count = userWorkRelationshipMapper.updateByExampleSelective(userWorkRelationbShip, example);
    if (count == 0) {
      log.debug("can not update one user detail record, the user id {}", userId);
    }

    // 关联更新扩展属性值
    updateUserExtendVal(param, false);
    return searchByUserId(userId);
  }

  private void updateUserExtendVal(UserWorkRelationshipParam param, boolean updateEmptyVal) {
    Map<String, String> attributes = Maps.newHashMap();
    Long managerId = param.getManagerId();
    Long supervisorId = param.getSupervisorId();
    if (updateEmptyVal || managerId != null) {
      attributes.put(AtrributeDefine.MANAGER_ID.getAttributeCode(),
          AtrributeDefine.MANAGER_ID.getTypeTranslater().toString(managerId));
    }
    if (updateEmptyVal || supervisorId != null) {
      attributes.put(AtrributeDefine.SUPERVISOR_ID.getAttributeCode(),
          AtrributeDefine.SUPERVISOR_ID.getTypeTranslater().toString(supervisorId));
    }
    userProfileInnerService.addOrUpdateUserAttributes(param.getUserId(), attributes);
  }

  private UserWorkRelationship getUserWorkRelationshipFromParam(UserWorkRelationshipParam param) {
    UserWorkRelationship userWorkRelationbShip = new UserWorkRelationship();
    if (param == null) {
      return null;
    }
    userWorkRelationbShip.setAssignmentDate(param.getAssignmentDate());
    userWorkRelationbShip.setBusinessUnitName(param.getBusinessUnitName());
    userWorkRelationbShip.setDepartmentName(param.getDepartmentName());
    userWorkRelationbShip.setHireDate(param.getHireDate());
    userWorkRelationbShip.setLegalEntityName(param.getLegalEntityName());
    userWorkRelationbShip.setManagerId(param.getManagerId());
    userWorkRelationbShip.setSupervisorId(param.getSupervisorId());
    userWorkRelationbShip.setUserId(param.getUserId());
    userWorkRelationbShip.setWorkAddress(param.getWorkAddress());
    userWorkRelationbShip.setWorkLocation(param.getWorkLocation());
    userWorkRelationbShip.setWorkPhone(param.getWorkPhone());
    return userWorkRelationbShip;
  }
}
