package com.demo.antizha

import android.os.Handler
import android.os.Looper
import com.demo.antizha.ui.Hicore
import com.demo.antizha.util.RequestParamInterceptor
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.*


fun getDataByGet(url: String,
                 addHead: Boolean,
                 saveFile: String,
                 callBackFunc: (data: String, saveFile: String) -> Unit): Int {
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
                callBackFunc(" ", "")
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, response: Response) {
                //更新界面必须在UI线程里调用，所以需要用Handler
                Handler(Looper.getMainLooper()).postDelayed({
                    //notifyDataSetChanged必须在UI线程里调用，所以需要用Handler
                    callBackFunc("" + response.body?.string(), saveFile)
                }, 0)
            }
        })
    } catch (e: Exception) {
        //callBackFunc("")
    }
    return 1
}

fun saveBuff2File(data: String, saveFile: String) {
    val path = Hicore.context.getExternalFilesDir(null)?.getPath()
    val file = File(path, saveFile)
    val fileWriter = FileOutputStream(file, false)
    fileWriter.write(data.toByteArray(charset("UTF_8")))
    fileWriter.close()
}

fun loadBuff4File(readFile: String): String {
    val path = Hicore.context.getExternalFilesDir(null)?.getPath()
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