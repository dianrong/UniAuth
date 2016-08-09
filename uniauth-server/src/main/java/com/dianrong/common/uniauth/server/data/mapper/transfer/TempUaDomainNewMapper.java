package com.dianrong.common.uniauth.server.data.mapper.transfer;

import java.util.List;

import com.dianrong.common.uniauth.server.data.entity.transfer.TempUaDomainNew;

public interface TempUaDomainNewMapper {

	public List<TempUaDomainNew> select();
	
	public List<TempUaDomainNew> selectAll();

	public int updateSuccess(Integer id);

}
