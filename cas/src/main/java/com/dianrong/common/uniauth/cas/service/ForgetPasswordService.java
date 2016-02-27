package com.dianrong.common.uniauth.cas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;

@Service
public class ForgetPasswordService {

	@Autowired
	private UARWFacade uarwFacade;
	
	@Autowired
	private UniClientFacade uniClientFacade;
	
	public void testService(){
		for(int i = 0;i < 10000;i++){
			System.out.println("-----------------------------------");
		}
		System.out.println(uarwFacade.getRoleRWResource().getAllRoleCodes());
		UserParam userParam = new UserParam();
		userParam.setEmail("zengwei.xu@dianrong.com");
		System.out.println(uniClientFacade.getUserResource().getSingleUser(userParam));
	}
}
