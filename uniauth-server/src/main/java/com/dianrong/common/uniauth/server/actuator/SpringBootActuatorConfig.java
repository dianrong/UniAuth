package com.dianrong.common.uniauth.server.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.EndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.PublicMetricsAutoConfiguration;
import org.springframework.boot.actuate.endpoint.*;
import org.springframework.boot.actuate.endpoint.mvc.EndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.boot.actuate.endpoint.mvc.HealthMvcEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collection;

/**
 * Created by Arc on 25/8/2016.
 */
@Configuration
@Import({ EndpointAutoConfiguration.class,PublicMetricsAutoConfiguration.class, HealthIndicatorAutoConfiguration.class})
public class SpringBootActuatorConfig {

    @Bean
    @Autowired
    public EndpointHandlerMapping endpointHandlerMapping(Collection<? extends MvcEndpoint> endpoints) {
        return new EndpointHandlerMapping(endpoints);
    }

    @Bean
    @Autowired
    public EndpointMvcAdapter metricsEndPoint(MetricsEndpoint delegate) {
        return new EndpointMvcAdapter(delegate);
    }

    @Bean
    @Autowired
    public HealthMvcEndpoint healthMvcEndpoint(HealthEndpoint delegete) {
        return new HealthMvcEndpoint(delegete, false);
    }

    @Bean
    @Autowired
    public EndpointMvcAdapter infoMvcEndPoint(InfoEndpoint delegate){
        return new EndpointMvcAdapter(delegate);
    }

    @Bean
    @Autowired
    public EndpointMvcAdapter beansEndPoint(BeansEndpoint delegate){
        return new EndpointMvcAdapter(delegate);
    }

    @Bean
    @Autowired
    public EndpointMvcAdapter requestMappingEndPoint(RequestMappingEndpoint delegate)
    {
        return new EndpointMvcAdapter(delegate);
    }
    @Bean
    public EndpointMvcAdapter dumpEndpoint() {
        return new EndpointMvcAdapter(new DumpEndpoint());
    }
}