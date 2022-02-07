package com.demo.antizha.ui.activity

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivityTagflowBinding
import com.demo.antizha.util.FileUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nex3z.flowlayout.FlowLayout
import java.io.InputStreamReader


class ReportZPEleBean {
    var id: Long = -1
    var name: String? = null
    var parentClusterID: String? = null
    var sort: String? = null
    var topClass: String? = null
}

class ReportZPBean {
    var children: List<ReportZPEleBean>? = null
    var id: Long = 0
    var name: String? = null
    var parentClusterID: String? = null
    var sort: String? = null
    var topClass: String? = null
}

class ZPTypeData(val code: Int, var data: ArrayList<ReportZPBean>)

class TagFlowLaoutActivity : BaseActivity() {
    private lateinit var infoBinding: ActivityTagflowBinding
    protected var mFlowString: List<ReportZPBean> = ArrayList()
    var childPost = 0
    val pageType: Int = 0
    var tagBean: Long = 0

    override fun initPage() {
        infoBinding = ActivityTagflowBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "诈骗类型"
        initTagAdapter()
        infoBinding.piTitle.ivBack.setOnClickListener {
            val intent = Intent()
            intent.putExtra("tagString", "")
            intent.putExtra("tagId", 0)
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

    fun initTagAdapter() {
        tagBean = intent.getLongExtra("int_tag_name", 0)
        val inputStream = FileUtil.openfile("xccasecategorys.txt")
        val zPTypeData: ZPTypeData = Gson().fromJson(InputStreamReader(inputStream, "UTF-8"),
            object : TypeToken<ZPTypeData>() {}.type)
        for (i in zPTypeData.data.indices) {
            initTagAdapter(zPTypeData.data[i], i)
        }
        inputStream.close()
    }

    fun initTagAdapter(reportZPBean: ReportZPBean, idx: Int) {
        val inflate: View = LayoutInflater.from(mActivity)
            .inflate(R.layout.layout_tag_flow, null as ViewGroup?, false)
        infoBinding.llParent.addView(inflate)
        val tvDivBg = inflate.findViewById<TextView>(R.id.tv_div_bg)
        val ivDrop = inflate.findViewById<ImageView>(R.id.iv_arrow)
        val tagFlowLayout = inflate.findViewById<FlowLayout>(R.id.flow_layout)
        tagFlowLayout.tag = false
        inflate.findViewById<TextView>(R.id.tag_title).setText(reportZPBean.name)
        tvDivBg.visibility = if (idx % 4 == 0) View.VISIBLE else View.GONE
        if (reportZPBean.children == null || reportZPBean.children!!.size == 0) {
            val arrayList = ArrayList<ReportZPEleBean>()
            val reportZPEleBean = ReportZPEleBean()
            reportZPEleBean.id = reportZPBean.id
            reportZPEleBean.name = reportZPBean.name
            reportZPEleBean.parentClusterID = reportZPBean.parentClusterID
            reportZPEleBean.sort = reportZPBean.sort
            reportZPEleBean.topClass = reportZPBean.topClass
            arrayList.add(reportZPEleBean)
            reportZPBean.children = arrayList
        }
        for (i in reportZPBean.children!!) {
            val tagView = LayoutInflater.from(mActivity)
                .inflate(R.layout.tag_flow_item, null as ViewGroup?, false) as TextView
            tagFlowLayout.addView(tagView)
            tagView.text = i.name
            if (tagBean != 0L && tagBean == i.id) {
                tagFlowLayout.visibility = View.VISIBLE
                tagFlowLayout.tag = true
                ivDrop.setImageResource(R.drawable.iv_tag_ar_up)
                tagView.setSelected(true)
                tagView.setTextColor(resources.getColor(R.color.white, null))
            }
            tagView.setOnClickListener(object : View.OnClickListener {
                val tagString: String? = i.name
                val tagId: Long = i.id
                override fun onClick(view: View?) {
                    val intent = Intent()
                    intent.putExtra("tagString", tagString)
                    intent.putExtra("tagId", tagId)
                    setResult(RESULT_OK, intent)
                    finish()
                }
            })
        }
        ivDrop.setOnClickListener(object : View.OnClickListener {
            val flowLayout: FlowLayout = tagFlowLayout
            override fun onClick(view: View?) {
                val show: Boolean = flowLayout.tag as Boolean
                flowLayout.visibility = if (show) View.GONE else View.VISIBLE
                ivDrop.setImageResource(if (show) R.drawable.iv_tag_ar_down else R.drawable.iv_tag_ar_up)
                flowLayout.tag = !show
            }
        })

    }
}