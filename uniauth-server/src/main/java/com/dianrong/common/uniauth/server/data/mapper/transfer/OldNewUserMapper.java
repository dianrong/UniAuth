package com.dianrong.common.uniauth.server.data.mapper.transfer;

import java.util.List;

import com.dianrong.common.uniauth.server.data.entity.transfer.OldNewUser;

public interface OldNewUserMapper {
	
	public void insertSelective(OldNewUser oldNewUser);
	
	public List<OldNewUser> selectAll();

}
