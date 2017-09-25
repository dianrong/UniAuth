package com.dianrong.common.uniauth.server.support.audit;

import java.lang.annotation.*;

/**
 * 用于标识Resource的Audit注解.
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResourceAudit {
}
