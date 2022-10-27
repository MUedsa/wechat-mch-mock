package com.muedsa.mock.wechat.mch.util;

import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AesUtil {
    public static final String ALGORITHM = "AEAD_AES_256_GCM";
    static final int TAG_LENGTH_BIT = 128;


    public static String encryptToBase64(String aesKey, String associatedData, String nonce, byte[] data)
            throws IllegalBlockSizeException, BadPaddingException {
        return encryptToBase64(aesKey, associatedData.getBytes(StandardCharsets.UTF_8), nonce.getBytes(StandardCharsets.UTF_8), data);
    }
    public static String encryptToBase64(String aesKey, String associatedData, String nonce, String text)
            throws IllegalBlockSizeException, BadPaddingException {
        return encryptToBase64(aesKey, associatedData.getBytes(StandardCharsets.UTF_8), nonce.getBytes(StandardCharsets.UTF_8), text.getBytes(StandardCharsets.UTF_8));
    }

    public static String encryptToBase64(String aesKey, byte[] associatedData, byte[] nonce, byte[] data)
            throws IllegalBlockSizeException, BadPaddingException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec key = new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData);
            return Base64Utils.encodeToString(cipher.doFinal(data));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String decryptFormBase64(String aesKey, String associatedData, String nonce, String base64Text)
            throws GeneralSecurityException {
        return decryptFormBase64(aesKey, associatedData.getBytes(StandardCharsets.UTF_8), nonce.getBytes(StandardCharsets.UTF_8), base64Text.getBytes(StandardCharsets.UTF_8));
    }

    public static String decryptFormBase64(String aesKey, byte[] associatedData, byte[] nonce, byte[] data)
            throws GeneralSecurityException {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec key = new SecretKeySpec(aesKey.getBytes(StandardCharsets.UTF_8), "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData);
            return new String(cipher.doFinal(Base64Utils.decode(data)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
