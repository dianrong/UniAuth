package com.dianrong.common.uniauth.client.config;

/**
 * 手动构造bean对象.
 *
 * @author wanglin
 */
public interface Configure<T> {

  /**
   * Create a new T.
   *
   * @return new T
   */
  T create();

  /**
   * Judge whether the class type is supported.
   *
   * @param cls the type
   * @return true if the class is supported
   */
  boolean isSupport(Class<?> cls);
}
