package com.amalitech.gpuconfigurator.service.encryption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class AesEncryptionService implements EncryptionService {

    @Value("${encryption-secret-key}")
    private String secretKey;
    private final static String algorithm = "AES/CBC/PKCS5PADDING";

    public static SecretKey getSecretKey(String secretKey) {
        byte[] SECRET_KEY_BYTES = secretKey.getBytes(UTF_8);
        return new SecretKeySpec(SECRET_KEY_BYTES, "AES");
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        return new IvParameterSpec(iv);
    }

    @Override
    public String encrypt(String word) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        SecretKey key = getSecretKey(secretKey);
        IvParameterSpec iv = generateIv();

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(word.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    @Override
    public String decrypt(String encryptedWord) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        SecretKey key = getSecretKey(secretKey);
        IvParameterSpec iv = generateIv();

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(encryptedWord));
        return new String(plainText);
    }

}
