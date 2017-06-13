package com.dianrong.common.uniauth.server.resource;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.CfgParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.server.service.ConfigService;
import com.dianrong.common.uniauth.sharerw.interfaces.IConfigRWResource;
import io.swagger.annotations.Api;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

;

/**
 * Created by Arc on 25/3/2016.
 */
@Api("Uniauth中的配置相关操作接口")
@RestController
public class ConfigResource implements IConfigRWResource {

  @Autowired
  private ConfigService configService;

  @Override
  public Response<ConfigDto> addOrUpdateConfig(CfgParam cfgParam) {
    ConfigDto configDto = configService.addOrUpdateConfig(cfgParam.getId(), cfgParam.getCfgKey(),
        cfgParam.getCfgTypeId(), cfgParam.getValue(), cfgParam.getFile());
    return Response.success(configDto);
  }

  @Override
  @Timed
  public Response<PageDto<ConfigDto>> queryConfig(CfgParam cfgParam) {
    PageDto<ConfigDto> configDtoPageDto =
        configService.queryConfig(cfgParam.getCfgKeys(), cfgParam.getCfgKeyLike(), cfgParam.getId(),
            cfgParam.getCfgKey(), cfgParam.getCfgTypeId(), cfgParam.getValue(),
            cfgParam.getNeedBLOBs(), cfgParam.getPageSize(), cfgParam.getPageNumber());
    return Response.success(configDtoPageDto);
  }

  @Override
  public Response<Void> delConfig(PrimaryKeyParam primaryKeyParam) {
    configService.delConfig(primaryKeyParam.getId());
    return Response.success();
  }

  @Override
  public Response<Map<Integer, String>> getAllCfgTypes() {
    Map<Integer, String> cfgTypesMap = configService.getAllCfgTypesIdCodePair();
    return Response.success(cfgTypesMap);
  }

}
