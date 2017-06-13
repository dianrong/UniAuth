package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.server.exp.AppException;

/**
 * . 类型转变相关的工具方法.
 *
 * @author wanglin
 */
public class TypeParseUtil {

  /**
   * 从object转换成Long，不然就报错抛出去.
   */
  public static Long parseToLongFromObject(Object val) {
    if (val == null) {
      throw new AppException(InfoName.VALIDATE_FAIL, UniBundle
          .getMsg("datafilter.typeparase.null.poniter", "TypeParseUtil.parseToLongFromObject"));
    }
    Long result = null;
    try {
      result = Long.parseLong(val.toString());
    } catch (Exception ex) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("datafilter.typeparase.parase.error", val, "Long"));
    }
    return result;
  }

  /**
   * 从object转换成Integer，不然就报错抛出去.
   */
  public static Integer parseToIntegerFromObject(Object val) {
    if (val == null) {
      throw new AppException(InfoName.VALIDATE_FAIL, UniBundle
          .getMsg("datafilter.typeparase.null.poniter", "TypeParseUtil.parseToIntegerFromObject"));
    }
    Integer result = null;
    try {
      result = Integer.parseInt(val.toString());
    } catch (Exception ex) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("datafilter.typeparase.parase.error", val, "Long"));
    }
    return result;
  }

  /**
   * 从object转换成String，不然就报错抛出去.
   */
  public static String parseToStringFromObject(Object val) {
    if (val == null) {
      throw new AppException(InfoName.VALIDATE_FAIL, UniBundle
          .getMsg("datafilter.typeparase.null.poniter", "TypeParseUtil.parseToStringFromObject"));
    }
    String result = null;
    try {
      result = String.valueOf(val);
    } catch (Exception ex) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("datafilter.typeparase.parase.error", val, "String"));
    }
    return result;
  }
}
