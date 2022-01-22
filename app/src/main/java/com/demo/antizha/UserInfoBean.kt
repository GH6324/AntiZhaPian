package com.demo.antizha

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.provider.Settings
import android.text.TextUtils
import com.beust.klaxon.Klaxon
import com.demo.antizha.ui.Hicore
import com.demo.antizha.util.CRC64
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList


object UserInfoBean {
    var perfectProgress: Int = 0     //进度
    var accountId: String = ""      //账号ID
    var imei: String = ""            //设备码
    var useorigimei: Boolean = false//是否使用原始的机器码
    var name: String = ""           //名字
    var id: String = ""             //身份证前后2个字符
    var mobileNumber: String = ""   //电话前2后3
    var region: String = ""         //地区,比如 安徽省.淮北市.杜集区
    var adcode: String = ""         //地区码，比如 安徽省淮北市杜集区 adcode:340602
    var addr: String = ""           //详细地址
    var professionName: String = "" //职业
    var urgentContactname: String = ""  //紧急联系人姓名
    var urgentContactmob: String = ""   //紧急联系人电话
    var qq: String = ""
    var wechat: String = ""
    var email: String = ""
    var acctoken: String = ""           //用户TOKEN只能正常注册登录后获得，否则服务器会拒绝
    var longitude: String = ""
    var latitude: String = ""
    var refTudeTime: String = ""
    fun Init() {
        val context: Context = Hicore.context
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
        longitude = settings.getString("longitude", "").toString()
        latitude = settings.getString("latitude", "").toString()
        refTudeTime = settings.getString("refTudeTime", "").toString()

        if (TextUtils.isEmpty(region)) {
            region = "北京市.北京市.东城区"
            adcode = "110101"
            checkLongitudeLatitude()
        } else
            checkLongitudeLatitude()
        getToken()
        CalcProgress()
    }

    fun commit() {
        val context: Context = Hicore.context
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
        editor.putString("longitude", longitude)
        editor.putString("latitude", latitude)
        editor.putString("refTudeTime", refTudeTime)
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

    fun checkLongitudeLatitude() {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd") // HH:mm:ss
        val currentDate = Date(System.currentTimeMillis())
        val dateString = simpleDateFormat.format(currentDate)
        if (!dateString.equals(refTudeTime)) {
            val regions = TextUtils.split(region, "\\.")
            if (regions.size != 3)
                return
            val provinces: List<CProvince>? = parseAddress()
            if (provinces == null)
                return
            for (province in provinces) {
                if (!province.name.equals(regions[0]))
                    continue
                for (city in province.cityList) {
                    if (!city.name.equals(regions[1]))
                        continue
                    for (town in city.townList) {
                        if (town.name.equals(regions[2])) {
                            val random = Random()
                            var flongitude =
                                town.longitude.toFloat() + (random.nextInt(200) - 100).toFloat() / 1000.0F
                            var flatitude =
                                town.latitude.toFloat() + (random.nextInt(200) - 100).toFloat() / 1000.0F
                            longitude = flongitude.toString()
                            latitude = flatitude.toString()
                            refTudeTime = dateString
                            commit()
                        }
                    }
                }
            }
        }
    }

    fun getToken() {
        try {
            val path =
                Hicore.context.getExternalFilesDir(null)?.getPath();
            val file = File(path, "anote_national.xml")
            //file.exists()总是返回false
            if (!file.canRead())
                return
            val iStream = FileInputStream(file)
            val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
            val builder: DocumentBuilder = factory.newDocumentBuilder()
            val document: Document = builder.parse(iStream)
            val stringList: NodeList = document.getElementsByTagName("string")
            for (i in 0..stringList.getLength() - 1) {
                val node = stringList.item(i)
                val nodeName = node.attributes.item(0).getTextContent()
                if (nodeName.equals("sp_user_bean")) {
                    class TokenPackage(val token: String)

                    val value = node.getTextContent()
                    val token = Klaxon().parse<TokenPackage>(value)
                    if (token != null) {
                        acctoken = token.token
                    }
                }
            }
        } catch (err: Exception) {

        }
    }

    //地区的基类
    open class AreaBase() {
        var code: String = ""
        var name: String = ""
    }

    //为了避免和库里的命名重了，加个C
//区
    class CDistrict : AreaBase() {
        var longitude: String = ""
        var latitude: String = ""
    }

    //市
    class CCity : AreaBase() {
        var townList: List<CDistrict> = ArrayList()
    }

    //省
    class CProvince : AreaBase() {
        var cityList: List<CCity> = ArrayList()
    }

    fun parseAddress(): List<CProvince>? {
        val inputStream = Hicore.app.getResources().getAssets().open("address.txt");
        return Klaxon().parseArray<CProvince>(inputStream)
    }

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
