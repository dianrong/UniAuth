package com.dianrong.common.uniauth.server.data.mapper.transfer;

import java.util.List;
import java.util.Map;

import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaUserNew;

public interface TempUaUserNewMapper {
	
	public List<TempUaUserNew> select();
	
	public List<TempUaUserNew> selectAll();
	
	public List<TempUaUserNew> selectByRange(Map<String,Object> m);
	

	public int updateSuccess(Long id);

}
