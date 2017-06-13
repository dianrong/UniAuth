package com.dianrong.common.techops.spring;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.EndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.PublicMetricsAutoConfiguration;
import org.springframework.boot.actuate.endpoint.BeansEndpoint;
import org.springframework.boot.actuate.endpoint.DumpEndpoint;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.actuate.endpoint.InfoEndpoint;
import org.springframework.boot.actuate.endpoint.MetricsEndpoint;
import org.springframework.boot.actuate.endpoint.RequestMappingEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.EndpointHandlerMapping;
import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.boot.actuate.endpoint.mvc.HealthMvcEndpoint;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Arc on 26/8/2016.
 */
@Configuration
// @EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@Import({EndpointAutoConfiguration.class, PublicMetricsAutoConfiguration.class,
    HealthIndicatorAutoConfiguration.class})
@ImportResource({"classpath:applicationContext.xml"})
public class PropertiesConfiguration {

  @Bean
  @Autowired
  public EndpointHandlerMapping endpointHandlerMapping(
      Collection<? extends MvcEndpoint> endpoints) {
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
  public EndpointMvcAdapter infoMvcEndPoint(InfoEndpoint delegate) {
    return new EndpointMvcAdapter(delegate);
  }

  @Bean
  @Autowired
  public EndpointMvcAdapter beansEndPoint(BeansEndpoint delegate) {
    return new EndpointMvcAdapter(delegate);
  }

  @Bean
  @Autowired
  public EndpointMvcAdapter requestMappingEndPoint(RequestMappingEndpoint delegate) {
    return new EndpointMvcAdapter(delegate);
  }

  @Bean
  public EndpointMvcAdapter dumpEndpoint() {
    return new EndpointMvcAdapter(new DumpEndpoint());
  }
}
