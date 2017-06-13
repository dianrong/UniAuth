package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.request.CfgParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.interfaces.read.IConfigResource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by Arc on 25/3/2016.
 */
public interface IConfigRWResource extends IConfigResource {

  @POST
  @Path("/addOrUpdate")
  Response<ConfigDto> addOrUpdateConfig(CfgParam cfgParam);

  @POST
  @Path("/del")
  Response<Void> delConfig(PrimaryKeyParam primaryKeyParam);
}
