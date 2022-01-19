package com.demo.antizha.util

import android.view.WindowManager

import android.util.DisplayMetrics

import android.app.Activity
import android.content.res.Configuration


class SystemUtils {

    /* renamed from: a */
    fun adjustFontScale(activity: Activity) {
        val configuration: Configuration = activity.resources.configuration
        if (configuration.fontScale > 1.0f) {
            configuration.fontScale = 0.9f
            activity.baseContext.createConfigurationContext(configuration)
        }
    }
}