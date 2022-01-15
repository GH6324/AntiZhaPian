package com.demo.antizha.ui

import android.app.Application
import android.content.Context
import java.util.Calendar

class Hicore : Application() {
    companion object {
        lateinit var app: Hicore
        lateinit var context: Application
        var mLastClickTime: Long = 0
        fun getContext(): Context {
            return context!!
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
}