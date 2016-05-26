package com.dianrong.common.auth.server.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.auth.server.data.entity.SlActorVirtualLoan;
import com.dianrong.common.auth.server.data.entity.SlActorVirtualLoanExample;
import com.dianrong.common.auth.server.data.mapperAdapter.SlActorVirtualLoanMapperAdapter;

@Service
public class SlActorVirtualLoanService {
	
	/**.
	 * 用户信息操作mapper
	 */
	@Autowired
	private SlActorVirtualLoanMapperAdapter slActorVirtualLoanMapper;
	
	  public boolean isXiaoming(Long id) {
		  if(id == null) {
			  return false;
		  }
		  SlActorVirtualLoanExample conditions = new SlActorVirtualLoanExample();
		  conditions.createCriteria().andAidEqualTo(new BigDecimal(id));
		  List<SlActorVirtualLoan> infoes = slActorVirtualLoanMapper.selectByExample(conditions);
		  if(infoes != null && infoes.size() > 0 ) {
			  return true;
		  }
		  return false;
	  }
}