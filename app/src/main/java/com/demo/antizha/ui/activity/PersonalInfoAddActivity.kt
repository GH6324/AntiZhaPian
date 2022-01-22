package com.demo.antizha.ui.activity

import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import cn.qqtheme.framework.entity.City
import cn.qqtheme.framework.entity.County
import cn.qqtheme.framework.entity.Province
import cn.qqtheme.framework.picker.AddressPicker
import com.demo.antizha.R
import com.demo.antizha.UserInfoBean
import com.demo.antizha.databinding.ActivityPersonaInfolBinding
import com.demo.antizha.ui.Hicore
import com.demo.antizha.util.AESUtil
import com.demo.antizha.util.CRC64
import com.demo.antizha.util.SpUtils
import qiu.niorgai.StatusBarCompat
import java.util.regex.Pattern


class PersonalInfoAddActivity : BaseActivity(), AddressPicker.OnAddressPickListener {
    companion object {
        const val pageBase = "Base"
        const val pageArea = "Area"
        const val pageAreaDetail = "AreaDetail"
        const val pageEmerg = "Emerg"
        const val pageContacts = "Contacts"
    }

    private var pageType: String = ""
    private var adcode: String = ""
    private var provinces: ArrayList<Province> = ArrayList<Province>()
    private lateinit var personaInfolBinding: ActivityPersonaInfolBinding
    override fun onAddressPicked(province: Province?, city: City?, county: County?) {
        personaInfolBinding.etArea.text = "${province?.name}.${city?.name}.${county?.name}"
        if (county != null) {
            adcode = county.areaId
        }
    }

    /*AddressPicker新版本代码
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
    */
    @RequiresApi(Build.VERSION_CODES.M)
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
            /*
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
            */
            val picker = AddressPicker(this, provinces)
            picker.setHideProvince(false)
            picker.setHideCounty(false)
            picker.setTextColor(mActivity!!.getResources().getColor(R.color.colorGray, null))
            picker.setSubmitTextColor(mActivity!!.getResources().getColor(R.color.black, null))
            picker.setCancelTextColor(mActivity!!.getResources().getColor(R.color.colorGray, null))
            picker.setDividerColor(mActivity!!.getResources().getColor(R.color.colorGray, null))
            picker.setColumnWeight(0.25f, 0.5f, 0.25f)
            picker.setOnAddressPickListener(this)
            val region: String = personaInfolBinding.etArea.text.toString()
            if (!TextUtils.isEmpty(region)) {
                val regions = TextUtils.split(region, "\\.")
                if (regions.size == 3) {
                    picker.setSelectedItem(regions[0], regions[1], regions[2])
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
                    if (name != UserInfoBean.name || id != UserInfoBean.id || mobileNumber != UserInfoBean.mobileNumber) {
                        UserInfoBean.name = name
                        UserInfoBean.id = id
                        UserInfoBean.mobileNumber = mobileNumber
                        changed = true
                    }
                    if (accountId != UserInfoBean.accountId) {
                        UserInfoBean.accountId = accountId
                    }
                    val imei = Settings.System.getString(
                        applicationContext?.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                    val crcimei = AESUtil.byteArray2HexStr(CRC64.digest(imei.toByteArray()).bytes)
                    val useorigimei = personaInfolBinding.sbOriginalimei.isChecked
                    if (useorigimei && imei != UserInfoBean.imei) {
                        UserInfoBean.imei = imei
                        UserInfoBean.useorigimei = useorigimei
                        changed = true
                    }
                    if (!useorigimei && crcimei != UserInfoBean.imei) {
                        UserInfoBean.imei = crcimei
                        UserInfoBean.useorigimei = useorigimei
                        changed = true
                    }
                    if (changed)
                        UserInfoBean.commit()
                }
                pageArea -> {
                    val region: String = personaInfolBinding.etArea.text.toString()
                    if (region != UserInfoBean.region) {
                        UserInfoBean.region = region
                        UserInfoBean.adcode = adcode
                        UserInfoBean.commit()
                    }
                }
                pageAreaDetail -> {
                    val address: String = personaInfolBinding.etAddress.text.toString()
                    if (address != UserInfoBean.addr) {
                        UserInfoBean.addr = address
                        UserInfoBean.commit()
                    }
                }
                pageEmerg -> {
                    val emergName: String = personaInfolBinding.etEmergName.text.toString()
                    val emergPhoneNum: String = personaInfolBinding.etEmergPhoneNum.text.toString()
                    if (emergName != UserInfoBean.urgentContactname || emergPhoneNum != UserInfoBean.urgentContactmob) {
                        UserInfoBean.urgentContactname = emergName
                        UserInfoBean.urgentContactmob = emergPhoneNum
                        UserInfoBean.commit()
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
                    if (qq != UserInfoBean.qq || wx != UserInfoBean.wechat || email != UserInfoBean.email) {
                        UserInfoBean.qq = qq
                        UserInfoBean.wechat = wx
                        UserInfoBean.email = email
                        UserInfoBean.commit()
                    }
                }
            }
            finish()
        }
    }

    fun initProvinceList() {
        var provs = UserInfoBean.parseAddress()
        if (provs != null) {
            for (prov in provs) {
                var province = Province()
                province.areaId = prov.code
                province.areaName = prov.name
                for (cit in prov.cityList) {
                    var city = City()
                    city.areaId = cit.code
                    city.areaName = cit.name
                    city.provinceId = prov.code
                    for (town in cit.townList) {
                        var county = County()
                        county.areaId = town.code
                        county.areaName = town.name
                        county.cityId = cit.code
                        city.counties.add(county)
                    }
                    province.cities.add(city)
                }
                provinces.add(province)
            }
        }
    }

    fun stringIsEmail(str: String): Boolean {
        return Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$").matcher(str)
            .matches()
    }

    fun stringIsUserID(str: String): Boolean {
        return Pattern.compile("^[1-6][0-9X]$").matcher(str).matches()
    }

    fun stringIsMobileNumber(str: String): Boolean {
        return Pattern.compile("^1[0-9]{4}$").matcher(str).matches()
    }

    private fun initPages() {
        when (pageType) {
            pageBase -> {
                personaInfolBinding.piTitle.tvTitle.text = "基础信息"
                personaInfolBinding.clBaseCont.visibility = View.VISIBLE
                personaInfolBinding.etName.setText(UserInfoBean.name)
                personaInfolBinding.etIdNum.setText(UserInfoBean.id)
                personaInfolBinding.etPhoneNum.setText(UserInfoBean.mobileNumber)
                personaInfolBinding.etAccountNum.setText(UserInfoBean.accountId)
                personaInfolBinding.sbOriginalimei.isChecked = UserInfoBean.useorigimei
                if (TextUtils.isEmpty(UserInfoBean.accountId))
                    personaInfolBinding.etAccountNum.inputType = InputType.TYPE_NULL

                val params: ViewGroup.MarginLayoutParams =
                    personaInfolBinding.btnConfirm.layoutParams as ViewGroup.MarginLayoutParams
                params.topMargin =
                    (280 * Hicore.context.resources.displayMetrics.density + 0.5f).toInt()
                personaInfolBinding.btnConfirm.layoutParams = params
                personaInfolBinding.btnConfirm.requestLayout()
            }
            pageArea -> {
                personaInfolBinding.piTitle.tvTitle.text = "地址"
                personaInfolBinding.clAreaCont.visibility = View.VISIBLE
                personaInfolBinding.etArea.text = UserInfoBean.region
                if (provinces.size == 0)
                    Handler(Looper.getMainLooper()).postDelayed({
                        initProvinceList()
                    }, 10)
            }
            pageAreaDetail -> {
                personaInfolBinding.piTitle.tvTitle.text = "详细地址"
                personaInfolBinding.clAreaDetailContent.visibility = View.VISIBLE
                personaInfolBinding.etAddress.setText(UserInfoBean.addr)
            }
            pageEmerg -> {
                personaInfolBinding.piTitle.tvTitle.text = "紧急联系人"
                personaInfolBinding.clEmergCont.visibility = View.VISIBLE
                personaInfolBinding.etEmergName.setText(UserInfoBean.urgentContactname)
                personaInfolBinding.etEmergPhoneNum.setText(UserInfoBean.urgentContactmob)
            }
            pageContacts -> {
                personaInfolBinding.piTitle.tvTitle.text = "社交通讯信息"
                personaInfolBinding.clContactsCont.visibility = View.VISIBLE
                personaInfolBinding.etQqNum.setText(UserInfoBean.qq)
                personaInfolBinding.etWxNum.setText(UserInfoBean.wechat)
                personaInfolBinding.etEmailNum.setText(UserInfoBean.email)
            }
        }
    }
}