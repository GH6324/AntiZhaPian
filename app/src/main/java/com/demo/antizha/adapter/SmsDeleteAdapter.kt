package com.demo.antizha.adapter

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.demo.antizha.R
import java.util.*


class SmsBean : Parcelable {
    var id: String = ""
    var isSelect: Boolean = false
    var report_type: Int = 0
    var smsNum: String = ""
    var smsContent: String = ""
    var stringDate: String = ""
    var isInput = false
    var index: Int = -1

    constructor() {

    }

    constructor(source: Parcel) {
        id = source.readString().toString()
        isSelect = source.readInt() > 0
        report_type = source.readInt()
        smsNum = source.readString().toString()
        smsContent = source.readString().toString()
        stringDate = source.readString().toString()
        isInput = source.readInt() > 0
        index = source.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeInt(if (isSelect) 1 else 0)
        dest.writeInt(report_type)
        dest.writeString(smsNum)
        dest.writeString(smsContent)
        dest.writeString(stringDate)
        dest.writeInt(if (isInput) 1 else 0)
        dest.writeInt(index)
    }

    companion object CREATOR : Parcelable.Creator<SmsBean> {
        override fun createFromParcel(parcel: Parcel): SmsBean {
            return SmsBean(parcel)
        }

        override fun newArray(size: Int): Array<SmsBean?> {
            return arrayOfNulls(size)
        }
    }
}

class SmsDeleteAdapter(resId: Int, list: List<SmsBean?>?) :
    BaseQuickAdapter<SmsBean, BaseViewHolder>(resId, list) {
    private var itemClickListener: ItemClickListener? = null

    inner class OnClickListener internal constructor(val holder: BaseViewHolder) :
        View.OnClickListener {
        override fun onClick(view: View) {
            if (itemClickListener != null) {
                itemClickListener!!.onItemClickListener(holder.adapterPosition,
                    this@SmsDeleteAdapter.mData)
            }
        }
    }

    interface ItemClickListener {
        fun onItemClickListener(i: Int, list: List<SmsBean>)
    }

    fun setOnItemClickListener(bVar: ItemClickListener?) {
        itemClickListener = bVar
    }

    public override fun convert(holder: BaseViewHolder, smsBean: SmsBean) {
        holder.setText(R.id.tv_sms_phone, smsBean.smsNum)
        holder.setText(R.id.tv_sms_content, smsBean.smsContent)
        holder.addOnClickListener(R.id.iv_clear)
        val stringDate = smsBean.stringDate
        if (!TextUtils.isEmpty(stringDate)) {
            val split = stringDate.split(" ").toTypedArray()
            val sb = StringBuilder().append(Calendar.getInstance()[1]).append("")
            val time = if (stringDate.contains(sb.toString())) stringDate.substring(5) else split[0]
            holder.setText(R.id.tv_time, time)
        } else {
            holder.setText(R.id.tv_time, "")
        }
        val ivEdit = holder.getView<View>(R.id.iv_edit) as ImageView
        if (smsBean.isInput) {
            ivEdit.visibility = View.VISIBLE
        } else {
            ivEdit.visibility = View.GONE
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener(OnClickListener(holder))
    }
}