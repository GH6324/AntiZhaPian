package com.demo.antizha

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.provider.Settings
import android.text.TextUtils
import com.demo.antizha.util.CRC64
import java.text.SimpleDateFormat
import java.util.regex.Pattern

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

class UserInfoBean() {
    var perfectProgress: Int = 0     //进度
    var accountId: String = ""      //账号ID
    var imei: String = ""            //设备码
    var useorigimei: Boolean = false//是否使用原始的机器码
    var name: String = ""           //名字
    var id: String = ""             //身份证前后2个字符
    var mobileNumber: String = ""   //电话前2后3
    var region: String = ""         //地区
    var adcode: String = ""         //地区码，比如 安徽省淮北市杜集区 adcode:340602
    var addr: String = ""           //详细地址
    var professionName: String = "" //职业
    var urgentContactname: String = ""  //紧急联系人姓名
    var urgentContactmob: String = ""   //紧急联系人电话
    var qq: String = ""
    var wechat: String = ""
    var email: String = ""
    fun Init(context: Context) {
        val settings: SharedPreferences = context.getSharedPreferences("setting", 0)
        accountId = settings.getString("account", "").toString()
        imei = settings.getString("imei", "").toString()
        useorigimei = settings.getBoolean("originalimei", false)
        if (TextUtils.isEmpty(imei)) {
            val timei =
                Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
            imei = if (useorigimei) timei else toHexStr(CRC64.digest(timei.toByteArray()).bytes)
        }
        name = settings.getString("name", "").toString()
        id = settings.getString("id", "").toString()
        mobileNumber = settings.getString("phone", "").toString()
        region = settings.getString("region", "").toString()
        adcode = settings.getString("adcode", "").toString()
        addr = settings.getString("address", "").toString()
        professionName = settings.getString("work", "").toString()
        urgentContactname = settings.getString("emergency_name", "").toString()
        urgentContactmob = settings.getString("emergency_phone", "").toString()
        qq = settings.getString("qq", "").toString()
        wechat = settings.getString("wechat", "").toString()
        email = settings.getString("mail", "").toString()
        CalcProgress()
    }

    fun commit(context: Context) {
        val settings: SharedPreferences = context.getSharedPreferences("setting", 0)
        var editor: SharedPreferences.Editor = settings.edit()
        editor.putString("account", accountId)
        editor.putString("imei", imei)
        editor.putBoolean("originalimei", useorigimei)
        editor.putString("name", name)
        editor.putString("id", id)
        editor.putString("phone", mobileNumber)
        editor.putString("region", region)
        editor.putString("adcode", adcode)
        editor.putString("address", addr)
        editor.putString("work", professionName)
        editor.putString("emergency_name", urgentContactname)
        editor.putString("emergency_phone", urgentContactmob)
        editor.putString("qq", qq)
        editor.putString("wechat", wechat)
        editor.putString("mail", email)
        editor.apply()
        CalcProgress()
    }

    fun CalcProgress() {
        perfectProgress = 0
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(id)) {
            perfectProgress += 30
        }
        if (!TextUtils.isEmpty(region)) {
            perfectProgress += 10
        }
        if (!TextUtils.isEmpty(addr)) {
            perfectProgress += 10
        }
        if (!TextUtils.isEmpty(professionName)) {
            perfectProgress += 5
        }
        if (!TextUtils.isEmpty(urgentContactmob)) {
            perfectProgress += 20
        }
        if (!TextUtils.isEmpty(qq)) {
            perfectProgress += 10
        }
        if (!TextUtils.isEmpty(wechat)) {
            perfectProgress += 10
        }
        if (!TextUtils.isEmpty(email)) {
            perfectProgress += 5
        }
        if (perfectProgress >= 100) {
            perfectProgress = 100
        }
    }
    fun isVerified(): Boolean {
        if (TextUtils.isEmpty(name))
            return false
        if (TextUtils.isEmpty(id))
            return false
        if (TextUtils.isEmpty(mobileNumber))
            return false
        if (TextUtils.isEmpty(accountId))
            return false
        return true
    }
}

val userInfoBean: UserInfoBean = UserInfoBean()

class AddressBean() {
    val cityList: ArrayList<AddressBean> = ArrayList<AddressBean>()
    val code: String = ""
    val name: String = ""
    val townList: ArrayList<AddressBean> = ArrayList<AddressBean>()
}

//地区的基类
open class AreaBase() {
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

class Dp2Px {
    constructor(context: Context) {
        density = context.resources.displayMetrics.density
    }

    var density: Float = 0.0F
    fun dp2px(value: Int): Int {
        return (value * density + 0.5).toInt()
    }
}

lateinit var dp2px: Dp2Px

fun toHexStr(byteArray: ByteArray) =
    with(StringBuilder()) {
        byteArray.forEach {
            val hex = it.toInt() and (0xFF)
            val hexStr = Integer.toHexString(hex)
            if (hexStr.length == 1) append("0").append(hexStr)
            else append(hexStr)
        }
        toString()
    }

fun getRoundBitmapByShader(
    bitmap: Bitmap?,
    outWidth: Int,
    outHeight: Int,
    radius: Float,
    boarder: Float
): Bitmap? {
    if (bitmap == null) {
        return null
    }
    val height = bitmap.height
    val width = bitmap.width
    val widthScale = outWidth * 1f / width
    val heightScale = outHeight * 1f / height
    val matrix = Matrix()
    matrix.setScale(widthScale, heightScale)
    //创建输出的bitmap
    val desBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888)
    //创建canvas并传入desBitmap，这样绘制的内容都会在desBitmap上
    val canvas = Canvas(desBitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    //创建着色器
    val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    //给着色器配置matrix
    bitmapShader.setLocalMatrix(matrix)
    paint.setShader(bitmapShader)
    //创建矩形区域并且预留出border
    val rect = RectF(
        boarder.toFloat(), boarder.toFloat(),
        (outWidth - boarder).toFloat(), (outHeight - boarder).toFloat()
    )
    //把传入的bitmap绘制到圆角矩形区域内
    canvas.drawRoundRect(rect, radius, radius, paint)
    if (boarder > 0) {
        //绘制boarder
        val boarderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        boarderPaint.setColor(Color.GREEN)
        boarderPaint.setStyle(Paint.Style.STROKE)
        boarderPaint.setStrokeWidth(boarder)
        canvas.drawRoundRect(rect, radius, radius, boarderPaint)
    }
    return desBitmap
}

fun str2time(str: String): Long {
    try {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str)
        return if (date == null) 0 else date.getTime()
    } catch (e2: Exception) {
        e2.printStackTrace()
        return 0
    }
}

fun optimizationTimeStr(str: String): String {
    if (TextUtils.isEmpty(str)) {
        return ""
    }
    val currentTimeMillis: Long = (System.currentTimeMillis() - str2time(str)) / 1000
    if (currentTimeMillis < 30) {
        return "刚刚"
    } else if (currentTimeMillis < 3600) {
        return (currentTimeMillis / 60).toString() + "分钟前"
    } else if (currentTimeMillis >= 86400) {
        return str.substring(0, 11)
    } else {
        return (currentTimeMillis / 3600).toString() + "小时前"
    }
}
