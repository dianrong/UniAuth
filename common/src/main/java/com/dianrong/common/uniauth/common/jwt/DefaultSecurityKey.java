package com.dianrong.common.uniauth.common.jwt;

/**
 * 提供默认的秘钥对.
 */
public class DefaultSecurityKey implements SpareSecurityKey {

  /**
   * 提供两个默认的秘钥对.生产环境情况下, 需要自行生成配置.
   */
  private final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMeWPDBubRmku/UIDlK5619xKwNk7DAySk5zprQADVhggNxAMW6bp2yBew9cZAmoXlcxPrU5/bgrv4iGvcl3SmKgD2kGDrPsB+aTJBJCsKff3/Ng2i0TupbTKAsX9GZgwm3uY/gjOZteACB+3+iG7JNp17PUoVSk9jIK9sXKjncFAgMBAAECgYEAgrV5JZ6sBKdGEOr9tl/cLd1HdimaJNZKo6ZJ/ogqNT6+JYBE31NBCmah2SWAvnJtllzB3J3l879y6E6V7GMbauMK7FGJ++73QC2C23peYDuiGjxZ9B2kcpk9FHY13Tz5G1sk6fc7QAW1J/2wlT9A4HR/h3V6gudy471LmTL+OeECQQDw+PavyjA9+I0bJTCnY4xf14suSwVSTdwY1F1WcoOCyAqer4yZMCDA+DpVvhBbMSgCeGhHimq6StihJ2NX1LBvAkEA1AiQnEczmwy67O5dix07aR1qQi9uzuChvPQFP6gJ+lTnjn+RNe3a/tG05mu9c1F9gQs+EK4sX9Oy1VHZ1/vhywJAffd3ZS+Jhmuuv7jyNR553AQd8qYu8vZWjvg8jpswGiaePpPAKLga9yULwZSokjMhGiF9SaCW6CmGEsA3U0uQqwJAHSP8M+45MnMCcbOk2Yyv8JdhVi1MrlQDBoQHAKOok2X92QDBRUcmVOUfVpeyFto5RHX2FrbJVHY02sqABQYSFwJAfjX1/iNNBIuMqWKRtpQV/D6u4tIUd/uKx1HHNl+/qbbj9rEu3HeZDMGlD5SOA9Y1YQaALW39XdTZexpZhuccrg==";
  private final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDHljwwbm0ZpLv1CA5SuetfcSsDZOwwMkpOc6a0AA1YYIDcQDFum6dsgXsPXGQJqF5XMT61Of24K7+Ihr3Jd0pioA9pBg6z7AfmkyQSQrCn39/zYNotE7qW0ygLF/RmYMJt7mP4IzmbXgAgft/ohuyTadez1KFUpPYyCvbFyo53BQIDAQAB";

  public String getPrivateKey() {
    return privateKey;
  }

  public String getPublicKey() {
    return publicKey;
  }
}
