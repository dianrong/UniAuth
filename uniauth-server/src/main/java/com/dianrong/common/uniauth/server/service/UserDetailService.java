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
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;

import java.util.List;

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
    UserDetail userDetail = getUserDetaiFromParam(param, user.getTenancyId());
    if (ObjectUtil.collectionIsEmptyOrNull(userDetails)) {
      // Add new one
      userDetailMapper.insert(userDetail);
    } else {
      // Update the exist one
      Long primaryId = userDetails.get(0).getId();
      userDetail.setId(primaryId);
      userDetailMapper.updateByPrimaryKey(userDetail);
    }
    return BeanConverter.convert(userDetail);
  }

  /**
   * 用于更新非空字段.
   */
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
    UserDetail userDetail = getUserDetaiFromParam(param, user.getTenancyId());
    int count = userDetailMapper.updateByExampleSelective(userDetail, userDetailExample);
    if (count == 0) {
      log.debug("can not update one user detail record, the user id {}", userId);
    }
    return searchByUserId(userId);
  }

  private UserDetail getUserDetaiFromParam(UserDetailInfoParam param, Long tenancyId) {
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
    userDetail.setTenancyId(tenancyId);
    return userDetail;
  }
}
