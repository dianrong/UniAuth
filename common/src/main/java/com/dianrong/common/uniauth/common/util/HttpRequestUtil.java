package com.dianrong.common.uniauth.common.util;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Arc on 20/6/2016.
 */
@Slf4j
public class HttpRequestUtil {

  /**
   * 判断请求是否是Ajax请求.
   */
  public static Boolean isAjaxRequest(HttpServletRequest httpServletRequest) {
    if (httpServletRequest == null) {
      return Boolean.FALSE;
    }
    String ajaxValue = httpServletRequest.getHeader(AppConstants.AJAX_HEADER);
    if (AppConstants.JQUERY_XMLHttpRequest_HEADER.equalsIgnoreCase(ajaxValue)) {
      return Boolean.TRUE;
    } else {
      return Boolean.FALSE;
    }
  }

  /**
   * 判断请求是否跨域.
   */
  public static Boolean isCorsRequest(HttpServletRequest httpServletRequest) {
    if (httpServletRequest == null) {
      return Boolean.FALSE;
    }

    String originValue = httpServletRequest.getHeader(AppConstants.CROSS_RESOURCE_ORIGIN_HEADER);

    if (originValue == null) {
      return Boolean.FALSE;
    } else {
      String baseUrl =
          httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":"
              + httpServletRequest.getServerPort();
      String baseUrlStr = baseUrl.replaceAll("https|http", "");
      String ajaxCrossStr = originValue.replaceAll("https|http", "");
      if (baseUrlStr.startsWith(ajaxCrossStr)) {
        return Boolean.FALSE;
      } else {
        return Boolean.TRUE;
      }
    }
  }

  private HttpRequestUtil() {
  }

  /**
   * URL 编码 指定的url.
   */
  public static String encodeUrl(String originalUrl) {
    if (originalUrl != null) {
      try {
        return URLEncoder.encode(originalUrl, "utf-8");
      } catch (UnsupportedEncodingException e) {
        log.error("Url encode error for " + originalUrl, e);
      }
    }
    return originalUrl;
  }

  /**
   * 获取请求端的IP地址.
   */
  public static String ipAddress(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  public static String extractRequestUrl(HttpServletRequest request) {
    return extractRequestUrl(request, true);
  }

  /**
   * 获取请求的URL.
   */
  public static String extractRequestUrl(HttpServletRequest request, boolean includingQuery) {
    String url = request.getServletPath();
    String pathInfo = request.getPathInfo();
    String query = request.getQueryString();
    if (pathInfo != null || (query != null && includingQuery)) {
      StringBuilder sb = new StringBuilder(url);
      if (pathInfo != null) {
        sb.append(pathInfo);
      }
      if (query != null && includingQuery) {
        sb.append('?').append(query);
      }
      url = sb.toString();
    }
    return url;
  }

}
