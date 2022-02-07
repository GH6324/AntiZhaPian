package com.demo.antizha.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.ExpandableListView.OnGroupClickListener
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivityAppSelectedBinding
import com.demo.antizha.ui.Hicore
import com.demo.antizha.util.AppUtil
import com.demo.antizha.util.AppUtil.AppInfoBean


class AppExSelectedAdapter(val context: Context,
                           var titles: List<String>,
                           var appGroups: ArrayList<List<AppInfoBean>>) :
    BaseExpandableListAdapter() {
    private val layout: LayoutInflater

    init {
        layout = LayoutInflater.from(context)
    }

    private inner class AppHolder(view: View) {
        var ivIcon: ImageView
        var tvAppName: TextView
        var tvVersion: TextView
        var ivCanSelect: ImageView
        var ivCantSelect: ImageView

        init {
            ivIcon = view.findViewById<ImageView>(R.id.app_icon)
            tvAppName = view.findViewById<TextView>(R.id.tv_app_name)
            tvVersion = view.findViewById<TextView>(R.id.tv_app_version)
            ivCanSelect = view.findViewById<ImageView>(R.id.iv_canselect)
            ivCantSelect = view.findViewById<ImageView>(R.id.iv_cantselect)
        }
    }

    /* renamed from: a */
    fun resetData(titles: List<String>, appGroups: ArrayList<List<AppInfoBean>>) {
        this.titles = titles
        this.appGroups = appGroups
        notifyDataSetChanged()
    }

    // android.widget.ExpandableListAdapter
    override fun getChild(group: Int, idx: Int): Any {
        return appGroups[group][idx]
    }

    // android.widget.ExpandableListAdapter
    override fun getChildId(group: Int, idx: Int): Long {
        return idx.toLong()
    }

    override fun getChildView(group: Int,
                              idx: Int,
                              isLastChild: Boolean,
                              view: View?,
                              parent: ViewGroup): View {
        var convertView = view
        val appHolder: AppHolder
        val appInfoBean = appGroups[group][idx]
        if (convertView == null) {
            convertView = layout.inflate(R.layout.recyclerview_app_select, parent, false)
            appHolder = AppHolder(convertView)
            convertView.tag = appHolder
        } else {
            appHolder = convertView.tag as AppHolder
        }
        val appIcon = appInfoBean.appIcon
        if (appIcon != null) {
            appHolder.ivIcon.setImageDrawable(appIcon)
        }
        appHolder.tvAppName.setText(appInfoBean.appName)
        val formatFileSize: String = Formatter.formatFileSize(Hicore.app, appInfoBean.size)
        appHolder.tvVersion.text = "版本:" + appInfoBean.version + "  |  " + formatFileSize
        if (appInfoBean.size > 209715200) {
            appHolder.ivCanSelect.setVisibility(View.GONE)
            appHolder.ivCantSelect.setVisibility(View.VISIBLE)
        } else if (appInfoBean.selected) {
            appHolder.ivCanSelect.setImageResource(R.mipmap.checkbox_checked)
            appHolder.ivCanSelect.setVisibility(View.VISIBLE)
            appHolder.ivCantSelect.setVisibility(View.GONE)
        } else {
            appHolder.ivCanSelect.setImageResource(R.mipmap.checkbox_unchecked)
            appHolder.ivCanSelect.setVisibility(View.VISIBLE)
            appHolder.ivCantSelect.setVisibility(View.GONE)
        }
        return convertView!!
    }

    override fun getChildrenCount(i: Int): Int {
        return appGroups[i].size
    }

    override fun getGroup(i: Int): Any {
        return titles[i]
    }

    override fun getGroupCount(): Int {
        return titles.size
    }

    override fun getGroupId(i: Int): Long {
        return i.toLong()
    }

    override fun getGroupView(group: Int,
                              isExpanded: Boolean,
                              view: View?,
                              viewGroup: ViewGroup): View {
        var convertView = view
        if (convertView == null) {
            convertView = layout.inflate(R.layout.app_select_tip, viewGroup, false)
        }
        (convertView!!.findViewById<View>(R.id.tv_lable) as TextView).text = titles[group]
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(i: Int, i2: Int): Boolean {
        return true
    }

}

class AppSelectedActivity : BaseActivity() {
    companion object {
        const val SELECT_TYPE = "select_type"
        const val SELECT_MAX = "extra_select_limite"
        const val SELECT_CURRENT = "extra_select_now"
    }

    private lateinit var infoBinding: ActivityAppSelectedBinding
    private lateinit var appSelectedAdapter: AppExSelectedAdapter
    private var selectedCount: Int = 0
    private var canSelect: Int = 9
    private var titles: ArrayList<String> = ArrayList<String>()
    private val appGroups: ArrayList<List<AppInfoBean>> = ArrayList()
    private val apps: ArrayList<AppInfoBean> = ArrayList()
    private val appSelected: ArrayList<AppInfoBean> = ArrayList()

    override fun initPage() {
        infoBinding = ActivityAppSelectedBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.setText("选择APP应用")
        infoBinding.piTitle.ivRight.setVisibility(View.GONE)
        selectedCount = intent.getIntExtra(SELECT_CURRENT, 0)
        val selectMax = intent.getIntExtra(SELECT_MAX, 0)
        if (selectMax != 0)
            canSelect = selectMax
        canSelect -= selectedCount
        initApps()
        infoBinding.piTitle.ivBack.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
        infoBinding.btnReport.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            intent.putParcelableArrayListExtra("app", appSelected)
            finish()
        }
    }

    fun initApps() {
        showProgressDialog("加载中...", true)
        Handler(Looper.getMainLooper()).postDelayed({
            if (!this.isFinishing()) {
                titles.add("未安装安装包")
                titles.add("已安装应用")
                appGroups.add(ArrayList<AppInfoBean>())
                val appinfos = AppUtil.getAppinfos()
                for (app in appinfos)
                    app.selected = false
                appGroups.add(appinfos)
                apps.addAll(appinfos)
                initAdapter()
                hideProgressDialog()
            }

        }, 500)
    }

    private fun initAdapter() {
        appSelectedAdapter = AppExSelectedAdapter(this, titles, appGroups)
        infoBinding.recyclerviewApp.setAdapter(appSelectedAdapter)
        for (i in titles.indices) {
            infoBinding.recyclerviewApp.expandGroup(i)
        }
        infoBinding.recyclerviewApp.setOnGroupClickListener(OnGroupClick.onGroupClick)
        infoBinding.recyclerviewApp.setOnChildClickListener(OnChildClick())
    }

    fun selectApp(appInfoBean: AppInfoBean, gVar: AppExSelectedAdapter) {
        if (appInfoBean.size > 209715200) {
            Toast.makeText(this, "200M以上文件不可选择", Toast.LENGTH_SHORT).show()
            return
        }
        if (!appInfoBean.selected) {
            if (canSelect == 1) {
                resetSelect()
            } else if (appSelected.size == canSelect) {
                Toast.makeText(this, "最多选择" + canSelect + "个", Toast.LENGTH_SHORT).show()
                return
            }
            appSelected.add(appInfoBean)
            appInfoBean.selected = true
        } else {
            appSelected.remove(appInfoBean)
            appInfoBean.selected = false
        }
        gVar.notifyDataSetChanged()
    }

    private fun resetSelect() {
        for (group in appGroups) {
            for (app in group) {
                app.selected = false
            }
        }
        appSelectedAdapter.resetData(titles, appGroups)
        appSelected.clear()
    }

    class OnGroupClick private constructor() : OnGroupClickListener {
        override fun onGroupClick(expandableListView: ExpandableListView,
                                  view: View,
                                  i: Int,
                                  j: Long): Boolean {
            return true
        }

        companion object {
            val onGroupClick = OnGroupClick()
        }
    }

    inner class OnChildClick internal constructor() : OnChildClickListener {
        override fun onChildClick(expandableListView: ExpandableListView,
                                  view: View,
                                  group: Int,
                                  pos: Int,
                                  id: Long): Boolean {
            selectApp(appGroups.get(group)[pos], appSelectedAdapter)
            return true
        }
    }

}