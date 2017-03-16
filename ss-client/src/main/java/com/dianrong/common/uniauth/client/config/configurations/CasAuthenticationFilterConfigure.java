package com.dianrong.common.uniauth.client.config.configurations;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondtion;
import com.dianrong.common.uniauth.client.custom.SSSavedRequestAwareAuthenticationSuccessHandler;

@Component
@Conditional(UniauthConfigEnvLoadCondtion.class)
public class CasAuthenticationFilterConfigure implements Configure<CasAuthenticationFilter> {

    private static final String DEFAULT_FILTER_PROCESS_URL = "/login/cas";

    @Autowired
    private SSSavedRequestAwareAuthenticationSuccessHandler ssAuthenticationSuccessHandler;

    @Resource(name = "authenticationManager")
    private AuthenticationManager authenticationManager;

    @Resource(name = "sas")
    private SessionAuthenticationStrategy sas;

    @Override
    public CasAuthenticationFilter create() {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager);
        casAuthenticationFilter.setFilterProcessesUrl(DEFAULT_FILTER_PROCESS_URL);
        casAuthenticationFilter.setAuthenticationSuccessHandler(ssAuthenticationSuccessHandler);
        casAuthenticationFilter.setSessionAuthenticationStrategy(sas);
        return casAuthenticationFilter;
    }

    @Override
    public boolean isSupport(Class<?> cls) {
        return CasAuthenticationFilter.class.equals(cls);
    }
}
