package com.dianrong.common.uniauth.server.support.apicontrl;

import java.io.Serializable;
import java.util.Set;

import com.google.common.collect.Sets;

public class ApiCtlPermission implements Serializable{

    private static final long serialVersionUID = -7921394951462763553L;

    private Set<ApiCtlPermissionItem> permissions;
    
    public ApiCtlPermission() {
        permissions = Sets.newHashSet();
    }
    
    public ApiCtlPermission add(ApiCtlPermissionItem permission) {
        if (permission != null) {
            this.permissions.add(permission);
        }
        return this;
    }
    
    /**
     * @return all permissions
     */
    public Set<ApiCtlPermissionItem> getPermissions() {
        return this.permissions;
    }
}
