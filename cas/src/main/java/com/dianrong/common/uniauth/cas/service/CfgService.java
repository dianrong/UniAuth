package com.dianrong.common.uniauth.cas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.cas.exp.ResetPasswordException;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.CfgParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;

@Service("cfgService")
public class CfgService extends BaseService{
	/**.
	 * 远程调用对象
	 */
	@Autowired
	private UniClientFacade uniClientFacade;
	
	public List<ConfigDto> queryConfigDtoByCfgKeys(List<String> cfgkeys) throws ResetPasswordException{
		if(cfgkeys == null || cfgkeys.isEmpty()){
			return new ArrayList<ConfigDto>();
		}
		// 配置查询入参
		CfgParam cfgParam = new CfgParam();
		// 长度乘一个2 保证一下
		cfgParam.setCfgKeys(cfgkeys).setPageNumber(0).setPageSize(cfgkeys.size() * 2);
		Response<PageDto<ConfigDto>> reponseInfo = uniClientFacade.getConfigResource().queryConfig(cfgParam);
		checkInfoList(reponseInfo.getInfo());
		return reponseInfo.getData().getData();
	}
}
