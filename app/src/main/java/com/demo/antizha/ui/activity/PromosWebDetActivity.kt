package com.demo.antizha.ui.activity

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import com.demo.antizha.OnWebListener
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivityPromWebDetBinding
import com.demo.antizha.ui.HiCore
import com.demo.antizha.ui.HiWebView
import com.demo.antizha.ui.SwipBackLayout
import com.demo.antizha.util.Parameters
import qiu.niorgai.StatusBarCompat
import java.util.concurrent.atomic.AtomicReference


class PromosWebDetActivity : BaseActivity() {
    private lateinit var promosWebDetBinding: ActivityPromWebDetBinding
    private lateinit var mHandler: Handler
    private lateinit var swipBackLayout: SwipBackLayout
    private var isVideo = false
    private var mArticleId: String = ""
    override fun initPage() {
        promosWebDetBinding = ActivityPromWebDetBinding.inflate(layoutInflater)
        setContentView(promosWebDetBinding.root)
        promosWebDetBinding.piTitle.ivBack.setOnClickListener {
            if (!HiCore.app.isDouble())
                finish()
        }
        promosWebDetBinding.piNetworkNo.llNetworkNo.setOnClickListener {
            if (!HiCore.app.isDouble()) {
                promosWebDetBinding.webview.reload()
                promosWebDetBinding.webview.visibility = View.VISIBLE
                promosWebDetBinding.piNetworkNo.llNetworkNo.visibility = View.GONE
            }
        }
        mHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(message: Message) {
                super.handleMessage(message)
                if (!isFinishing) {
                    when (message.what) {
                        6 -> {
                            promosWebDetBinding.piTitle.rlTitle.visibility = View.GONE
                            promosWebDetBinding.llToReport.visibility = View.GONE
                            if (isVideo) {
                                promosWebDetBinding.flTitWhite.visibility = View.GONE
                                promosWebDetBinding.tvHelp.visibility = View.VISIBLE
                                StatusBarCompat.translucentStatusBar(this@PromosWebDetActivity,
                                    true,
                                    false)
                                return
                            }
                            StatusBarCompat.translucentStatusBar(this@PromosWebDetActivity,
                                true,
                                false)

                        }
                        7 -> {
                            promosWebDetBinding.llToReport.visibility = View.VISIBLE
                            if (isVideo) {
                                promosWebDetBinding.piTitle.rlTitle.visibility = View.GONE
                                promosWebDetBinding.flTitWhite.visibility = View.VISIBLE
                                promosWebDetBinding.tvHelp.visibility = View.VISIBLE
                                StatusBarCompat.translucentStatusBar(this@PromosWebDetActivity,
                                    true,
                                    false)
                                return
                            }
                            promosWebDetBinding.piTitle.rlTitle.visibility = View.VISIBLE
                            StatusBarCompat.translucentStatusBar(this@PromosWebDetActivity,
                                true,
                                true)
                        }
                    }
                }
            }
        }
        loadWeb()
    }

    // androidx.activity.ComponentActivity, android.app.Activity
    override fun onBackPressed() {
        if (!promosWebDetBinding.webview.canGoBack()) {
            super.onBackPressed()
        }
        promosWebDetBinding.webview.goBack()
    }

    // ui.activity.BaseActivity
    private fun loadWeb() {
        swipBackLayout = SwipBackLayout.create(mActivity)
        swipBackLayout.init()
        promosWebDetBinding.webview.setSwipLayout(this, swipBackLayout)
        promosWebDetBinding.piTitle.ivRight.setBackgroundResource(R.drawable.iv_share_dot)
        mArticleId = intent.getStringExtra("extra_web_id").toString()
        val stringExtra = intent.getStringExtra("extra_web_url")
        val stringExtra2 = intent.getStringExtra("extra_web_title")
        promosWebDetBinding.piTitle.tvTitle.text = stringExtra2
        initWebView(promosWebDetBinding.webview)
        promosWebDetBinding.webview.loadUrl("$stringExtra#app=1")
        if (stringExtra!!.contains("shareVideo")) {
            isVideo = true
            promosWebDetBinding.piTitle.rlTitle.visibility = View.GONE
            promosWebDetBinding.flTitWhite.visibility = View.VISIBLE
            promosWebDetBinding.tvHelp.visibility = View.VISIBLE
            StatusBarCompat.translucentStatusBar(this as Activity, true, false)
            return
        }
        promosWebDetBinding.piTitle.rlTitle.visibility = View.VISIBLE
        promosWebDetBinding.flTitWhite.visibility = View.GONE
        StatusBarCompat.translucentStatusBar(this as Activity, true, true)
    }


    private fun initWebView(myWebView: HiWebView) {
        myWebView.setListener(this, PromosWebListener())
        myWebView.webChromeClient = PromosWebChromeClient()
        myWebView.webViewClient = PromosWebViewClient()
    }

    fun sendWebMsg(param: Parameters) {
        if (!param.isEmpty) {
            try {
                val isOnlyFullScreen: String = param.value("isOnlyFullScreen")
                val isFullScreen: String = param.value("isfullScreen")
                when {
                    TextUtils.equals("yes", isFullScreen) -> {
                        mHandler.sendEmptyMessage(6)
                    }
                    TextUtils.equals("no", isFullScreen) -> {
                        mHandler.sendEmptyMessage(7)
                    }
                    TextUtils.equals("yes", isOnlyFullScreen) -> {
                        mHandler.sendEmptyMessage(6)
                    }
                    TextUtils.equals("no", isOnlyFullScreen) -> {
                        mHandler.sendEmptyMessage(7)
                    }
                }
            } catch (unused: Exception) {
            }
        }
    }

    inner class PromosWebListener internal constructor() : OnWebListener {
        // interfaces.OnWebListener
        override fun shouldIntercept(aVar: Parameters?) {
            aVar?.let { sendWebMsg(it) }
        }

        // interfaces.OnWebListener
        override fun webJsFinish() {}
    }

    inner class PromosWebChromeClient internal constructor() : WebChromeClient() {
        // android.webkit.WebChromeClient
        override fun onProgressChanged(webView: WebView?, progress: Int) {
            val progressBar: ProgressBar = promosWebDetBinding.progressBar
            if (progress == 100) {
                progressBar.visibility = View.GONE
                return
            }
            progressBar.visibility = View.VISIBLE
            promosWebDetBinding.progressBar.progress = progress
        }

        // android.webkit.WebChromeClient
        override fun onReceivedTitle(webView: WebView?, str: String?) {
            super.onReceivedTitle(webView, str)
        }
    }

    inner class PromosWebViewClient internal constructor() : WebViewClient() {
        // android.webkit.WebViewClient
        override fun onPageFinished(webView: WebView?, str: String?) {
            super.onPageFinished(webView, str)
        }

        // android.webkit.WebViewClient
        override fun onReceivedError(
            webView: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(webView, request, error)
            promosWebDetBinding.piNetworkNo.llNetworkNo.visibility = View.VISIBLE
            promosWebDetBinding.webview.visibility = View.GONE
        }

        // android.webkit.WebViewClient
        override fun shouldOverrideUrlLoading(webView: WebView?,
                                              webResourceRequest: WebResourceRequest): Boolean {
            val atomicReference = AtomicReference<String>()
            atomicReference.set(webResourceRequest.url.path)
            return super.shouldOverrideUrlLoading(webView, webResourceRequest)
        }
    }
}