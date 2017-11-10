package com.jasonfunderburker.couchpotato.entities.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Created on 12.06.2017
 *
 * @author ${author}
 */
public class CryptMaster {
    private static final Logger logger = LoggerFactory.getLogger(CryptMaster.class);
    private static final String ALGORITHM_PADDING = "AES/ECB/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static byte[] KEY;

    private CryptMaster(){}

    public static void setKey(String key) {
        if (KEY == null) {
            KEY = new byte[16];
            System.arraycopy(key.getBytes(), 0, KEY, 0, KEY.length);
        }
    }

    public static String encrypt(String plainText) {
        logger.debug("encrypt plainText: {}", plainText);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_PADDING);
            SecretKeySpec secretKey = new SecretKeySpec(getRawKey(), ALGORITHM);
            logger.debug("encrypt secretKey: {}", getRawKey());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF8"));
            return new String(Base64.getEncoder().encode(cipherText),"UTF-8");
        } catch (Exception e) {
            logger.error("errorStackTrace: {} ", e);
        }
        return null;
    }

    public static String decrypt(String encryptedText) {
        logger.debug("decrypt encryptedText: {}", encryptedText);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_PADDING);
            SecretKeySpec secretKey = new SecretKeySpec(getRawKey(), ALGORITHM);
            logger.debug("decrypt secretKey: {}", getRawKey());
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherText = Base64.getDecoder().decode(encryptedText.getBytes("UTF8"));
            return new String(cipher.doFinal(cipherText),"UTF-8");
        } catch (Exception e) {
            logger.error("errorStackTrace: {} ", e);
        }
        return null;
    }

    private static byte[] getRawKey() {
        return KEY;
    }
}