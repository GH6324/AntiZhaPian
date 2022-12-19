package com.demo.antizha

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.demo.antizha.interfaces.IApiResult
import com.demo.antizha.newwork.HookDns
import com.demo.antizha.newwork.RequestParamInterceptor
import com.demo.antizha.ui.HiCore
import com.google.gson.Gson
import okhttp3.*
import java.io.*
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object UnsafeOkHttpClient {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>,
                                        authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>,
                                        authType: String) {
        }

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
            return arrayOf()
        }
    })

    fun getBuilder(): OkHttpClient.Builder {
        //创建不安全的SSL，忽略证书错误，这样就可以用自己的服务器了。
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory

        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        val hostnameVerifier = HostnameVerifier { _, _ -> true }
        builder.hostnameVerifier(hostnameVerifier)
        return builder
    }

    fun getDataByGet(url: String, addHead: Boolean, iApiResult: IApiResult) {
        try {
            val builder = getBuilder()
            if (addHead)
                builder.addInterceptor(RequestParamInterceptor(true))
            //val client = builder.build()
            val client = builder.dns(HookDns()).build()
            val requestb = Request.Builder().get().url(url)
            val request = requestb.build()

            val call = client.newCall(request)
            //异步请求
            asynchronousCall(call, iApiResult)
        } catch (e: Exception) {
            //callBackFunc("")
        }
    }

    fun getDataByPost(url: String,
                      bodyMap: HashMap<String, String>?,
                      addHead: Boolean,
                      iApiResult: IApiResult) {
        try {
            val sc: SSLContext = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())

            val builder = getBuilder()
            if (addHead)
                builder.addInterceptor(RequestParamInterceptor(false))
            //val client = builder.build()
            val client = builder.dns(HookDns()).build()
            val json = if (bodyMap == null) "{}" else Gson().toJson(bodyMap)

            val requestb = Request.Builder()
                //.post(json.toRequestBody("application/json; charset=UTF-8".toMediaTypeOrNull()))
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .url(url)
            val request = requestb.build()
            val call = client.newCall(request)
            //异步请求
            asynchronousCall(call, iApiResult)
        } catch (e: Exception) {
            //callBackFunc("")
        }
    }

    fun getDataByPostSyn(url: String,
                         bodyMap: HashMap<String, String>?,
                         addHead: Boolean,
                         iApiResult: IApiResult) {
        try {
            val sc: SSLContext = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())

            val builder = getBuilder()
            if (addHead)
                builder.addInterceptor(RequestParamInterceptor(false))
            //val client = builder.build()
            val client = builder.dns(HookDns()).readTimeout(5, TimeUnit.SECONDS).build()
            val json = if (bodyMap == null) "{}" else Gson().toJson(bodyMap)

            val requestb = Request.Builder()
                //.post(json.toRequestBody("application/json; charset=UTF-8".toMediaTypeOrNull()))
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .url(url)
            val request = requestb.build()
            val call = client.newCall(request)
            //痛步请求
            try {
                val response = call.execute()
                val body = response.body()
                val data = body?.bytes()
                val headers = response.headers()
                callBack(data, headers, iApiResult)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            //callBackFunc("")
        }
    }

}

fun bodyIsGzip(array: ByteArray): Boolean {
    if (array == null)
        return false
    if (array.size < 6)
        return false
    if (array[1] != 31.toByte())
        return false
    if (array[2] != 139.toByte())
        return false
    if (array[3] != 8.toByte())
        return false
    return true
}

fun callBack(data: ByteArray?, headers: Headers, iApiResult: IApiResult) {
    var iszip = false
    val encoding = headers.get("Content-Encoding")
    if (encoding != null && TextUtils.equals("gzip", encoding))
        iszip = true
    if (iszip != bodyIsGzip(data!!)) {
        iApiResult.callBack("", headers)
        return
    }
    val bodyString: String?
    if (bodyIsGzip(data!!)) {
        var baos = ByteArrayOutputStream()
        val gzipIn = GZIPInputStream(ByteArrayInputStream(data))
        var readByte: Int
        while (gzipIn.read().also { readByte = it } != -1) baos.write(readByte)
        bodyString = baos.toString("UTF-8")
    } else {
        bodyString = data.toString(charset("UTF-8"))
    }
    iApiResult.callBack("" + bodyString, headers)
}

fun asynchronousCall(call: Call, iApiResult: IApiResult) {
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            iApiResult.callBack(" ", null)
        }

        @Throws(IOException::class)
        override fun onResponse(call: Call, response: Response) {
            //更新界面必须在UI线程里调用，所以需要用Handler
            var mHandler: Handler = Handler(Looper.getMainLooper())
            val mRunnable: Runnable = object : Runnable {
                val body = response.body()
                val data = body?.bytes()
                val headers = response.headers()
                override fun run() {
                    callBack(data, headers, iApiResult)
                }
            }
            mHandler.postDelayed(mRunnable, 0)
        }
    })
}

fun saveBuff2File(data: String, saveFile: String) {
    val path = HiCore.context.getExternalFilesDir(null)?.path
    val file = File(path, saveFile)
    val fileWriter = FileOutputStream(file, false)
    fileWriter.write(data.toByteArray(charset("UTF_8")))
    fileWriter.close()

}

fun loadBuff4File(readFile: String): String {
    val path = HiCore.context.getExternalFilesDir(null)?.path
    val file = File(path, readFile)
    //file.exists()总是返回false
    if (!file.canRead())
        return ""
    val inStream = FileInputStream(file)
    val inputReader = InputStreamReader(inStream, charset("UTF_8"))
    inputReader.encoding
    val buff = inputReader.readText()
    inStream.close()
    return buff
}