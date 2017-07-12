package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailInfoDto;
import com.dianrong.common.uniauth.common.bean.request.UserDetailInfoParam;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.entity.UserDetail;
import com.dianrong.common.uniauth.server.data.entity.UserDetailExample;
import com.dianrong.common.uniauth.server.data.mapper.UserDetailMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserMapper;
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

/**
 * UserDetail操作.
 */

@Slf4j
@Service
public class UserDetailService extends TenancyBasedService {

  @Autowired
  private UserDetailMapper userDetailMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private UserProfileInnerService userProfileInnerService;

  /**
   * 根据UserId去查询关联的UserDetail信息.
   * 
   * @param userId 用户id,不能为空.
   */
  public UserDetailInfoDto searchByUserId(Long userId) {
    CheckEmpty.checkEmpty(userId, "userId");
    UserDetailExample userDetailExample = new UserDetailExample();
    UserDetailExample.Criteria criteria = userDetailExample.createCriteria();
    criteria.andUserIdEqualTo(userId);
    List<UserDetail> userDetails = userDetailMapper.selectByExample(userDetailExample);
    if (ObjectUtil.collectionIsEmptyOrNull(userDetails)) {
      return null;
    }
    return BeanConverter.convert(userDetails.get(0));
  }

  /**
   * 添加获取更新用户的detail信息.
   */
  @Transactional
  public UserDetailInfoDto addOrUpdateUserDetail(UserDetailInfoParam param) {
    Long userId = param.getUserId();
    CheckEmpty.checkEmpty(userId, "userId");

    // Check 这个UserId是否正确
    User user = userMapper.selectByPrimaryKey(userId);
    if (user == null) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.parameter.wrong", userId));
    }

    UserDetailExample userDetailExample = new UserDetailExample();
    UserDetailExample.Criteria criteria = userDetailExample.createCriteria();
    criteria.andUserIdEqualTo(userId);
    List<UserDetail> userDetails = userDetailMapper.selectByExample(userDetailExample);
    UserDetail userDetail = getUserDetaiFromParam(param);
    userDetail.setTenancyId(user.getTenancyId());
    if (ObjectUtil.collectionIsEmptyOrNull(userDetails)) {
      // Add new one
      userDetailMapper.insert(userDetail);
    } else {
      // Update the exist one
      Long primaryId = userDetails.get(0).getId();
      userDetail.setId(primaryId);
      userDetailMapper.updateByPrimaryKey(userDetail);
    }
    updateUserExtendVal(param, true);
    return BeanConverter.convert(userDetail);
  }

  /**
   * 用于更新非空字段.
   */
  @Transactional
  public UserDetailInfoDto updateUserDetail(UserDetailInfoParam param) {
    Long userId = param.getUserId();
    CheckEmpty.checkEmpty(userId, "userId");

    // Check 这个UserId是否正确
    User user = userMapper.selectByPrimaryKey(userId);
    if (user == null) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.parameter.wrong", userId));
    }
    UserDetailExample userDetailExample = new UserDetailExample();
    UserDetailExample.Criteria criteria = userDetailExample.createCriteria();
    criteria.andUserIdEqualTo(userId);
    UserDetail userDetail = getUserDetaiFromParam(param);
    userDetail.setTenancyId(user.getTenancyId());
    int count = userDetailMapper.updateByExampleSelective(userDetail, userDetailExample);
    if (count == 0) {
      log.debug("can not update one user detail record, the user id {}", userId);
    }
    updateUserExtendVal(param, false);
    return searchByUserId(userId);
  }

  private void updateUserExtendVal(UserDetailInfoParam param, boolean updateEmptyVal) {
    Map<String, String> attributes = Maps.newHashMap();
    updateAttributes(attributes, AtrributeDefine.FIRST_NAME, param.getFirstName(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.LAST_NAME, param.getLastName(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.DISPLAY_NAME, param.getDisplayName(),
        updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.NICK_NAME, param.getNickName(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.IDENTITY_NO, param.getIdentityNo(),
        updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.MOTTO, param.getMotto(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.IMAGE, param.getImage(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.SSN, param.getSsn(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.WEIBO, param.getWeibo(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.WECHAT_NO, param.getWechatNo(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.ADDRESS, param.getAddress(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.BIRTHDAY, param.getBirthday(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.GENDER, param.getGender(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.POSITION, param.getPosition(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.LAST_POSITION_MODIFY_DATE,
        param.getLastPositionModifyDate(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.DEPARTMENT, param.getDepartment(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.TITLE, param.getTitle(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.AID, param.getAid(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.ENTRY_DATE, param.getEntryDate(), updateEmptyVal);
    updateAttributes(attributes, AtrributeDefine.LEAVE_DATE, param.getLeaveDate(), updateEmptyVal);
    userProfileInnerService.addOrUpdateUserAttributes(param.getUserId(), attributes);
  }

  private void updateAttributes(Map<String, String> attributes, AtrributeDefine define, Object val,
      boolean updateEmptyVal) {
    if (updateEmptyVal || val != null) {
      attributes.put(define.getAttributeCode(), define.getTypeTranslater().toString(val));
    }
  }

  private UserDetail getUserDetaiFromParam(UserDetailInfoParam param) {
    UserDetail userDetail = new UserDetail();
    if (param == null) {
      return null;
    }
    userDetail.setAddress(param.getAddress());
    userDetail.setAid(param.getAid());
    userDetail.setBirthday(param.getBirthday());
    userDetail.setDepartment(param.getDepartment());
    userDetail.setDisplayName(param.getDisplayName());
    userDetail.setEntryDate(param.getEntryDate());
    userDetail.setFirstName(param.getFirstName());
    userDetail.setGender(param.getGender());
    userDetail.setIdentityNo(param.getIdentityNo());
    userDetail.setImage(param.getImage());
    userDetail.setLastName(param.getLastName());
    userDetail.setLastPositionModifyDate(param.getLastPositionModifyDate());
    userDetail.setLeaveDate(param.getLeaveDate());
    userDetail.setMotto(param.getMotto());
    userDetail.setNickName(param.getNickName());
    userDetail.setPosition(param.getPosition());
    userDetail.setRemark(param.getRemark());
    userDetail.setSsn(param.getSsn());
    userDetail.setTitle(param.getTitle());
    userDetail.setUserId(param.getUserId());
    userDetail.setWechatNo(param.getWechatNo());
    userDetail.setWeibo(param.getWeibo());
    return userDetail;
  }
}
