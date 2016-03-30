package com.dianrong.common.uniauth.common.client;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZooKeeperConfig {
	@Resource(name="uniauthConfig")
	private Map<String, String> allZkNodeMap;
	
	@Autowired
	private DomainDefine domainDefine;
	
	public String getParam(String nodeName){
		return allZkNodeMap.get(nodeName);
	}
	
	public String getCasServerUrl(){
		return getParam("cas_server");
	}
	
	public String getDomainUrl(){
		String domainCode = domainDefine.getDomainCode();
		return getParam("domains." + domainCode);
	}
}
