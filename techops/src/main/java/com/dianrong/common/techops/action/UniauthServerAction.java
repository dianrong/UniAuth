package com.dianrong.common.techops.action;

import com.dianrong.common.techops.service.ProxyInvokeService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
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
  @RequestMapping(value = "{path}")
  @PreAuthorize("(hasRole('ROLE_SUPER_ADMIN') or (hasRole('ROLE_ADMIN')"
      + " and hasPermission(null,'SYSTEM_ACCOUNT_CHECK'))")
  public String proxyUniauthServerApi(@RequestBody String param, @PathVariable String path,
      HttpServletRequest request, HttpServletResponse response) {
    return null;
  }
}
