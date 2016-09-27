package com.jasonfunderburker.couchpotato.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class TorrentUserInfo {
    private Long torrentTypeId;
    private String userName;
    private String hash;
    private String clearHash;

    public Long getTorrentTypeId() {
        return torrentTypeId;
    }

    public void setTorrentTypeId(Long torrentTypeId) {
        this.torrentTypeId = torrentTypeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHash() {
        return hash;
    }

    public String getPassword() {
        return CryptMaster.decrypt(hash);
    }

    public void setHash(String password) {
        this.hash = CryptMaster.encrypt(password);
    }

    public void setClearHash(String clearHash) {
        this.hash = clearHash;
    }

    @Override
    public String toString() {
        return "TorrentUserInfo{" +
                "torrentTypeId="+ torrentTypeId + ',' +
                "userName='" + userName + '\'' +
                '}';
    }
}

class CryptMaster {
    private static final Logger logger = LoggerFactory.getLogger(CryptMaster.class);
    private static final String ALGORITHM_PADDING = "AES/ECB/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static byte[] KEY;


    static String encrypt(String plainText) {
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

    static String decrypt(String encryptedText) {
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
        if (KEY == null) {
            SecureRandom random = new SecureRandom();
            random.setSeed(System.currentTimeMillis());
            KEY = new byte[16];
            random.nextBytes(KEY);
        }
        return KEY;
    }
}