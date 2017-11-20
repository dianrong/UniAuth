package com.dianrong.common.uniauth.common.jwt;

/**
 * 备用秘钥对.
 */
public interface SpareSecurityKey {

  /**
   * 返回私钥.
   */
  String getPrivateKey();

  /**
   * 返回公钥.
   */
  String getPublicKey();
}
