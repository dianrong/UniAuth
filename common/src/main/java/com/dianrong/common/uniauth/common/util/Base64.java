package com.dianrong.common.uniauth.common.util;

import com.google.common.io.BaseEncoding;

/**
 * Created by Guoying on 2015/9/8.
 */
public class Base64 {

  public static String encode(byte[] bytes) {
    return bytes == null ? null : BaseEncoding.base64().encode(bytes);
  }

  public static byte[] decode(String chars) {
    return (chars == null || "".equals(chars.trim())) ? null : BaseEncoding.base64().decode(chars);
  }
}
