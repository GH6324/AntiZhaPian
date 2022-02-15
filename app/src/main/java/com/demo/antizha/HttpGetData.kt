package com.demo.antizha

import android.os.Handler
import android.os.Looper
import com.demo.antizha.ui.HiCore
import com.demo.antizha.util.RequestParamInterceptor
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*


fun getDataByGet(url: String,
                 addHead: Boolean,
                 saveFile: String,
                 callBackFunc: (data: String, saveFile: String) -> Unit) {
    try {
        val builder = OkHttpClient.Builder()
        if (addHead)
            builder.addInterceptor(RequestParamInterceptor())
        val client = builder.build()
        val requestb = Request.Builder().get().url(url)
        val request = requestb.build()

        val call = client.newCall(request)
        //异步请求
        callBack(call, saveFile, callBackFunc)
    } catch (e: Exception) {
        //callBackFunc("")
    }
}

fun getDataByPost(url: String,
                  bodyMap: HashMap<String, String>?,
                  addHead: Boolean,
                  saveFile: String,
                  callBackFunc: (data: String, saveFile: String) -> Unit) {
    try {
        val builder = OkHttpClient.Builder()
        if (addHead)
            builder.addInterceptor(RequestParamInterceptor())
        val client = builder.build()
        val json = Gson().toJson(bodyMap)
        val requestb = Request.Builder()
            .post(json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()))
            .url(url)
        val request = requestb.build()
        val call = client.newCall(request)
        //异步请求
        callBack(call, saveFile, callBackFunc)
    } catch (e: Exception) {
        //callBackFunc("")
    }
}

fun callBack(call: Call, saveFile: String, callBackFunc: (data: String, saveFile: String) -> Unit) {
    call.enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            callBackFunc(" ", "")
        }

        @Throws(IOException::class)
        override fun onResponse(call: Call, response: Response) {
            //更新界面必须在UI线程里调用，所以需要用Handler
            Handler(Looper.getMainLooper()).postDelayed({
                callBackFunc("" + response.body?.string(), saveFile)
            }, 0)
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