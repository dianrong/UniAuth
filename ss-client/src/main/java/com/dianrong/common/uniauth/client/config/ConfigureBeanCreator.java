package com.dianrong.common.uniauth.client.config;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.dianrong.common.uniauth.client.exp.NoSuchConfigureException;

import lombok.extern.slf4j.Slf4j;

/**
 * create configure bean
 * @author wanglin
 */
@Component
@Slf4j
public class ConfigureBeanCreator implements ApplicationContextAware{

	private volatile  ApplicationContext applicationContext;
	
	private CountDownLatch lock = new CountDownLatch(1);
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
			lock.countDown();
	}
	
	/**
	 * create bean with the class cls
	 * @param cls the class
	 * @return new bean
	 * @throws 
	 */
	public <T> T  create(Class<T> cls) {
		Assert.notNull(cls);
		try {
			log.debug("create " + cls + " supended, until ApplicationContext is setted");
			lock.await();
		} catch (InterruptedException e) {
			log.error("failed to create " + cls);
			throw new RuntimeException(e);
		}
		String[] names = applicationContext.getBeanNamesForType(Configure.class, true, false);
		if (names.length == 0) {
			throw new NoSuchConfigureException("no configure for class " + cls);
		}
		for (int i=0; i < names.length; i++) {
			Configure<?> _configure = (Configure<?>)applicationContext.getBean(names[i]);
			if (_configure.isSupport(cls)) {
				@SuppressWarnings("unchecked")
				Configure<T> configure = (Configure<T>)_configure;
				return configure.create();
			}
		}
		throw new NoSuchConfigureException("no configure for class " + cls);
	}
}
