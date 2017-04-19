package com.jasonfunderburker.couchpotato.entities;

import com.jasonfunderburker.couchpotato.entities.converters.TorrentTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.*;
import java.security.SecureRandom;
import java.util.Base64;

@Entity
@Table(name = "torrents_accounts")
public class TorrentUserInfo {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "type_id")
    @Convert(converter = TorrentTypeConverter.class)
    private TorrentType type;

    private String username;
    private String hash;
    private String clearHash;

    public TorrentUserInfo() {
        super();
    }

    public TorrentUserInfo(TorrentType type) {
        this.type = type;
    }

    public TorrentUserInfo(String userName, String password) {
        this.username = userName;
        setHash(password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TorrentType getType() {
        return type;
    }

    public void setType(TorrentType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
                "type="+ type + ',' +
                "username='" + username + '\'' +
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