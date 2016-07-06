package com.dianrong.common.techops.action;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dianrong.common.techops.bean.I18NResultModel;
import com.dianrong.common.techops.bean.LangDto;
import com.dianrong.common.techops.service.TechOpsResourceService;
import com.dianrong.common.uniauth.common.bean.Response;

@RestController
@RequestMapping("i18n")
public class I18NAction {
	
	@Autowired
	private TechOpsResourceService techOpsResourceService;
	
	
	@RequestMapping(value = "query",produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<I18NResultModel> query(){
		List<LangDto> supportLangs = techOpsResourceService.getLanguageList();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Object lang = request.getSession().getAttribute("techops_i18n_lang");
		if(lang == null){
			lang = Locale.getDefault().getLanguage();
		}
		return Response.success(I18NResultModel.of(supportLangs).current(String.valueOf(lang)));
	}
	
	@RequestMapping(value = "changeLanguage",produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<?> changeLanguage(@RequestParam(name = "lang", required = false)String lang){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		request.getSession().setAttribute("techops_i18n_lang", lang);
		return Response.success();
	}
	
	
	@RequestMapping(value = "getCurrentLanguage",produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<String> getCurrentLanguage(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Object lang = request.getSession().getAttribute("techops_i18n_lang");
		if(lang == null){
			lang = Locale.getDefault().getLanguage();
		}
		return Response.success(String.valueOf(lang));
	}

}
