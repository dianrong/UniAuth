package com.dianrong.common.uniauth.common.bean.dto;

import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@ToString
public class UserDetailDto implements Serializable {
  private static final long serialVersionUID = -7182531560501252283L;
  private UserDto userDto;
  // owner group list not considered
  private List<DomainDto> domainList;

  // 每个域共有的权限信息
  private AllDomainPermissionDto allDomainPermissionDto;

  public UserDto getUserDto() {
    return userDto;
  }

  public UserDetailDto setUserDto(UserDto userDto) {
    this.userDto = userDto;
    return this;
  }

  public List<DomainDto> getDomainList() {
    return domainList;
  }

  public AllDomainPermissionDto getAllDomainPermissionDto() {
    return allDomainPermissionDto;
  }

  public UserDetailDto setDomainList(List<DomainDto> domainList) {
    this.domainList = domainList;
    return this;
  }

  public UserDetailDto setAllDomainPermissionDto(AllDomainPermissionDto allDomainPermissionDto) {
    this.allDomainPermissionDto = allDomainPermissionDto;
    return this;
  }
}
