package com.demo.antizha.ui.activity

import android.app.Activity
import android.app.AlertDialog
import android.provider.Settings
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.demo.antizha.*
import com.demo.antizha.databinding.ActivityPersonaInfolBinding
import com.demo.antizha.util.CRC64
import com.demo.antizha.util.SpUtils
import com.github.gzuliyujiang.wheelpicker.AddressPicker
import com.github.gzuliyujiang.wheelpicker.annotation.AddressMode
import com.github.gzuliyujiang.wheelpicker.contract.OnAddressPickedListener
import com.github.gzuliyujiang.wheelpicker.entity.CityEntity
import com.github.gzuliyujiang.wheelpicker.entity.CountyEntity
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity
import com.github.gzuliyujiang.wheelpicker.widget.LinkageWheelLayout
import qiu.niorgai.StatusBarCompat


class PersonalInfoAddActivity : BaseActivity(), OnAddressPickedListener {
    companion object {
        const val pageBase = "Base"
        const val pageArea = "Area"
        const val pageAreaDetail = "AreaDetail"
        const val pageEmerg = "Emerg"
        const val pageContacts = "Contacts"
    }

    private var pageType: String = ""
    private var adcode: String = ""
    private lateinit var personaInfolBinding: ActivityPersonaInfolBinding
    override fun onAddressPicked(
        province: ProvinceEntity?,
        city: CityEntity?,
        county: CountyEntity?
    ) {
        personaInfolBinding.etArea.text = "${province?.name}.${city?.name}.${county?.name}"
        if (county != null) {
            adcode = county.code
        }
    }

    override fun initPage() {
        supportActionBar?.hide()
        personaInfolBinding = ActivityPersonaInfolBinding.inflate(layoutInflater)
        setContentView(personaInfolBinding.root)
        StatusBarCompat.translucentStatusBar(this as Activity, true, false)
        pageType = intent.getStringExtra("from_page_type").toString()
        initPages()


        personaInfolBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
        personaInfolBinding.etArea.setOnClickListener {
            val picker = AddressPicker(this)
            picker.setAddressMode(AddressMode.PROVINCE_CITY_COUNTY)
            picker.setOnAddressPickedListener(this)
            picker.cancelView.setTextColor(ContextCompat.getColor(this, R.color.colorGray))
            picker.okView.setTextColor(ContextCompat.getColor(this, R.color.black))
            picker.topLineView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlue1))
            val wheelLayout: LinkageWheelLayout = picker.wheelLayout
            val colorGray: Int = ContextCompat.getColor(this, R.color.colorGray)
            wheelLayout.firstWheelView.selectedTextColor = colorGray
            wheelLayout.secondWheelView.selectedTextColor = colorGray
            wheelLayout.thirdWheelView.selectedTextColor = colorGray
            wheelLayout.firstWheelView.indicatorColor = colorGray
            wheelLayout.secondWheelView.indicatorColor = colorGray
            wheelLayout.thirdWheelView.indicatorColor = colorGray
            wheelLayout.firstWheelView.layoutParams =
                LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.25F)
            wheelLayout.secondWheelView.layoutParams =
                LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.5F)
            wheelLayout.thirdWheelView.layoutParams =
                LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.25F)
            val region: String = personaInfolBinding.etArea.text.toString()
            if (!TextUtils.isEmpty(region)) {
                val regions = TextUtils.split(region, "\\.")
                if (regions.size == 3) {
                    picker.setDefaultValue(regions[0], regions[1], regions[2])
                }
            }
            picker.show()
        }
        personaInfolBinding.etAccountNum.setOnClickListener {
            if (personaInfolBinding.etAccountNum.inputType == InputType.TYPE_NULL) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("关于账号ID")
                builder.setMessage(
                    "必须先使用官方软件注册一个账号，然后在手机的/data/data/com.hicorenational.antifraud/shared_prefs/note_national.xml" +
                            "中找到类似提示的字符串，当然这些可以在虚拟机中执行，如果在手机里操作的话，你还得有root权限，当然你也可以选择不填，只是部分功能会受限，" +
                            "你也可以随机生成一个，某些功能可能也能用"
                )
                builder.setPositiveButton("确定") { _, _ ->
                    personaInfolBinding.etAccountNum.inputType = InputType.TYPE_CLASS_TEXT
                }
                builder.setNegativeButton("取消") { _, _ ->
                }
                builder.setNeutralButton("随机生成") { _, _ ->
                    var account = ""
                    val strtemplate = "1111aaaa-aaaa-aaaa-aaaa-aaaa11111111"
                    val nummap = "1234567890"
                    val hexmap = "1234567890abcdefabcdef"
                    for (i in 1..strtemplate.length) {
                        when (strtemplate[i - 1]) {
                            '1' -> account += nummap[(0 until nummap.length - 1).random()]
                            'a' -> account += hexmap[(0 until hexmap.length - 1).random()]
                            '-' -> account += "-"
                        }
                    }
                    personaInfolBinding.etAccountNum.setText(account)
                }
                builder.show()
            }
        }
        personaInfolBinding.btnClearpermiss.setOnClickListener {
            SpUtils.setValue(SpUtils.primissAuto, false)
            SpUtils.setValue(SpUtils.primissPower, false)
            SpUtils.setValue(SpUtils.primissLock, false)
        }
        personaInfolBinding.btnConfirm.setOnClickListener {
            when (pageType) {
                pageBase -> {
                    val name: String = personaInfolBinding.etName.text.toString()
                    val id: String = personaInfolBinding.etIdNum.text.toString()
                    val mobileNumber: String = personaInfolBinding.etPhoneNum.text.toString()
                    if (!TextUtils.isEmpty(id) && !stringIsUserID(id)) {
                        Toast.makeText(this@PersonalInfoAddActivity, "身份证格式不对", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                    if (!TextUtils.isEmpty(mobileNumber) && !stringIsMobileNumber(mobileNumber)) {
                        Toast.makeText(this@PersonalInfoAddActivity, "电话格式不对", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }
                    val accountId: String = personaInfolBinding.etAccountNum.text.toString()
                    var changed = false
                    if (name != userInfoBean.name || id != userInfoBean.id || mobileNumber != userInfoBean.mobileNumber) {
                        userInfoBean.name = name
                        userInfoBean.id = id
                        userInfoBean.mobileNumber = mobileNumber
                        changed = true
                    }
                    if (accountId != userInfoBean.accountId) {
                        userInfoBean.accountId = accountId
                    }
                    val imei = Settings.System.getString(
                        applicationContext?.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                    val crcimei = toHexStr(CRC64.digest(imei.toByteArray()).bytes)
                    val useorigimei = personaInfolBinding.sbOriginalimei.isChecked
                    if (useorigimei && imei != userInfoBean.imei) {
                        userInfoBean.imei = imei
                        userInfoBean.useorigimei = useorigimei
                        changed = true
                    }
                    if (!useorigimei && crcimei != userInfoBean.imei) {
                        userInfoBean.imei = crcimei
                        userInfoBean.useorigimei = useorigimei
                        changed = true
                    }
                    if (changed)
                        userInfoBean.commit(this)
                }
                pageArea -> {
                    val region: String = personaInfolBinding.etArea.text.toString()
                    if (region != userInfoBean.region) {
                        userInfoBean.region = region
                        userInfoBean.adcode = adcode
                        userInfoBean.commit(this)
                    }
                }
                pageAreaDetail -> {
                    val address: String = personaInfolBinding.etAddress.text.toString()
                    if (address != userInfoBean.addr) {
                        userInfoBean.addr = address
                        userInfoBean.commit(this)
                    }
                }
                pageEmerg -> {
                    val emergName: String = personaInfolBinding.etEmergName.text.toString()
                    val emergPhoneNum: String = personaInfolBinding.etEmergPhoneNum.text.toString()
                    if (emergName != userInfoBean.urgentContactname || emergPhoneNum != userInfoBean.urgentContactmob) {
                        userInfoBean.urgentContactname = emergName
                        userInfoBean.urgentContactmob = emergPhoneNum
                        userInfoBean.commit(this)
                    }
                }
                pageContacts -> {
                    val qq: String = personaInfolBinding.etQqNum.text.toString()
                    val wx: String = personaInfolBinding.etWxNum.text.toString()
                    val email: String = personaInfolBinding.etEmailNum.text.toString()
                    if (!TextUtils.isEmpty(email) && !stringIsEmail(email)) {
                        Toast.makeText(this@PersonalInfoAddActivity, "邮箱格式不对", Toast.LENGTH_SHORT)
                            .show()
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

    private fun initPages() {
        when (pageType) {
            pageBase -> {
                personaInfolBinding.piTitle.tvTitle.text = "基础信息"
                personaInfolBinding.clBaseCont.visibility = View.VISIBLE
                personaInfolBinding.etName.setText(userInfoBean.name)
                personaInfolBinding.etIdNum.setText(userInfoBean.id)
                personaInfolBinding.etPhoneNum.setText(userInfoBean.mobileNumber)
                personaInfolBinding.etAccountNum.setText(userInfoBean.accountId)
                personaInfolBinding.sbOriginalimei.isChecked = userInfoBean.useorigimei
                if (TextUtils.isEmpty(userInfoBean.accountId))
                    personaInfolBinding.etAccountNum.inputType = InputType.TYPE_NULL

                val params: ViewGroup.MarginLayoutParams =
                    personaInfolBinding.btnConfirm.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin = dp2px.dp2px(280)
                personaInfolBinding.btnConfirm.layoutParams = params
                personaInfolBinding.btnConfirm.requestLayout()
            }
            pageArea -> {
                personaInfolBinding.piTitle.tvTitle.text = "地址"
                personaInfolBinding.clAreaCont.visibility = View.VISIBLE
                personaInfolBinding.etArea.text = userInfoBean.region
            }
            pageAreaDetail -> {
                personaInfolBinding.piTitle.tvTitle.text = "详细地址"
                personaInfolBinding.clAreaDetailContent.visibility = View.VISIBLE
                personaInfolBinding.etAddress.setText(userInfoBean.addr)
            }
            pageEmerg -> {
                personaInfolBinding.piTitle.tvTitle.text = "紧急联系人"
                personaInfolBinding.clEmergCont.visibility = View.VISIBLE
                personaInfolBinding.etEmergName.setText(userInfoBean.urgentContactname)
                personaInfolBinding.etEmergPhoneNum.setText(userInfoBean.urgentContactmob)
            }
            pageContacts -> {
                personaInfolBinding.piTitle.tvTitle.text = "社交通讯信息"
                personaInfolBinding.clContactsCont.visibility = View.VISIBLE
                personaInfolBinding.etQqNum.setText(userInfoBean.qq)
                personaInfolBinding.etWxNum.setText(userInfoBean.wechat)
                personaInfolBinding.etEmailNum.setText(userInfoBean.email)
            }
        }
    }
}