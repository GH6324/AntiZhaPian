package com.demo.antizha.util

import android.content.SharedPreferences
import android.text.TextUtils
import com.demo.antizha.ui.Hicore
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


object AESUtil {
    private var cipher: Cipher? = null
    private const val charSet = "UTF-8"
    private const val str_MD5 = "MD5"
    private const val str_AES = "AES"
    private const val KEY_ALGORITHM = "SHA1PRNG"
    private const val DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"
    private const val PRIVATE_KEY_SIZE_BIT = 128
    private const val PRIVATE_KEY_SIZE_BYTE = 16
    private var cryptoSalt: ByteArray? = null
    const val ACCEPT_TIME_SEPARATOR_SP = ","

    @Throws(Exception::class)
    private fun createSecretKey(str: String): ByteArray? {
        if (cryptoSalt == null || cryptoSalt!!.size != 32) {
            val sharedPreferences: SharedPreferences =
                Hicore.app.getSharedPreferences("crypto_info", 0)
            val string = sharedPreferences.getString("salt", "").toString()
            if (!TextUtils.isEmpty(string)) {
                cryptoSalt = splitString2ByteArray(string)
            }
            if (cryptoSalt == null || cryptoSalt!!.size != 32) {
                val bArr3 = ByteArray(32)
                SecureRandom().nextBytes(bArr3)
                sharedPreferences.edit().putString("salt", byteArray2SplitString(bArr3)).apply()
                cryptoSalt = bArr3
            }
        }
        return SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            .generateSecret(PBEKeySpec(str.toCharArray(), cryptoSalt, 1000, 256)).encoded,
            str_AES).encoded
    }

    fun splitString2ByteArray(str: String): ByteArray? {
        return splitString2ByteArray(str, "", 0)
    }

    fun splitString2ByteArray(str: String, separator: String, default: Byte): ByteArray? {
        var separate = separator
        if (TextUtils.isEmpty(str)) {
            return null
        }
        if (TextUtils.isEmpty(separator)) {
            separate = ACCEPT_TIME_SEPARATOR_SP
        }
        val split = str.split(separate.toRegex()).toTypedArray()
        val length = split.size
        val bArr = ByteArray(length)
        for (i2 in 0 until length) {
            try {
                bArr[i2] = split[i2].toByte()
            } catch (unused: Exception) {
                bArr[i2] = default
            }
        }
        return bArr
    }

    fun byteArray2SplitString(bArr: ByteArray?): String? {
        return byteArray2SplitString(bArr, null as String?)
    }

    fun byteArray2SplitString(bArr: ByteArray?, separator: String?): String? {
        var separate = separator
        if (bArr == null) {
            return null
        }
        val length = bArr.size
        if (length <= 0) {
            return ""
        }
        if (TextUtils.isEmpty(separate)) {
            separate = ACCEPT_TIME_SEPARATOR_SP
        }
        val sb = StringBuilder()
        for (i in 0 until length) {
            sb.append(bArr[i].toString())
            if (i != length - 1) {
                sb.append(separate)
            }
        }
        return sb.toString()
    }

    fun encrypt(seedkey: String, input: String): String {
        val seed = MD5Utils.getMd5Half(seedkey)
        return if (seed.length == PRIVATE_KEY_SIZE_BYTE) {
            try {
                cipherInit(seed, 1)
                byteArray2HexStr(cipher!!.doFinal(input.toByteArray(charset("UTF-8"))))
            } catch (err: Exception) {
                throw RuntimeException("AESUtil:encrypt fail!", err)
            }
        } else {
            throw RuntimeException("AESUtil:Invalid AES secretKey length (must be 16 bytes)")
        }
    }

    fun decrypt(seedkey: String, input: String): String {
        val seed = MD5Utils.getMd5Half(seedkey)
        return if (seed.length == PRIVATE_KEY_SIZE_BYTE) {
            try {
                cipherInit(seed, 2)
                String(cipher!!.doFinal(hexString2ByteArray(input)), charset("UTF-8"))
            } catch (e2: Exception) {
                throw RuntimeException("AESUtil:decrypt fail!", e2)
            }
        } else {
            throw RuntimeException("AESUtil:Invalid AES secretKey length (must be 16 bytes)")
        }
    }

    fun byteArray2HexStr(bArr: ByteArray): String {
        val sb = StringBuilder(bArr.size * 2)
        val length = bArr.size
        for (i in 0 until length) {
            sb.append(String.format("%02X", java.lang.Byte.valueOf(bArr[i])))
        }
        return sb.toString()
    }

    private fun hexString2ByteArray(str: String): ByteArray {
        val bArr = ByteArray(str.length / 2)
        var i2 = 0
        while (i2 < str.length) {
            val i3 = i2 + 2
            bArr[i2 / 2] = str.substring(i2, i3).toInt(16).toByte()
            i2 = i3
        }
        return bArr
    }

    fun cipherInit(seed: String, opmode: Int) {
        try {
            val instance = SecureRandom.getInstance(KEY_ALGORITHM)
            instance.setSeed(seed.toByteArray())
            KeyGenerator.getInstance(str_AES).init(PRIVATE_KEY_SIZE_BIT, instance)
            val secretKeySpec = SecretKeySpec(seed.toByteArray(), str_AES)
            cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM)
            cipher!!.init(opmode,
                secretKeySpec,
                IvParameterSpec(MD5Utils.getMd5Half(seed).toByteArray()))
        } catch (err: Exception) {
            throw RuntimeException("AESUtil:initParam fail!", err)
        }
    }

}