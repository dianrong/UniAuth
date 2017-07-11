package com.dianrong.common.uniauth.common.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.DigestUtils;

@Slf4j
public class StringUtil {

  /**
   * 手机号码正则.
   */
  public static final Pattern PHONE_NUMBER = Pattern.compile("^1(3|4|5|7|8)\\d{9}$");

  /**
   * 邮箱正则.
   */
  public static final Pattern EMAIL_NUMBER = Pattern.compile(
      "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

  /**
   * Judge string is null or empty.
   *
   * @param str str
   * @return boolean
   */
  public static boolean strIsNullOrEmpty(final String str) {
    if (str == null || "".equals(str)) {
      return true;
    }

    return false;
  }

  /**
   * 获取对象的字符串表示方式.
   *
   * @param obj 对象
   * @return 字符串结果
   */
  public static String getObjectStr(Object obj) {
    if (obj == null) {
      return "";
    }
    return obj.toString();
  }

  /**
   * 获取对象的字符串表示方式.
   *
   * @return 字符串结果
   */
  public static Integer tryToTranslateStrToInt(String str) {
    if (str == null) {
      return null;
    }
    try {
      return Integer.parseInt(str);
    } catch (NumberFormatException e) {
      log.warn(str + "is a invalid number format string", e);
    }
    return null;
  }

  /**
   * Translate Integer object to Long object.
   *
   * @param intNum Integer
   * @return null if intNum is null Long if intNum is not null
   */
  public static Long translateIntegerToLong(Integer intNum) {
    if (intNum == null) {
      return null;
    }
    return new Long(intNum);
  }

  /**
   * Translate Long object to Integer object.
   * 
   * @return null if intNum is null Long if intNum is not null
   */
  public static Integer translateLongToInteger(Long longNum) {
    if (longNum == null) {
      return null;
    }
    return new Integer(longNum.toString());
  }

  /**
   * 将一个String转化为Long.
   * 
   * @return 不能正常转化则返回null.
   */
  public static Long translateStringToLong(String str) {
    if (str == null) {
      return null;
    }
    try {
      return Long.parseLong(str.trim());
    } catch (NumberFormatException nfe) {
      log.debug("failed to translate " + str + " to a Long", nfe);
      return null;
    }
  }

  /**
   * 将一个Object转化为Long.
   * 
   * @return 不能正常转化则返回null.
   */
  public static Long translateObjectToLong(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return Long.parseLong(object.toString());
    } catch (NumberFormatException nfe) {
      log.debug("failed to translate " + object + " to a Long", nfe);
      return null;
    }
  }

  /**
   * 将一个Object转化为Integer.
   * 
   * @return 不能正常转化则返回null.
   */
  public static Integer translateObjectToInteger(Object object) {
    if (object == null) {
      return null;
    }
    try {
      return Integer.parseInt(object.toString());
    } catch (NumberFormatException nfe) {
      log.debug("failed to translate " + object + " to a Integer", nfe);
      return null;
    }
  }

  /**
   * 生成目标长度的数字字符串.
   */
  public static String generateNumberStr(int length) {
    if (length <= 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    Random r = new Random(System.currentTimeMillis());
    for (int i = 0; i < length; i++) {
      sb.append(r.nextInt(10));
    }
    return sb.toString();
  }

  /**
   * 获取异常信息的第一行简单信息.
   *
   * @param detailMessage detailMessage
   * @return 返回信息
   */
  public static String getExceptionSimpleMessage(String detailMessage) {
    if (strIsNullOrEmpty(detailMessage)) {
      return "";
    }
    int firstIndex = detailMessage.indexOf('\r');
    int tfirstIndex = detailMessage.indexOf('\n');
    firstIndex =
        firstIndex < tfirstIndex ? (firstIndex > 0 ? firstIndex : tfirstIndex) : tfirstIndex;
    if (firstIndex < 0) {
      return detailMessage;
    }
    return detailMessage.substring(0, firstIndex);
  }

  /**
   * 兼容null字符串的trim处理
   *
   * @param orginlStr 原始字符串,可能为null
   * @return 去除两边空格之后的字符串. 如果orginlStr为null, 则返回null
   */
  public static String trimCompatibleNull(String orginlStr) {
    if (orginlStr == null) {
      return null;
    }
    return orginlStr.trim();
  }

  /**
   * Check whether the string is a regular phone number.
   *
   * @return true if the parameter phoneNumber is a regular phone number
   */
  public static boolean isPhoneNumber(String phoneNumber) {
    return PHONE_NUMBER.matcher(phoneNumber).matches();
  }

  /**
   * Check whether the string is a regular email.
   *
   * @return true if the email string is a regular email
   */
  public static boolean isEmailAddress(String email) {
    return EMAIL_NUMBER.matcher(email).matches();
  }

  /**
   * 判断一个URL是不是一个规范的地址.
   */
  public static boolean isValidUrl(String url) {
    if (strIsNullOrEmpty(url)) {
      return false;
    }
    try {
      new URL(url);
      return true;
    } catch (MalformedURLException ex) {
      log.debug(url + " is not a valid url tring", ex);
    }
    return false;
  }
  
  /**
   * 将一个字符串Md5.默认以UTF-8的格式处理.
   * @param str 字符串不能为空.
   * @throws UnsupportedEncodingException 指定编码格式不支持.
   */
  public static String md5(String str) throws UnsupportedEncodingException {
    return md5(str, "UTF-8");
  }
  
  /**
   * 将一个字符串Md5.
   * @param str 字符串不能为空.
   * @param charsetName 编码格式.
   * @throws UnsupportedEncodingException 指定编码格式不支持.
   */
  public static String md5(String str, String charsetName) throws UnsupportedEncodingException {
    Assert.notNull(str);
    Assert.notNull(charsetName);
    return new String(DigestUtils.md5Digest(str.getBytes(charsetName)), charsetName);
  }
}
