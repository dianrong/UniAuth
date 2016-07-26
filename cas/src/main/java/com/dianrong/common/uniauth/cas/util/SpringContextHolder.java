package com.dianrong.common.uniauth.cas.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * . 用于管理spring context对象
 * 
 * @author wanglin
 */
public class SpringContextHolder {
    /**
     * . 日志对象
     */
    private static final Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

    /**
     * . spring 容器上下文
     */
    private static ApplicationContext context = null;

    /**
     * . 注入spring容器的上下文到当前对象中
     * 
     * @param tcontext
     */
    public static void injectApplicationContext(ApplicationContext tcontext) {
        if (tcontext == null) {
            throw new NullPointerException();
        }
        synchronized (SpringContextHolder.class) {
            context = tcontext;
            SpringContextHolder.class.notifyAll();
        }
    }

    /**
     * . 获取spring bean
     * 
     * @param beanName bean的名称
     * @return 获取的bean对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        synchronized (SpringContextHolder.class) {
            if (context == null) {
                try {
                    SpringContextHolder.class.wait();
                } catch (InterruptedException e) {
                    logger.warn("getBean(String) wait InterruptedException", e);
                }
            }
            Object obj = context.getBean(beanName);
            if (obj == null) {
                return null;
            }
            return (T) obj;
        }
    }

    /**
     * . 获取spring bean
     * 
     * @param beanName bean的class
     * @return 获取的bean对象
     */
    public static <T> T getBean(Class<T> clst) {
        synchronized (SpringContextHolder.class) {
            if (context == null) {
                try {
                    SpringContextHolder.class.wait();
                } catch (InterruptedException e) {
                    logger.warn("getBean(Class<T>) wait InterruptedException", e);
                }
            }
            return context.getBean(clst);
        }
    }
}
