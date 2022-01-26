package com.demo.antizha.ui.activity

import android.os.Build
import androidx.annotation.RequiresApi
import com.demo.antizha.databinding.ActivitySocialAccountBinding

class SocialAccountActivity : BaseActivity() {
    private lateinit var infoBinding: ActivitySocialAccountBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initPage() {
        supportActionBar?.hide()
        infoBinding = ActivitySocialAccountBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "添加诈骗社交账号"
        infoBinding.tvSelectTip.text = "添加"
        infoBinding.lyComplete.tvCommitTip.text = "提示：最多可上传20条社交账号"
        infoBinding.lyComplete.btnCommit.text = "确定"
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
    }
}