package com.demo.antizha.util;

import android.text.TextUtils;
import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: UrlAES.java */
/* renamed from: util.v1.b */
/* loaded from: classes3.dex */
public class UrlAES {

    /* renamed from: a  reason: collision with root package name */
    private static final String f14586a = "hicore2020051518";

    /* renamed from: b  reason: collision with root package name */
    private static final String f14587b = "AES/ECB/PKCS5Padding";

    /* renamed from: c  reason: collision with root package name */
    private static final String f14588c = "hicore2020051518";

    /* renamed from: d  reason: collision with root package name */
    private static final String f14589d = "UTF-8";

    /* renamed from: e  reason: collision with root package name */
    private static final String f14590e = "AES";

    public static String a(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return "";
            }
            return AESUtil.cipherDecrypt(str.replaceAll(" ", "+"), "hicore2020051518", "hicore2020051518");
        } catch (Exception unused) {
            return "";
        }
    }

    public static String b(String str) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec("hicore2020051518".getBytes("UTF-8"), f14590e);
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
            instance.init(2, secretKeySpec, new IvParameterSpec("hicore2020051518".getBytes()));
            return new String(instance.doFinal(Base64.decode(str, 0)));
        } catch (Exception unused) {
            return "";
        }
    }

    public static String c(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return "";
            }
            return AESUtil.cipherEncrypt(str.replaceAll(" ", "+"), "hicore2020051518", "hicore2020051518");
        } catch (Exception unused) {
            return "";
        }
    }
}
