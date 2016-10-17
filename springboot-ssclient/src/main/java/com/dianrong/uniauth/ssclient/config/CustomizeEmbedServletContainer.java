package com.dianrong.uniauth.ssclient.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.dianrong.uniauth.ssclient.config.support.DisableSpringFilterBeanAutomicRegistry;

@Component
public class CustomizeEmbedServletContainer implements EmbeddedServletContainerCustomizer{
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(8280);
		container.addErrorPages(
				new ErrorPage(HttpStatus.FORBIDDEN, "/errors/403.html"),
				new ErrorPage(HttpStatus.NOT_FOUND, "/errors/404.html"),
				new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/errors/500.html"));
		// 用于替代不想处理的bean
		container.addInitializers(new DisableSpringFilterBeanAutomicRegistry());
	}
}
