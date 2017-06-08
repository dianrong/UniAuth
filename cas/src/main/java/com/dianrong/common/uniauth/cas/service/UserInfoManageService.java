package com.dianrong.common.uniauth.cas.service;

import com.dianrong.common.uniauth.cas.service.support.annotation.TenancyIdentity;
import com.dianrong.common.uniauth.cas.service.support.annotation.TenancyIdentity.Type;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.enm.UserActionEnum;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoManageService extends BaseService {

  @Autowired
  private UARWFacade uarwFacade;

  @Autowired
  private UniClientFacade uniClientFacade;

  /**
   * . 根据用户邮箱或者电话获取用户信息
   *
   * @param account 用户信息标识
   * @param tenancyCode 租户code
   * @return user
   */
  @TenancyIdentity(type = Type.CODE, index = 1)
  public UserDto getUserDetailInfo(String account, String tenancyCode) throws Exception {
    LoginParam loginParam = new LoginParam();
    loginParam.setAccount(StringUtil.trimCompatibleNull(account));
    loginParam.setTenancyCode(StringUtil.trimCompatibleNull(tenancyCode));
    Response<UserDto> response = uniClientFacade.getUserResource().getUserInfoByUserTag(loginParam);
    List<Info> infoList = response.getInfo();
    checkInfoList(infoList);
    return response.getData();
  }

  /**
   * . 根据用户邮箱或者电话获取用户信息
   *
   * @param account 用户信息标识
   * @param tenancyId 租户idgetUserDetailInfo
   * @return user
   */
  @TenancyIdentity(index = 1)
  public UserDto getUserDetailInfo(String account, Long tenancyId) throws Exception {
    LoginParam loginParam = new LoginParam();
    loginParam.setAccount(StringUtil.trimCompatibleNull(account));
    loginParam.setTenancyId(tenancyId);
    Response<UserDto> response = uniClientFacade.getUserResource().getUserInfoByUserTag(loginParam);
    List<Info> infoList = response.getInfo();
    checkInfoList(infoList);
    return response.getData();
  }

  /**
   * 根据账号信息更新用户name.
   * @param account 账号
   * @param tenancyId 租户id
   * @param newName 新的姓名
   * @throws Exception 异常
   */
  @TenancyIdentity(index = 1)
  public void updateUserInfo(String account, Long tenancyId, String newName) throws Exception {
    UserParam userParam = new UserParam();
    userParam.setAccount(StringUtil.trimCompatibleNull(account));
    userParam.setTenancyId(tenancyId);
    userParam.setName(newName);
    userParam.setUserActionEnum(UserActionEnum.UPDATE_INFO_BY_ACCOUNT);
    Response<UserDto> response = uarwFacade.getUserRWResource().updateUser(userParam);
    List<Info> infoList = response.getInfo();
    checkInfoList(infoList);
  }

  /**
   * 根据账号信息更新Email.
   * @param account 账号
   * @param tenancyId 租户id
   * @param email 新的邮箱
   * @throws Exception 异常
   */
  @TenancyIdentity(index = 1)
  public void updateEmail(String account, Long tenancyId, String email) throws Exception {
    UserParam userParam = new UserParam();
    userParam.setAccount(StringUtil.trimCompatibleNull(account));
    userParam.setTenancyId(tenancyId);
    userParam.setEmail(StringUtil.trimCompatibleNull(email));
    userParam.setUserActionEnum(UserActionEnum.UPDATE_EMAIL_BY_ACCOUNT);
    Response<UserDto> response = uarwFacade.getUserRWResource().updateUser(userParam);
    List<Info> infoList = response.getInfo();
    checkInfoList(infoList);
  }

  /**
   * 根据账号信息更新电话号码.
   *
   * @param account 账号
   * @param tenancyId 租户id
   * @param phone 新的电话号码
   * @throws Exception 异常
   */
  @TenancyIdentity(index = 1)
  public void updatePhone(String account, Long tenancyId, String phone) throws Exception {
    UserParam userParam = new UserParam();
    userParam.setAccount(StringUtil.trimCompatibleNull(account));
    userParam.setTenancyId(tenancyId);
    userParam.setPhone(StringUtil.trimCompatibleNull(phone));
    userParam.setUserActionEnum(UserActionEnum.UPDATE_PHONE_BY_ACCOUNT);
    Response<UserDto> response = uarwFacade.getUserRWResource().updateUser(userParam);
    List<Info> infoList = response.getInfo();
    checkInfoList(infoList);
  }

  /**
   * 根据账号更新密码.
   *
   * @param account 账号
   * @param tenancyId 租户id
   * @param newPassword 新密码
   * @param originPassword 原始密码
   * @throws Exception 异常
   */
  @TenancyIdentity(index = 1)
  public void updatePassword(String account, Long tenancyId, String newPassword,
      String originPassword) throws Exception {
    UserParam userParam = new UserParam();
    userParam.setAccount(StringUtil.trimCompatibleNull(account));
    userParam.setTenancyId(tenancyId);
    userParam.setPassword(newPassword);
    userParam.setOriginPassword(originPassword);
    userParam.setUserActionEnum(UserActionEnum.UPDATE_PASSWORD_BY_ACCOUNT);
    Response<UserDto> response = uarwFacade.getUserRWResource().updateUser(userParam);
    List<Info> infoList = response.getInfo();
    checkInfoList(infoList);
  }

  /**
   * 根据用户id和原始密码更新用户密码.
   */
  public void updateUserPassword(Long id, String newPassword, String originPassword)
      throws Exception {
    UserParam userParam = new UserParam();
    userParam.setId(id);
    userParam.setOriginPassword(originPassword);
    userParam.setPassword(newPassword);
    userParam.setUserActionEnum(UserActionEnum.RESET_PASSWORD_AND_CHECK);
    Response<UserDto> response = uarwFacade.getUserRWResource().updateUser(userParam);
    List<Info> infoList = response.getInfo();
    checkInfoList(infoList);
  }
}
