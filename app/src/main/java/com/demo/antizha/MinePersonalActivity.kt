package com.demo.antizha
//个人信息页窗口
import android.content.Intent
import android.view.View
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.demo.antizha.databinding.ActivityMinePersonalBinding

//AddressBean
class MinePersonalActivity : AppCompatActivity(), View.OnClickListener{
    lateinit var infoBinding: ActivityMinePersonalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        infoBinding = ActivityMinePersonalBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
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
    override fun onResume()
    {
        super.onResume()
        onSuccRequest()
    }
    fun onSuccRequest()
    {
        infoBinding.tvProgress.setText("已完善" + userInfoBean.perfectProgress + "%");
        infoBinding.pbProgress.setProgress(userInfoBean.perfectProgress);

        if (!TextUtils.isEmpty(userInfoBean.name) && !TextUtils.isEmpty(userInfoBean.id)) {
            infoBinding.tvIdfineVar.setVisibility(View.GONE)
            infoBinding.llIdfineVar.setVisibility(View.VISIBLE)
            infoBinding.userName.setVisibility(View.VISIBLE)
        }
        else{
            infoBinding.tvIdfineVar.setVisibility(View.VISIBLE)
            infoBinding.llIdfineVar.setVisibility(View.GONE)
            infoBinding.userName.setVisibility(View.GONE)
        }
        if (!TextUtils.isEmpty(userInfoBean.name)) {
            infoBinding.userName.text = "*" + userInfoBean.name
        }
        else{
            infoBinding.userName.text = ""
        }
        if (!TextUtils.isEmpty(userInfoBean.id)) {
            infoBinding.userID.text = userInfoBean.id[0]+ "****************"+userInfoBean.id[1]
        }
        else{
            infoBinding.userID.text = ""
        }
        infoBinding.area.text = userInfoBean.region
        infoBinding.areaDetail.text = userInfoBean.addr
        infoBinding.tvJob.text = userInfoBean.professionName
        if (TextUtils.isEmpty(userInfoBean.urgentContactname) || TextUtils.isEmpty(userInfoBean.urgentContactmob)) {
            infoBinding.tvEmergCont.setText("添加紧急联系人")
            infoBinding.tvEmergPhone.setVisibility(View.GONE);
            infoBinding.tvAddEmerg.setVisibility(View.VISIBLE);
        } else {
            infoBinding.tvEmergCont.setText(userInfoBean.urgentContactname);
            infoBinding.tvEmergPhone.setVisibility(View.VISIBLE);
            infoBinding.tvEmergPhone.setText("手机号：" + userInfoBean.urgentContactmob);
            infoBinding.tvAddEmerg.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(userInfoBean.qq)) {
            infoBinding.tvQqCont.setVisibility(View.VISIBLE);
            infoBinding.tvQqCont.setText(userInfoBean.qq);
            infoBinding.tvAddQq.setVisibility(View.GONE);
        } else {
            infoBinding.tvQqCont.setVisibility(View.GONE);
            infoBinding.tvAddQq.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(userInfoBean.wechat)) {
            infoBinding.tvWxCont.setVisibility(View.VISIBLE);
            infoBinding.tvWxCont.setText(userInfoBean.wechat);
            infoBinding.tvAddWx.setVisibility(View.GONE);
        } else {
            infoBinding.tvWxCont.setVisibility(View.GONE);
            infoBinding.tvAddWx.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(userInfoBean.email)) {
            infoBinding.tvEmailCont.setVisibility(View.VISIBLE);
            infoBinding.tvEmailCont.setText(userInfoBean.email);
            infoBinding.tvAddEmail.setVisibility(View.GONE);
        } else {
            infoBinding.tvEmailCont.setVisibility(View.GONE);
            infoBinding.tvAddEmail.setVisibility(View.VISIBLE);
        }
    }
    override fun onClick(view:View)
    {
        when(view.id){
            R.id.tv_idfine_var, R.id.user_name, R.id.ll_idfine_var, R.id.user_ID->{
                val intent = Intent(this, PersonalInfoAddActivity::class.java)
                intent.putExtra("from_page_type", PersonalInfoAddActivity.pageBase)
                startActivity(intent)
            }

            R.id.area->{
                val intent = Intent(this, PersonalInfoAddActivity::class.java)
                intent.putExtra("from_page_type", PersonalInfoAddActivity.pageArea)
                startActivity(intent)
            }
            R.id.area_detail->{
                val intent = Intent(this, PersonalInfoAddActivity::class.java)
                intent.putExtra("from_page_type", PersonalInfoAddActivity.pageAreaDetail)
                startActivity(intent)
            }
            R.id.tv_emerg_phone, R.id.tv_add_emerg, R.id.tv_emerg_cont->{
                val intent = Intent(this, PersonalInfoAddActivity::class.java)
                intent.putExtra("from_page_type", PersonalInfoAddActivity.pageEmerg)
                startActivity(intent)
            }
            R.id.ll_address_ls_cont, R.id.tv_add_qq, R.id.tv_add_wx, R.id.tv_add_email->{
                val intent = Intent(this, PersonalInfoAddActivity::class.java)
                intent.putExtra("from_page_type", PersonalInfoAddActivity.pageContacts)
                startActivity(intent)
            }
            R.id.tv_job->{
                val intent = Intent(this, IndustryActivity::class.java)
                startActivity(intent)
            }
        }
    }
}