package com.dianrong.common.techops.action;

import com.dianrong.common.techops.service.ProxyInvokeService;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.request.TagQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
  public String proxyUniauthServerApi(@RequestBody String param, @PathVariable String path) {

  }
}
