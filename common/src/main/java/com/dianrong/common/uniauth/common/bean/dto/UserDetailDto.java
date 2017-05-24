package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;
import java.util.List;

import lombok.ToString;

@ToString
public class UserDetailDto implements Serializable {
    private static final long serialVersionUID = -7182531560501252283L;
    private UserDto userDto;
    // owner group list not considered
    private List<DomainDto> domainList;
    
    // IPA账号的权限设计为每个域共有的权限信息
    private IPAPermissionDto ipaPermissionDto;

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

    public UserDetailDto setDomainList(List<DomainDto> domainList) {
        this.domainList = domainList;
        return this;
    }

    public IPAPermissionDto getIpaPermissionDto() {
        return ipaPermissionDto;
    }

    public UserDetailDto setIpaPermissionDto(IPAPermissionDto ipaPermissionDto) {
        this.ipaPermissionDto = ipaPermissionDto;
        return this;
    }
}
