package com.dianrong.common.uniauth.common.util;

import com.dianrong.common.uniauth.common.cons.AppConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Arc on 20/6/2016.
 */
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
}
