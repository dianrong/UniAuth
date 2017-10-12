package com.dianrong.common.uniauth.common.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.regex.Pattern;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import lombok.extern.slf4j.Slf4j;

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
   * 验证码用到的字符数组.
   */
  public static final char[] CAPTCHA_CHARACTERS = {'1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's',
      't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L',
      'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

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
   * 截取字符串,返回指定的前几个字符.
   *
   * @param str str
   * @param num 指定的前几个字
   * @return 截取处理过的字符串.
   */
  public static String subStrIfNeed(final String str, int num) {
    if (strIsNullOrEmpty(str)) {
      return str;
    }
    int len = str.length();
    if (len <= num) {
      return str;
    }
    return str.substring(0, num);
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
   * 生成目标长度的验证码字符串.
   */
  public static String generateCaptchaStr(int length) {
    if (length <= 0) {
      return StringUtils.EMPTY;
    }
    StringBuilder sb = new StringBuilder();
    Random r = new Random();
    int charNum = CAPTCHA_CHARACTERS.length;
    for (int i = 0; i < length; i++) {
      sb.append(CAPTCHA_CHARACTERS[r.nextInt(charNum)]);
    }
    return sb.toString();
  }

  /**
   * 生成目标长度的数字字符串.
   */
  public static String generateNumberStr(int length) {
    if (length <= 0) {
      return StringUtils.EMPTY;
    }
    StringBuilder sb = new StringBuilder();
    Random r = new Random();
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
   * 
   * @param str 字符串不能为空.
   * @throws UnsupportedEncodingException 指定编码格式不支持.
   */
  public static String md5(String str) {
    try {
      return md5(str, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      log.error("UTF-8 is not supported.", e);
      throw new UniauthCommonException("UTF-8 is not supported", e);
    }
  }

  /**
   * 将一个字符串Md5.
   * 
   * @param str 字符串不能为空.
   * @param charsetName 编码格式.
   * @throws UnsupportedEncodingException 指定编码格式不支持.
   */
  public static String md5(String str, String charsetName) throws UnsupportedEncodingException {
    Assert.notNull(str);
    Assert.notNull(charsetName);
    return DigestUtils.md5DigestAsHex(str.getBytes(charsetName));
  }
}
