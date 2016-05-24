package com.dr.cas.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CORSFilter implements Filter {
  
  Logger logger = LoggerFactory.getLogger(CORSFilter.class);
  @Override
  public void init(FilterConfig arg0) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp,
                       FilterChain chain) throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) resp;
    HttpServletRequest request = (HttpServletRequest) req;

    String origin = request.getHeader("Origin");

    if ("OPTIONS".equals(request.getMethod())) {
      response.setHeader("Access-Control-Allow-Methods", "GET, POST");
      response.setHeader("Access-Control-Allow-Credentials", "true");
      response.setHeader("Access-Control-Max-Age", "3600");
      response.setStatus(HttpServletResponse.SC_OK);
      return;
    }

    
    logger.trace("doFilter origin: " + origin);
    try {
      if (StringUtils.contains(origin, ".dianrong.com") || StringUtils.equals(origin, "null")) {
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Vary", "Origin");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    chain.doFilter(req, resp);
  }

  @Override
  public void destroy() {
  }
  
}

