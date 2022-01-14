package com.demo.antizha.ui.web

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.demo.antizha.R
import com.demo.antizha.userInfoBean
import com.just.agentweb.AgentWeb
import android.webkit.WebView
import android.graphics.Bitmap
import android.widget.*
import com.demo.antizha.WebViewFrag
import com.just.agentweb.AbsAgentWebSettings
import com.just.agentweb.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import android.app.Activity
import com.demo.antizha.OnWebListener
import com.demo.antizha.util.Parameters
import com.just.agentweb.WebChromeClient
import android.net.ConnectivityManager
import com.demo.antizha.ui.Hicore
import android.net.Network
import android.net.NetworkCapabilities
import androidx.annotation.RequiresApi
import com.demo.antizha.util.AESUtil.a

import com.demo.antizha.util.AESUtil.c

import android.content.Intent
import android.os.*
import com.demo.antizha.util.UrlAES
import org.greenrobot.eventbus.EventBus
import java.lang.Exception
import java.net.URLDecoder


class WebFragment : Fragment() {
    private lateinit var mActivity: Activity
    private lateinit var dashboardViewModel: WebViewModel
    private lateinit var agentWeb: AgentWeb
    private lateinit var webView: WebView
    private lateinit var webViewFrag: WebViewFrag
    private lateinit var root: View
    private lateinit var mVirtualWeb: LinearLayout
    private lateinit var mllWebContainer: LinearLayout
    private lateinit var mIvRight: ImageView
    private lateinit var mLinearLayout: LinearLayout
    private lateinit var mLlNetworkNo: View
    private lateinit var mNetTips: TextView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mRlTitle: RelativeLayout
    private lateinit var mIvBack: ImageView
    private lateinit var mTvTitle: TextView
    private lateinit var mWebChromeClient: WebChromeClient
    private lateinit var mWebViewClient: WebViewClient
    private lateinit var mOnWebListener: OnWebListener
    private lateinit var mHandler: Handler
    private var IsInitWeb:Boolean = false
    private var id:String = ""
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        dashboardViewModel = ViewModelProvider(this).get(WebViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_web, container, false)
        mVirtualWeb = root.findViewById<LinearLayout>(R.id.ll_virtual_web)
        mllWebContainer = root.findViewById<LinearLayout>(R.id.ll_web_container)
        mIvRight = root.findViewById<ImageView>(R.id.iv_right)
        mLinearLayout = root.findViewById<LinearLayout>(R.id.web_container)
        mLlNetworkNo = root.findViewById<View>(R.id.ll_network_no)
        mNetTips = root.findViewById<TextView>(R.id.net_tips)
        mProgressBar = root.findViewById<ProgressBar>(R.id.pro_bar)
        mRlTitle = root.findViewById<RelativeLayout>(R.id.rl_title)
        mIvBack = root.findViewById<ImageView>(R.id.iv_back)
        mTvTitle = root.findViewById<TextView>(R.id.tv_title)

        mWebChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(webView: WebView?, progress: Int) {
                    if (progress == 100) {
                        mProgressBar.setVisibility(View.GONE)
                    }
                    else {
                        mProgressBar.setVisibility(View.VISIBLE)
                        mProgressBar.setProgress(progress)
                    }
            }
        }
        mWebViewClient = object : WebViewClient(){
            override fun doUpdateVisitedHistory(webView: WebView, url: String?, isReload: Boolean) {
                super.doUpdateVisitedHistory(webView, url, isReload)
                webView.clearHistory()
            }
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onPageStarted(webView: WebView?, str: String?, bitmap: Bitmap?) {
                super.onPageStarted(webView, str, bitmap)
                if (!networkConnected()) {
                    switchLoadingPage(true)
                }
            }
            @RequiresApi(Build.VERSION_CODES.M)
            override fun shouldOverrideUrlLoading(webView: WebView?, str: String?): Boolean {
                if (!networkConnected()){
                    switchLoadingPage(true)
                    return false
                }
                else {
                    return super.shouldOverrideUrlLoading(webView, str)
                }
            }
        }
        mOnWebListener = object : OnWebListener {
            override fun shouldIntercept(aVar: Parameters?) {
                if (aVar != null)
                    func7878(aVar)
            }
            override fun webJsFinish() {
                mActivity.runOnUiThread({switchLoadingPage(false)})
            }
        }
        mHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(message: Message) {
                super.handleMessage(message)
                if (!mActivity.isFinishing()) {
                    val what: Int = message.what
                    if (what == 0) {
                        mRlTitle.visibility = View.VISIBLE
                    } else if (what == 1) {
                        mRlTitle.visibility = View.GONE
                    }
                }
            }
        }
        return root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    protected fun networkConnected():Boolean {
        val cm: ConnectivityManager = Hicore.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network: Network? = cm.activeNetwork
        if (network != null)
        {
            val nc: NetworkCapabilities? = cm.getNetworkCapabilities(network)
            if(nc!=null){
                if(nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){//WIFI
                    return true
                }else if(nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){//移动数据
                    return true
                }
            }
        }
        return false;
    }
    protected fun initPage() {
        this.mIvRight.setBackgroundResource(R.drawable.iv_share_white)
        mIvBack.visibility = View.GONE
        mRlTitle.visibility = View.GONE
        mRlTitle.setBackgroundColor(0)
        webViewFrag = WebViewFrag()
        initWebViewFrag()
        ViewWeb()
    }
    protected fun ViewWeb(){
        val url = "https://fzapph5.gjfzpt.cn/?userid=" + userInfoBean.accountId + "&imei=" + userInfoBean.imei + "&" + (System.currentTimeMillis() / 3000)
        agentWeb = AgentWeb.with(this).
        setAgentWebParent(mLinearLayout, LinearLayout.LayoutParams(-1, -1)).
        closeIndicator().setWebViewClient(mWebViewClient).
        addJavascriptInterface("appjs", WebViewFrag.JsObject()).
        setAgentWebWebSettings(AbsAgentWebSettings.getInstance()).
        setMainFrameErrorView(R.layout.web_page_error, -1).createAgentWeb().ready().go(url)
        webView = agentWeb.getWebCreator().getWebView()
        agentWeb.getWebCreator().getWebView().setHorizontalScrollBarEnabled(false)
        webView.setWebChromeClient(mWebChromeClient)
        webView.getSettings().setTextZoom(100)
    }
    protected fun initWebViewFrag() {
        switchLoadingPage(true)
        webViewFrag.init(mActivity, mOnWebListener)
    }
    fun switchLoadingPage(isLoading: Boolean) {
        mLinearLayout.visibility = View.VISIBLE
        if (isLoading) {
            mNetTips.text = "正在努力加载中..."
            mLlNetworkNo.visibility = View.VISIBLE
        }
        else{
            mLlNetworkNo.visibility = View.GONE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }
    override fun onResume(){
        super.onResume()
        if (TextUtils.isEmpty(userInfoBean.accountId))
            return
        if (!IsInitWeb)
        {
            mVirtualWeb.visibility = View.GONE
            mllWebContainer.visibility = View.VISIBLE
            mProgressBar.visibility = View.VISIBLE
            IsInitWeb = true
            initPage()
        }
     }
    fun func7878(aVar: Parameters) {
        if (!aVar.c()) {
            try {
                val tid: String = aVar.value("id")
                if (!TextUtils.isEmpty(tid)) {
                    id = tid
                }
                val a2: String = UrlAES.a(aVar.value("url"))
                if (!TextUtils.isEmpty(a2)) {
                    val decode: String = URLDecoder.decode(aVar.value("title"), "UTF-8")
                    /*
                    val intent = Intent(mActivity, PromosWebDetActivity::class.java)
                    intent.putExtra("extra_web_title", decode)
                    intent.putExtra("extra_web_url", a2)
                    intent.putExtra(IntentUtils.Y, this.O)
                    intent.putExtra(IntentUtils.Z, 2)
                    startActivity(intent)*/
                }
                val b3: String = aVar.value("isOnlyFullScreen")
                val b4: String = aVar.value("isfullScreen")
                val b5: String = aVar.value("stylecolor")
                if (TextUtils.equals("yes", b4)) {
                    //EventBus.getDefault().postSticky(a(100, null))
                    mHandler.sendEmptyMessage(0)
                } else if (TextUtils.equals("no", b4)) {
                    //EventBus.getDefault().postSticky(a(101, null))
                    mHandler.sendEmptyMessage(1)
                } else if (TextUtils.equals("yes", b3)) {
                    //EventBus.getDefault().postSticky(a(100, null))
                    mHandler.sendEmptyMessage(1)
                } else if (TextUtils.equals("no", b3)) {
                    //EventBus.getDefault().postSticky(a(101, null))
                    mHandler.sendEmptyMessage(1)
                } else if (TextUtils.equals("black", b5)) {
                    //EventBus.getDefault().postSticky(a(102, null))
                } else if (TextUtils.equals("white", b5)) {
                    //EventBus.getDefault().postSticky(a(103, null))
                }
            } catch (unused: Exception) {
            }
        }
    }


}