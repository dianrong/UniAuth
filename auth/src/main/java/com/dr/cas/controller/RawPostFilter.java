package com.dr.cas.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * treat raw post as x-www-urlencoded content type
 * XDomainRequest doesn't sends content-type with POST, as a workaround, server must parse urlencoded content type for it
 * @author zhaow
 *
 */
public class RawPostFilter implements Filter {

  static private Logger logger = LoggerFactory.getLogger(RawPostFilter.class);

  @Override
  public void init(FilterConfig arg0) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp,
                       FilterChain chain) throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) resp;
    HttpServletRequest request = (HttpServletRequest) req;

    String contentType = request.getContentType();
    if ("post".equalsIgnoreCase(request.getMethod())) {
      logger.info("parsing raw post " + contentType + " class = " + request.getClass().getName());
      if (isRaw(request)) {
        request = new RawPostRequestWrapper(request);
      }
    }
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() { }

  private boolean isRaw(HttpServletRequest request) {
    String contentType = request.getContentType();
    return StringUtils.isBlank(contentType) || "text/plain".equals(contentType);
  }


  public static class RawPostRequestWrapper extends HttpServletRequestWrapper {

    private MultiValueMap<String, String> params;

    public RawPostRequestWrapper(HttpServletRequest request) {
      super(request);
      this.parsePostData(request);
    }

    @Override
    public Enumeration<String> getParameterNames() {
      Set<String> names = new LinkedHashSet<String>();
      names.addAll(Collections.list(super.getParameterNames()));
      names.addAll(params.keySet());
      return Collections.enumeration(names);
    }

    @Override
    public String getParameter(String name) {
      String value = super.getParameter(name);
      if (value == null) {
        value = params.getFirst(name);
      }
      return value;
    }

    @Override
    public String[] getParameterValues(String name) {

      String[] values = super.getParameterValues(name);
      List<String> formValues = this.params.get(name);
      if (formValues == null) {
          return values;
      }
      if (values == null) {
          return formValues.toArray(new String[formValues.size()]);
      }
      // if both name is on both query params and form post
      List<String> result = new ArrayList<String>();
      result.addAll(Arrays.asList(values));
      result.addAll(formValues);
      return result.toArray(new String[result.size()]);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
      Map<String, String[]> result = new LinkedHashMap<String, String[]>();
      Enumeration<String> names = this.getParameterNames();
      while (names.hasMoreElements()) {
        String name = names.nextElement();
        result.put(name, this.getParameterValues(name));
      }
      return result;
    }

    /**
     * handle GET params and POST data
     *
     * @param request
     * @return
     */
    private void parsePostData(HttpServletRequest request) {

      params = new LinkedMultiValueMap<String, String>();
      // check raw post content type, because IE XDomainRequest insists on sending raw POST
      // parse RAW POST
      StringBuilder body = new StringBuilder();
      try {
        BufferedReader br = request.getReader();
        for (String line = ""; (line = br.readLine()) != null; ) {
          body.append(line);
        }
        for (String pair : body.toString().split("&")) {
          int idxOfEqual = pair.indexOf("=");
          if (idxOfEqual < 0) {
            params.add(pair, "");
            continue;
          }
          String key = pair.substring(0, idxOfEqual);
          String value = pair.substring(idxOfEqual + 1);
          params.add(key, URLDecoder.decode(value, "UTF-8"));
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}

