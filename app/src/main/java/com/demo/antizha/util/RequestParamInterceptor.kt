package com.demo.antizha.util

import android.text.TextUtils
import com.demo.antizha.UserInfoBean
import com.demo.antizha.ui.Hicore
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*


class RequestParamInterceptor : Interceptor {
    private fun encodeHead(str: String): String? {
        return if (TextUtils.isEmpty(str)) {
            ""
        } else try {
            URLEncoder.encode(str, "UTF-8")
        } catch (err: UnsupportedEncodingException) {
            err.printStackTrace()
            ""
        }
    }

    private fun signStr(): String {
        val a2: String = MD5Utils.getMd5StringCharLowercase("hicore123")
        val a3: String = MD5Utils.getMd5StringCharLowercase(("android/api/file/upload$a2")
            .lowercase(Locale.getDefault()))
        return "android $a3"
    }

    private fun setHeader(builder: Request.Builder?) {
        if (builder != null) {
            builder.addHeader("deviceid", UserInfoBean.imei)
            builder.addHeader("os-version", SystemUtils.getOsVer())
            builder.addHeader("os-type", "0")
            builder.addHeader("os-brand", SystemUtils.getBrand())
            builder.addHeader("os-model", SystemUtils.getModel())
            builder.addHeader("market", Hicore.app.getChannel())
            builder.addHeader("app-version", UserInfoBean.version)
            builder.addHeader("imei", UserInfoBean.imei)
            builder.addHeader("app-version-code", UserInfoBean.innerVersion.toString() + "")
            builder.addHeader("api-version", "163")
            builder.addHeader("sign", signStr())
            //builder.addHeader("UM-deviceToken", "umengToken")//!!!!!
            builder.addHeader("nodeid", UserInfoBean.adcode)
            builder.addHeader("nodeCode", UserInfoBean.adcode)//RegionConfigHttp.getNodeRegionId())
            //http://api.map.baidu.com/geocoder/v2/?ak=2ae1130ce176b453fb29e59a69b18407&callback=renderOption&output=xml&address=北京市.北京市.东城区&city=北京市
            builder.addHeader("longitude",
                UserInfoBean.longitude)//RegionConfigHttp.getLongtude().toString() + "")  //经度
            builder.addHeader("latitude",
                UserInfoBean.latitude)//RegionConfigHttp.getLatitude().toString() + "")   //维度
            builder.addHeader("address", "")//encodeHead(RegionConfigHttp.getGPSAddress()))
            //token的格式类似如此，但是如果加上一个自己编的，那就会返回错误，如果不写，那某些安全级别低的就访问还能成功
            //val s = "Bearer 6v7eoQunKVCNJfY8K2BHVLRsPKVxrSL08l3i.H1ExaHIY0cvFNH1EQM2LiZY7bD9zIsgUwaSlgrSmbB2Hh8Y6vKiK3lS8fP40KLdJ3Weo0VenjprZiXsQsTIlJq4oRKAL8TsBBL4IgcE41pMHUX5JahP2QqGQjkwKankuZtqkpSGtn92Bt71GWaQL3jXwTxnukNDr4FLqAM6Z8OdtWAzznOSgegTF90yhrruEA5Yd8adfaUxWp2FoM96TqFe05LdV0AhSfRxu5KxW4DJMjuwMGcdzeWwSsmyPptpKe5VQVZvftLBkLPVN5xi4ciL47HudeksIPQuMTZYC0txMHLD2HLssS6pWPYvhZJ3c2O2avEsagmjT2g.H1q2zQUsbxW7oieMVAcCH63xEzkXVGvYkhjnrIJEhO5"
            if (!TextUtils.isEmpty(UserInfoBean.acctoken))
                builder.addHeader("Authorization", UserInfoBean.acctoken)
            builder.addHeader("policeToken", "")
        }
    }

    @Throws(IOException::class)  // okhttp3.Interceptor
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val newBuilder: Request.Builder = request.newBuilder()
        setHeader(newBuilder)
        if (request.body is FormBody) {
            val builder = FormBody.Builder()
            val formBody = request.body as FormBody
            for (i in 0 until formBody.size) {
                builder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i))
            }
            newBuilder.method(request.method, builder.build())
        }
        return chain.proceed(newBuilder.build())
    }

}