package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.ProfileParam;
import com.dianrong.common.uniauth.sharerw.interfaces.IGroupProfileRWResource;

import java.util.Map;

/**
 * Group的Profile相关操作.
 */
public class GroupProfileResource implements IGroupProfileRWResource {

  @Override
  public Response<Map<String, Object>> getGroupProfile(Long groupId, Long profileId) {
    return null;
  }

  @Override
  public Response<Map<String, Object>> addOrUpdateUserProfile(Long groupId, Long profileId,
      ProfileParam param) {
    return null;
  }
}
