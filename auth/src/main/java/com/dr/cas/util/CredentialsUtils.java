package com.dr.cas.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.bouncycastle.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CredentialsUtils {

  private final static Logger logger = LoggerFactory.getLogger(CredentialsUtils.class);

  private static final String SALT_GENERATION_ALGORITHM = "SHA1PRNG";
  private static final String PASSWORD_DIGEST_ALGORITHM = "SHA";

  public static String createSalt() {
    byte[] salt = new byte[16];
    try {
      SecureRandom random = SecureRandom.getInstance(SALT_GENERATION_ALGORITHM);
      random.nextBytes(salt);
    } catch (NoSuchAlgorithmException e) {
      logger.warn("Specified algorithm " + SALT_GENERATION_ALGORITHM + " is not supported", e);
      return null;
    }
    return byteArrayToHexString(salt);
  }
  
  public static byte[] createSaltByte() {
	    byte[] salt = new byte[16];
	    try {
	      SecureRandom random = SecureRandom.getInstance(SALT_GENERATION_ALGORITHM);
	      random.nextBytes(salt);
	    } catch (NoSuchAlgorithmException e) {
	      logger.warn("Specified algorithm " + SALT_GENERATION_ALGORITHM + " is not supported", e);
	      return null;
	    }
	    return salt;
	  }
  
  public static String digest(String password, String hexSalt) {
    byte[] salt = hexStringToByteArray(hexSalt);
    try {
      MessageDigest msgDigest = MessageDigest.getInstance(PASSWORD_DIGEST_ALGORITHM);
      if (salt != null && salt.length > 0) {
        msgDigest.update(salt);
      }

      byte[] digest = msgDigest.digest(password.getBytes());
      return byteArrayToHexString(digest);
    } catch (NoSuchAlgorithmException e) {
      logger.error("Specified algorithm " + PASSWORD_DIGEST_ALGORITHM + " is not supported", e);
    }
    return null;
  }
  
  public static byte[] digest(String password, byte[] salt) {
	    try {
	      MessageDigest msgDigest = MessageDigest.getInstance(PASSWORD_DIGEST_ALGORITHM);
	      if (salt != null && salt.length > 0) {
	        msgDigest.update(salt);
	      }

	      return msgDigest.digest(password.getBytes());
	    } catch (NoSuchAlgorithmException e) {
	      logger.error("Specified algorithm " + PASSWORD_DIGEST_ALGORITHM + " is not supported", e);
	    }
	    return null;
	  }

  public static String byteArrayToHexString(byte[] b) {
    String result = "";
    for (int i = 0; i < b.length; i++) {
      result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
  }

  public static byte[] hexStringToByteArray(String data) {
    if (data == null)
      return null;
    byte[] bytes = new byte[data.length() / 2];
    for (int i = 0; i < bytes.length; i++) {
      try {
        bytes[i] = (byte) (0xff & Integer.parseInt(data.substring(i * 2, i * 2 + 2), 16));
      } catch (Exception e) {
        logger.error("invalid hexstring " + data);
        return null;
      }
    }
    return bytes;
  }
  
  public static boolean passwordMatching(String password, byte[] salt, byte[] credential) {
	  byte[] pwdCredential = digest(password, salt);
	  return Arrays.areEqual(pwdCredential, credential);
  }
}
