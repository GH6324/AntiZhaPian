package com.demo.antizha.util

import android.app.Activity
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import cn.qqtheme.framework.entity.City
import cn.qqtheme.framework.entity.County
import cn.qqtheme.framework.entity.Province
import cn.qqtheme.framework.picker.AddressPicker
import cn.qqtheme.framework.picker.AddressPicker.OnAddressPickListener
import com.demo.antizha.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader


object AddressBean {
    open class HiAddressListener : OnAddressPickListener {
        override fun onAddressPicked(province: Province?, city: City?, county: County?) {}
        open fun onClear() {}
        open fun onData(arrayList: ArrayList<Province?>?) {}
    }

    open class AreaBase() {
        var code: String = ""
        var name: String = ""
    }

    //为了避免和库里的命名重了，加个C
    //区
    class HiDistrict : AreaBase() {
        var longitude: String = ""
        var latitude: String = ""
    }

    //市
    class HiCity : AreaBase() {
        var townList: List<HiDistrict> = ArrayList()
    }

    //省
    class HiProvince : AreaBase() {
        var cityList: List<HiCity> = ArrayList()
    }

    var hiProvinces: List<HiProvince> = ArrayList<HiProvince>() //address直接导出的结构
    var provinces: ArrayList<Province> = ArrayList<Province>()  //地区选择器使用的结构
    fun getHiProvince(): List<HiProvince> {
        if (hiProvinces.size == 0) {
            val inputStream = FileUtil.openfile("address.txt")
            hiProvinces = Gson().fromJson(InputStreamReader(inputStream, "UTF-8"),
                object : TypeToken<List<HiProvince>>() {}.type)
            initProvinceList()
            inputStream.close()
        }
        return hiProvinces
    }

    fun getProvince(): ArrayList<Province> {
        if (hiProvinces.size == 0) {
            val inputStream = FileUtil.openfile("address.txt")
            hiProvinces = Gson().fromJson(InputStreamReader(inputStream, "UTF-8"),
                object : TypeToken<List<HiProvince>>() {}.type)
            initProvinceList()
            inputStream.close()
        }
        return provinces
    }

    fun createAddressPicker(activity: Activity,
                            region: String,
                            showClear: Boolean,
                            listener: HiAddressListener): AddressPicker {
        val picker = AddressPicker(activity, getProvince())
        picker.setHideProvince(false)
        picker.setHideCounty(false)
        picker.setTextColor(activity.getResources()
            .getColor(com.demo.antizha.R.color.colorGray, null))
        picker.setSubmitTextColor(activity.getResources().getColor(R.color.black, null))
        picker.setCancelTextColor(activity.getResources().getColor(R.color.colorGray, null))
        picker.setDividerColor(activity.getResources().getColor(R.color.colorGray, null))
        picker.setColumnWeight(0.25f, 0.5f, 0.25f)
        if (showClear) {
            val tvTtile = TextView(picker.getContext())
            tvTtile.layoutParams = LinearLayout.LayoutParams(-1, -1)
            tvTtile.visibility = View.VISIBLE
            tvTtile.text = "清空"
            tvTtile.gravity = Gravity.CENTER
            tvTtile.setTextColor(activity.getResources().getColor(R.color.black, null))
            tvTtile.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    picker.dismiss()
                    listener.onClear()
                }
            })
            picker.setTitleView(tvTtile)
        }
        if (!TextUtils.isEmpty(region)) {
            val regions = TextUtils.split(region, "\\.")
            if (regions.size == 3) {
                picker.setSelectedItem(regions[0], regions[1], regions[2])
            }
        }
        picker.setOnAddressPickListener(listener)
        return picker
    }

    fun initProvinceList() {
        for (prov in hiProvinces) {
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