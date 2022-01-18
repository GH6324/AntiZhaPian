package com.demo.antizha.util

import android.text.TextUtils
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object UrlAES {
    private const val charSet = "UTF-8"
    private const val str_AES = "AES"
    private const val DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"

    @Throws(java.lang.Exception::class)
    fun cipherDecrypt(str: String?, keySalt: String, iv: String): String {
        val secretKeySpec = SecretKeySpec(keySalt.toByteArray(charset(charSet)), str_AES)
        val instance = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM)
        instance.init(
            Cipher.DECRYPT_MODE,
            secretKeySpec,
            IvParameterSpec(iv.toByteArray(charset(charSet)))
        )
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