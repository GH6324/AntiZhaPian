package com.demo.antizha

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.demo.antizha.databinding.ActivityPersonaInfolBinding
import com.github.gzuliyujiang.wheelpicker.AddressPicker
import com.github.gzuliyujiang.wheelpicker.annotation.AddressMode
import com.github.gzuliyujiang.wheelpicker.contract.OnAddressPickedListener
import com.github.gzuliyujiang.wheelpicker.entity.CityEntity
import com.github.gzuliyujiang.wheelpicker.entity.CountyEntity
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity
import com.github.gzuliyujiang.wheelpicker.widget.LinkageWheelLayout
const val pageBase = 1

class PersonalInfoAddActivity : AppCompatActivity(), OnAddressPickedListener {
    companion object {
        val pageBase = "Base"
        val pageArea = "Area"
        val pageAreaDetail = "AreaDetail"
        val pageEmerg = "Emerg"
        val pageContacts = "Contacts"
    }
    var pageType:String = ""
    lateinit var personaInfolBinding: ActivityPersonaInfolBinding
    override fun onAddressPicked(province: ProvinceEntity?, city: CityEntity?, county: CountyEntity?)
    {
        personaInfolBinding.etArea.setText(province?.name + "." + city?.name + "." + county?.name)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        personaInfolBinding = ActivityPersonaInfolBinding.inflate(layoutInflater)
        setContentView(personaInfolBinding.root)
        pageType = getIntent().getStringExtra("from_page_type").toString();
        InitPage()


        personaInfolBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
        personaInfolBinding.etArea.setOnClickListener {
            val picker = AddressPicker(this)
            picker.setAddressMode(AddressMode.PROVINCE_CITY_COUNTY)
            picker.setOnAddressPickedListener(this)
            picker.getCancelView().setTextColor(ContextCompat.getColor(this, R.color.colorGray))
            picker.getOkView().setTextColor(ContextCompat.getColor(this, R.color.black))
            picker.topLineView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlue1))
            val wheelLayout:LinkageWheelLayout = picker.getWheelLayout();
            val colorGray :Int = ContextCompat.getColor(this, R.color.colorGray)
            wheelLayout.firstWheelView.selectedTextColor = colorGray
            wheelLayout.secondWheelView.selectedTextColor = colorGray
            wheelLayout.thirdWheelView.selectedTextColor = colorGray
            wheelLayout.firstWheelView.indicatorColor = colorGray
            wheelLayout.secondWheelView.indicatorColor = colorGray
            wheelLayout.thirdWheelView.indicatorColor = colorGray
            wheelLayout.firstWheelView.layoutParams = LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,0.25F)
            wheelLayout.secondWheelView.layoutParams = LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,0.5F)
            wheelLayout.thirdWheelView.layoutParams = LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,0.25F)
            val region:String = personaInfolBinding.etArea.text.toString()
            if (!TextUtils.isEmpty(region))
            {
                val regions = TextUtils.split(region, "\\.")
                if (regions.size == 3){
                    picker.setDefaultValue(regions[0], regions[1], regions[2])
                }
            }
            picker.show()
        }
        personaInfolBinding.btnConfirm.setOnClickListener {
            when(pageType){
                pageBase->{
                    val name:String = personaInfolBinding.etName.text.toString()
                    val id:String = personaInfolBinding.etIdNum.text.toString()
                    val mobileNumber:String = personaInfolBinding.etPhoneNum.text.toString()
                    if (!TextUtils.isEmpty(id) && !stringIsUserID(id)){
                        Toast.makeText(this@PersonalInfoAddActivity, "身份证格式不对", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (!TextUtils.isEmpty(mobileNumber) && !stringIsMobileNumber(mobileNumber)){
                        Toast.makeText(this@PersonalInfoAddActivity, "电话格式不对", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (name != userInfoBean.name || id != userInfoBean.id || mobileNumber != userInfoBean.mobileNumber) {
                        userInfoBean.name = name
                        userInfoBean.id = id
                        userInfoBean.mobileNumber = mobileNumber
                        userInfoBean.commit(this)
                    }
                }
                pageArea->{
                    val region:String = personaInfolBinding.etArea.text.toString()
                    if (region != userInfoBean.region){
                        userInfoBean.region = region
                        userInfoBean.commit(this)
                    }
                }
                pageAreaDetail->{
                    val address:String = personaInfolBinding.etAddress.text.toString()
                    if (address != userInfoBean.addr){
                        userInfoBean.addr = address
                        userInfoBean.commit(this)
                    }
                }
                pageEmerg->{
                    val emergName:String = personaInfolBinding.etEmergName.text.toString()
                    val emergPhoneNum:String = personaInfolBinding.etEmergPhoneNum.text.toString()
                    if (emergName != userInfoBean.urgentContactname || emergPhoneNum != userInfoBean.urgentContactmob) {
                        userInfoBean.urgentContactname = emergName
                        userInfoBean.urgentContactmob = emergPhoneNum
                        userInfoBean.commit(this)
                    }
                }
                pageContacts->{
                    val qq:String = personaInfolBinding.etQqNum.text.toString()
                    val wx:String = personaInfolBinding.etWxNum.text.toString()
                    val email:String = personaInfolBinding.etEmailNum.text.toString()
                    if (!TextUtils.isEmpty(email) && !stringIsEmail(email)){
                        Toast.makeText(this@PersonalInfoAddActivity, "邮箱格式不对", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (qq != userInfoBean.qq || wx != userInfoBean.wechat || email != userInfoBean.email) {
                        userInfoBean.qq = qq
                        userInfoBean.wechat = wx
                        userInfoBean.email = email
                        userInfoBean.commit(this)
                    }
                }
            }
            finish()
        }
    }
    fun InitPage(){
        when (pageType){
            pageBase->{
                personaInfolBinding.piTitle.tvTitle.text = "基础信息"
                personaInfolBinding.clBaseCont.visibility = View.VISIBLE
                personaInfolBinding.etName.setText(userInfoBean.name)
                personaInfolBinding.etIdNum.setText(userInfoBean.id)
                personaInfolBinding.etPhoneNum.setText(userInfoBean.mobileNumber)
            }
            pageArea->{
                personaInfolBinding.piTitle.tvTitle.text = "地址"
                personaInfolBinding.clAreaCont.visibility = View.VISIBLE
                personaInfolBinding.etArea.text = userInfoBean.region
            }
            pageAreaDetail->{
                personaInfolBinding.piTitle.tvTitle.text = "详细地址"
                personaInfolBinding.clAreaDetailContent.visibility = View.VISIBLE
                personaInfolBinding.etAddress.setText(userInfoBean.addr)
            }
            pageEmerg->{
                personaInfolBinding.piTitle.tvTitle.text = "紧急联系人"
                personaInfolBinding.clEmergCont.visibility = View.VISIBLE
                personaInfolBinding.etEmergName.setText(userInfoBean.urgentContactname)
                personaInfolBinding.etEmergPhoneNum.setText(userInfoBean.urgentContactmob)
            }
            pageContacts->{
                personaInfolBinding.piTitle.tvTitle.text = "社交通讯信息"
                personaInfolBinding.clContactsCont.visibility = View.VISIBLE
                personaInfolBinding.etQqNum.setText(userInfoBean.qq)
                personaInfolBinding.etWxNum.setText(userInfoBean.wechat)
                personaInfolBinding.etEmailNum.setText(userInfoBean.email)
            }
        }
    }
}