package com.dianrong.common.techops.action;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Arc on 14/2/16.
 */
@RestController
@RequestMapping("domain")
public class DomainAction {

    @Resource
    private UARWFacade uARWFacade;

    @RequestMapping(value = "/all" , method= RequestMethod.GET)
    public Response<List<DomainDto>> getAllLoginDomains() {
        Response<List<DomainDto>> domainDtos =  uARWFacade.getDomainRWResource().getAllLoginDomains();
        return domainDtos;
    }
}
