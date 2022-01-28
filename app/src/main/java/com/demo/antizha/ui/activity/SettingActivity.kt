package com.demo.antizha.ui.activity

import android.os.Build
import androidx.annotation.RequiresApi
import com.demo.antizha.databinding.ActivitySettingBinding
import com.demo.antizha.ui.IClickListener
import com.demo.antizha.util.DataCleanManager
import com.demo.antizha.util.DialogUtils

class SettingActivity : BaseActivity() {
    private lateinit var infoBinding: ActivitySettingBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initPage() {
        infoBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.setText("设置")
        infoBinding.switchShowCheck.isChecked = false
        infoBinding.switchShowPush.isChecked = true
        infoBinding.cacheNum.text = DataCleanManager.getCheckSize(this)
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
        infoBinding.logoutBtn.setOnClickListener {
            DialogUtils.showBtTitleDialog(this.mActivity,
                "您确认要退出登录吗？",
                "",
                "确定",
                "取消",
                -1,
                -1,
                true,
                logoutListener())
        }
        infoBinding.rlCacheCalean.setOnClickListener {
            infoBinding.cacheNum.text = "0KB"
        }
    }

    inner class logoutListener : IClickListener {
        override fun cancelBtn() {
        }

        override fun clickOKBtn() {
        }
    }
}