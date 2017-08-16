package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.ThirdAccountType;
import com.dianrong.common.uniauth.common.bean.dto.AllDomainPermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.InnerStringUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.ldap.ipa.dao.UserDao;
import com.dianrong.common.uniauth.server.ldap.ipa.entity.User;
import com.dianrong.common.uniauth.server.ldap.ipa.support.IpaUtil;
import com.dianrong.common.uniauth.server.service.cache.TenancyCache;
import com.dianrong.common.uniauth.server.service.common.CommonService;
import com.dianrong.common.uniauth.server.service.multidata.UserAuthentication;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.OperationNotSupportedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 处理IPA相关信息.
 *
 * @author wanglin
 */
@Service
@Slf4j
public class IpaService implements UserAuthentication {

  @Autowired
  private UserDao userDao;

  @Autowired
  private TenancyCache tenancyCache;

  @Autowired
  private CommonService commonService;

  @Autowired
  private UserThirdAccountService serThirdAccountService;

  @Autowired
  private UserService userService;

  /**
   * IPA账号的登陆.
   */
  public UserDto login(LoginParam loginParam) {
    Long defaultTenancyId = tenancyIdentityCheck(loginParam.getTenancyCode(),
        loginParam.getTenancyId(), loginParam.getAccount());
    try {
      userDao.authenticate(loginParam.getAccount(), loginParam.getPassword());
    } catch (AuthenticationException ae) {
      log.warn("IPA login, account {0}, password not match", ae);
      throw new AppException(InfoName.LOGIN_ERROR, UniBundle.getMsg("user.login.error"));
    } catch (EmptyResultDataAccessException eda) {
      log.warn("IPA login, account {0} not exsits", eda);
      throw new AppException(InfoName.LOGIN_ERROR_USER_NOT_FOUND,
          UniBundle.getMsg("user.login.notfound", loginParam.getAccount()));
    } catch (OperationNotSupportedException ose) {
      log.warn("IPA login, account {0} too many times login failed", ose);
      throw new AppException(InfoName.LOGIN_ERROR_IPA_TOO_MANY_FAILED,
          UniBundle.getMsg("user.login.account.lock"));
    } catch (Exception e) {
      log.warn("IPA login, account {0} login failed", e);
      throw new AppException(InfoName.LOGIN_ERROR, UniBundle.getMsg("user.login.error"));
    }
    // load user basic information
    User user = userDao.getUserByAccount(loginParam.getAccount());
    if (StringUtils.hasText(user.getEmail())) {
      UserDto uniauthUserDto = getUniauthUserInfo(user, loginParam.getAccount());
      if (uniauthUserDto != null) {
        return uniauthUserDto;
      }
    }
    // 没有关联的Uniauth账号
    UserDto ipaDto = BeanConverter.convert(user);
    ipaDto.setTenancyId(StringUtil.translateLongToInteger(defaultTenancyId));

    // Third Account info
    ipaDto.setThirdAccountInfo(
        userService.thirdAccountInfo(ipaDto.getId(), loginParam.getIncludeThirdAccount()));
    return ipaDto;
  }

  /**
   * 获取IPA用户的详细信息.<br>
   * 1 如果能根据IPA账号的email找到对应的Uniauth账号,则返回Uniauth+IPA的所有信息(包括权限信息).<br>
   * 2 如果不能根据email找到对应的Uniauth账号,则创建一个Uniauth用户,并关联IPA账号和Uniauth账号
   */
  public UserDetailDto getUserDetailInfo(LoginParam loginParam) {
    CheckEmpty.checkEmpty(loginParam.getAccount(), "账号");
    Long defaultTenancyId = tenancyIdentityCheck(loginParam.getTenancyCode(),
        loginParam.getTenancyId(), loginParam.getAccount());
    User user = userDao.getUserByAccount(loginParam.getAccount());
    if (user == null) {
      log.warn("Account {0} not found detail infomartion", loginParam.getAccount());
      return null;
    }
    UserDto uniauthUserDto = null;
    if (StringUtils.hasText(user.getEmail())) {
      uniauthUserDto = getUniauthUserInfo(user, loginParam.getAccount());
    }
    UserDetailDto userDetailDto = null;

    // 如果没找到,构造空的UniauthUser
    if (uniauthUserDto == null) {
      UserDto dto = BeanConverter.convert(user);
      dto.setTenancyId(StringUtil.translateLongToInteger(defaultTenancyId));

      // Third Account info
      dto.setThirdAccountInfo(
          userService.thirdAccountInfo(dto.getId(), loginParam.getIncludeThirdAccount()));

      userDetailDto = new UserDetailDto();
      userDetailDto.setUserDto(dto);
    } else {
      userDetailDto = userService.getUserDetailInfo(
          new LoginParam().setAccount(user.getEmail()).setTenancyId(defaultTenancyId)
              .setIncludeThirdAccount(loginParam.getIncludeThirdAccount()),
          true);
    }
    userDetailDto.setAllDomainPermissionDto(constructIpaPermission(user.getGroups()));
    return userDetailDto;
  }


  public UserDto getUserByEmailOrPhone(LoginParam loginParam) {
    throw new AppException(InfoName.BAD_REQUEST,
        UniBundle.getMsg("user.info.load.ipa.account", loginParam.getAccount()));
  }

  /**
   * 根据Email从Uniauth的数据源中获取Uniauth用户信息. 1 如果不存在,则创建一个,并关联IPA账号和Uniauth账号 2 如果存在则直接返回对应的账号.
   */
  private UserDto getUniauthUserInfo(User ipaUser, String ipaAccount) {
    UserDto user = serThirdAccountService.queryUserByThirdAccount(ipaAccount, ThirdAccountType.IPA);
    // 创建新用户 并关联
    if (user == null) {
      user = serThirdAccountService.createNewUserAndRelateThirdAccount(ipaUser.getDisplayName(),
          ipaUser.getPhone(), ipaUser.getEmail(), ipaAccount, ThirdAccountType.IPA);
    }
    // check user lock status
    if (user.getFailCount() >= AppConstants.MAX_AUTH_FAIL_COUNT) {
      throw new AppException(InfoName.LOGIN_ERROR_EXCEED_MAX_FAIL_COUNT,
          UniBundle.getMsg("user.login.account.lock"));
    }
    // 用户如果被禁用,则报错
    if (user.getStatus() != AppConstants.STATUS_ENABLED) {
      throw new AppException(InfoName.LOGIN_ERROR_STATUS_1,
          UniBundle.getMsg("user.login.status.lock"));
    }

    return user;
  }

  /**
   * 所有IPA相关的操作都当做是DIANRONG租户来处理.
   *
   * @param tenancyCode 传入的租户编码
   * @param tenancyId 传入的租户id
   * @param account 账号信息
   * @return 默认的租户id
   */
  private Long tenancyIdentityCheck(String tenancyCode, Long tenancyId, String account) {
    TenancyDto defaultTenancy =
        tenancyCache.getEnableTenancyByCode(AppConstants.DEFAULT_TANANCY_CODE);
    if (!defaultTenancy.getCode().equals(tenancyCode)
        && !defaultTenancy.getId().equals(tenancyId)) {
      throw new AppException(InfoName.LOGIN_ERROR_USER_NOT_FOUND,
          UniBundle.getMsg("user.login.notfound", account));
    }
    return defaultTenancy.getId();
  }

  private AllDomainPermissionDto constructIpaPermission(List<String> ipaGroups) {
    Set<String> groupCodes = IpaUtil.translateMemberGroupToGroupName(ipaGroups);
    AllDomainPermissionDto ipaPermission = new AllDomainPermissionDto();
    if (groupCodes.isEmpty()) {
      return ipaPermission;
    }
    TenancyDto defaultTenancy =
        tenancyCache.getEnableTenancyByCode(AppConstants.DEFAULT_TANANCY_CODE);
    String tenancyCode = defaultTenancy.getCode();
    Integer tenancyId = StringUtil.translateLongToInteger(defaultTenancy.getId());
    Integer roleCodeId = commonService.getRoleCodeId(AppConstants.ROLE_CODE_ROLE_NORMAL);
    Integer permTypeId = commonService.getPermTypeId(AppConstants.PERM_TYPE_THIRD_ACCOUNT);

    // 统一设置成third_account类型权限
    String permType = AppConstants.PERM_TYPE_THIRD_ACCOUNT;
    List<RoleDto> roleList = Lists.newArrayList();
    for (String groupCode : groupCodes) {
      RoleDto roleDto = new RoleDto();
      Map<String, Set<String>> permMap = Maps.newHashMap();
      Map<String, Set<PermissionDto>> permDtoMap = Maps.newHashMap();
      roleDto.setDescription(groupCode).setName(groupCode)
          .setRoleCode(AppConstants.ROLE_CODE_ROLE_NORMAL).setRoleCodeId(roleCodeId)
          .setStatus(AppConstants.STATUS_ENABLED).setPermMap(permMap).setPermDtoMap(permDtoMap)
          .setTenancyCode(tenancyCode).setTenancyId(tenancyId);
      Set<String> permissionStr = Sets.newHashSet();
      permissionStr.add(groupCode);
      permMap.put(permType, permissionStr);
      Set<PermissionDto> permission = Sets.newHashSet();
      PermissionDto pdto = new PermissionDto();
      pdto.setDescription(groupCode).setPermType(permType).setPermTypeId(permTypeId)
          .setStatus(AppConstants.STATUS_ENABLED).setValue(groupCode).setTenancyCode(tenancyCode)
          .setTenancyId(tenancyId);
      permission.add(pdto);
      permDtoMap.put(permType, permission);
      roleList.add(roleDto);
    }
    ipaPermission.setRoleList(roleList);
    ipaPermission.setTenancyCode(tenancyCode);
    ipaPermission.setTenancyId(tenancyId);
    return ipaPermission;
  }

  @Override
  public int getOrder() {
    return -10;
  }

  @Override
  public boolean supported(LoginParam loginParam) {
    return InnerStringUtil.isIpaAccount(loginParam.getAccount());
  }
}
