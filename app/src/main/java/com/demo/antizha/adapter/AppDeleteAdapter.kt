package com.demo.antizha.adapter

import android.text.format.Formatter
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.demo.antizha.R
import com.demo.antizha.ui.Hicore
import com.demo.antizha.ui.activity.BaseUploadActivity
import com.demo.antizha.util.AppUtil.AppInfoBean


class AppDeleteAdapter(i: Int, list: List<AppInfoBean?>?, /* renamed from: a */
                       var upStates: List<BaseUploadActivity.UploadStateInfo>) :
    BaseQuickAdapter<AppInfoBean, BaseViewHolder>(i, list) {

    public override fun convert(holder: BaseViewHolder, appInfo: AppInfoBean) {
        if (appInfo.appIcon != null)
            holder.setImageDrawable(R.id.app_icon, appInfo.appIcon)

        val formatFileSize: String = Formatter.formatFileSize(Hicore.app, appInfo.size)
        holder.setText(R.id.tv_app_name, appInfo.appName)
        holder.setText(R.id.tv_app_version,
            "版本:" + appInfo.version + "  |  " + formatFileSize)
        holder.addOnClickListener(R.id.iv_clear)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val tvUpState = holder.getView<TextView>(R.id.tv_upload_state)
        if (position < upStates.size) {
            BaseUploadActivity.showUpState(tvUpState, upStates[position])
        }
    }

}