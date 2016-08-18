package com.dianrong.common.uniauth.server.data.mapper.transfer;

import java.util.List;
import java.util.Map;

import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaUserRoleCrmNew;

public interface TempUaUserRoleUriPatternNewMapper2 {
	
	public List<TempUaUserRoleCrmNew> select();

	public int updateSuccess(Map<String,Object> map);

}
