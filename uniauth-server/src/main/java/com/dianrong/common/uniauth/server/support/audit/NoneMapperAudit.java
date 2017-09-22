package com.dianrong.common.uniauth.server.support.audit;

import java.lang.annotation.*;

/**
 * 不记录Audit日志的方法.
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoneMapperAudit {
}
