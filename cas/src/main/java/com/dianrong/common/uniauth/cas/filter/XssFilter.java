package com.dianrong.common.uniauth.cas.filter;

import com.dianrong.common.uniauth.cas.filter.support.CasRequest;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.Assert;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class XssFilter extends OncePerRequestFilter {

  // 匹配不需要过滤路径的正则表达式
  private final Set<CasRequestPatternCache> requestPattern;

  /**
   * 放过的参数名集合.
   */
  private final Set<String> excludeParameters = Sets.newHashSet();

  /**
   * 构造函数.
   */
  public XssFilter(CasRequest... casRequest) {
    Set<CasRequestPatternCache> patternSet = Sets.newHashSet();
    if (casRequest != null && casRequest.length > 0) {
      for (int i = 0; i < casRequest.length; i++) {
        patternSet
            .add(new CasRequestPatternCache(casRequest[i].getUrl(), casRequest[i].getMethod()));
      }
    }
    this.requestPattern = Collections.unmodifiableSet(patternSet);
  }

  /**
   * Xss过滤.
   */
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (checkIfPassWithoutProcess(request)) {
      filterChain.doFilter(request, response);
    } else {
      EscapeScriptWrapper escapeScriptwrapper = new EscapeScriptWrapper(request);
      filterChain.doFilter(escapeScriptwrapper, response);
    }
  }

  /**
   * 检查是否可以放过不处理.
   * 
   * @return 是否可以放过不处理
   */
  private boolean checkIfPassWithoutProcess(HttpServletRequest request) {
    String requestMethod = request.getMethod();
    for (CasRequestPatternCache pattern : this.requestPattern) {
      String patternMethod = pattern.getMethod();
      // check method
      if (patternMethod.equalsIgnoreCase(AppConstants.HTTP_METHOD_ALL)
          || patternMethod.equalsIgnoreCase(requestMethod)) {
        // check request url
        String requestUri = request.getRequestURI();
        if (StringUtils.isNotBlank(requestUri)) {
          requestUri = requestUri.replace(request.getContextPath(), "");
        }
        if (pattern.getRequestUrlPattern().matcher(requestUri).matches()) {
          return true;
        }
      }
    }
    return false;
  }

  public void setExcludeParameters(String parameters) {
    if (StringUtils.isBlank(parameters)) {
      return;
    }
    String[] excludeParameterArray = parameters.split(",");
    for (String excludeParameter : excludeParameterArray) {
      if (!StringUtils.isBlank(excludeParameter)) {
        excludeParameters.add(excludeParameter);
      }
    }
  }

  /**
   * 辅助类, 用于缓存正则对象.
   */
  private static class CasRequestPatternCache {

    private final Pattern requestUrlPattern;
    private final String method;

    CasRequestPatternCache(String requestUrl, String method) {
      Assert.notNull(requestUrl);
      Assert.notNull(method);
      this.requestUrlPattern = Pattern.compile(requestUrl);
      this.method = method;
    }

    public Pattern getRequestUrlPattern() {
      return requestUrlPattern;
    }

    public String getMethod() {
      return method;
    }
  }

  /**
   * 继承HttpServletRequestWrapper,创建装饰类,以达到修改HttpServletRequest参数的目的.
   */
  private class EscapeScriptWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> parameterMap; // 所有参数的Map集合
    private Pattern tmpPattern = Pattern.compile("[sS][cC][rR][iI][pP][tT]");

    public EscapeScriptWrapper(HttpServletRequest request) {
      super(request);
      parameterMap = request.getParameterMap();
    }

    // 重写几个HttpServletRequestWrapper中的方法

    /**
     * 获取所有参数名.
     *
     * @return 返回所有参数名
     */
    @Override
    public Enumeration<String> getParameterNames() {
      Vector<String> vector = new Vector<String>(parameterMap.size());
      for (Entry<String, String[]> entry : this.parameterMap.entrySet()) {
        vector.addElement(entry.getKey());
      }
      return vector.elements();
    }

    /**
     * 获取指定参数名的值，如果有重复的参数名，则返回第一个的值.<br>
     * 接收一般变量 ，如text类型
     *
     * @param name 指定参数名
     * @return 指定参数名的值
     */
    @Override
    public String getParameter(String name) {
      String[] results = parameterMap.get(name);
      if (results == null || results.length == 0) {
        return null;
      } else {
        if (excludeParameters.contains(name)) {
          return results[0];
        }
        return escapeXss(results[0]);
      }
    }

    /**
     * 获取指定参数名的所有值的数组，如：checkbox的所有数据. 接收数组变量.
     */
    @Override
    public String[] getParameterValues(String name) {
      String[] results = parameterMap.get(name);
      if (results == null || results.length <= 0) {
        return results;
      } else {
        if (!excludeParameters.contains(name)) {
          for (int i = 0; i < results.length; i++) {
            results[i] = escapeXss(results[i]);
          }
        }
        return results;
      }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
      Map<String, String[]> parameterMap = Maps.newHashMap();
      for (Entry<String, String[]> entry : this.parameterMap.entrySet()) {
        String key = entry.getKey();
        parameterMap.put(key, getParameterValues(key));
      }
      return parameterMap;
    }

    /**
     * 过滤Xss攻击字符串.
     */
    private String escapeXss(String str) {
      str = StringEscapeUtils.escapeXml11(str);
      Matcher tmpMatcher = tmpPattern.matcher(str);
      if (tmpMatcher.find()) {
        str = tmpMatcher.replaceAll(tmpMatcher.group(0) + "\\\\");
      }
      return str;
    }
  }
}
