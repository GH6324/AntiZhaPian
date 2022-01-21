package com.demo.antizha

import android.os.Handler
import android.os.Looper
import com.demo.antizha.util.RequestParamInterceptor
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.HttpUrl
import okhttp3.Response
import java.io.IOException


fun getDataByGet(url: String, addHead:Boolean, callBackFunc: (data: String) -> Unit): Int {
    try {
        val builder = OkHttpClient.Builder()
        if (addHead)
            builder.addInterceptor(RequestParamInterceptor());
        val client = builder.build()
        var requestb = Request.Builder().get()
        requestb = requestb.url(url)
        var request = requestb.build()

        val call = client.newCall(request)
        //异步请求
        call.enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callBackFunc(" ")
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, response: Response) {
                //更新界面必须在UI线程里调用，所以需要用Handler
                Handler(Looper.getMainLooper()).postDelayed({
                    //notifyDataSetChanged必须在UI线程里调用，所以需要用Handler
                    callBackFunc("" + response.body?.string())
                }, 0)

            }
        })
    } catch (e: Exception) {
        //callBackFunc("")
    }
    return 1
}
