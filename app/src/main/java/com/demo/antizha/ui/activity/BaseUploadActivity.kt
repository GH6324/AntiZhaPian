package com.demo.antizha.ui.activity

import android.widget.TextView
import com.demo.antizha.R
import com.demo.antizha.ui.Hicore

abstract class BaseUploadActivity : BaseActivity() {
    companion object {
        const val UPLOAD_STATE_LOADING = 0
        const val UPLOAD_STATE_UPLOAD = 1
        const val UPLOAD_STATE_SUCCESS = 2
        const val UPLOAD_STATE_FAIL = 3

        fun showUpState(textView: TextView, uploadStateInfo: UploadStateInfo) {
            val uploadState = uploadStateInfo.uploadState
            if (uploadState == UPLOAD_STATE_LOADING) {
                textView.text = "等待上传"
                textView.setTextColor(Hicore.app.getResources().getColor(R.color.colorGray, null))
            } else if (uploadState == UPLOAD_STATE_UPLOAD) {
                textView.text = "上传中"
                textView.setTextColor(Hicore.app.getResources().getColor(R.color.black_dark, null))
            } else if (uploadState == UPLOAD_STATE_SUCCESS) {
                textView.text = "上传完成"
                textView.setTextColor(Hicore.app.getResources().getColor(R.color.blue, null))
            } else if (uploadState == UPLOAD_STATE_FAIL) {
                textView.text = "上传失败"
                textView.setTextColor(Hicore.app.getResources().getColor(R.color.colorRed, null))
            }
        }
    }

    class UploadStateInfo {
        var fileId: String? = null
        var fileName: String? = null
        var filePath: String? = null
        var fileSize: Long = 0
        var isPlayState = false
        var progress: Long = 0
        var total: Long = 0
        var uploadState = 0

        constructor() {}
        constructor(j: Long) {
            fileSize = j
        }
    }

    var mUploadStateList: ArrayList<UploadStateInfo> = ArrayList()

}