package com.dianrong.common.auth.server.data.mapperAdapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianrong.common.auth.server.data.entity.SlActorVirtualLoan;
import com.dianrong.common.auth.server.data.entity.SlActorVirtualLoanExample;
import com.dianrong.common.auth.server.data.mapper.SlActorVirtualLoanMapper;

public class SlActorVirtualLoanMapperAdapter extends BaseMapperAdapter{
	
	/**.
	 * 用户信息操作mapper
	 */
	@Autowired
	private SlActorVirtualLoanMapper slActorVirtualLoanMapper;
	
    public List<SlActorVirtualLoan> selectByExample(SlActorVirtualLoanExample example) {
    	return slActorVirtualLoanMapper.selectByExample(example);
    }
}