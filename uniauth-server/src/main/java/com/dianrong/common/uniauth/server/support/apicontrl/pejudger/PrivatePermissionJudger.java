package com.dianrong.common.uniauth.server.support.apicontrl.pejudger;

import java.util.regex.Pattern;

import com.dianrong.common.uniauth.common.apicontrol.server.CallerCredential;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.support.apicontrl.ApiCtlPermission;
import com.dianrong.common.uniauth.server.support.apicontrl.ServerPermissionCacher;

public class PrivatePermissionJudger extends AbstractHttpRequestPermissionJudger {

    private ServerPermissionCacher serverPermissionCacher;

    public PrivatePermissionJudger(ServerPermissionCacher serverPermissionCacher) {
        Assert.notNull(serverPermissionCacher);
        this.serverPermissionCacher = serverPermissionCacher;
    }
    
    @Override
    protected Pattern getPattern(String patternStr) {
        return serverPermissionCacher.getPattern(patternStr);
    }

    @Override
    protected ApiCtlPermission getPermissionInfo(CallerCredential<ApiCtlPermission> credential) {
        return credential.getPermissionInfo();
    }
}
