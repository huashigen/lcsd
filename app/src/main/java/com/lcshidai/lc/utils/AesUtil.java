package com.lcshidai.lc.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * AES 加密解密
 *
 * @author ZhaoYe
 */
public class AesUtil {
    private Cipher cipher;
    private SecretKeySpec key;
    private AlgorithmParameterSpec spec;

    private static final String CODE = "UTF-8";
    private static final String SHA = "SHA-256";
    private static final String MODE = "AES/CBC/PKCS7Padding";
    /**
     * 加密方式
     */
    private static final String AES = "AES";
    /**
     * 16位加密秘钥
     */
    private static final String SEED_16_CHARACTER = "DUs38E+nPg#!1mwj*=@NCqi|yjnBl&oeiXS0Gf&RT#FHifCVFp";

    private static AesUtil instance;

    public static AesUtil getInstance() {
        if (instance == null) {
            try {
                instance = new AesUtil();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public AesUtil() throws Exception {
        // hash password with SHA-256 and crop the output to 128-bit for key
        MessageDigest digest = MessageDigest.getInstance(SHA);
        digest.update(SEED_16_CHARACTER.getBytes(CODE));
        byte[] keyBytes = new byte[32];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
        cipher = Cipher.getInstance(MODE);
//		key=setSecretKey(new String(keyBytes));
        key = new SecretKeySpec(keyBytes, AES);
        spec = getIV();
    }

    private AlgorithmParameterSpec getIV() {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec(iv);
        return ivParameterSpec;
    }

    /**
     * 加密
     *
     * @param plainText 加密串
     * @return
     * @throws Exception
     */

    public String encrypt(String plainText) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(CODE));
        String encryptedText = new String(Base64.encode(encrypted, Base64.DEFAULT), CODE);
        return StringUtils.ox2String("4dec") + encryptedText.trim() + StringUtils.ox2String("4dd9");
    }

    /**
     * 解密
     *
     * @param cryptedText 解密串
     * @return
     * @throws Exception
     */

    public String decrypt(String cryptedText) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] bytes = Base64.decode(cryptedText, Base64.DEFAULT);
        byte[] decrypted = cipher.doFinal(bytes);
        String decryptedText = new String(decrypted, CODE);
        return decryptedText.trim();
    }

    private static final String ALGORITHM = "AES";

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return byte数组
     */
    public static byte[] encrypt(String content, String password) {
        try {
            SecretKeySpec key = setSecretKey(password);
            Cipher cipher = Cipher.getInstance(ALGORITHM);// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            return cipher.doFinal(byteContent); // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return byte数组
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            SecretKeySpec key = setSecretKey(password);
            Cipher cipher = Cipher.getInstance(ALGORITHM);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            return cipher.doFinal(content); // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf buf
     * @return 16进制字符串
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr 16进制字符串
     * @return 二进制数组
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 获得Key
     *
     * @param password password
     * @return 根据password生成的key
     */
    public static SecretKeySpec setSecretKey(String password) throws Exception {
        SecretKeySpec key = null;
        KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(password.getBytes());
        kgen.init(128, sr);
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        key = new SecretKeySpec(enCodeFormat, ALGORITHM);

        return key;
    }

    /**
     * 获得Key
     *
     * @param password 二进制password
     * @return 根据password生成的key
     */
    public static SecretKeySpec setSecretKey(byte[] password) {
        SecretKeySpec key = null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
//            sr.setSeed(password);
            sr.setSeed(password);
            kgen.init(128, sr);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            key = new SecretKeySpec(enCodeFormat, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return key;
    }

}
