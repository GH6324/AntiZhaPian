package com.demo.antizha.ui.activity

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivityCheckFraudBinding
import com.demo.antizha.ui.Hicore
import com.demo.antizha.ui.IEditAfterListener
import com.demo.antizha.util.EditUtil
import qiu.niorgai.StatusBarCompat


class CheckFraudActivity: BaseActivity() {
    private lateinit var infoBinding: ActivityCheckFraudBinding
    private lateinit var onViewClickListener: OnClickListener
    @RequiresApi(Build.VERSION_CODES.N)
    override fun initPage() {
        infoBinding = ActivityCheckFraudBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        StatusBarCompat.translucentStatusBar(this , true, false)
        infoBinding.piTitle.tvTitle.text = "风险查询"
        infoBinding.piTitle.ivRight.setImageResource(R.drawable.iv_share_white)
        onViewClickListener = OnClickListener()
        initView()
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {
        infoBinding.rbPay.typeface = typ_ME
        infoBinding.rbUrl.typeface = typ_ME
        infoBinding.rbChat.typeface = typ_ME
        infoBinding.etContent.typeface = typ_ME
        infoBinding.ivClear.visibility = View.GONE
        infoBinding.etContent.addTextChangedListener(EditUtil.textWatcher(EditAfterListener()))
        infoBinding.rbPay.tag = 0
        infoBinding.rbUrl.tag = 1
        infoBinding.rbChat.tag = 2
        infoBinding.llScan.tag = 1
        infoBinding.rbPay.setOnClickListener(onViewClickListener)
        infoBinding.rbUrl.setOnClickListener(onViewClickListener)
        infoBinding.rbChat.setOnClickListener(onViewClickListener)
        infoBinding.llScan.setOnClickListener(onViewClickListener)
        val str = "今日剩余可查询次数" + getColorStr(1) + "次，本周剩余可查询次数" + getColorStr(3) + "次"
        infoBinding.tvCountTip.text = Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY)

    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun changeState(model:Int) {
        infoBinding.etContent.setText("")
        if (model == 1) {
            radioState(infoBinding.rbUrl, infoBinding.rbPay, infoBinding.rbChat, R.mipmap.ic_fraud_radio_center)
            infoBinding.etContent.hint = "请输入或粘贴要查询的IP或URL网址"
            infoBinding.llScan.visibility = View.VISIBLE
        } else if (model == 2) {
            radioState(infoBinding.rbChat, infoBinding.rbPay, infoBinding.rbUrl, R.mipmap.ic_fraud_radio_right)
            infoBinding.etContent.hint = "请输入或粘贴要查询的QQ或微信账号"
            infoBinding.llScan.visibility = View.GONE
        } else {
            radioState(infoBinding.rbPay, infoBinding.rbUrl, infoBinding.rbChat, R.mipmap.ic_fraud_radio_left)
            infoBinding.etContent.hint = "请输入或粘贴要查询的银行卡号或支付账户"
            infoBinding.llScan.visibility = View.GONE
        }
    }
    private fun getColorStr(i: Int): String {
        val sb = StringBuilder()
        sb.append("<font color=")
        sb.append(if (i > 0) "#1A57F3" else "#FF0000")
        sb.append(">")
        sb.append(i)
        sb.append("</font>")
        return sb.toString()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun radioState(rb1: RadioButton,
                           rb2: RadioButton,
                           rb3: RadioButton,
                           resID: Int) {
        rb1.background = resources.getDrawable(resID, null)
        rb1.setTextColor(resources.getColor(R.color.black_dark, null))
        rb2.background = null
        rb2.setTextColor(resources.getColor(R.color.colorWhite, null))
        rb3.background = null
        rb3.setTextColor(resources.getColor(R.color.colorWhite, null))
    }

    inner class EditAfterListener internal constructor() : IEditAfterListener {
        override fun editLength(length: Int) {
            if (length > 0) {
                infoBinding.ivClear.visibility = View.VISIBLE
            } else {
                infoBinding.ivClear.visibility = View.GONE
            }
        }
    }
    inner class OnClickListener: View.OnClickListener {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onClick(v: View?) {
            if (Hicore.app.isDouble())
                return
            when (v?.id){
                R.id.ll_scan, R.id.rb_pay, R.id.rb_url, R.id.rb_chat->{
                    val tag = v.tag!! as Int
                    changeState(tag)
                }
            }
        }
    }
}