package com.dianrong.common.uniauth.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Guoying on 2015/9/8.
 */
public class AuthUtils {

	/**./**.
	 * 日志对象
	 */
	private final static Logger logger = LoggerFactory.getLogger(AuthUtils.class);
	
    private static final String SALT_GENERATION_ALGORITHM = "SHA1PRNG";
    private static final String PASSWORD_DIGEST_ALGORITHM = "SHA";

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final String DIGITS = "0123456789";
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String SPECIALS = "!@#$%^&*()_+-=,.?/;:{}[]|`~<>'";

    public static byte[] createSalt() {
        byte[] salt = new byte[16];
        try {
            SecureRandom random = SecureRandom.getInstance(SALT_GENERATION_ALGORITHM);
            random.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static byte[] digest(String password, byte[] salt) {
        try {
            MessageDigest msgDigest = MessageDigest.getInstance(PASSWORD_DIGEST_ALGORITHM);
            if (salt != null && salt.length > 0) {
                msgDigest.update(salt);
            }

            byte[] digest = msgDigest.digest(password.getBytes());
            return digest;
        } catch (NoSuchAlgorithmException e) {
        	logger.warn("digest exception",e);
            return null;
        }
    }

    public static String randomPassword() {
        StringBuilder sb = new StringBuilder();
        sb.append(SPECIALS.charAt(RandomUtils.nextInt(0, SPECIALS.length()))); // 1
        sb.append(DIGITS.charAt(RandomUtils.nextInt(0, DIGITS.length())));     // 2
        sb.append(LETTERS.charAt(RandomUtils.nextInt(0, LETTERS.length())));    // 3
        sb.append(LETTERS.charAt(RandomUtils.nextInt(0, LETTERS.length())));    // 4
        sb.append(Character.toUpperCase(LETTERS.charAt(RandomUtils.nextInt(0, LETTERS.length()))));    // 5
        sb.append(LETTERS.charAt(RandomUtils.nextInt(0, LETTERS.length())));    // 6
        sb.append(LETTERS.charAt(RandomUtils.nextInt(0, LETTERS.length())));    // 7
        sb.append(Character.toUpperCase(LETTERS.charAt(RandomUtils.nextInt(0, LETTERS.length()))));    // 8
        sb.append(DIGITS.charAt(RandomUtils.nextInt(0, DIGITS.length())));     // 9
        sb.append(DIGITS.charAt(RandomUtils.nextInt(0, DIGITS.length())));     // 10
        return sb.toString();
    }

    public static boolean validatePasswordRule(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }

        if (password.length() < MIN_PASSWORD_LENGTH
            || password.length() > MAX_PASSWORD_LENGTH) {
            return false;
        }

        boolean specialFound = false;
        boolean digitFound = false;
        boolean lowerLetterFound = false;
        boolean upperLetterFound = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isDigit(c)) {
                digitFound = true;
            } else if (Character.isLetter(c)) {
                if (Character.isLowerCase(c)) {
                    lowerLetterFound = true;
                } else {
                    upperLetterFound = true;
                }
            } else if (SPECIALS.indexOf(c) != -1) {
                specialFound = true;
            }
        }

        return (specialFound && digitFound && lowerLetterFound && upperLetterFound);
    }
}
