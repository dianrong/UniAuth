package com.dianrong.common.uniauth.server.support;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 配置ObjectMapper
 * 
 * @author wanglin
 */
public final class ConfigurableObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 8454391087040217847L;
    /**
     * config DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
     */
    private boolean disableleFailOnUnKnownProperties = false;

    public boolean isDisableleFailOnUnKnownProperties() {
        return disableleFailOnUnKnownProperties;
    }

    public void setDisableleFailOnUnKnownProperties(boolean disableleFailOnUnKnownProperties) {
        this.disableleFailOnUnKnownProperties = disableleFailOnUnKnownProperties;
    }

    @PostConstruct
    public void init() {
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, !disableleFailOnUnKnownProperties);
    }
}
