package com.amalitech.gpuconfigurator.service;

import com.amalitech.gpuconfigurator.service.encryption.AesEncryptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class EncryptionServiceTest {

    private final static String secretKey = "2b7e151628aed2a6abf7158809cf4f3c";
    private final static String algorithm = "AES/CBC/PKCS5PADDING";
    private AesEncryptionService aesEncryptionService;

    @BeforeEach
    public void setUp() {
        aesEncryptionService = new AesEncryptionService();
    }

    @Test
    public void encrypt_shouldReturnEncryptedWord() throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String plainText = "test";

        String cipherText = aesEncryptionService.encrypt(plainText);
        String decryptedWord = aesEncryptionService.decrypt(cipherText);

        Assertions.assertEquals(plainText, decryptedWord);

    }
}
