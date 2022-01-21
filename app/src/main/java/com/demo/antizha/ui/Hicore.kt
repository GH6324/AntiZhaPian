package com.demo.antizha.ui

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import java.lang.Exception
import java.util.*

class Hicore : Application() {
    companion object {
        lateinit var app: Hicore
        lateinit var context: Application
        var mLastClickTime: Long = 0
        fun getContext(): Context {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        app = this
    }

    fun isDouble(): Boolean {
        val timeInMillis: Long = Calendar.getInstance().timeInMillis
        val j2: Long = timeInMillis - mLastClickTime
        if (j2 in 0..800) {
            return true
        }
        mLastClickTime = timeInMillis
        return false
    }
    fun getChannel(): String {
        try {
            return app.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData.getString("CHANNEL").toString()
        } catch (e2: Exception) {
            e2.printStackTrace()
            return "oss"
        }
    }
}