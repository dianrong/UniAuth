package com.dianrong.common.uniauth.client.config.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondtion;
import com.dianrong.common.uniauth.client.custom.CustomizedRedirectFormat;
import com.dianrong.common.uniauth.client.custom.SSExceptionTranslationFilter;
import com.dianrong.common.uniauth.client.custom.UniauthAjaxResponseProcessor;
import com.dianrong.common.uniauth.common.client.ZooKeeperConfig;

import lombok.extern.slf4j.Slf4j;

@Component
@Conditional(UniauthConfigEnvLoadCondtion.class)
@Slf4j
public class SSExceptionTranslationFilterConfigure implements Configure<SSExceptionTranslationFilter> {

    private static final String DEFAULT_ERROR_PAGE = "/errors/403.jsp";

    @Autowired(required = false)
    private AccessDeniedHandlerImpl accessDeniedHandlerImpl;

    @Autowired
    private CasAuthenticationEntryPoint casAuthEntryPoint;

    @Autowired
    private ZooKeeperConfig zooKeeperConfig;

    @Autowired(required = false)
    private CustomizedRedirectFormat customizedRedirectFormat;

    @Override
    public SSExceptionTranslationFilter create() {
        if (accessDeniedHandlerImpl == null) {
            // construct auto
            log.info("accessDeniedHandlerImpl is null, construct auto, the default error page is : " + DEFAULT_ERROR_PAGE);
            this.accessDeniedHandlerImpl = new AccessDeniedHandlerImpl();
            this.accessDeniedHandlerImpl.setErrorPage(DEFAULT_ERROR_PAGE);
        }

        SSExceptionTranslationFilter ssExceptionTranslationFilter = new SSExceptionTranslationFilter(casAuthEntryPoint);
        ssExceptionTranslationFilter.setAccessDeniedHandler(accessDeniedHandlerImpl);
        ssExceptionTranslationFilter.setZooKeeperConfig(zooKeeperConfig);
        ssExceptionTranslationFilter.setCustomizedRedirectFormat(customizedRedirectFormat);
        ssExceptionTranslationFilter.setAjaxResponseProcessor(new UniauthAjaxResponseProcessor(this.zooKeeperConfig));
        return ssExceptionTranslationFilter;
    }

    @Override
    public boolean isSupport(Class<?> cls) {
        return SSExceptionTranslationFilter.class.equals(cls);
    }
}
