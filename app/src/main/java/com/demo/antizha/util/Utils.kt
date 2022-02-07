package com.demo.antizha.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.demo.antizha.ui.Hicore
import com.hjq.toast.ToastUtils


object Utils {
    fun showAnimation(context: Context?, resId: Int, imageView: ImageView) {
        val loadAnimation: Animation = AnimationUtils.loadAnimation(context, resId)
        loadAnimation.interpolator = LinearInterpolator()
        imageView.startAnimation(loadAnimation)
    }

    fun copyToClipboard(str: String?) {
        (Hicore.app.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText("data", str))
        ToastUtils.show("复制成功")
    }

}