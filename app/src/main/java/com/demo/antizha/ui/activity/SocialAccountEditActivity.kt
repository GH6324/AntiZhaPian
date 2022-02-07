package com.demo.antizha.ui.activity

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import com.demo.antizha.R
import com.demo.antizha.adapter.SocialAccBean
import com.demo.antizha.databinding.ActivitySocialAccEditBinding
import com.demo.antizha.util.FileUtil
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
    private lateinit var socialTypeData: SocialTypeData
    private val FINATYPE = "其他类型"
    private var pos: Int = 0
    private var socialAccount: SocialAccBean? = null
    private var socialTypeIdx: Int = -1

    override fun initPage() {
        infoBinding = ActivitySocialAccEditBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        flowLayout = infoBinding.flowLayout
        infoBinding.piTitle.tvTitle.text = "添加诈骗社交账号"
        getIntentData()
        initTagAdapter()
        infoBinding.piTitle.ivBack.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
        infoBinding.btnCommit.setOnClickListener {
            if (TextUtils.equals(FINATYPE, socialTypeData.data[socialTypeIdx].text)) {
                socialAccount!!.accountName = infoBinding.etTagOther.text.toString()
                socialAccount!!.accountNum = infoBinding.etAccountOther.text.toString()
            } else {
                socialAccount!!.accountName = socialTypeData.data[socialTypeIdx].text
                socialAccount!!.accountNum = infoBinding.etAccount.text.toString()
            }
            if (TextUtils.isEmpty(socialAccount!!.accountNum)) {
                ToastUtils.show("社交类型不能为空")
                return@setOnClickListener
            }
            val intent = Intent()
            intent.putExtra("tag", pos)
            intent.putExtra("account", socialAccount)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    fun onSelectType(view: View, type: String, pos: Int) {
        if (TextUtils.equals(FINATYPE, type)) {
            infoBinding.llAccOther.setVisibility(View.VISIBLE)
            infoBinding.llAccNomar.setVisibility(View.GONE)
        } else {
            infoBinding.llAccOther.setVisibility(View.GONE)
            infoBinding.llAccNomar.setVisibility(View.VISIBLE)
            socialAccount!!.accountName = type
        }
        infoBinding.tvAccName.text = type + "账号"
        if (socialTypeIdx >= 0) {
            flowLayout[socialTypeIdx].setSelected(false)
            (flowLayout[socialTypeIdx] as TextView).setTextColor(resources.getColor(R.color._1F1F1F,
                null))
        }
        view.setSelected(true)
        (view as TextView).setTextColor(resources.getColor(R.color.white, null))
        socialTypeIdx = pos
    }

    fun getIntentData() {
        pos = intent.getIntExtra("pos", 0)
        socialAccount = intent.getParcelableExtra("acc")
        if (socialAccount == null)
            socialAccount = SocialAccBean()
    }

    fun contains(list: List<SocialType>, str: String): Int {
        for (i in list.indices) {
            if (TextUtils.equals(list[i].text, str)) {
                return i
            }
        }
        return -1
    }

    fun addTagType(tagType: String, id: Int) {
        val tagView = LayoutInflater.from(mActivity)
            .inflate(R.layout.tag_flow_item, null as ViewGroup?, false) as TextView
        infoBinding.flowLayout.addView(tagView)
        tagView.text = tagType
        tagView.setOnClickListener(object : View.OnClickListener {
            val tagString: String = tagType
            val tagId: Int = id

            override fun onClick(view: View?) {
                if (tagId != socialTypeIdx)
                    onSelectType(view!!, tagString, tagId)
            }
        })
    }

    fun initTagAdapter() {
        val inputStream = FileUtil.openfile("socialaccounttypes.txt")
        socialTypeData = Gson().fromJson(InputStreamReader(inputStream, "UTF-8"),
            object : TypeToken<SocialTypeData>() {}.type)
        inputStream.close()
        if (TextUtils.isEmpty(socialAccount!!.accountName))
            socialAccount!!.accountName = socialTypeData.data[0].text
        socialTypeData.data.add(SocialType(FINATYPE))
        for ((i, stype) in socialTypeData.data.withIndex()) {
            addTagType(stype.text, i)
        }
        var id = contains(socialTypeData.data, socialAccount!!.accountName)
        if (id == -1) {
            id = socialTypeData.data.size - 1
            infoBinding.etTagOther.setText(socialAccount!!.accountName)
            infoBinding.etAccountOther.setText(socialAccount!!.accountNum)
            onSelectType(infoBinding.flowLayout[id], FINATYPE, id)
        } else {
            infoBinding.etAccount.setText(socialAccount!!.accountNum)
            onSelectType(infoBinding.flowLayout[id], socialAccount!!.accountName, id)
        }
    }

}