package com.demo.antizha.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import androidx.annotation.RequiresApi
import java.lang.Exception


class BaseDialog : Dialog {
    companion object {
        const val BOTTOM = 0
        const val CENTER = 2
        const val TOP = 1
        const val WARPHEIGHT = -2.0f
        const val WARPWIDTH = -2.0f
    }

    private var mContext: Context
    private var display: Display? = null
    private var dm: DisplayMetrics? = null
    private var lp: WindowManager.LayoutParams? = null
    private var mWindow: Window? = null
    var widthDialog = 0.0F
    var heightDialog = 0.0F
    var heightDialogdp = 0.0F
    var widthDialogdp = 0.0F
    var mGravityLayout:Int = 0

    @RequiresApi(Build.VERSION_CODES.R)
    constructor(context: Context) : super(context) {
        mContext = context
        initWindowState()
    }
    @RequiresApi(Build.VERSION_CODES.R)
    constructor(context: Context, i2: Int) : super(context, i2) {
        mContext = context
        initWindowState()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun initWindowState() {
        mWindow = window
        lp = mWindow!!.attributes
        val windowManager = (mContext as Activity).windowManager
        display =  context.getDisplay()
        dm = DisplayMetrics()
        dm  = mContext.resources.getDisplayMetrics()
    }

    fun dp2px(f2: Float): Int {
        return (f2 * mContext.resources.displayMetrics.density + 0.5f).toInt()
    }

    val statusBarHeight: Int
        get() = try {
            val cls = Class.forName("com.android.internal.R\$dimen")
            context.resources.getDimensionPixelSize(cls.getField("status_bar_height")[cls.newInstance()].toString()
                .toInt())
        } catch (e2: Exception) {
            e2.printStackTrace()
            0
        }

    fun <T : View?> getViewById(i2: Int): T {
        return findViewById<View>(i2) as T
    }

    fun initOnCreate() {
        val layoutParams = lp
        layoutParams!!.gravity = mGravityLayout
        if (widthDialog > 0.0) {
            layoutParams.width = (display!!.width.toDouble() * widthDialog).toInt()
        } else {
            val f2 = widthDialogdp
            if (f2 > 0.0f) {
                layoutParams.width = dp2px(f2)
            } else if (f2 == -2.0f) {
                layoutParams.width = -2
            } else {
                layoutParams.width = display!!.width
            }
        }
        if (heightDialog > 0.0) {
            lp!!.height = (display!!.height.toDouble() * heightDialog).toInt()
        } else {
            val f3 = heightDialogdp
            if (f3 > 0.0f) {
                lp!!.height = dp2px(f3)
            } else if (f3 == -2.0f) {
                lp!!.height = -2
            } else {
                lp!!.height = display!!.height - statusBarHeight
            }
        }
        mWindow!!.attributes = lp
    }

    fun setGravityLayout(i2: Int) {
        if (i2 == 0) {
            mGravityLayout = 80
        }
        if (1 == i2) {
            mGravityLayout = 48
        }
        if (2 == i2) {
            mGravityLayout = 17
        }
    }
 }