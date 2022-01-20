package com.demo.antizha.util

import android.content.Context
import android.view.animation.LinearInterpolator

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView


object Utils {
    fun showAnimation(context: Context?, resId: Int, imageView: ImageView) {
        val loadAnimation: Animation = AnimationUtils.loadAnimation(context, resId)
        loadAnimation.interpolator = LinearInterpolator()
        imageView.startAnimation(loadAnimation)
    }

}