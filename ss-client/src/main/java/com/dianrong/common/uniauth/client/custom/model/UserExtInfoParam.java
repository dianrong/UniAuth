package com.dianrong.common.uniauth.client.custom.model;

import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

/**
 * 用于进行UserExtInfo构造的传参model.
 *
 * @author wanglin
 */
@ToString
public class UserExtInfoParam implements Serializable {

  private static final long serialVersionUID = 3389698235561645882L;

  private String username;
  private String password;
  private boolean enabled;
  private boolean accountNonExpired;
  private boolean credentialsNonExpired;
  private boolean accountNonLocked;
  private Collection<GrantedAuthority> authorities;
  private Long id;
  private UserDto userDto;
  private DomainDto domainDto;
  private Map<String, Set<String>> permMap;
  private Map<String, Set<PermissionDto>> permDtoMap;

  public String getUsername() {
    return username;
  }

  public UserExtInfoParam setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public UserExtInfoParam setPassword(String password) {
    this.password = password;
    return this;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public UserExtInfoParam setEnabled(boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  public UserExtInfoParam setAccountNonExpired(boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
    return this;
  }

  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  public UserExtInfoParam setCredentialsNonExpired(boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
    return this;
  }

  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  public UserExtInfoParam setAccountNonLocked(boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
    return this;
  }

  public Collection<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public UserExtInfoParam setAuthorities(Collection<GrantedAuthority> authorities) {
    this.authorities = authorities;
    return this;
  }

  public Long getId() {
    return id;
  }

  public UserExtInfoParam setId(Long id) {
    this.id = id;
    return this;
  }

  public UserDto getUserDto() {
    return userDto;
  }

  public UserExtInfoParam setUserDto(UserDto userDto) {
    this.userDto = userDto;
    return this;
  }

  public Map<String, Set<String>> getPermMap() {
    return permMap;
  }

  public UserExtInfoParam setPermMap(Map<String, Set<String>> permMap) {
    this.permMap = permMap;
    return this;
  }

  public Map<String, Set<PermissionDto>> getPermDtoMap() {
    return permDtoMap;
  }

  public UserExtInfoParam setPermDtoMap(Map<String, Set<PermissionDto>> permDtoMap) {
    this.permDtoMap = permDtoMap;
    return this;
  }

  public DomainDto getDomainDto() {
    return domainDto;
  }

  public UserExtInfoParam setDomainDto(DomainDto domainDto) {
    this.domainDto = domainDto;
    return this;
  }
}
