package com.dianrong.common.uniauth.cas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.cas.exp.ResetPasswordException;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;

@Service
public class ForgetPasswordService extends BaseService{

	@Autowired
	private UARWFacade uarwFacade;
	
	@Autowired
	private UniClientFacade uniClientFacade;
	
	public void checkUser(String email) throws ResetPasswordException {
		UserParam userParam = new UserParam();
		userParam.setEmail(email);
		Response<UserDto> response = uniClientFacade.getUserResource().getSingleUser(userParam);
		List<Info> infoList = response.getInfo();

		checkInfoList(infoList);
	}
	
	public void resetPassword(String email, String password) throws ResetPasswordException {
		UserParam userParam = new UserParam();
		userParam.setEmail(email);
		userParam.setPassword(password);
		Response<Void> response = uarwFacade.getUserRWResource().resetPassword(userParam);
		List<Info> infoList = response.getInfo();

		checkInfoList(infoList);
	}
}
