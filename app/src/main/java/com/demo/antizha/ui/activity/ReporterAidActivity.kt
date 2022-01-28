package com.demo.antizha.ui.activity

import android.app.Activity
import com.demo.antizha.databinding.ActivityAidReportBinding
import com.demo.antizha.databinding.ActivityIdCheckBinding
import qiu.niorgai.StatusBarCompat

class ReporterAidActivity:BaseActivity() {
    private lateinit var infoBinding: ActivityAidReportBinding
    override fun initPage() {
        infoBinding = ActivityAidReportBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "报案助手"
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
    }
}