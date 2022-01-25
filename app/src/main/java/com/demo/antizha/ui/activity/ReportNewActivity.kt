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


class ReportNewActivity : BaseActivity() {
    private lateinit var infoBinding: ActivityReportNewBinding
    private var duperyType: Int = 0
    private var provinces: ArrayList<Province> = ArrayList<Province>()
    val TAGFLOWLAOUTACTIVITY = 1

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAGFLOWLAOUTACTIVITY -> {
                val tagString = data?.extras!!.getString("tagString")
                val tagId = data?.extras!!.getInt("tagId")
                if (tagId != 0) {
                    duperyType = tagId
                    infoBinding.tvDuperyType.text = tagString
                }
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