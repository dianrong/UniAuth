package com.dianrong.common.uniauth.client.config;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.Assert;

import com.dianrong.common.uniauth.common.client.DomainDefine;

/**
 * decide whether create filter type bean auto
 * @author wanglin
 */
public class UniauthFilterBeanCreateCondtion implements  Condition{
	
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		DomainDefine domainDefine = beanFactory.getBean("domainDefine", DomainDefine.class);
		Assert.notNull(domainDefine, "can not get deomainDefine from spring bean factory");
		Boolean createFilterManully =  domainDefine.isCreateFilterManully();
		if (createFilterManully != null) {
			return !createFilterManully;
		}
		// 自动判断
		String[] names = beanFactory.getBeanNamesForType(UniauthSecurityConfig.class);
		if (names.length > 0) {
			return false;
		}
		return true;
	}
}
