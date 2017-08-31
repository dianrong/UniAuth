package com.dianrong.common.techops.action;

import com.dianrong.common.techops.util.PureHttpRequestUtil;
import com.dianrong.common.uniauth.common.bean.request.SuperAdminParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.HttpMethod;
import java.util.UUID;

/**
 * Created by Arc on 31/8/2017.
 */
@RestController
@RequestMapping("admin")
@Slf4j
public class SuperAdminAction {

  @Autowired
  private ObjectMapper objectMapper;

  @RequestMapping(value = "/proxy", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public String httpRequest(@RequestBody SuperAdminParam superAdminParam) throws JsonProcessingException {
    String result = "";
    if (superAdminParam == null) {
      return result;
    }
    String uuid = UUID.randomUUID().toString();
    log.warn("adminHttpRequest " + uuid + " : " + objectMapper.writeValueAsString(superAdminParam));
    if (HttpMethod.GET.equalsIgnoreCase(superAdminParam.getHttpMethod())) {
      result = PureHttpRequestUtil.sendGet(superAdminParam.getUrl(), superAdminParam.getParams(), superAdminParam.getRequestHeaders());
    }
    if (HttpMethod.POST.equalsIgnoreCase(superAdminParam.getHttpMethod())) {
      result = PureHttpRequestUtil.sendPost(superAdminParam.getUrl(), superAdminParam.getParams(), superAdminParam.getRequestHeaders());
    }
    log.warn("adminHttpResult " + uuid + " : " + result);
    return result;
  }

}
