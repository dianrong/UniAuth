package com.dianrong.common.uniauth.cas.service.support.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解用于处理租户标识信息的传递.
 * @author wanglin
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenancyIdentity {

  public enum Type {
    ID, CODE
  }

  ;

  /**
   * 处理的参数类型 默认为ID类型
   */
  public Type type() default Type.ID;

  /**
   * . 参数在第几个 start from 0
   */
  public int index();

}
