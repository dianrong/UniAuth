package com.dianrong.common.uniauth.cas.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ZkNodeUtils;

@Service("domainService")
public class DomainService extends BaseService{
	
	@Resource(name="uniauthConfig")
	private Map<String, String> allZkNodeMap;
	
	@Autowired
	private UniClientFacade uniClientFacade;
	
	@Autowired
	private TenancyService tenancyService;
	
	public List<DomainDto> getAllLoginPageDomains(String tenancyCode) {
		DomainParam domainParam = new DomainParam();
		List<String> domainCodeList = new ArrayList<String>();
		Iterator<Entry<String, String>> iterator = allZkNodeMap.entrySet().iterator();
		boolean isDefaultTenancy = tenancyService.isDefaultTenancy(tenancyCode);
		while(iterator.hasNext()){
			Entry<String,String> entry = iterator.next();
			String zkNodeName = entry.getKey();
			if(ZkNodeUtils.isDomainNode(zkNodeName, tenancyCode, isDefaultTenancy)){
				final String domainName  = ZkNodeUtils.getDomainName(zkNodeName);
				// 过滤有自定义页面的域
				String customLoginUrl = ZkNodeUtils.getDomainTCustomLoginUrl(domainName, tenancyCode, isDefaultTenancy, allZkNodeMap);
				boolean showInHomePage = ZkNodeUtils.canShowInHomePage(domainName, tenancyCode, isDefaultTenancy, allZkNodeMap);
				if(!showInHomePage && StringUtils.hasText(customLoginUrl)) {
					continue;
				}
				domainCodeList.add(domainName);
			}
		}
		domainParam.setDomainCodeList(domainCodeList).setTenancyCode(tenancyCode);
		Response<List<DomainDto>> response = uniClientFacade.getDomainResource().getAllLoginDomains(domainParam);
		List<DomainDto> domainDtoList = response.getData();
		if(domainDtoList != null && !domainDtoList.isEmpty()){
			for(DomainDto domainDto :domainDtoList){
				String domainCode = domainDto.getCode();
				String zkDomainUrl = ZkNodeUtils.getDomainUrl(domainCode, tenancyCode, isDefaultTenancy, allZkNodeMap);
				zkDomainUrl += AppConstants.SERVICE_LOGIN_POSTFIX;
				domainDto.setZkDomainUrl(zkDomainUrl);
				String zkDomainUrlEncoded = null;
				try {
					zkDomainUrlEncoded = URLEncoder.encode(zkDomainUrl, "utf-8");
				} catch (UnsupportedEncodingException e) {
				}
				domainDto.setZkDomainUrlEncoded(zkDomainUrlEncoded);
			}
		}
		
		return domainDtoList;
	}
}
