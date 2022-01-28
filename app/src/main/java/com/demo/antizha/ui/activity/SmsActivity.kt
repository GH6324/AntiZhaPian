package com.demo.antizha.ui.activity

import android.os.Build
import androidx.annotation.RequiresApi
import com.demo.antizha.databinding.ActivityCallBinding

class SmsActivity : BaseActivity() {
    private lateinit var infoBinding: ActivityCallBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initPage() {
        infoBinding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "添加诈骗短信"
        infoBinding.lySelect.tvSelectTip.text = "选择短信"
        infoBinding.lySelect.tvInputTip.text = "手动输入"
        infoBinding.lyComplete.tvCommitTip.text = "最多可选择" + 20 + "条短信"
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
    }
}