package com.dianrong.common.uniauth.cas.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.cons.AppConstants;

@Service
public class DomainService {
	
	@Resource(name = "uniauthConfig")
	private Map<String, String> allZkNodeMap;
	
	@Autowired
	private UniClientFacade uniUniClientFacade;
	
	public List<DomainDto> getAllLoginPageDomains() {
		DomainParam domainParam = new DomainParam();
		List<String> domainCodeList = new ArrayList<String>();
		Iterator<Entry<String, String>> iterator = allZkNodeMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String,String> entry = iterator.next();
			String zkNodeName = entry.getKey();
			//String zkNodeValue = entry.getValue();
			
			if(zkNodeName.startsWith(AppConstants.ZK_DOMAIN_PREFIX)){
				zkNodeName = zkNodeName.substring(AppConstants.ZK_DOMAIN_PREFIX.length());
				domainCodeList.add(zkNodeName);
			}
		}
		System.out.println(domainCodeList);
		domainParam.setDomainCodeList(domainCodeList);
		Response<List<DomainDto>> response = uniUniClientFacade.getDomainResource().getAllLoginDomains(domainParam);
		List<DomainDto> domainDtoList = response.getData();
		if(domainDtoList != null && !domainDtoList.isEmpty()){
			for(DomainDto domainDto :domainDtoList){
				String domainCode = domainDto.getCode();
				String zkDomainUrl = allZkNodeMap.get(AppConstants.ZK_DOMAIN_PREFIX + domainCode);
				zkDomainUrl += AppConstants.SERVICE_LOGIN_POSTFIX;
				domainDto.setZkDomainUrl(zkDomainUrl);
			}
		}
		
		return domainDtoList;
	}
}
