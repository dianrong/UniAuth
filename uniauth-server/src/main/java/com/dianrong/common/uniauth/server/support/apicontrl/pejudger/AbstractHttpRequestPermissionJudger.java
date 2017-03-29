package com.dianrong.common.uniauth.server.support.apicontrl.pejudger;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;

import com.dianrong.common.uniauth.common.apicontrol.server.CallerCredential;
import com.dianrong.common.uniauth.common.apicontrol.server.PermissionJudger;
import com.dianrong.common.uniauth.common.bean.dto.ApiPermissionDto.UriMethod;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;
import com.dianrong.common.uniauth.server.support.apicontrl.ApiCtlPermission;
import com.dianrong.common.uniauth.server.support.apicontrl.ApiCtlPermissionItem;

public abstract class AbstractHttpRequestPermissionJudger implements PermissionJudger<ApiCtlPermission, HttpServletRequest> {
    @Override
    public boolean judge(CallerCredential<ApiCtlPermission> Credential, HttpServletRequest request) {
        Assert.notNull(Credential);
        Assert.notNull(Credential.getPermissionInfo());
        Assert.notNull(request);
        String requestUrl = HttpRequestUtil.extractRequestUrl(request, false);
        String requestMethod = request.getMethod();
        ApiCtlPermission permissionInfo = getPermissionInfo(Credential);
        for (ApiCtlPermissionItem item : permissionInfo.getPermissions()) {
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
        Pattern pattern = getPattern(item.getUri());
        if (!pattern.matcher(requestUrl).matches()) {
            return false;
        }
        return true;
    }

    /**
     * get pattern
     * 
     * @param patternStr patternStr
     * @return pattern
     * @throws PatternSyntaxException - If the expression's syntax is invalid
     */
    protected Pattern getPattern(String patternStr) {
        Assert.notNull(patternStr);
        Assert.notNull(patternStr);
        return Pattern.compile(patternStr);
    }

    protected abstract ApiCtlPermission getPermissionInfo(CallerCredential<ApiCtlPermission> credential);
}
