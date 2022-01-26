package com.demo.antizha.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import androidx.annotation.RequiresApi
import cn.qqtheme.framework.entity.City
import cn.qqtheme.framework.entity.County
import cn.qqtheme.framework.entity.Province
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivityReportNewBinding
import com.demo.antizha.ui.IClickListener
import com.demo.antizha.util.AddressBean
import com.demo.antizha.util.DialogUtils
import java.io.Serializable

class ReportNewActivity : BaseActivity() {
    private lateinit var infoBinding: ActivityReportNewBinding
    private var duperyType: Int = 0
    private var provinces: ArrayList<Province> = ArrayList<Province>()
    private var urls: ArrayList<String> = ArrayList<String>()
    private var calls: ArrayList<CallBean> = ArrayList()
    val TAGFLOWLAOUTACTIVITY = 1
    val EVIDENCEDISCACTIVITY = 2
    val CALLACTIVITY = 3
    val SMSACTIVITY = 4
    val APPACTIVITY = 5
    val PICTUREACTIVITY = 6
    val AUDIOACTIVITY = 7
    val WEBSITEACTIVITY = 8
    val SOCIALACCOUNTACTIVITY = 9
    val TRADACCOUNTACTIVITY = 10

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initPage() {
        supportActionBar?.hide()
        infoBinding = ActivityReportNewBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "我要举报"
        infoBinding.tvNumTip.text =
            Html.fromHtml("今日剩余可举报次数<font color=#2B4CFF>2</font>次", FROM_HTML_MODE_LEGACY)
        //直接调用好像会让界面卡一下，所以先压入事件循环，让界面先显示出来
        Handler(Looper.getMainLooper()).postDelayed({
            provinces = AddressBean.getProvince()
        }, 10)
        infoBinding.piTitle.ivBack.setOnClickListener {
            DialogUtils.showBtTitleDialog(this,
                "将此次编辑保留",
                "",
                "不保留",
                "保留",
                R.color._353536,
                -1,
                true,
                isSaveRecordListener())
        }

        infoBinding.tvDuperyType.setOnClickListener {
            val intent = Intent(this, TagFlowLaoutActivity::class.java)
            intent.putExtra("int_tag_name", duperyType)
            startActivityForResult(intent, TAGFLOWLAOUTACTIVITY)
        }
        infoBinding.region.setOnClickListener {
            val picker = AddressBean.createAddressPicker(this, "", true, AddressListener())
            picker.show()
        }
        infoBinding.etCaseDescribe.setOnClickListener {
            val intent = Intent(mActivity, EvidenceDiscActivity::class.java)
            intent.putExtra("disc", infoBinding.etCaseDescribe.text.toString())
            intent.putExtra("title", "举报描述")
            startActivityForResult(intent, EVIDENCEDISCACTIVITY)
        }
        infoBinding.lyCall.tvUploadCall.setOnClickListener {
            val intent = Intent(this, CallActivity::class.java)
            intent.putExtra("call", calls as Serializable)
            startActivityForResult(intent, CALLACTIVITY)
        }
        infoBinding.lySms.tvUploadSms.setOnClickListener {
            val intent = Intent(this, SmsActivity::class.java)
            startActivity(intent)
        }
        infoBinding.lyApp.tvUploadApp.setOnClickListener {
            val intent = Intent(this, AppActivity::class.java)
            startActivity(intent)
        }
        infoBinding.lyPicture.tvUploadPicture.setOnClickListener {
            val intent = Intent(this, PictureActivity::class.java)
            startActivity(intent)
        }
        infoBinding.lyAudio.tvUploadAudio.setOnClickListener {
            val intent = Intent(this, AudioActivity::class.java)
            startActivity(intent)
        }
        infoBinding.lyUrl.tvUploadUrl.setOnClickListener {
            val intent = Intent(this, WebsiteActivity::class.java)
            intent.putStringArrayListExtra("url", urls)
            startActivityForResult(intent, WEBSITEACTIVITY)
        }
        infoBinding.lyContact.tvSocial.setOnClickListener {
            val intent = Intent(this, SocialAccountActivity::class.java)
            startActivity(intent)
        }
        infoBinding.lyDeal.tvTrad.setOnClickListener {
            val intent = Intent(this, TradAccountActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAGFLOWLAOUTACTIVITY -> {
                if (data == null)
                    return
                if (data.extras == null)
                    return
                val tagString = data.extras!!.getString("tagString")
                val tagId = data.extras!!.getInt("tagId")
                if (tagId != 0) {
                    duperyType = tagId
                    infoBinding.tvDuperyType.text = tagString
                }
            }
            EVIDENCEDISCACTIVITY -> {
                if (data == null)
                    return
                if (data.extras == null)
                    return
                val disc = data.extras!!.getString("disc")
                if (disc != null)
                    infoBinding.etCaseDescribe.text = disc
            }
            CALLACTIVITY -> {
                if (data == null)
                    return
                val array: ArrayList<CallBean>? =
                    data.getSerializableExtra("call") as ArrayList<CallBean>?
                if (array == null)
                    return
                calls = array
                if (calls.size == 0)
                    infoBinding.lyCall.tvUploadCall.text = ""
                else
                    infoBinding.lyCall.tvUploadCall.text = calls.size.toString() + "个"
            }
            WEBSITEACTIVITY -> {
                if (data == null)
                    return
                val array = data.getStringArrayListExtra("url")
                if (array == null)
                    return
                urls = array
                if (urls.size == 0)
                    infoBinding.lyUrl.tvUploadUrl.text = ""
                else
                    infoBinding.lyUrl.tvUploadUrl.text = urls.size.toString() + "个"
            }
        }
    }

    inner class AddressListener : AddressBean.HiAddressListener() {
        override fun onAddressPicked(province: Province?, city: City?, county: County?) {
            if (province == null || city == null || county == null)
                infoBinding.region.text = ""
            else
                infoBinding.region.text = "${province.name}.${city.name}.${county.name}"
        }

        override fun onClear() {
            infoBinding.region.text = ""
        }
    }

    inner class isSaveRecordListener : IClickListener {
        override fun cancelBtn() {
            finish()
        }

        override fun clickOKBtn() {
            finish()
        }
    }

    //诈骗类型窗口TagFlowLaoutActivity
    //https://fzapp.gjfzpt.cn/hicore/api/EvidenceType通用诈骗类型
    //https://fzapp.gjfzpt.cn/hicore/api/DK/getcasecategorys断卡诈骗类型?
    //https://fzapp.gjfzpt.cn/hicore/api/xc/getxccasecategorys
}