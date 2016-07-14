package com.dianrong.common.techops.action;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dianrong.common.uniauth.common.bean.I18NResultModel;
import com.dianrong.common.uniauth.common.bean.LangDto;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.server.UniauthResourceService;
import com.dianrong.common.uniauth.common.server.UniauthLocaleChangeInterceptor;

@RestController
@RequestMapping("i18n")
public class I18NAction {
	
	@Autowired
	private UniauthResourceService techOpsResourceService;
	
	
	@RequestMapping(value = "query",produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<I18NResultModel> query(){
		List<LangDto> supportLangs = techOpsResourceService.getLanguageList();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Object lang = request.getSession().getAttribute(UniauthLocaleChangeInterceptor.sessionName);
		if(lang == null){
			lang = Locale.getDefault().getLanguage();
		}
		return Response.success(I18NResultModel.of(supportLangs).current(String.valueOf(lang)));
	}
	
	@RequestMapping(value = "changeLanguage",produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<?> changeLanguage(@RequestParam(name = "lang", required = false)String lang){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		request.getSession().setAttribute(UniauthLocaleChangeInterceptor.sessionName,org.springframework.util.StringUtils.parseLocaleString(lang));
		return Response.success();
	}
	
	
	@RequestMapping(value = "getCurrentLanguage",produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<String> getCurrentLanguage(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		Object lang = request.getSession().getAttribute(UniauthLocaleChangeInterceptor.sessionName);
		if(lang == null){
			lang = Locale.getDefault().getLanguage();
		}
		return Response.success(String.valueOf(lang));
	}

}
