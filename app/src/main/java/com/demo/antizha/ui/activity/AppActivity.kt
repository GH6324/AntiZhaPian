package com.demo.antizha.ui.activity

import android.os.Build
import androidx.annotation.RequiresApi
import com.demo.antizha.databinding.ActivityAudioBinding

class AppActivity : BaseActivity() {
    private lateinit var infoBinding: ActivityAudioBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initPage() {
        supportActionBar?.hide()
        infoBinding = ActivityAudioBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "添加APP应用程序"
        infoBinding.lySelect.tvSelectTip.text = "添加"
        infoBinding.lyComplete.tvCommitTip.text = "最多可选择" + 2 + "个APP应用程序"
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
    }
}