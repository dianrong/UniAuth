package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;
import java.util.List;

public class UserDetailDto implements Serializable {
	private UserDto userDto;
	//owner group list not considered
	private List<DomainDto> domainList;

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
}
