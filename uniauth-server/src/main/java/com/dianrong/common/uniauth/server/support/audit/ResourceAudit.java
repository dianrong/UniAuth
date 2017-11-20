package com.dianrong.common.uniauth.server.support.audit;

import java.lang.annotation.*;

/**
 * 标识了该注解的Resource接口每次访问都会记录进Audit表.
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResourceAudit {
}
