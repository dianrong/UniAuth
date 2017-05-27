package com.dianrong.common.uniauth.server.support.apicontrl.pejudger;

import com.dianrong.common.uniauth.common.apicontrol.server.CallerCredential;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.support.apicontrl.ApiCtlPermission;
import com.dianrong.common.uniauth.server.support.apicontrl.ApiCtlPermissionItem;
import com.dianrong.common.uniauth.server.support.apicontrl.ServerPermissionCacher;
import java.util.Set;
import java.util.regex.Pattern;

public class PublicPermissionJudger extends AbstractHttpRequestPermissionJudger {

  private ServerPermissionCacher serverPermissionCacher;

  public PublicPermissionJudger(ServerPermissionCacher serverPermissionCacher) {
    Assert.notNull(serverPermissionCacher);
    this.serverPermissionCacher = serverPermissionCacher;
  }

  @Override
  protected Pattern getPattern(String patternStr) {
    return serverPermissionCacher.getPattern(patternStr);
  }

  @Override
  protected ApiCtlPermission getPermissionInfo(CallerCredential<ApiCtlPermission> credential) {
    ApiCtlPermission publicPermission = new ApiCtlPermission();
    Set<ApiCtlPermissionItem> permissions = serverPermissionCacher.getPublicPermissions();
    for (ApiCtlPermissionItem item : permissions) {
      publicPermission.add(item);
    }
    return publicPermission;
  }
}
