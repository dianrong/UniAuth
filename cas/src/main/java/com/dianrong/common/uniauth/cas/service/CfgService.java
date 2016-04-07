package com.dianrong.common.uniauth.cas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.CfgParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.util.StringUtil;

@Service("cfgService")
public class CfgService extends BaseService{
	/**.
	 * 远程调用对象
	 */
	@Autowired
	private UniClientFacade uniClientFacade;
	
	public List<ConfigDto> queryConfigDtoByCfgKeys(List<String> cfgkeys) throws Exception{
		if(cfgkeys == null || cfgkeys.isEmpty()){
			return new ArrayList<ConfigDto>();
		}
		// 配置查询入参
		CfgParam cfgParam = new CfgParam();
		cfgParam.setCfgKeys(cfgkeys).setNeedBLOBs(true);
		// 长度 * 2
		cfgParam.setPageNumber(0).setPageSize(cfgkeys.size() * 2);
		Response<PageDto<ConfigDto>> reponseInfo = uniClientFacade.getConfigResource().queryConfig(cfgParam);
		checkInfoList(reponseInfo.getInfo());
		PageDto<ConfigDto> pinfo = reponseInfo.getData();
		if(pinfo != null){
			return pinfo.getData();
		}
		return null;
	}
	
	public List<ConfigDto> queryConfigDtoByLikeCfgKeys(String likeCfgKey) throws Exception{
		if(StringUtil.strIsNullOrEmpty(likeCfgKey)){
			return new ArrayList<ConfigDto>();
		}
		// 配置查询入参
		CfgParam cfgParam = new CfgParam();
		cfgParam.setCfgKeyLike(likeCfgKey).setNeedBLOBs(true);
		cfgParam.setPageNumber(0).setPageSize(20);
		Response<PageDto<ConfigDto>> reponseInfo = uniClientFacade.getConfigResource().queryConfig(cfgParam);
		checkInfoList(reponseInfo.getInfo());
		PageDto<ConfigDto> pinfo = reponseInfo.getData();
		if(pinfo != null){
			return pinfo.getData();
		}
		return null;
	}
}
