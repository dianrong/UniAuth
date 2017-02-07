package com.dianrong.common.uniauth.server.support.apicontrl;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.apicontrol.server.CallerCredential;
import com.dianrong.common.uniauth.common.apicontrol.server.PermissionJudger;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.support.apicontrl.pejudger.PrivatePermissionJudger;
import com.dianrong.common.uniauth.server.support.apicontrl.pejudger.PublicPermissionJudger;
import com.google.common.collect.Sets;

@Component
public class ComposedPermissionJudger implements PermissionJudger<ApiCtlPermission, HttpServletRequest>{
    
    @Autowired
    private ServerPermissionCacher serverPermissionCacher;
    
    private Set<PermissionJudger<ApiCtlPermission, HttpServletRequest>> permissionJudgers = Sets.newConcurrentHashSet();;
    
    @Override
    public boolean judge(CallerCredential<ApiCtlPermission> Credential, HttpServletRequest request) {
        for (PermissionJudger<ApiCtlPermission, HttpServletRequest> judger : permissionJudgers) {
            if (judger.judge(Credential, request)) {
                return true;
            }
        }
        return false;
    }
    
    @PostConstruct
    public void init() {
        this.addJudger(new PublicPermissionJudger(this.serverPermissionCacher))
        .addJudger(new PrivatePermissionJudger(this.serverPermissionCacher));
    }
    
    /**
     * add a new judger
     * @param judger new judger
     * @return ComposedPermissionJudger
     */
    public ComposedPermissionJudger addJudger(PermissionJudger<ApiCtlPermission, HttpServletRequest> judger) {
        Assert.notNull(judger);
        permissionJudgers.add(judger);
        return this;
    }
}
