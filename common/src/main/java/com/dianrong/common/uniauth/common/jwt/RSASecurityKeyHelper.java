package com.dianrong.common.uniauth.common.jwt;

import com.dianrong.common.uniauth.common.util.Assert;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.util.Base64Utils;

/**
 * RSA的秘钥处理的帮助类.
 * 
 * @author wanglin
 * 
 */
public final class RSASecurityKeyHelper {

  /**
   * 定义算法名称.
   */
  public static final String ALGORITHM = "RSA";
  public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

  /**
   * 根据传入的私钥字符串获取对应的私钥对象.
   * 
   * @param base64UrlEncodedPrivateKey Base64编码过的私钥字符串,不能为空.
   * @throws InvalidKeySpecException 传入的私钥字符串信息不规范.
   */
  public static RSAPrivateKey getPrivateKey(String base64UrlEncodedPrivateKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    Assert.notNull(base64UrlEncodedPrivateKey);
    PKCS8EncodedKeySpec privateKeySpec =
        new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(base64UrlEncodedPrivateKey));
    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
    return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
  }

  /**
   * 根据传入的公钥字符串获取对应的公钥对象.
   * 
   * @param base64UrlEncodedPublicKey Base64编码过的公钥字符串,不能为空.
   * @throws InvalidKeySpecException 传入的公钥字符串不规范.
   */
  public static RSAPublicKey getPublickKey(String base64UrlEncodedPublicKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    Assert.notNull(base64UrlEncodedPublicKey);
    X509EncodedKeySpec pubKeySpec =
        new X509EncodedKeySpec(Base64Utils.decodeFromString(base64UrlEncodedPublicKey));
    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
    return (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
  }
}
