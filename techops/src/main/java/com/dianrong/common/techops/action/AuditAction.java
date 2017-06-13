package com.dianrong.common.techops.action;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.AuditDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.AuditParam;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Arc on 24/3/2016.
 */
@RestController
@RequestMapping("audit")
public class AuditAction {

  @Resource
  private UARWFacade uarwFacade;

  @RequestMapping(value = "/query", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<PageDto<AuditDto>> searchAudits(@RequestBody AuditParam auditParam) {
    return uarwFacade.getAuditResource().searchAudit(auditParam);
  }

  @RequestMapping(value = "/delete", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<Integer> deleteAudits(@RequestBody AuditParam auditParam) {
    return uarwFacade.getAuditResource().deleteAudit(auditParam);
  }

}
