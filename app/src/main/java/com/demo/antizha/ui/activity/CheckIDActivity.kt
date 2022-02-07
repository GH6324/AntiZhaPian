package com.demo.antizha.ui.activity

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.demo.antizha.databinding.ActivityIdCheckBinding
import com.demo.antizha.databinding.ActivityWarnSettingBinding
import com.demo.antizha.ui.Hicore
import com.demo.antizha.ui.IClickListener

import qiu.niorgai.StatusBarCompat
import java.lang.Exception
import com.demo.antizha.util.DialogUtils

class CheckIDActivity: BaseActivity(), IClickListener {
    private lateinit var infoBinding: ActivityIdCheckBinding
    override fun initPage() {
        infoBinding = ActivityIdCheckBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        StatusBarCompat.translucentStatusBar(this as Activity, true, true)
        infoBinding.piTitle.tvTitle.text = "身份核实"
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
        infoBinding.btnIdvrfySend.setOnClickListener {
            val checkID: String = infoBinding.etCheckPhone.getText().toString()
            if (checkID.trim { it <= ' ' }.length < 11) {
                var toast = Toast.makeText(Hicore.app, "请输入正确手机号~", Toast.LENGTH_SHORT)
                toast.setGravity(17, 0, 0)
                toast.show()
            } else {
                DialogUtils.showNormalDialog(mActivity, "向该号码发送身份核实请求?", checkID, "取消", "确认发送", this)
            }
        }
    }
    override fun cancelBtn(){

    }
    override fun clickOKBtn(){
        var toast = Toast.makeText(Hicore.app, "发送成功", Toast.LENGTH_SHORT)
        toast.setGravity(17, 0, 0)
        toast.show()
    }
}