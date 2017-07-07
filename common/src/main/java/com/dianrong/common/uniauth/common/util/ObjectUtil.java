package com.dianrong.common.uniauth.common.util;

import java.util.Collection;

/**
 * Object处理相关的工具类.
 *
 * @author wanglin
 */
public final class ObjectUtil {

  /**
   * 判断 o1 和 o2 是否相等.
   *
   * @return true or false
   */
  public static boolean objectEqual(Object o1, Object o2) {
    if (o1 == null && o2 == null) {
      return true;
    }
    // 其中某个字段为空了 都不能认为是相等的
    if (o1 == null || o2 == null) {
      return false;
    }
    return o1.equals(o2);
  }
  
  /**
   * 判断 o1 和 o2 是否相等.
   *
   * @return true or false
   */
  public static boolean objectEqualIgnoreCase(Object o1, Object o2) {
    if (o1 == null && o2 == null) {
      return true;
    }
    // 其中某个字段为空了 都不能认为是相等的
    if (o1 == null || o2 == null) {
      return false;
    }
    if ((o1 instanceof String) && (o2 instanceof String)) {
      return ((String)o1).equalsIgnoreCase((String)o2);
    }
    return o1.equals(o2);
  }

  /**
   * 判断集合是否为空或为null.
   *
   * @param elements Collection
   * @return 是否为空或null
   */
  public static boolean collectionIsEmptyOrNull(Collection<?> elements) {
    if (elements == null || elements.isEmpty()) {
      return true;
    }
    return false;
  }
}
