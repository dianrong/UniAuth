package com.dianrong.loanbusiness.subsystem.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UrlRoleMappingDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.loanbusiness.subsystem.model.TestModel;
import com.dianrong.loanbusiness.subsystem.service.MyService;

@Controller
@RequestMapping("/main2")
public class MainController2 {
	protected static Logger logger = Logger.getLogger("controller");
	
	@Autowired
	private MyService myService;
	
	@Autowired
	private UniClientFacade uniClientFacade;
	
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
/*		DomainParam domainParam = new DomainParam();
		domainParam.setCode("techops");
		Response<List<UrlRoleMappingDto>> response = uniClientFacade.getPermissionResource().getUrlRoleMapping(domainParam);
		
		TestModel tm = new TestModel();
		myService.testModel(tm);*/
		logger.debug("Received request to show admin page");
		return "adminpage";

	}

}
