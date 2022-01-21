package com.demo.antizha.util

import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


object MD5Utils {

    fun getMd5String_char_lowercase(str: String): String {
        return getMd5String_char(str).lowercase(Locale.getDefault())
    }

    fun getMd5String_char(str: String): String {
        return try {
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            val bArr = str.toByteArray()
            val digest: ByteArray = instance.digest(bArr)
            val stringBuffer = StringBuffer()
            for (i in 0 until digest.size) {
                stringBuffer.append(String.format("%02X",
                    Integer.valueOf(digest[i].toInt() and 255)))
            }
            stringBuffer.toString()
        } catch (e2: UnsupportedEncodingException) {
            e2.printStackTrace()
            ""
        } catch (e3: NoSuchAlgorithmException) {
            e3.printStackTrace()
            ""
        }
    }
    fun getMd5Half(str: String): String {
        return getMd5String_utf8(str).substring(8, 24)
    }

    fun getMd5String_utf8(str: String): String {
        return try {
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            val bArr = str.toByteArray(charset("UTF-8"))
            val digest: ByteArray = instance.digest(bArr)
            val stringBuffer = StringBuffer()
            for (i in 0 until digest.size) {
                stringBuffer.append(String.format("%02X",
                    Integer.valueOf(digest[i].toInt() and 255)))
            }
            stringBuffer.toString()
        } catch (e2: UnsupportedEncodingException) {
            e2.printStackTrace()
            ""
        } catch (e3: NoSuchAlgorithmException) {
            e3.printStackTrace()
            ""
        }
    }
}