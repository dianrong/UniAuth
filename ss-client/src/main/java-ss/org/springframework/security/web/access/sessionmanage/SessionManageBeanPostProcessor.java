package org.springframework.security.web.access.sessionmanage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionManagementFilter;

import com.dianrong.common.uniauth.common.util.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于手态配置SessionManagementFilter
 * @author wanglin
 */
@Slf4j
public class SessionManageBeanPostProcessor implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
	    if (bean instanceof SessionManagementFilter) {
	        log.info("config SessionManagementFilter manually");
	        SessionManagementFilter sessionManagementFilter = (SessionManagementFilter)bean;
	        InvalidSessionStrategy invalidSessionStrategy = (InvalidSessionStrategy)ReflectionUtils.getField(sessionManagementFilter, "invalidSessionStrategy");
	        List<InvalidSessionStrategy> invalidSessionStrategys = new ArrayList<InvalidSessionStrategy>();
	        if(invalidSessionStrategy != null) {
	            invalidSessionStrategys.add(invalidSessionStrategy);
	            log.info("SessionManagementFilter has original InvalidSessionStrategy : " + invalidSessionStrategys);
	        }
	        invalidSessionStrategys.add(new RequestCacheInvalidSessionStrategy());
	        CompositeInvalidSessionStrategy compositeInvalidSessionStrategy = new CompositeInvalidSessionStrategy();
	        compositeInvalidSessionStrategy.setInvalidSessionStrategys(invalidSessionStrategys);
	        sessionManagementFilter.setInvalidSessionStrategy(compositeInvalidSessionStrategy);
	    }
		return bean;
	}
}
