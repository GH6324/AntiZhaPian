package com.demo.antizha.util

import android.text.TextUtils
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/* compiled from: UrlAES.java */ /* renamed from: util.v1.b */ /* loaded from: classes3.dex */
object UrlAES {
    private const val charSet = "UTF-8"
    private const val str_AES = "AES"
    private const val DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"

    @Throws(java.lang.Exception::class)
    fun cipherDecrypt(str: String?, str2: String, str3: String): String {
        val secretKeySpec = SecretKeySpec(str2.toByteArray(charset(charSet)), str_AES)
        val instance = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM)
        instance.init(2, secretKeySpec, IvParameterSpec(str3.toByteArray(charset(charSet))))
        return String(instance.doFinal(Base64.decode(str, 0)), charset(charSet))
    }

    fun urlDecrypt(str: String): String {
        try {
            if (TextUtils.isEmpty(str)) {
                return ""
            } else
                return cipherDecrypt(
                    str.replace(" ".toRegex(), "+"), "hicore2020051518", "hicore2020051518"
                )
        } catch (unused: Exception) {
            return ""
        }
    }
}