package com.dianrong.common.uniauth.server.support.apicontrl;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.apicontrol.server.CallerCredential;
import com.dianrong.common.uniauth.common.apicontrol.server.PermissionJudger;
import com.dianrong.common.uniauth.common.bean.dto.ApiPermissionDto.UriMethod;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;

@Component
public class UniauthServerPermissionJudger implements PermissionJudger<ApiCtlPermission, HttpServletRequest>{
    
    @Autowired
    private ServerPermissionCacher serverPermissionCacher;
    
    @Override
    public boolean judge(CallerCredential<ApiCtlPermission> Credential, HttpServletRequest request) {
        Assert.notNull(Credential);
        Assert.notNull(Credential.getPermissionInfo());
        Assert.notNull(request);
        String requestUrl = HttpRequestUtil.extractRequestUrl(request, false);
        String requestMethod = request.getMethod();
        ApiCtlPermission permissionInfo = Credential.getPermissionInfo();
        for(ApiCtlPermissionItem item:permissionInfo.getPermissions()) {
            if (checkPermission(requestUrl, requestMethod, item)) {
                return true;
            }
        }
        return false;
    }
    
    // check permission
    private boolean checkPermission(String requestUrl, String requestMethod, ApiCtlPermissionItem item) {
        // check method
        // METHOD ALL, ignore
        if (!item.getMethod().equals(UriMethod.ALL)) {
            if (!item.getMethod().toString().equalsIgnoreCase(requestMethod)) {
                return false;
            }
        }
        // check request url
        Pattern pattern = serverPermissionCacher.getPattern(item.getUri());
        if (!pattern.matcher(requestUrl).matches()) {
            return false;
        }
        return true;
    }
}
