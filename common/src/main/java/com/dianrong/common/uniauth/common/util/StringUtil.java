package com.dianrong.common.uniauth.common.util;

import java.util.Random;

import lombok.extern.slf4j.Slf4j;

/**
 * . some functions process String
 * 
 * @author R9GBP97
 */
@Slf4j
public class StringUtil {
    /**
     * . judge str is null or empty
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
     * . 获取对象的字符串表示方式
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
     * . 获取对象的字符串表示方式
     * 
     * @param obj 对象
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
     * Translate Integer object to Long object
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
     * Translate Long object to Integer object
     * 
     * @param intNum Integer
     * @return null if intNum is null Long if intNum is not null
     */
    public static Integer translateLongToInteger(Long longNum) {
        if (longNum == null) {
            return null;
        }
        return new Integer(longNum.toString());
    }

    /**
     * . 生成目标长度的数字字符串
     * 
     * @param length
     * @return
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
     * . 获取异常信息的第一行简单信息
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
        firstIndex = firstIndex < tfirstIndex ? (firstIndex > 0 ? firstIndex : tfirstIndex) : tfirstIndex;
        if (firstIndex < 0) {
            return detailMessage;
        }
        return detailMessage.substring(0, firstIndex);
    }

    /**
     * verify phone number
     * 
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        if (strIsNullOrEmpty(phoneNumber)) {
            return false;
        }
        String regExp = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147))\\d{8}$";
        return phoneNumber.matches(regExp);
    }

    /**
     * verify phone number
     * 
     * @param phoneNumber
     * @return
     */
    public static boolean isEmailAddress(String emailAddress) {
        if (strIsNullOrEmpty(emailAddress)) {
            return false;
        }
        String regExp = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return emailAddress.matches(regExp);
    }


}
