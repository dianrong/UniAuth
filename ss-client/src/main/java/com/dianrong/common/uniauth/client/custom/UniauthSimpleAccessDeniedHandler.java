package com.dianrong.common.uniauth.client.custom;

import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;
import com.dianrong.common.uniauth.common.util.JsonUtil;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Uniauth自定义的默认AccessDeniedHandler实现.
 */
@Slf4j
public class UniauthSimpleAccessDeniedHandler implements AccessDeniedHandler {

  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    if (!response.isCommitted()) {
      if (HttpRequestUtil.isAjaxRequest(request) || HttpRequestUtil.isCorsRequest(request)) {
        log.debug("Ajax request no privilege.");
        response.setContentType("application/json");
        response.addHeader("Cache-Control", "no-store");
        Info info = Info.build(InfoName.NO_PRIVILEGE,
            "You do not have permission to access this resource!");
        response.getWriter().write(JsonUtil.object2Jason(Arrays.asList(info)));
        response.flushBuffer();
      } else {
        log.debug("Request no privilege.");
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write("You do not have permission to access this resource!");
        response.getWriter().write("<a href='/logout/cas'>Logout</a>");
        response.flushBuffer();
      }
    }
  }
}
