package com.dianrong.common.uniauth.common.bean.dto;

import java.util.List;

public class UserDetailDto {
	private UserDto userDto;
	//owner group list not considered
	private List<DomainDto> domainList;
	
	public List<DomainDto> getDomainList() {
		return domainList;
	}
	public void setDomainList(List<DomainDto> domainList) {
		this.domainList = domainList;
	}
	
	public UserDto getUserDto() {
		return userDto;
	}
	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}
}
