package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.interfaces.ICommonResource;

/**
 * Created by Arc on 15/1/16.
 */
@RestController
public class CommonResource implements ICommonResource {

	@Override
	public Response<List<RoleCodeDto>> getAllRoleCodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<UserDetailDto> getUserDetailInfo(LoginParam loginParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> login(LoginParam loginParam) {
		// TODO Auto-generated method stub
		return null;
	}
}
