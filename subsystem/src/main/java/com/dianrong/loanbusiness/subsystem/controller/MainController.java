package com.dianrong.loanbusiness.subsystem.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.client.ZooKeeperConfig;
import com.dianrong.loanbusiness.subsystem.model.TestModel;
import com.dianrong.loanbusiness.subsystem.service.MyService;

@Controller
@RequestMapping("/main")
public class MainController {
	protected static Logger logger = Logger.getLogger("controller");
	
	@Autowired
	private MyService myService;
	
	@Autowired
	private UniClientFacade uniClientFacade;
	
	@Autowired
	private ZooKeeperConfig zooKeeperConfig;


	@RequestMapping(value = "/common", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")   
	public String getCommonPage() {
		logger.debug("Received request to show common page");
		//myService.testService();
		return "commonpage";
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")   
	public String getAadminPage() {
		for(int i = 0;i < 100;i++){
			System.out.println("----------------------------------------------");
		}
		
		System.out.println(zooKeeperConfig.getDomainUrl());
		System.out.println(zooKeeperConfig.getCasServerUrl());
		
		TestModel tm = new TestModel();
		myService.testModel(tm);
		logger.debug("Received request to show admin page");
		return "adminpage";

	}


	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		for(int i = 0;i < 100;i++){
			System.out.println("----------------------------------------------");
		}
		
		System.out.println(zooKeeperConfig.getDomainUrl());
		System.out.println(zooKeeperConfig.getCasServerUrl());
		
		return "adminpage";
	}
}
