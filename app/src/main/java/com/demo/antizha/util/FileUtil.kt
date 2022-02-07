package com.demo.antizha.util

import android.text.TextUtils
import com.demo.antizha.getDataByGet
import com.demo.antizha.saveBuff2File
import com.demo.antizha.ui.Hicore
import com.hjq.toast.ToastUtils
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

object FileUtil {
    fun openfile(name: String): InputStream {
        val extPath = Hicore.context.getExternalFilesDir(null)?.getPath()
        val file = File(extPath, name)
        if (file.canRead())
            return FileInputStream(file)
        val assetManager = Hicore.app.getResources().getAssets()
        return assetManager.open(name)
    }

    fun update() {
        val positionsUrl = "https://fzapp.gjfzpt.cn/hicore/api/Position/positions"
        getDataByGet(positionsUrl, true, "positions.txt", ::onNormalSave)
        val noticeListUrl = "https://fzapp.gjfzpt.cn/hicore/api/Notice/getnoticelistforuser"
        getDataByGet(noticeListUrl, true, "noticelist.txt", ::onNormalSave)
        val saccTypesUrl = "https://fzapp.gjfzpt.cn/hicore/api/EvidenceType/getsocialaccounttypes"
        getDataByGet(saccTypesUrl, true, "socialaccounttypes.txt", ::onNormalSave)
        val paymenttypesUrl = "https://fzapp.gjfzpt.cn/hicore/api/EvidenceType/getpaymenttypes"
        getDataByGet(paymenttypesUrl, true, "paymenttypes.txt", ::onNormalSave)
        val qaListUrl = "https://fzapp.gjfzpt.cn/hicore/api/QA/getqalist"
        getDataByGet(qaListUrl, true, "qalist.txt", ::onNormalSave)
        val evidenceTypeUrl = "https://fzapp.gjfzpt.cn/hicore//api/xc/getxccasecategorys"
        getDataByGet(evidenceTypeUrl, true, "xccasecategorys.txt", ::onNormalSave)
        val casecategorysUrl = "https://fzapp.gjfzpt.cn/hicore/api/DK/getcasecategorys"
        getDataByGet(casecategorysUrl, true, "casecategorys.txt", ::onNormalSave)
        val bannerUrl = "https://fzapp.gjfzpt.cn/hicore/api/Banner"
        getDataByGet(bannerUrl, true, "bander.txt", ::onNormalSave)
    }

    fun onNormalSave(data: String, saveFile: String) {
        if (data[0] != '{')
            return
        if (!TextUtils.isEmpty(saveFile)) {
            ToastUtils.cancel()
            ToastUtils.show("savefile:" + saveFile)
            saveBuff2File(data, saveFile)
        }
    }
}