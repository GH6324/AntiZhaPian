package com.demo.antizha

import android.webkit.JavascriptInterface
import android.text.TextUtils
import com.demo.antizha.util.UrlUtils
import android.app.Activity
import android.accounts.AccountManager
import com.beust.klaxon.Klaxon
import com.demo.antizha.ui.Hicore


fun getH5Data(): String {
    val hashMap = HashMap<Any, Any>()
    hashMap["deviceid"] = userInfoBean.imei
    hashMap["os-version"] = "0"
    hashMap["market"] = "oss"
    hashMap["app-version"] = "1.1.20"
    hashMap["app-version-code"] = "82" //hashMap.put("app-version-code", SystemUtils.m() + "");     public static int m() {return 82;}
    hashMap["haveLiuhai"] = "" + 0
    hashMap["userid"] = userInfoBean.accountId
    hashMap["pcode"] = userInfoBean.adcode
    hashMap["nodeId"] = userInfoBean.adcode
    hashMap["nodeCode"] = userInfoBean.adcode
    val regions = TextUtils.split(userInfoBean.region, "\\.")
    if (regions.size == 3){
        hashMap["pname"] = regions[2]
        hashMap["nodeName"] = regions[2]
        hashMap["nodeProvince"] = regions[0]
    }
    return Klaxon().toJsonString(hashMap)
}

class WebViewFrag {
    class JsObject {
        @JavascriptInterface
        fun getHCData():String
        {
            return getH5Data()
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

    companion object {
        /* renamed from: a */
        private var mActivity: Activity? = null

        /* renamed from: b */
        private var mListener: OnWebListener? = null
    }
}