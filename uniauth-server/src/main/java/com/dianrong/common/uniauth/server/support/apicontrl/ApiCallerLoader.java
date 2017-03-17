package com.dianrong.common.uniauth.server.support.apicontrl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.apicontrol.exp.LoadCredentialFailedException;
import com.dianrong.common.uniauth.common.apicontrol.server.CallerCredential;
import com.dianrong.common.uniauth.common.apicontrol.server.LoadCredential;
import com.dianrong.common.uniauth.common.bean.dto.ApiCallerInfoDto;
import com.dianrong.common.uniauth.common.bean.dto.ApiPermissionDto;
import com.dianrong.common.uniauth.server.service.ApiCallerService;
import com.dianrong.common.uniauth.server.service.ApiPermissionService;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ApiCallerLoader implements LoadCredential<ApiCtlPermission> {

    // @Autowired
    // private ServerPermissionCacher serverPermissionCacher;

    @Autowired
    private ApiCallerService ApiCallerService;

    @Autowired
    private ApiPermissionService apiPermissionService;

    @Override
    public CallerCredential<ApiCtlPermission> loadCredential(String account, String password) throws LoadCredentialFailedException {
        try {
            ApiCallerInfoDto apiCallerInfo = ApiCallerService.searchApiCaller(account, password);
            if (apiCallerInfo == null) {
                throw new LoadCredentialFailedException();
            }
            // get private permissions
            List<ApiPermissionDto> privatePermissions = apiPermissionService.searchPrivatePermissions(apiCallerInfo.getId());

            // get public permissions
            // Set<ApiCtlPermissionItem> publicPermissions =
            // serverPermissionCacher.getPublicPermissions();
            Set<ApiCtlPermissionItem> allPermissions = Sets.newHashSet();

            // combination private permissions and public permissions
            if (privatePermissions != null && !privatePermissions.isEmpty()) {
                for (ApiPermissionDto dto : privatePermissions) {
                    ApiCtlPermissionItem item = new ApiCtlPermissionItem();
                    item.setMethod(dto.getMethod()).setUri(dto.getUri());
                    allPermissions.add(item);
                }
            }
            // for (ApiCtlPermissionItem item : publicPermissions) {
            // allPermissions.add(item);
            // }
            CallerCredential<ApiCtlPermission> apiCallerCredential = new ApiCaller(apiCallerInfo.getDomainCode(), apiCallerInfo.getDomainName(), allPermissions);
            return apiCallerCredential;
        } catch (Throwable t) {
            log.error("loadCredential failed, account " + account, t);
            throw new LoadCredentialFailedException();
        }
    }
}
