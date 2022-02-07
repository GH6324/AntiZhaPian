package com.demo.antizha.adapter

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.demo.antizha.R
import com.demo.antizha.ui.Hicore
import com.demo.antizha.ui.activity.BaseUploadActivity

class PictureSelectAdapter(resId: Int, var medias: ArrayList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(resId, medias) {
    companion object {
        val styleVideo = "style_item_video"
        val stylePicture = "style_item_picture"
    }

    private var style: String? = null
    private var maxCount = 0
    private var uploadStateInfos = ArrayList<BaseUploadActivity.UploadStateInfo>()

    init {
        style = ""
        maxCount = 9
    }

    constructor(resId: Int,
                medias: ArrayList<String>,
                style: String?,
                maxCount: Int,
                upStates: ArrayList<BaseUploadActivity.UploadStateInfo>) : this(resId, medias) {
        this@PictureSelectAdapter.style = style
        this@PictureSelectAdapter.maxCount = maxCount
        this@PictureSelectAdapter.uploadStateInfos = upStates
    }

    @Suppress("UNUSED_PARAMETER")
    private fun convertMedia2Video(baseViewHolder: BaseViewHolder, localMedia: String) {
    }

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
                BaseUploadActivity.showUpState(tvUploadState, uploadStateInfos.get(indexOf))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        ivClear.setVisibility(View.VISIBLE)
        ivSelect.setVisibility(View.VISIBLE)
        tvUploadState.visibility = View.VISIBLE
        Glide.with(Hicore.context).load(localMedia).into(ivSelect)
    }

    // com.chad.library.adapter.base.BaseQuickAdapter
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        //baseViewHolder.itemView.setOnClickListener(`View$OnClickListenerC0347a`(baseViewHolder))
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        if (styleVideo.equals(style)) {
            convertMedia2Video(holder, item)
        } else {
            convertMedia2Picture(holder, item)
        }
        holder.addOnClickListener(R.id.iv_clear)
        holder.addOnClickListener(R.id.picture_foot)
        holder.addOnClickListener(R.id.picture_select)
    }

}
