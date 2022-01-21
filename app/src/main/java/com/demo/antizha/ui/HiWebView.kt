package com.demo.antizha.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import com.beust.klaxon.Klaxon
import com.demo.antizha.OnWebListener
import com.demo.antizha.util.UrlUtils
import com.demo.antizha.*

fun getFixedContext(context: Context): Context {
    return if (Build.VERSION.SDK_INT >= 17) context.createConfigurationContext(Configuration()) else context
}

class HiWebView : WebView {
    private var mActivity: Activity? = null
    private lateinit var mJsObject: Any
    private var mOnWebListener: OnWebListener? = null

    constructor(context: Context) : super(getFixedContext(context)) {
        initWebView(this)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(
        getFixedContext(context),
        attributeSet
    ) {
        initWebView(this)
    }

    constructor(
        context: Context,
        attributeSet: AttributeSet,
        defStyle: Int
    ) : super(getFixedContext(context), attributeSet, defStyle) {
        initWebView(this)
    }

    fun setListener(activity: Activity?, onWebListener: OnWebListener?) {
        mActivity = activity!!
        mOnWebListener = onWebListener!!
    }

    fun setActivity(activity: Activity?) {
        mActivity = activity
        //this.mSwipBackLayout = swipBackLayout
    }

    @SuppressLint("JavascriptInterface")
    private fun initWebView(webView: WebView) {
        isClickable = true
        val settings = webView.settings
        settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JsObject(), "appjs")
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowFileAccess = false
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        settings.setSupportZoom(false)
        settings.displayZoomControls = false
        settings.builtInZoomControls = false
        settings.useWideViewPort = true
        settings.setSupportMultipleWindows(true)
        settings.setGeolocationEnabled(true)
        settings.loadWithOverviewMode = true
        settings.domStorageEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.databaseEnabled = true
        settings.blockNetworkImage = false
        if (Build.VERSION.SDK_INT >= 21) {
            settings.mixedContentMode = 0
        }
        settings.textZoom = 100
        settings.mediaPlaybackRequiresUserGesture = false

    }

    inner class JsObject {
        @JavascriptInterface
        fun getHCData(): String {
            return getH5Data()
        }

        @JavascriptInterface
        fun getPageParams(str: String?) {
            if (this@HiWebView.mActivity != null && TextUtils.equals("pageFinish=1", str) &&
                this@HiWebView.mActivity != null && this@HiWebView.mOnWebListener != null
            ) {
                this@HiWebView.mOnWebListener!!.webJsFinish()
            }
        }

        @JavascriptInterface
        fun sendWebMsg(str: String?) {
            if (this@HiWebView.mActivity != null && this@HiWebView.mOnWebListener != null &&
                str != null && !TextUtils.isEmpty(str)
            ) {
                this@HiWebView.mOnWebListener!!.shouldIntercept(UrlUtils.string2Param(str))
            }
        }
    }

    companion object {
        fun getH5Data(): String {
            val hashMap = HashMap<Any, Any>()
            hashMap["deviceid"] = UserInfoBean.imei
            hashMap["os-version"] = "0"
            hashMap["market"] = Hicore.app.getChannel()
            hashMap["app-version"] = "1.1.20"
            hashMap["app-version-code"] =
                "82" //hashMap.put("app-version-code", SystemUtils.m() + "");     public static int m() {return 82;}
            hashMap["haveLiuhai"] = "" + 0
            hashMap["userid"] = UserInfoBean.accountId
            hashMap["pcode"] = UserInfoBean.adcode
            hashMap["nodeId"] = UserInfoBean.adcode
            hashMap["nodeCode"] = UserInfoBean.adcode
            val regions = TextUtils.split(UserInfoBean.region, "\\.")
            if (regions.size == 3) {
                hashMap["pname"] = regions[2]
                hashMap["nodeName"] = regions[2]
                hashMap["nodeProvince"] = regions[0]
            }
            return Klaxon().toJsonString(hashMap)
        }
    }

}