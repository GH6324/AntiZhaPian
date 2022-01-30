package com.demo.antizha.ui.activity

import android.os.Build
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivityPictureBinding
import com.demo.antizha.ui.Hicore
import com.demo.antizha.ui.Hicore.Companion.context


class PictureSelectAdapter(resId: Int, var medias: ArrayList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(resId, medias) {
    companion object {
        val styleVideo = "style_item_video"
        val stylePicture = "style_item_picture"
    }

    private var style: String? = null
    private var maxCount = 0
    private var uploadStateInfos = ArrayList<UploadStateInfo>()

    init {
        style = ""
        maxCount = 9
    }

    constructor(resId: Int,
                medias: ArrayList<String>,
                style: String?,
                maxCount: Int,
                upStates: ArrayList<UploadStateInfo>) : this(resId, medias) {
        this@PictureSelectAdapter.style = style
        this@PictureSelectAdapter.maxCount = maxCount
        this@PictureSelectAdapter.uploadStateInfos = upStates
    }

    @Suppress("UNUSED_PARAMETER")
    private fun convertMedia2Video(baseViewHolder: BaseViewHolder, localMedia: String) {
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun convertMedia2Picture(baseViewHolder: BaseViewHolder, localMedia: String) {
        //baseViewHolder.addOnClickListener(R.id.iv_clear)

        val ivSelect = baseViewHolder.getView<ImageView>(R.id.picture_select)
        val ivFoot = baseViewHolder.getView<ImageView>(R.id.picture_foot)
        val ivClear = baseViewHolder.getView<ImageView>(R.id.iv_clear)
        val tvUploadState = baseViewHolder.getView<TextView>(R.id.tv_upload_state)
        if (TextUtils.isEmpty(localMedia)) {
            ivClear.setVisibility(View.GONE)
            ivSelect.setVisibility(View.GONE)
            tvUploadState.visibility = View.GONE
            ivFoot.setVisibility(View.VISIBLE)
            return
        }
        ivFoot.setVisibility(View.GONE)
        try {
            val indexOf = medias.indexOf(localMedia)
            val size = uploadStateInfos.size
            if (size > 0 && indexOf < size) {
                showUploadState(tvUploadState, this.uploadStateInfos.get(indexOf))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        ivClear.setVisibility(View.VISIBLE)
        ivSelect.setVisibility(View.VISIBLE)
        tvUploadState.visibility = View.VISIBLE
        Glide.with(context).load(localMedia).into(ivSelect)
    }

    // com.chad.library.adapter.base.BaseQuickAdapter
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        //baseViewHolder.itemView.setOnClickListener(`View$OnClickListenerC0347a`(baseViewHolder))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun convert(holder: BaseViewHolder, item: String) {
        if (styleVideo.equals(this.style)) {
            convertMedia2Video(holder, item);
        } else {
            convertMedia2Picture(holder, item);
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showUploadState(textView: TextView, uploadStateInfo: UploadStateInfo) {
        val uploadState: Int = uploadStateInfo.uploadState
        if (uploadState == 0) {
            textView.text = "等待上传"
            textView.setTextColor(Hicore.app.getResources().getColor(R.color.colorGray, null))
        } else if (uploadState == 1) {
            textView.text = "上传中"
            textView.setTextColor(Hicore.app.getResources().getColor(R.color.black_dark, null))
        } else if (uploadState == 2) {
            textView.text = "上传完成"
            textView.setTextColor(Hicore.app.getResources().getColor(R.color.blue, null))
        } else if (uploadState == 3) {
            textView.text = "上传失败"
            textView.setTextColor(Hicore.app.getResources().getColor(R.color.colorRed, null))
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
}

class PictureActivity : BaseActivity() {
    private lateinit var infoBinding: ActivityPictureBinding

    private var mAdapter: PictureSelectAdapter? = null
    private val mLocalMedia: ArrayList<String> = ArrayList<String>()
    private var previewPosition = -1
    private val mMaxSelectNum = 6
    var mUploadStateList: ArrayList<PictureSelectAdapter.UploadStateInfo> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initPage() {
        infoBinding = ActivityPictureBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "添加图片"
        infoBinding.lyComplete.tvCommitTip.text = "最多可选择" + 6 + "张图片"
        infoBinding.recyclerview.setLayoutManager(GridLayoutManager(this, 3))
        mLocalMedia.add("")
        mAdapter = PictureSelectAdapter(R.layout.recyclerview_picture,
            mLocalMedia,
            PictureSelectAdapter.stylePicture,
            this.mMaxSelectNum,
            this.mUploadStateList)
        infoBinding.recyclerview.setAdapter(mAdapter)
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
    }
}