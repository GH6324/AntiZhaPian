package com.demo.antizha.ui.activity

import com.demo.antizha.databinding.ActivityReportNewBinding

class ReportNewActivity: BaseActivity() {
    private lateinit var infoBinding: ActivityReportNewBinding
    override fun initPage() {
        supportActionBar?.hide()
        infoBinding = ActivityReportNewBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "我要举报"
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
    }
    //诈骗类型窗口TagFlowLaoutActivity
    //https://fzapp.gjfzpt.cn/hicore/api/xc/getxccasecategorys获取诈骗类型
    //https://fzapp.gjfzpt.cn/hicore/api/DK/getcasecategorys断卡诈骗类型
}