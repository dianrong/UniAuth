package com.dianrong.common.techops.action;

import com.dianrong.common.techops.exp.InvalidParameterException;
import com.dianrong.common.techops.exp.NoAuthorityException;
import com.dianrong.common.techops.exp.NotFoundApiException;
import com.dianrong.common.techops.service.ProxyInvokeService;
import com.dianrong.common.techops.util.UniBundle;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 代理访问UniauthServer后端的api.
 */
@RestController
@RequestMapping("proxy")
public class UniauthServerAction {

  @Autowired
  private ProxyInvokeService proxyInvokeService;

  /**
   * 代理方法.
   */
  @RequestMapping(value = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') or (hasRole('ROLE_ADMIN') "
      + "and hasPermission(null,'SYSTEM_ACCOUNT_CHECK')))")
  public Object proxyUniauthServerApi(@RequestBody(required = false) String param,
      HttpServletRequest request) {
    String path = getRelativePath(request);
    String requestMethod = request.getMethod();
    try {
      return proxyInvokeService.invokeApi(param, path, requestMethod);
    } catch (NotFoundApiException e) {
      return Response
          .failure(InfoName.BAD_REQUEST, UniBundle.getMsg("proxy.api.not.found"));
    } catch (NoAuthorityException e) {
      return Response
          .failure(InfoName.BAD_REQUEST, UniBundle.getMsg("proxy.api.no.authority"));
    } catch (InvalidParameterException e) {
      return Response
          .failure(InfoName.BAD_REQUEST, UniBundle.getMsg("proxy.api.invalid.parameter"));
    }
  }

  private String getRelativePath(HttpServletRequest request) {
    String path = clearPath(request.getRequestURI());
    String contextPath = clearPath(request.getContextPath());
    if (path.startsWith(contextPath)) {
      path = path.substring(contextPath.length());
    }
    if (path.startsWith("/proxy")) {
      path = path.substring("/proxy".length());
    }
    return path;
  }

  /**
   * 将指定路径中重复的'/'去掉，开头的'/'也去掉.
   *
   * @param path 目标字符.
   * @return 如果传入字符串为空，则直接返回.
   */
  private String clearPath(String path) {
    if (!StringUtils.hasText(path)) {
      return path;
    }
    StringBuilder sb = new StringBuilder();
    boolean isTargetChar = false;
    for (char c : path.toCharArray()) {
      if (c != '/') {
        sb.append(c);
        isTargetChar = false;
        continue;
      }
      if (isTargetChar) {
        // 过滤掉重复字符串
        continue;
      } else {
        isTargetChar = true;
        sb.append(c);
      }
    }
    String clearPath = sb.toString();
    if (clearPath.startsWith("/")) {
      clearPath = clearPath.substring(1);
    }
    return clearPath;
  }
}
