package com.dianrong.common.uniauth.server.support.tree;

import java.lang.annotation.*;

/**
 * 标识当前服务处理的树类型.
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TreeTypeTag {
  /**
   * 指定当前服务处理的树类型.
   */
  TreeType value();
}
