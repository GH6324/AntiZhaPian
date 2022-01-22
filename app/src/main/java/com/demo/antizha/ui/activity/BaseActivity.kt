package com.demo.antizha.ui.activity

import android.R
import android.app.Activity
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.demo.antizha.ui.Hicore
import com.demo.antizha.util.NotchUtils
import com.demo.antizha.util.SystemUtils.adjustFontScale
import qiu.niorgai.StatusBarCompat


abstract class BaseActivity : AppCompatActivity() {
    var mActivity: Activity? = null
    var typ_ME: Typeface? = null
    var haveLiuhai = false
    var liuhaiHeight = 0

    abstract fun initPage()

    open fun isDouble(): Boolean {
        return Hicore.app.isDouble()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mActivity = this
        this.typ_ME = Typeface.createFromAsset(getAssets(), "DIN-Medium.otf")
        adjustFontScale(this)
        setStatusBar();
        initPage()
        haveLiuhai = NotchUtils.isNotch(this);
        liuhaiHeight = NotchUtils.getNotchHeight(this);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setBlackStatusBar() {
        StatusBarCompat.setStatusBarColor((this as Activity),
            resources.getColor(R.color.black, null))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setWhiteStatusBar() {
        StatusBarCompat.setStatusBarColor((this as Activity),
            resources.getColor(R.color.white, null))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    protected fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= 23) {
            setWhiteStatusBar()
        } else {
            setBlackStatusBar()
        }
        StatusBarCompat.translucentStatusBar(this, true, true)
    }
}