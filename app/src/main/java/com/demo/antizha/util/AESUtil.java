package com.demo.antizha.util;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import com.demo.antizha.ui.Hicore;

/* compiled from: AESUtil.java */
/* renamed from: util.m0 */
/* loaded from: classes3.dex */
public class AESUtil {

    /* renamed from: a  reason: collision with root package name */
    private static Cipher cipher = null;

    /* renamed from: b  reason: collision with root package name */
    private static final String charSet = "UTF-8";

    /* renamed from: c  reason: collision with root package name */
    private static final String f14419c = "AES";

    /* renamed from: d  reason: collision with root package name */
    private static final String KEY_ALGORITHM = "SHA1PRNG";

    /* renamed from: e  reason: collision with root package name */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    /* renamed from: f  reason: collision with root package name */
    private static final Integer PRIVATE_KEY_SIZE_BIT = 128;

    /* renamed from: g  reason: collision with root package name */
    private static final Integer PRIVATE_KEY_SIZE_BYTE = 16;

    /* renamed from: h  reason: collision with root package name */
    private static byte[] f14424h;

    /* compiled from: AESUtil.java */
    /* renamed from: util.m0$a */
    /* loaded from: classes3.dex */
    public static class a extends Provider {
        public a() {
            super("Crypto", 1.0d, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG", "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }

    private static byte[] a(String str) throws Exception {
        byte[] bArr = f14424h;
        if (bArr == null || bArr.length != 32) {
            SharedPreferences sharedPreferences = Hicore.context.getSharedPreferences("crypto_info", 0);
            String string = sharedPreferences.getString("salt", "");
            if (!TextUtils.isEmpty(string)) {
                f14424h = e(string);
            }
            byte[] bArr2 = f14424h;
            if (bArr2 == null || bArr2.length != 32) {
                byte[] bArr3 = new byte[32];
                new SecureRandom().nextBytes(bArr3);
                sharedPreferences.edit().putString("salt", c(bArr3)).commit();
                f14424h = bArr3;
            }
        }
        return new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(new PBEKeySpec(str.toCharArray(), f14424h, 1000, 256)).getEncoded(), f14419c).getEncoded();
    }

    @SuppressLint("DeletedProvider")
    private static byte[] b(byte[] bArr) throws Exception {
        SecureRandom secureRandom;
        KeyGenerator instance = KeyGenerator.getInstance(f14419c);
        int i2 = Build.VERSION.SDK_INT;
        if (i2 > 23) {
            secureRandom = SecureRandom.getInstance(KEY_ALGORITHM, new a());
        } else if (i2 >= 17) {
            secureRandom = SecureRandom.getInstance(KEY_ALGORITHM, "Crypto");
        } else {
            secureRandom = SecureRandom.getInstance(KEY_ALGORITHM);
        }
        secureRandom.setSeed(bArr);
        instance.init(128, secureRandom);
        return instance.generateKey().getEncoded();
    }

    public static String c(byte[] bArr) {
        return a(bArr, (String) null);
    }

    /* renamed from: d */
    public static String getMd5String_16(String str) {
        return getMd5String(str).substring(8, 24);
    }

    public static byte[] e(String str) {
        return a(str, (String) null, (byte) 0);
    }

    /* renamed from: c */
    public static String getMd5String(String str) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            int length = digest.length;
            for (int i2 = 0; i2 < length; i2++) {
                stringBuffer.append(String.format("%02X", Integer.valueOf(digest[i2] & 255)));
            }
            return stringBuffer.toString();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return "";
        } catch (NoSuchAlgorithmException e3) {
            e3.printStackTrace();
            return "";
        }
    }

    /* renamed from: b */
    public static String cipherEncrypt(String str, String str2, String str3) throws Exception {
        Cipher instance = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        instance.init(1, new SecretKeySpec(str2.getBytes("UTF-8"), f14419c), new IvParameterSpec(str3.getBytes("UTF-8")));
        String encodeToString = Base64.encodeToString(instance.doFinal(str.getBytes("UTF-8")), 0);
        return encodeToString;
    }

    public static String a(byte[] bArr, String str) {
        if (bArr == null) {
            return null;
        }
        int length = bArr.length;
        if (length <= 0) {
            return "";
        }
        if (TextUtils.isEmpty(str)) {
            str = ",";
        }
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < length; i2++) {
            sb.append(String.valueOf((int) bArr[i2]));
            if (i2 != length - 1) {
                sb.append(str);
            }
        }
        return sb.toString();
    }

    /* renamed from: b */
    public static String encrypt(String str, String str2) {
        String d2 = getMd5String_16(str);
        if (d2.length() == PRIVATE_KEY_SIZE_BYTE.intValue()) {
            try {
                cipherInit(d2, 1);
                return ByteArray2HexStr(cipher.doFinal(str2.getBytes("UTF-8")));
            } catch (Exception e2) {
                throw new RuntimeException("AESUtil:encrypt fail!", e2);
            }
        } else {
            throw new RuntimeException("AESUtil:Invalid AES secretKey length (must be 16 bytes)");
        }
    }

    public static byte[] a(String str, String str2, byte b2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = ",";
        }
        String[] split = str.split(str2);
        int length = split.length;
        byte[] bArr = new byte[length];
        for (int i2 = 0; i2 < length; i2++) {
            try {
                bArr[i2] = Byte.parseByte(split[i2]);
            } catch (Exception unused) {
                bArr[i2] = b2;
            }
        }
        return bArr;
    }

    /* renamed from: b */
    private static byte[] HexString2ByteArray(String str) {
        byte[] bArr = new byte[str.length() / 2];
        int i2 = 0;
        while (i2 < str.length()) {
            int i3 = i2 + 2;
            bArr[i2 / 2] = (byte) Integer.parseInt(str.substring(i2, i3), 16);
            i2 = i3;
        }
        return bArr;
    }

    /* renamed from: a */
    public static String cipherDecrypt(String str, String str2, String str3) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes("UTF-8"), f14419c);
        Cipher instance = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        instance.init(2, secretKeySpec, new IvParameterSpec(str3.getBytes("UTF-8")));
        return new String(instance.doFinal(Base64.decode(str, 0)), "UTF-8");
    }

    /* renamed from: a */
    public static String decrypt(String str, String str2) {
        String md5_16 = getMd5String_16(str);
        if (md5_16.length() == PRIVATE_KEY_SIZE_BYTE.intValue()) {
            try {
                cipherInit(md5_16, 2);
                return new String(cipher.doFinal(HexString2ByteArray(str2)), "UTF-8");
            } catch (Exception e2) {
                throw new RuntimeException("AESUtil:decrypt fail!", e2);
            }
        } else {
            throw new RuntimeException("AESUtil:Invalid AES secretKey length (must be 16 bytes)");
        }
    }

    /* renamed from: a */
    public static String ByteArray2HexStr(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        int length = bArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            sb.append(String.format("%02X", Byte.valueOf(bArr[i2])));
        }
        return sb.toString();
    }

    /* renamed from: a */
    public static void cipherInit(String str, int i2) {
        try {
            SecureRandom instance = SecureRandom.getInstance(KEY_ALGORITHM);
            instance.setSeed(str.getBytes());
            KeyGenerator.getInstance(f14419c).init(PRIVATE_KEY_SIZE_BIT.intValue(), instance);
            SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), f14419c);
            cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(i2, secretKeySpec, new IvParameterSpec(getMd5String_16(str).getBytes()));
        } catch (Exception e2) {
            throw new RuntimeException("AESUtil:initParam fail!", e2);
        }
    }
}
