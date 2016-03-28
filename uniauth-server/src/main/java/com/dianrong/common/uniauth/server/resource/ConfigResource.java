package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.CfgParam;
import com.dianrong.common.uniauth.server.service.ConfigService;
import com.dianrong.common.uniauth.sharerw.interfaces.IConfigRWResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Arc on 25/3/2016.
 */
@RestController
public class ConfigResource implements IConfigRWResource {

    @Autowired
    private ConfigService configService;

    @Override
    public Response<ConfigDto> addOrUpdateConfig(CfgParam cfgParam) {
        ConfigDto configDto = configService.addOrUpdateConfig(cfgParam.getId(),cfgParam.getKey(),cfgParam.getType(),
                cfgParam.getValue(),cfgParam.getFile());
        return Response.success(configDto);
    }

    @Override
    public Response<PageDto<ConfigDto>> queryConfig(CfgParam cfgParam) {
        PageDto<ConfigDto> configDtoPageDto = configService.queryConfig(cfgParam.getId(),cfgParam.getKey(),cfgParam.getType(),
                cfgParam.getValue(),cfgParam.getPageSize(),cfgParam.getPageNumber());
        return Response.success(configDtoPageDto);
    }

    @Override
    public Response<Void> delConfig(Integer cfgId) {
        configService.delConfig(cfgId);
        return Response.success();
    }
}
