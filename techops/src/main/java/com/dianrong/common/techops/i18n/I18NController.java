package com.dianrong.common.techops.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.dianrong.common.techops.service.TechOpsResourceService;

public class I18NController extends AbstractController{
	
	@Autowired
	private TechOpsResourceService techOpsResourceService;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String lang = request.getParameter("lang");
		Locale locale = request.getLocale();
		
		if(StringUtils.isNotBlank(lang)){
			locale = new Locale(lang);
		}
		return new ModelAndView(new MappingJackson2JsonView(),techOpsResourceService.getProperties(locale));
	}
	

}
