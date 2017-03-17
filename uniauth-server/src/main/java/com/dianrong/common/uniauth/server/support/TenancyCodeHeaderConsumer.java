package com.dianrong.common.uniauth.server.support;

import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.common.server.cxf.server.impl.AbstractTenancyCodeHeaderConsumer;

@Component
public class TenancyCodeHeaderConsumer extends AbstractTenancyCodeHeaderConsumer {
    @Override
    public void consume(String tenancyCode) {
        CxfHeaderHolder.TENANCYCODE.set(tenancyCode);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
