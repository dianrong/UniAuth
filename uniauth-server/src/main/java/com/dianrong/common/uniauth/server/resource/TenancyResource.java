package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.bean.request.TenancyParam;
import com.dianrong.common.uniauth.server.service.TenancyService;
import com.dianrong.common.uniauth.sharerw.interfaces.ITenancyRWResource;

import io.swagger.annotations.Api;

@Api("租户信息操作接口")
@RestController
public class TenancyResource implements ITenancyRWResource {

    @Autowired
    private TenancyService tanancyService;

    @Override
    @Timed
    public Response<List<TenancyDto>> searchTenancy(TenancyParam tenancyParam) {
        List<TenancyDto> tenancyDtoList = tanancyService.getAllTenancy(tenancyParam.getId(), tenancyParam.getCode(), tenancyParam.getStatus(), tenancyParam.getName(),
                tenancyParam.getContactName(), tenancyParam.getPhone(), tenancyParam.getDescription());
        return new Response<List<TenancyDto>>(tenancyDtoList);
    }

    @Override
    public Response<TenancyDto> queryDefaultTenancy() {
        TenancyDto tenancy = tanancyService.getDefaultTenancy();
        return new Response<TenancyDto>(tenancy);
    }
}
