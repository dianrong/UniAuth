package com.dianrong.common.uniauth.server.data.mapper.transfer;

import java.util.List;

import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaUriPatternNew;

public interface TempUaUriPatternNewMapper {
	
	public List<TempUaUriPatternNew> select();
	
	public List<TempUaUriPatternNew> selectAll();

	public int updateSuccess(Integer id);

}
