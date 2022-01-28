package com.demo.antizha.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.demo.antizha.databinding.ActivitySocialAccountBinding

class SocialAccountActivity : BaseActivity() {
    class SocialAccountBean : Parcelable {
        var type: String = ""
        var typeEx: String = ""
        var account: String = ""

        @RequiresApi(Build.VERSION_CODES.Q)
        constructor(source: Parcel) {
            type = source.readString().toString()
            typeEx = source.readString().toString()
            account = source.readString().toString()
        }

        override fun describeContents(): Int {
            return 0
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(type)
            dest.writeString(typeEx)
            dest.writeString(account)
        }

        companion object CREATOR : Parcelable.Creator<SocialAccountBean> {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun createFromParcel(parcel: Parcel): SocialAccountBean {
                return SocialAccountBean(parcel)
            }

            override fun newArray(size: Int): Array<SocialAccountBean?> {
                return arrayOfNulls(size)
            }
        }
    }

    private lateinit var infoBinding: ActivitySocialAccountBinding
    private val SocialAccounts: ArrayList<SocialAccountBean> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initPage() {
        infoBinding = ActivitySocialAccountBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "添加诈骗社交账号"
        infoBinding.tvSelectTip.text = "添加"
        infoBinding.lyComplete.tvCommitTip.text = "提示：最多可上传20条社交账号"
        infoBinding.lyComplete.btnCommit.text = "确定"
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
        infoBinding.tvSelectTip.setOnClickListener {
            val intent = Intent(this, SocialAccountEditActivity::class.java)
            //intent.putExtra("call", calls as Serializable)

        }
    }
}