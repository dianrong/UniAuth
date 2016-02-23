package com.dianrong.loanbusiness.subsystem.controller;

import java.lang.reflect.Method;
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
import com.dianrong.loanbusiness.subsystem.service.MyService;

@Controller
@RequestMapping("/main")
public class MainController {
	protected static Logger logger = Logger.getLogger("controller");
	
	@Autowired
	private MyService myService;
	
	@Autowired
	private UniClientFacade uniClientFacade;

	@PreAuthorize("hasRole('ROLE_USER')")   
	@RequestMapping(value = "/common", method = RequestMethod.GET)
	public String getCommonPage() {
		logger.debug("Received request to show common page");
		//myService.testService();
		return "commonpage";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")   
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String getAadminPage() {
		DomainParam domainParam = new DomainParam();
		domainParam.setCode("techops");
		Response<List<UrlRoleMappingDto>> response = uniClientFacade.getPermissionResource().getUrlRoleMapping(domainParam);
		
		logger.debug("Received request to show admin page");
		return "adminpage";

	}

}
