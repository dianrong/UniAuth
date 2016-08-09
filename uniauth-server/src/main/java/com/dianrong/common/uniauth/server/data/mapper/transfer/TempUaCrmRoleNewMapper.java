package com.dianrong.common.uniauth.server.data.mapper.transfer;

import java.util.List;

import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaCrmRoleNew;

public interface TempUaCrmRoleNewMapper {
	
	public List<TempUaCrmRoleNew> select();
	
	public List<TempUaCrmRoleNew> selectAll();
	
	public int updateSuccess(Integer id);

}
