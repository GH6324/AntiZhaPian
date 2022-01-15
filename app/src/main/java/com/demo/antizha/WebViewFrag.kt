package com.demo.antizha

import android.webkit.JavascriptInterface
import android.text.TextUtils
import com.demo.antizha.util.UrlUtils
import android.app.Activity
import com.demo.antizha.ui.HiWebView



class WebViewFrag {
    companion object {
        var mActivity: Activity? = null
        var mListener: OnWebListener? = null
    }
    class JsObject {
        @JavascriptInterface
        fun getHCData():String
        {
            return HiWebView.getH5Data()
        }
        @JavascriptInterface
        fun getPageParams(str: String?) {
            if (mActivity != null && TextUtils.equals("pageFinish=1",str) &&
                mActivity != null && mListener != null) {
                mListener!!.webJsFinish()
            }
        }

        @JavascriptInterface
        fun sendWebMsg(str: String?) {
            if (mActivity != null && mListener != null && !TextUtils.isEmpty(str)) {
                mListener!!.shouldIntercept(UrlUtils.string2Param(str))
            }
        }
    }

    /* renamed from: a */
    fun init(activity: Activity?, onWebListener: OnWebListener?) {
        mActivity = activity
        mListener = onWebListener
    }

}