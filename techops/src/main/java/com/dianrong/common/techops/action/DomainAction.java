package com.dianrong.common.techops.action;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Arc on 14/2/16.
 */
@RestController
@RequestMapping("domain")
public class DomainAction {

  @Resource
  private UARWFacade uarwFacade;

  @RequestMapping(value = "/query", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<PageDto<DomainDto>> searchDomains(@RequestBody DomainParam domainParam) {
    return uarwFacade.getDomainRWResource().searchDomain(domainParam);
  }

  @RequestMapping(value = "/modify", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<Void> modifyDomain(@RequestBody DomainParam domainParam) {
    return uarwFacade.getDomainRWResource().updateDomain(domainParam);
  }

  @RequestMapping(value = "/add", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<DomainDto> addNewDomain(@RequestBody DomainParam domainParam) {
    return uarwFacade.getDomainRWResource().addNewDomain(domainParam);
  }

  @RequestMapping(value = "/stakeholder-query", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<List<StakeholderDto>> searchStakeholders(@RequestBody DomainParam domainParam) {
    return uarwFacade.getDomainRWResource().getAllStakeholdersInDomain(domainParam);
  }

  @RequestMapping(value = "/stakeholder-add", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<StakeholderDto> addNewStakeholder(
      @RequestBody StakeholderParam stakeholderParam) {
    return uarwFacade.getDomainRWResource().addNewStakeholder(stakeholderParam);
  }

  @RequestMapping(value = "/stakeholder-delete", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<Void> deleteStakeholder(@RequestBody PrimaryKeyParam primaryKeyParam) {
    return uarwFacade.getDomainRWResource().deleteStakeholder(primaryKeyParam);
  }

  @RequestMapping(value = "/stakeholder-modify", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<Void> modifyStakeholder(@RequestBody StakeholderParam stakeholderParam) {
    return uarwFacade.getDomainRWResource().updateStakeholder(stakeholderParam);
  }

}
