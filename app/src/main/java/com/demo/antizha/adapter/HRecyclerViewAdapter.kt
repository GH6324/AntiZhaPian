package com.demo.antizha.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.antizha.R
import com.demo.antizha.util.NotchUtils.dp2px


class HRecyclerViewAdapter : RecyclerView.Adapter<HRecyclerViewAdapter.HImageHolder> {
    private lateinit var shareStrings: Array<String>
    private lateinit var shareImages: IntArray
    private lateinit var context: Context
    private lateinit var onItemClickListener: (Int, String) -> Unit
    private var shareType = 0
    private var widthPixels = 0

    inner class HImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        var flShareItem: FrameLayout
        var ivImage: ImageView
        var tvName: TextView

        init {
            flShareItem = view.findViewById(R.id.fl_share_item)
            ivImage = view.findViewById(R.id.image) as ImageView
            tvName = view.findViewById(R.id.tv_name)
            if (this@HRecyclerViewAdapter.shareType === 5) {
                this@HRecyclerViewAdapter.setShareItemWidth(flShareItem)
            }
        }
    }

    constructor(context: Context, i: Int) {
        this.widthPixels = context.resources.displayMetrics.widthPixels
        this.context = context
        shareType = i
        if (shareType == 5) {
            shareStrings = arrayOf("微信好友", "QQ好友", "钉钉", "复制链接")
            shareImages = intArrayOf(R.drawable.iv_share_wx,
                R.drawable.iv_login_qq,
                R.drawable.iv_share_dding,
                R.drawable.iv_share_copy)
            return
        }
        shareStrings = arrayOf("微信好友", "朋友圈", "QQ好友", "QQ空间", "微博", "钉钉", "复制链接")
        shareImages = intArrayOf(R.drawable.iv_share_wx,
            R.drawable.iv_share_wxcicle,
            R.drawable.iv_login_qq,
            R.drawable.iv_share_qzone,
            R.drawable.iv_share_sina,
            R.drawable.iv_share_dding,
            R.drawable.iv_share_copy)
    }

    override fun getItemCount(): Int {
        return shareStrings.size
    }

    fun setOnItemClickListener(onItemClickListener: (Int, String) -> Unit) {
        this.onItemClickListener = onItemClickListener!!
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HImageHolder {
        return HImageHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_hrecyclerview, viewGroup, false))
    }

    override fun onBindViewHolder(hImageHolder: HImageHolder, i: Int) {
        hImageHolder.tvName.text = shareStrings[i]
        hImageHolder.ivImage.setImageResource(shareImages[i])
        if (shareType == 1 && i == shareStrings.size - 1) {
            hImageHolder.tvName.text = "生成海报"
            hImageHolder.ivImage.setImageResource(R.drawable.iv_share_download)
        }
        if (onItemClickListener != null) {
            hImageHolder.itemView.setOnClickListener { view ->
                onItemClickListener(hImageHolder.getLayoutPosition(), shareStrings[i])
            }
        }
    }

    fun setShareItemWidth(frameLayout: FrameLayout) {
        val layoutParams = frameLayout.layoutParams
        layoutParams.width = (widthPixels - dp2px(16.0f)) / shareStrings.size
        frameLayout.layoutParams = layoutParams
    }

}