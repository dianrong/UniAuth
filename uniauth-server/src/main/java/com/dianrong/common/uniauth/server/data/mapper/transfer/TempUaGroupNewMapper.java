package com.dianrong.common.uniauth.server.data.mapper.transfer;

import java.util.List;

import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaGroupNew;

public interface TempUaGroupNewMapper {
	
	
	public List<TempUaGroupNew> select();
	
	public List<TempUaGroupNew> selectAll();

	public int updateSuccess(Integer id);

}
