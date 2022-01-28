package com.demo.antizha.ui.activity

import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.get
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivitySocialAccEditBinding
import com.demo.antizha.ui.Hicore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hjq.toast.ToastUtils
import com.nex3z.flowlayout.FlowLayout
import java.io.InputStreamReader

class SocialType(val text: String)
class SocialTypeData(val code: Int, var data: ArrayList<SocialType>)

class SocialAccountEditActivity : BaseActivity() {
    private lateinit var infoBinding: ActivitySocialAccEditBinding
    private lateinit var flowLayout: FlowLayout
    private val FINATYPE = "其他类型"
    private var tag: Int = 0
    private lateinit var socialAccount: SocialAccountActivity.SocialAccountBean
    private var socialTypeIdx: Int = -1

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initPage() {
        infoBinding = ActivitySocialAccEditBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        flowLayout = infoBinding.flowLayout
        infoBinding.piTitle.tvTitle.text = "添加诈骗社交账号"
        socialAccount = intent.getParcelableExtra("account")!!
        tag = intent.getIntExtra("tag", -1)
        initSocialTypeAdapter()
        infoBinding.piTitle.ivBack.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
        infoBinding.btnCommit.setOnClickListener {
            if (TextUtils.equals(FINATYPE, socialAccount.type)) {
                socialAccount.account = infoBinding.etAccountOther.text.toString()
                socialAccount.typeEx = infoBinding.etTagOther.text.toString()
                if (TextUtils.isEmpty(socialAccount.account) || TextUtils.isEmpty(socialAccount.typeEx)) {
                    ToastUtils.show("社交类型不能为空")
                    return@setOnClickListener
                }
            } else {
                socialAccount.account = infoBinding.etAccount.text.toString()
                socialAccount.typeEx = ""
                if (TextUtils.isEmpty(socialAccount.account)) {
                    ToastUtils.show("社交类型不能为空")
                    return@setOnClickListener
                }
            }
            val intent = Intent()
            intent.putExtra("tag", tag)
            intent.putExtra("account", socialAccount)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun onSelectType(view: View, type: String, pos: Int) {
        if (TextUtils.equals(FINATYPE, type)) {
            infoBinding.llAccOther.setVisibility(View.VISIBLE)
            infoBinding.llAccNomar.setVisibility(View.GONE)
        } else {
            infoBinding.llAccOther.setVisibility(View.GONE)
            infoBinding.llAccNomar.setVisibility(View.VISIBLE)
        }
        infoBinding.tvAccName.text = type + "账号"
        flowLayout[socialTypeIdx].setSelected(false)
        (flowLayout[socialTypeIdx] as TextView).setTextColor(0x1f1f1f)
        view.setSelected(true)
        (view as TextView).setTextColor(resources.getColor(R.color.white, null))
        socialTypeIdx = pos
        socialAccount.type = type
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun initSocialTypeAdapter() {
        val inputStream = Hicore.app.getResources().getAssets().open("socialaccounttypes.txt")
        val socialTypeData: SocialTypeData =
            Gson().fromJson(InputStreamReader(inputStream, "UTF-8"),
                object : TypeToken<SocialTypeData>() {}.type)
        socialTypeData.data.add(SocialType(FINATYPE))
        if (TextUtils.isEmpty(socialAccount.type))
            socialAccount.type = socialTypeData.data[0].text
        for ((i, stype) in socialTypeData.data.withIndex()) {
            val tagView = LayoutInflater.from(mActivity)
                .inflate(R.layout.tag_flow_item, null as ViewGroup?, false) as TextView
            infoBinding.flowLayout.addView(tagView)
            tagView.text = stype.text
            if (TextUtils.equals(stype.text, socialAccount.type)) {
                onSelectType(tagView, socialAccount.type, i)
                if (TextUtils.equals(FINATYPE, socialAccount.type))
                    infoBinding.etAccountOther.setText(socialAccount.account)
                else
                    infoBinding.etAccount.setText(socialAccount.account)
            }
            tagView.setOnClickListener(object : View.OnClickListener {
                val tagString: String = stype.text
                val tagIdx: Int = i

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onClick(view: View?) {
                    if (tagIdx != socialTypeIdx)
                        onSelectType(view!!, tagString, tagIdx)
                }
            })
        }
    }

}
