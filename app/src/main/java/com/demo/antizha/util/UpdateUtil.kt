package com.demo.antizha.util

import com.demo.antizha.UserInfoBean
import com.demo.antizha.getDataByPost
import com.google.gson.Gson
import java.util.*

object UpdateUtil {
    class DownloadInfo {
        var url: String? = null
        var isIsUpdatable = false
        var fileMD5: String? = null
        var version: String? = null
        var innerVersion = 0
        var content: String? = null
        var isForcedVersion = false
    }

    class DownloadInfoPackage(val data: DownloadInfo, val code: Int)

    fun <T> buildParam(timeStamp: String, t: T): HashMap<String, String> {
        var sign = ""
        var jsonEncrypt = ""

        val hashMap: HashMap<String, String> = HashMap()
        try {
            val json = ResponseDataTypeAdaptor.buildGson().toJson(t)
            sign = MD5Utils.getMd5StringUtf8(json).lowercase(Locale.getDefault())
            val signHash = MD5Utils.getMd5StringUtf8(sign).lowercase(Locale.getDefault())
            val timeHash = MD5Utils.getMd5Half(timeStamp + "").lowercase(Locale.getDefault())
            jsonEncrypt = try {
                AESUtil.cipherEncrypt(json, signHash, timeHash)
            } catch (e2: Exception) {
                ""
            }
        } catch (e3: Exception) {
            e3.printStackTrace()
        }
        //理论上好像应该是没变化的，加密出来的结果是BASE64处理过的
        //根据标准每76个字符会加一个回车，后加的，去掉后不影响解密
        val data = jsonEncrypt.replace("[\\s*\t\n\r]".toRegex(), "")
        hashMap["timestamp"] = timeStamp
        hashMap["data"] = data
        hashMap["sign"] = sign
        return hashMap
        //接收到后，先根据sign计算出signHash，根据timestamp计算出timeHash，然后把data解密出json
    }

    fun checkVer() {
        val registerBody = RegisterBody()
        registerBody.imei = UserInfoBean.imei
        registerBody.innerversion = UserInfoBean.innerVersion.toString()
        val str = System.currentTimeMillis().toString() + ""
        val hashMap = buildParam(str, registerBody)
        getDataByPost(
            "https://fzapp.gjfzpt.cn/hicore/api/AppVersion/checkv2",
            bodyMap = hashMap,
            addHead = true,
            "",
            callBackFunc = ::onGetVersion
        )
    }

    /*新颁布返回类似信息，无法获得最新版本号*/
    /*{"code":2,"msg":"当前版本过低，请前往应用市场下载最新版本"}*/
    @Suppress("UNUSED_PARAMETER")
    fun onGetVersion(data: String, saveFile: String) {
        if (data[0] != '{')
            return
        val hashMap = Gson().fromJson(data, HashMap::class.java)
        if (!hashMap.containsKey("sign") || !hashMap.containsKey("timestamp") ||
            !hashMap.containsKey("sData")
        )
            return
        if (hashMap["code"].toString() == "2")
            return
        val signHash =
            MD5Utils.getMd5StringUtf8(hashMap["sign"].toString()).lowercase(Locale.getDefault())
        val timeHash =
            MD5Utils.getMd5Half(hashMap["timestamp"].toString()).lowercase(Locale.getDefault())

        val json = AESUtil.cipherDecrypt(hashMap["sData"].toString(), signHash, timeHash)
        val ver = Gson().fromJson(json, DownloadInfoPackage::class.java)
        if (ver.code == 0)
            UserInfoBean.setVer(ver.data.version.toString(), ver.data.innerVersion)
    }
}