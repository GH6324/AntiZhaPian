package com.demo.antizha

import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.demo.antizha.databinding.ActivityMinePersonalBinding
import java.util.regex.Pattern

fun stringIsEmail(str:String):Boolean{
    return Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$").matcher(str).matches();
}
fun stringIsUserID(str:String):Boolean{
    return Pattern.compile("^[1-6][0-9X]$").matcher(str).matches();
}
fun stringIsMobileNumber(str:String):Boolean{
    return Pattern.compile("^1[0-9]{4}$").matcher(str).matches();
}
class UserInfoBean(){
    var perfectProgress:Int = 0     //进度
    var name: String = ""           //名字
    var id: String = ""             //身份证前后2个字符
    var mobileNumber: String = ""   //电话前2后3
    var region: String = ""         //地区
    var addr: String = ""           //详细地址
    var professionName: String = "" //职业
    var urgentContactname: String = ""  //紧急联系人姓名
    var urgentContactmob: String = ""   //紧急联系人电话
    var qq: String = ""
    var wechat: String = ""
    var email: String = ""
    fun Init(activity: FragmentActivity){
        val settings: SharedPreferences = activity.getSharedPreferences("setting", 0)
        name = settings.getString("name", "").toString()
        id = settings.getString("id", "").toString()
        mobileNumber = settings.getString("phone", "").toString()
        region = settings.getString("region", "").toString()
        addr = settings.getString("address", "").toString()
        professionName = settings.getString("work", "").toString()
        urgentContactname = settings.getString("emergency_name", "").toString()
        urgentContactmob = settings.getString("emergency_phone", "").toString()
        qq = settings.getString("qq", "").toString()
        wechat = settings.getString("wechat", "").toString()
        email = settings.getString("mail", "").toString()
        CalcProgress()
    }
    fun commit(activity: FragmentActivity){
        val settings: SharedPreferences = activity.getSharedPreferences("setting", 0)
        var editor: SharedPreferences.Editor = settings.edit()
        editor.putString("name", name)
        editor.putString("id", id)
        editor.putString("phone", mobileNumber)
        editor.putString("region", region)
        editor.putString("address", addr)
        editor.putString("work", professionName)
        editor.putString("emergency_name",urgentContactname)
        editor.putString("emergency_phone",urgentContactmob)
        editor.putString("qq",qq)
        editor.putString("wechat",wechat)
        editor.putString("mail",email)
        editor.apply()
        CalcProgress()
    }
    fun CalcProgress(){
        perfectProgress = 0
        if (!TextUtils.isEmpty(userInfoBean.name) && !TextUtils.isEmpty(userInfoBean.id)) {
            perfectProgress += 30
        }
        if (!TextUtils.isEmpty(userInfoBean.region)) {
            perfectProgress += 10;
        }
        if (!TextUtils.isEmpty(userInfoBean.addr)) {
            perfectProgress += 10;
        }
        if (!TextUtils.isEmpty(userInfoBean.professionName)) {
            perfectProgress += 5;
        }
        if (!TextUtils.isEmpty(userInfoBean.urgentContactmob)) {
            perfectProgress += 20;
        }
        if (!TextUtils.isEmpty(userInfoBean.qq)) {
            perfectProgress += 10;
        }
        if (!TextUtils.isEmpty(userInfoBean.wechat)) {
            perfectProgress += 10;
        }
        if (!TextUtils.isEmpty(userInfoBean.email)) {
            perfectProgress += 5;
        }
        if (perfectProgress >= 100) {
            perfectProgress = 100;
        }
    }
}
val userInfoBean:UserInfoBean = UserInfoBean()

class AddressBean(){
    val cityList: ArrayList<AddressBean> = ArrayList<AddressBean>()
    val code: String = ""
    val name: String = ""
    val townList: ArrayList<AddressBean> = ArrayList<AddressBean>()
}
//地区的基类
open class AreaBase(){
    var areaId: String = ""
    var areaName: String = ""
}
//区
class District : AreaBase() {
    var cityId: String = ""
}
//市
class City : AreaBase() {
    var counties: List<District> = ArrayList()
}
//省
class Province : AreaBase() {
    var citys: List<City> = ArrayList()
}