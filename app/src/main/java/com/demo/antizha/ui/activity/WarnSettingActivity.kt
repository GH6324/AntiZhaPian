package com.demo.antizha.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.text.TextUtils
import android.view.View
import com.airbnb.lottie.RenderMode
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivityWarnSettingBinding
import com.demo.antizha.util.SpUtils
import com.suke.widget.SwitchButton
import qiu.niorgai.StatusBarCompat

class WarnSettingActivity : BaseActivity() {
    private lateinit var infoBinding: ActivityWarnSettingBinding
    private var currentAnimation = ""
    override fun initPage() {
        infoBinding = ActivityWarnSettingBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        StatusBarCompat.translucentStatusBar(this as Activity, true, false)
        this.typ_ME = Typeface.createFromAsset(getAssets(), "DIN-Medium.otf")

        infoBinding.piTitle.tvTitle.setText("来电预警")
        infoBinding.piTitle.ivRight.setImageResource(R.mipmap.ic_warn_setting)
        infoBinding.tvCall.setTypeface(typ_ME)
        infoBinding.tvSms.setTypeface(typ_ME)
        infoBinding.tvApp.setTypeface(typ_ME)
        //监听改版状态的事件
        infoBinding.switchCall.setOnCheckedChangeListener(OnCheckedChangeListener(SpUtils.warnCall))
        infoBinding.switchSms.setOnCheckedChangeListener(OnCheckedChangeListener(SpUtils.warnSms))
        //先显示一个打开的动画，具体是否打开在onResume事件中判断
        infoBinding.lottieLikeanim.setRenderMode(RenderMode.SOFTWARE);
        infoBinding.lottieLikeanim.setImageAssetsFolder("images/");
        infoBinding.lottieLikeanim.setAnimation("lottie_on.json");
        infoBinding.lottieLikeanim.playAnimation();
        //监听2个点击事件
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
        infoBinding.piTitle.ivRight.setOnClickListener {
            val intentInfo = Intent(this, WarnPrimessActivity::class.java)
            startActivity(intentInfo)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        infoBinding.lottieLikeanim.cancelAnimation()
        infoBinding.lottieLikeanim.clearAnimation()
        infoBinding.lottieLikeanim.clearFocus()
    }

    override fun onPause() {
        super.onPause()
        infoBinding.lottieLikeanim.pauseAnimation()
    }

    override fun onResume() {
        super.onResume()
        infoBinding.lottieLikeanim.playAnimation()
        checkWarnIsOpen()
    }

    private fun checkWarnIsOpen() {
        infoBinding.switchCall.setVisibility(View.VISIBLE)
        infoBinding.switchSms.setVisibility(View.VISIBLE)
        infoBinding.tvGoPermission.setVisibility(View.GONE)
        val warnCallIsOpen = SpUtils.getValue(SpUtils.warnCall, true)
        val warnSmsIsOpen = SpUtils.getValue(SpUtils.warnSms, true)
        if (warnCallIsOpen || warnSmsIsOpen) {
            if (warnCallIsOpen) {
                infoBinding.switchCall.setChecked(true)
            }
            if (warnSmsIsOpen) {
                infoBinding.switchSms.setChecked(true)
            }
            switchWarnText(true)
            return
        }
        switchWarnText(false)
    }


    fun switchWarnText(warnIsOpen: Boolean) {
        if (!switchAnimation(warnIsOpen)) {
            if (warnIsOpen) {
                infoBinding.tvContent.setText("来电预警守护中")
                infoBinding.tvContentSecond.setText("准确识别电信诈骗")
                return
            }
            infoBinding.tvContent.setText("来电预警未开启")
            infoBinding.tvContentSecond.setText("无法准确识别电信诈骗，请立即开启")
        }
    }

    private fun switchAnimation(warnIsOpen: Boolean): Boolean {
        val str = if (warnIsOpen) "lottie_on.json" else "lottie_off.json"
        if (TextUtils.equals(str, this.currentAnimation)) {
            return true
        }
        this.currentAnimation = str
        infoBinding.lottieLikeanim.setAnimation(str)
        infoBinding.lottieLikeanim.playAnimation()
        return false
    }

    fun showCurrentWarn() {
        var warnIsOpen = false
        val callIsOpen: Boolean = SpUtils.getValue(SpUtils.warnCall, true)
        val smsIsOpen: Boolean = SpUtils.getValue(SpUtils.warnSms, true)
        if (callIsOpen || smsIsOpen) {
            warnIsOpen = true
        }
        switchWarnText(warnIsOpen)
    }

    inner class OnCheckedChangeListener internal constructor(val key: String) :
        SwitchButton.OnCheckedChangeListener {

        override fun onCheckedChanged(switchButton: SwitchButton?, isChecked: Boolean) {
            SpUtils.setValue(key, isChecked)
            if (isChecked) {
                switchWarnText(true)
            } else {
                showCurrentWarn()
            }
        }
    }
}