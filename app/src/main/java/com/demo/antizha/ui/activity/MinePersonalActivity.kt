package com.demo.antizha.ui.activity
//个人信息页窗口
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivityMinePersonalBinding
import com.demo.antizha.userInfoBean
import qiu.niorgai.StatusBarCompat

//AddressBean
class MinePersonalActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var infoBinding: ActivityMinePersonalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        infoBinding = ActivityMinePersonalBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        StatusBarCompat.translucentStatusBar(this as Activity, true, false)
        infoBinding.piTitle.tvTitle.text = "个人信息"
        infoBinding.tvIdfineVar.setOnClickListener(this)
        infoBinding.llIdfineVar.setOnClickListener(this)
        infoBinding.userName.setOnClickListener(this)
        infoBinding.userID.setOnClickListener(this)
        infoBinding.area.setOnClickListener(this)
        infoBinding.areaDetail.setOnClickListener(this)
        infoBinding.tvJob.setOnClickListener(this)
        infoBinding.tvEmergPhone.setOnClickListener(this)
        infoBinding.tvEmergCont.setOnClickListener(this)
        infoBinding.tvAddEmerg.setOnClickListener(this)
        infoBinding.tvAddQq.setOnClickListener(this)
        infoBinding.tvAddWx.setOnClickListener(this)
        infoBinding.tvAddEmail.setOnClickListener(this)
        infoBinding.llAddressLsCont.setOnClickListener(this)
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        onSuccRequest()
    }

    private fun onSuccRequest() {
        infoBinding.tvProgress.text = "已完善" + userInfoBean.perfectProgress + "%"
        infoBinding.pbProgress.progress = userInfoBean.perfectProgress

        if (!TextUtils.isEmpty(userInfoBean.name) && !TextUtils.isEmpty(userInfoBean.id)) {
            infoBinding.tvIdfineVar.visibility = View.GONE
            infoBinding.llIdfineVar.visibility = View.VISIBLE
            infoBinding.userName.visibility = View.VISIBLE
        } else {
            infoBinding.tvIdfineVar.visibility = View.VISIBLE
            infoBinding.llIdfineVar.visibility = View.GONE
            infoBinding.userName.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(userInfoBean.name)) {
            infoBinding.userName.text = "*" + userInfoBean.name
        } else {
            infoBinding.userName.text = ""
        }
        if (!TextUtils.isEmpty(userInfoBean.id)) {
            infoBinding.userID.text = userInfoBean.id[0] + "****************" + userInfoBean.id[1]
        } else {
            infoBinding.userID.text = ""
        }
        infoBinding.area.text = userInfoBean.region
        infoBinding.areaDetail.text = userInfoBean.addr
        infoBinding.tvJob.text = userInfoBean.professionName
        if (TextUtils.isEmpty(userInfoBean.urgentContactname) || TextUtils.isEmpty(userInfoBean.urgentContactmob)) {
            infoBinding.tvEmergCont.text = "添加紧急联系人"
            infoBinding.tvEmergPhone.visibility = View.GONE
            infoBinding.tvAddEmerg.visibility = View.VISIBLE
        } else {
            infoBinding.tvEmergCont.text = userInfoBean.urgentContactname
            infoBinding.tvEmergPhone.visibility = View.VISIBLE
            infoBinding.tvEmergPhone.text = "手机号：" + userInfoBean.urgentContactmob
            infoBinding.tvAddEmerg.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(userInfoBean.qq)) {
            infoBinding.tvQqCont.visibility = View.VISIBLE
            infoBinding.tvQqCont.text = userInfoBean.qq
            infoBinding.tvAddQq.visibility = View.GONE
        } else {
            infoBinding.tvQqCont.visibility = View.GONE
            infoBinding.tvAddQq.visibility = View.VISIBLE
        }
        if (!TextUtils.isEmpty(userInfoBean.wechat)) {
            infoBinding.tvWxCont.visibility = View.VISIBLE
            infoBinding.tvWxCont.text = userInfoBean.wechat
            infoBinding.tvAddWx.visibility = View.GONE
        } else {
            infoBinding.tvWxCont.visibility = View.GONE
            infoBinding.tvAddWx.visibility = View.VISIBLE
        }
        if (!TextUtils.isEmpty(userInfoBean.email)) {
            infoBinding.tvEmailCont.visibility = View.VISIBLE
            infoBinding.tvEmailCont.text = userInfoBean.email
            infoBinding.tvAddEmail.visibility = View.GONE
        } else {
            infoBinding.tvEmailCont.visibility = View.GONE
            infoBinding.tvAddEmail.visibility = View.VISIBLE
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_idfine_var, R.id.user_name, R.id.ll_idfine_var, R.id.user_ID -> {
                val intent = Intent(this, PersonalInfoAddActivity::class.java)
                intent.putExtra("from_page_type", PersonalInfoAddActivity.pageBase)
                startActivity(intent)
            }

            R.id.area -> {
                val intent = Intent(this, PersonalInfoAddActivity::class.java)
                intent.putExtra("from_page_type", PersonalInfoAddActivity.pageArea)
                startActivity(intent)
            }
            R.id.area_detail -> {
                val intent = Intent(this, PersonalInfoAddActivity::class.java)
                intent.putExtra("from_page_type", PersonalInfoAddActivity.pageAreaDetail)
                startActivity(intent)
            }
            R.id.tv_emerg_phone, R.id.tv_add_emerg, R.id.tv_emerg_cont -> {
                val intent = Intent(this, PersonalInfoAddActivity::class.java)
                intent.putExtra("from_page_type", PersonalInfoAddActivity.pageEmerg)
                startActivity(intent)
            }
            R.id.ll_address_ls_cont, R.id.tv_add_qq, R.id.tv_add_wx, R.id.tv_add_email -> {
                val intent = Intent(this, PersonalInfoAddActivity::class.java)
                intent.putExtra("from_page_type", PersonalInfoAddActivity.pageContacts)
                startActivity(intent)
            }
            R.id.tv_job -> {
                val intent = Intent(this, IndustryActivity::class.java)
                startActivity(intent)
            }
        }
    }
}