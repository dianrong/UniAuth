package com.dianrong.common.uniauth.server.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.util.TaskInvoker;

@Service
public class TaskInitService implements InitializingBean,ApplicationContextAware{

	private ApplicationContext  context;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		TaskInvoker.init();
		TaskInvoker.register("userTransferTask", context.getBean(TransferService.class));
		TaskInvoker.register("commonTransferService", context.getBean(CRMPreTransferService.class));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context=applicationContext;
	}

}
