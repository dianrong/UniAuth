package com.dianrong.common.uniauth.server.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.AuditDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.AuditParam;
import com.dianrong.common.uniauth.common.interfaces.read.IAuditResource;
import com.dianrong.common.uniauth.server.service.AuditService;

/**
 * Created by Arc on 24/3/2016.
 */
@RestController
public class AuditResource implements IAuditResource {
    @Autowired
    private AuditService auditService;

    @Override
    public Response<PageDto<AuditDto>> searchAudit(AuditParam auditParam) {
        PageDto<AuditDto> auditDtoPageDto = auditService.searchAudit(auditParam.getUserId(),auditParam.getMinRequestDate(),auditParam.getMaxRequestDate(),
                auditParam.getDomainId(),auditParam.getReqIp(),auditParam.getReqUuid(),
                auditParam.getReqUrl(),auditParam.getReqSequence(),auditParam.getReqClass(),
                auditParam.getReqMethod(),auditParam.getReqSuccess(),auditParam.getReqException(),
                auditParam.getMinReqElapse(),auditParam.getMaxReqElapse(),auditParam.getReqParam(),
                auditParam.getReqResult(),auditParam.getOrderBy(),auditParam.getAscOrDesc(),
                auditParam.getPageNumber(),auditParam.getPageSize());
        return Response.success(auditDtoPageDto);
    }

    @Override
    public Response<Integer> deleteAudit(AuditParam auditParam) {
        Integer affectedRows = auditService.deleteAudit(auditParam.getUserId(),auditParam.getMinRequestDate(),auditParam.getMaxRequestDate(),
                auditParam.getDomainId(),auditParam.getReqIp(),auditParam.getReqUuid(),
                auditParam.getReqUrl(),auditParam.getReqSequence(),auditParam.getReqClass(),
                auditParam.getReqMethod(),auditParam.getReqSuccess(),auditParam.getReqException(),
                auditParam.getMinReqElapse(),auditParam.getMaxReqElapse(),auditParam.getReqParam(),
                auditParam.getReqResult());
        return Response.success(affectedRows);
    }

}
