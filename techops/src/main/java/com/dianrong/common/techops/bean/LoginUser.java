package com.dianrong.common.techops.bean;

import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Arc on 2/3/2016.
 */
public class LoginUser {

  private UserDto user;
  private List<RoleDto> roles;
  private Map<String, Set<String>> permMap;
  private List<DomainDto> switchableDomains;

  // 用户更新数据的跳转url
  private transient String userInfoUpdateUrl;

  public UserDto getUser() {
    return user;
  }

  public LoginUser setUser(UserDto user) {
    this.user = user;
    return this;
  }

  public List<RoleDto> getRoles() {
    return roles;
  }

  public LoginUser setRoles(List<RoleDto> roles) {
    this.roles = roles;
    return this;
  }

  public Map<String, Set<String>> getPermMap() {
    return permMap;
  }

  public LoginUser setPermMap(Map<String, Set<String>> permMap) {
    this.permMap = permMap;
    return this;
  }

  public List<DomainDto> getSwitchableDomains() {
    return switchableDomains;
  }

  public LoginUser setSwitchableDomains(List<DomainDto> switchableDomains) {
    this.switchableDomains = switchableDomains;
    return this;
  }

  public String getUserInfoUpdateUrl() {
    return userInfoUpdateUrl;
  }

  public void setUserInfoUpdateUrl(String userInfoUpdateUrl) {
    this.userInfoUpdateUrl = userInfoUpdateUrl;
  }
}
