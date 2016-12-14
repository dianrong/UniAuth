package com.dianrong.common.uniauth.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import com.dianrong.common.uniauth.common.cons.AppConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Arc on 20/6/2016.
 */
@Slf4j
public class HttpRequestUtil {
    public static Boolean isAjaxRequest(HttpServletRequest httpServletRequest) {
        if(httpServletRequest == null) {
            return Boolean.FALSE;
        }
        String ajaxValue = httpServletRequest.getHeader(AppConstants.AJAX_HEADER);
        if(AppConstants.JQUERY_XMLHttpRequest_HEADER.equalsIgnoreCase(ajaxValue)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static Boolean isCORSRequest(HttpServletRequest httpServletRequest) {
        if(httpServletRequest == null) {
            return Boolean.FALSE;
        }

        String originValue = httpServletRequest.getHeader(AppConstants.CROSS_RESOURCE_ORIGIN_HEADER);

        if(originValue == null) {
            return Boolean.FALSE;
        } else {
            String baseUrl = httpServletRequest.getScheme()+"://"+httpServletRequest.getServerName()+":"+httpServletRequest.getServerPort();
            String baseUrlStr = baseUrl.replaceAll("https|http", "");
            String ajaxCrossStr = originValue.replaceAll("https|http", "");
            if(baseUrlStr.startsWith(ajaxCrossStr)){
                return Boolean.FALSE;
            } else {
                return Boolean.TRUE;
            }
        }
    }
    
    private HttpRequestUtil(){
    	
    }
    
    public static String encodeUrl(String originalUrl){
    	if(originalUrl != null){
        	try {
    			return URLEncoder.encode(originalUrl, "utf-8");
    		} catch (UnsupportedEncodingException e) {
    			log.error("Url encode error for " + originalUrl, e);
    		}
    	}
    	return originalUrl;
    }
    
}
