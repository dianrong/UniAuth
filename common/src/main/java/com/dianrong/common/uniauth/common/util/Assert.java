package com.dianrong.common.uniauth.common.util;

/**
 * Copy from spring.
 *
 * @author wanglin
 */
public class Assert {

  /**
   * Assert that an object is not {@code null} .
   *
   * <pre class="code">
   * Assert.notNull(clazz);
   * </pre>
   *
   * @param object the object to check
   * @throws IllegalArgumentException if the object is {@code null}
   */
  public static void notNull(Object object) {
    notNull(object, "[Assertion failed] - this argument is required; it must not be null");
  }

  /**
   * Assert that an object is not {@code null} .
   *
   * <pre class="code">
   * Assert.notNull(clazz, "The class must not be null");
   * </pre>
   *
   * @param object the object to check
   * @param message the exception message to use if the assertion fails
   * @throws IllegalArgumentException if the object is {@code null}
   */
  public static void notNull(Object object, String message) {
    if (object == null) {
      throw new IllegalArgumentException(message);
    }
  }
}
