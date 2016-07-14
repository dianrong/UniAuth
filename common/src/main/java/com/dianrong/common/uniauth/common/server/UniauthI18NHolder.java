package com.dianrong.common.uniauth.common.server;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dianrong.common.uniauth.common.server.UniauthResourceService;
import com.dianrong.common.uniauth.common.server.UniauthLocaleChangeInterceptor;

public class UniauthI18NHolder implements ApplicationContextAware,InitializingBean {
	
	private static UniauthResourceService techOpsResource;
	
	private ApplicationContext context;
	
	
	/**
	 * 获取国际化文案（此方法只适用于spring web环境中调用）,如果不在spring web环境中请自行取到Request，然后调用{@link #getProperties(HttpServletRequest, String)}
	 * @param key
	 * @return
	 */
	public static String getProperties(String key){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return getProperties(request,key);
	}
	
	
	/**
	 * 获取国际化文案
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getProperties(HttpServletRequest request,String key){
		if(request == null) return null;
		String lang = (String)request.getSession().getAttribute(UniauthLocaleChangeInterceptor.sessionName);
		Locale locale=null;
		if(StringUtils.isNotEmpty(lang)){
			locale=new Locale(lang);
		}else{
			locale=Locale.getDefault();
		}
		Map<String, String> properties = techOpsResource.getProperties(locale);
		return properties==null?null:properties.get(key);
	}
	
	public void setTechOpsResource(UniauthResourceService techOpsResource) {
		UniauthI18NHolder.techOpsResource = techOpsResource;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context=applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(techOpsResource == null){
			UniauthI18NHolder.techOpsResource = context.getBean(UniauthResourceService.class);
		}
	}

}
