package com.dianrong.common.uniauth.common.server.cxf.propset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.dianrong.common.uniauth.common.server.cxf.client.ClientFilterSingleton;
import com.dianrong.common.uniauth.common.server.cxf.client.HeaderProducer;
import com.dianrong.common.uniauth.common.server.cxf.server.HeaderConsumer;
import com.dianrong.common.uniauth.common.server.cxf.server.ServerFilterSingletion;

/**
 * . 在spring 初始化完成之后执行
 * 
 * @author wanglin
 */
@Component
public class PropReadyListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
	private static final Logger logger = Logger.getLogger(PropReadyListener.class);

	// spring 容器对象引用
	private volatile ApplicationContext applicationContext;

	private Object lock = new Serializable(){};

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// root context
		if (event.getApplicationContext().getParent() == null) {
			try {
				ClientFilterSingleton.propSetInvoke(findBeanList(HeaderProducer.class));
				ServerFilterSingletion.propSetInvoke(findBeanList(HeaderConsumer.class));
			} catch (InterruptedException e) {
				logger.error("failed to set prop to cxf filter", e);
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		synchronized (lock) {
			this.applicationContext = applicationContext;
			lock.notifyAll();
		}
	}

	/**
	 * . 从applicationContext 中获取对应类型的bean
	 * 
	 * @param requiredType
	 *            can not be null
	 * @return
	 * @throws InterruptedException
	 */
	private <T> List<T> findBeanList(Class<T> requiredType) throws InterruptedException {
		synchronized (lock) {
			Assert.notNull(requiredType);
			if (this.applicationContext == null) {
				lock.wait();
			}
			List<T> beans = new ArrayList<T>();
			String[] beanNames = this.applicationContext.getBeanNamesForType(requiredType, true, false);
			for (String tname : beanNames) {
				@SuppressWarnings("unchecked")
				T bean = (T) this.applicationContext.getBean(tname);
				beans.add(bean);
			}
			return beans;
		}
	}
}
