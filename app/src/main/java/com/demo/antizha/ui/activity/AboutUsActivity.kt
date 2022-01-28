package com.demo.antizha.ui.activity

import android.os.Handler
import android.os.Looper
import com.demo.antizha.databinding.ActivityAboutUsBinding
import com.demo.antizha.util.DialogUtils
import com.demo.antizha.util.SystemUtils
import com.hjq.toast.ToastUtils

class AboutUsActivity : BaseActivity() {
    private lateinit var infoBinding: ActivityAboutUsBinding
    override fun initPage() {
        infoBinding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.setText("关于我们");
        infoBinding.tvAppVersion.text = "v" + SystemUtils.getAppVer()
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
        infoBinding.checkUpadte.setOnClickListener {
            DialogUtils.showProgressDialog("检测中...", true, this.mActivity)
            Handler(Looper.getMainLooper()).postDelayed({
                DialogUtils.destroyProgressDialog()
                ToastUtils.show("已是最新版本");
            }, 500)
        }
    }
}