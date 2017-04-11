package com.dianrong.common.uniauth.client.config.configurations;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Conditional;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;

@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class ConcurrentSessionFilterConfigure implements Configure<ConcurrentSessionFilter> {

    @Resource(name = "sessionRegistry")
    private SessionRegistry sessionRegistry;

    @Resource(name = "uniauthConfig")
    private Map<String, String> uniauthConfig;

    @Override
    public ConcurrentSessionFilter create() {
        ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(sessionRegistry, getExpiredUrl());
        return concurrentSessionFilter;
    }

    @Override
    public boolean isSupport(Class<?> cls) {
        return ConcurrentSessionFilter.class.equals(cls);
    }

    private String getExpiredUrl() {
        return uniauthConfig.get("cas_server") + "/logout?dupsession=true";
    }
}
