package com.demo.antizha.ui.dashboard

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.demo.antizha.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?){
        super.onActivityCreated(savedInstanceState)
        val imei = Settings.System.getString(getActivity()?.getApplicationContext()?.getContentResolver(), Settings.Secure.ANDROID_ID)
        val url = "https://fzapph5.gjfzpt.cn/?userid=" + "48808f6c-c936-47b7-9b85-aece58589577" + "&imei=" + imei + "&" + (System.currentTimeMillis() / 3000)
        /*
        this.e0 = AgentWeb.with(this).setAgentWebParent(this.mLinearLayout, new LinearLayout.LayoutParams(-1, -1)).closeIndicator().setWebViewClient(new e()).addJavascriptInterface("appjs", new v.a()).setAgentWebWebSettings(AbsAgentWebSettings.getInstance()).setMainFrameErrorView(R.layout.web_page_error, -1).createAgentWeb().ready().go(j());
        this.d0 = this.e0.getWebCreator().getWebView();
        this.e0.getWebCreator().getWebView().setHorizontalScrollBarEnabled(false);
        this.d0.setWebChromeClient(new d(this, null));
        this.d0.getSettings().setTextZoom(100);

        */

    }
}