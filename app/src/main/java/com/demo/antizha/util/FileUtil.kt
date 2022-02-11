package com.demo.antizha.util

import android.text.TextUtils
import com.demo.antizha.BuildConfig
import com.demo.antizha.getDataByGet
import com.demo.antizha.saveBuff2File
import com.demo.antizha.ui.Hicore
import com.hjq.toast.ToastUtils
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

object FileUtil {
    fun openfile(name: String): InputStream {
        val extPath = Hicore.context.getExternalFilesDir(null)?.path
        val file = File(extPath, name)
        if (file.canRead())
            return FileInputStream(file)
        val assetManager = Hicore.app.resources.assets
        return assetManager.open(name)
    }

    fun update() {
        val positionsUrl = BuildConfig.RELEASE_API_URL + "/api/Position/positions"
        getDataByGet(positionsUrl, true, "positions.txt", ::onNormalSave)
        val noticeListUrl = BuildConfig.RELEASE_API_URL + "/api/Notice/getnoticelistforuser"
        getDataByGet(noticeListUrl, true, "noticelist.txt", ::onNormalSave)
        val saccTypesUrl = BuildConfig.RELEASE_API_URL + "/api/EvidenceType/getsocialaccounttypes"
        getDataByGet(saccTypesUrl, true, "socialaccounttypes.txt", ::onNormalSave)
        val paymenttypesUrl = BuildConfig.RELEASE_API_URL + "/api/EvidenceType/getpaymenttypes"
        getDataByGet(paymenttypesUrl, true, "paymenttypes.txt", ::onNormalSave)
        val qaListUrl = BuildConfig.RELEASE_API_URL + "/api/QA/getqalist"
        getDataByGet(qaListUrl, true, "qalist.txt", ::onNormalSave)
        val bannerUrl = BuildConfig.RELEASE_API_URL + "/api/Banner"
        getDataByGet(bannerUrl, true, "bander.txt", ::onNormalSave)
        //进入警察界面好像是实名认证的时候会自动根据数据库记录判断你是不是警察，是的话，在首页上会显示一个警察专用图标，
        //点击以后再进入警察专用登陆界面，再次登陆后，才可以打开警察界面，可以进行报案登记什么的.
        //CaseActivity里用到，CaseActivity由PoliceInfoActivity调用，可能是警察报案登记界面用的？
        val xccasecategorysUrl = BuildConfig.RELEASE_API_URL + "/api/xc/getxccasecategorys"
        getDataByGet(xccasecategorysUrl, true, "xccasecategorys.txt", ::onNormalSave)
        //SnapCardReportActivity里用的，可能是信用卡诈骗的类型
        val casecategorysUrl = BuildConfig.RELEASE_API_URL + "/api/DK/getcasecategorys"
        getDataByGet(casecategorysUrl, true, "casecategorys.txt", ::onNormalSave)
        //默认的诈骗类型数据
        val evidenceTypeUrl = BuildConfig.RELEASE_API_URL + "/api/EvidenceType"
        getDataByGet(evidenceTypeUrl, true, "EvidenceType.txt", ::onNormalSave)
    }

    private fun onNormalSave(data: String, saveFile: String) {
        if (data[0] != '{')
            return
        if (!TextUtils.isEmpty(saveFile)) {
            ToastUtils.cancel()
            ToastUtils.show("savefile:$saveFile")
            saveBuff2File(data, saveFile)
        }
    }
}