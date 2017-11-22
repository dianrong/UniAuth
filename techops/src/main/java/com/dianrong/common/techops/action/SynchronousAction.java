package com.dianrong.common.techops.action;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.HrSynchronousLogDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.HrSynchronousLogParam;
import com.dianrong.common.uniauth.common.bean.request.HrSynchronousProcessParam;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 同步处理的Action.
 */
@RestController
@RequestMapping("synchronous")
public class SynchronousAction {

  @Resource
  private UARWFacade uarwFacade;

  @RequestMapping(value = "/hr/log-query", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<PageDto<HrSynchronousLogDto>> searchHrLogs(@RequestBody
      HrSynchronousLogParam synchronousLogParam) {
    return uarwFacade.getSynchronousDataRWResource().querySynchronousLog(synchronousLogParam);
  }

  @RequestMapping(value = "/hr/synchronous-once", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<Void> hrSynchronousOnce(@RequestBody HrSynchronousProcessParam param) {
    return uarwFacade.getSynchronousDataRWResource().synchronousHrData(param);
  }

  @RequestMapping(value = "/hr/delete-expired-files", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<Void> hrDeleteExpiredFiles(@RequestBody HrSynchronousProcessParam param) {
    return uarwFacade.getSynchronousDataRWResource().deleteExpiredFile(param);
  }
}
