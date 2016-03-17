package com.dianrong.common.techops.action;

import com.dianrong.common.techops.service.TechOpsService;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Arc on 14/2/16.
 */
@RestController
@RequestMapping("domain")
public class DomainAction {

    @Resource
    private UARWFacade uARWFacade;
    
    @RequestMapping(value = "/query" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<PageDto<DomainDto>> searchDomains(@RequestBody DomainParam domainParam) {
        return uARWFacade.getDomainRWResource().searchDomain(domainParam);
    }

    @RequestMapping(value = "/stakeholder-query" , method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<StakeholderDto>> searchStakeholders(@RequestBody DomainParam domainParam) {
        return uARWFacade.getDomainRWResource().getAllStakeHoldersInDomain(domainParam);
    }
}
