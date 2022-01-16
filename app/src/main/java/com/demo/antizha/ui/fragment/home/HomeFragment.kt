package com.demo.antizha.ui.fragment.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.bumptech.glide.Glide
import com.demo.antizha.R
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import android.accounts.AccountManager

import com.demo.antizha.ui.activity.PromosWebDetActivity

import android.content.Intent
import android.text.TextUtils

import com.demo.antizha.ui.Hicore
import com.demo.antizha.userInfoBean


class ToolBean(val name: String, val imageId: Int)

class ToolViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var name: TextView
    var image: ImageView

    init {
        name = view.findViewById(R.id.tv_name) as TextView
        image = view.findViewById(R.id.iv_icon) as ImageView
    }
}

class ToolHolderAdapte(private var context: Context, private var list: ArrayList<ToolBean>) :
    RecyclerView.Adapter<ToolViewHolder>() {

    init {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ToolViewHolder, i: Int) {
        holder.name.text = list[i].name
        holder.image.setImageResource(list[i].imageId)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ToolViewHolder {
        return ToolViewHolder(
            LayoutInflater.from(context).inflate(R.layout.tool_item, viewGroup, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class NewCase(val id: String, val updateTime: String, val title: String, val author: String, val cdnCover: String, val localFilePath: String)

class NewCaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var time: TextView
    var title: TextView
    var image: ImageView

    init {
        time = view.findViewById(R.id.iv_topic_time) as TextView
        title = view.findViewById(R.id.iv_topic_tit) as TextView
        image = view.findViewById(R.id.iv_topic_pic) as ImageView
    }
}

class NewCaseHolderAdapte(
    private var context: Context,
    private var view: View,
    private var recycle: RecyclerView,
    private var list: ArrayList<NewCase>
) : RecyclerView.Adapter<NewCaseViewHolder>() {

    init {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: NewCaseViewHolder, i: Int) {
        holder.time.text = list[i].author + " " + list[i].updateTime.substring(0, 10)
        holder.title.text = list[i].title
        Glide.with(view).load(list[i].cdnCover).into(holder.image)
        holder.itemView.setOnClickListener (object: View.OnClickListener{
            val url:String = list[holder.adapterPosition].localFilePath
            val id:String = list[holder.adapterPosition].id
            override fun onClick(v: View?) {
                if (!Hicore.app.isDouble()) {
                    val intent = Intent(context, PromosWebDetActivity::class.java)
                    intent.putExtra("extra_web_title", "国家反诈中心")
                    val adcode = if (TextUtils.isEmpty(userInfoBean.adcode)) "110105" else userInfoBean.adcode
                    intent.putExtra("extra_web_url",url + "&nodeId=" + adcode)
                    intent.putExtra("extra_web_id", id)
                    intent.putExtra("extra_web_enter", 2)
                    context.startActivity(intent)
                }
            }
        })
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): NewCaseViewHolder {
        return NewCaseViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_home_new_case, viewGroup, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addNewCase(newCase: NewCase) {
        list.add(newCase)
        this.recycle.layoutParams?.height =
            context.resources.displayMetrics.density.let { (100 * list.size * it + 0.5).toInt() }
    }

    fun addNewCase(newCases: ArrayList<NewCase>) {
        list.addAll(newCases)
        this.recycle.layoutParams?.height =
            context.resources.displayMetrics.density.let { (100 * list.size * it + 0.5).toInt() }
    }
}

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var root: View
    private lateinit var newCaseAdapter: NewCaseHolderAdapte
    private lateinit var refreshLayout: SmartRefreshLayout
    private var pageIndex = 1
    private var pageSize = 10
    private var total = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this).get(HomeViewModel::class.java).also { homeViewModel = it }
        root = inflater.inflate(R.layout.fragment_home, container, false)

        val toolText = arrayOf("我要举报", "举报助手", "来电预警", "身份核实")
        val toolimage = arrayOf(
            R.drawable.iv_home_report,
            R.drawable.iv_home_case,
            R.drawable.iv_home_warn,
            R.drawable.iv_home_id_check
        )
        val toolBeans = ArrayList<ToolBean>()
        for ((i, text) in toolText.withIndex()) {
            val toolBean = ToolBean(text, toolimage[i])
            toolBeans.add(toolBean)
        }

        val toolRecycle: RecyclerView = root.findViewById(R.id.rcy_tool)
        toolRecycle.layoutManager = GridLayoutManager(root.context, 4)
        val toolAdapter = ToolHolderAdapte(root.context, toolBeans)
        toolRecycle.adapter = toolAdapter

        val newCaseRecycle: RecyclerView = root.findViewById(R.id.rcy_newcase)
        newCaseRecycle.layoutManager = LinearLayoutManager(root.context)
        this.newCaseAdapter =
            NewCaseHolderAdapte(root.context, root, newCaseRecycle, ArrayList<NewCase>())
        newCaseRecycle.adapter = newCaseAdapter

        refreshLayout = root.findViewById(R.id.refreshLayout)
        refreshLayout.setRefreshHeader(ClassicsHeader(root.context))
        refreshLayout.setRefreshFooter(BallPulseFooter(root.context))
        refreshLayout.setOnRefreshListener { //下拉刷新
            refreshLayout.finishRefresh(2000/*,false*/)//传入false表示刷新失败
        }
        refreshLayout.setOnLoadMoreListener { //上拉加载更多
            refreshLayout.finishLoadMore(5000/*,false*/)//传入false表示加载失败
            if (newCaseAdapter.itemCount < total) {
                val nextIndex = newCaseAdapter.itemCount / pageSize + 1
                if (pageIndex < nextIndex) {
                    pageIndex = nextIndex
                    getNewCaseApi(pageIndex, pageSize)
                }
            }
        }
        getNewCaseApi(1, pageSize)
        return root
    }

    class NewCasePackage(val data: NewCaseData, val code: Int)
    class NewCaseData(val total: Int, var rows: ArrayList<NewCase>)

    private fun getNewCaseApi(page: Int, row: Int) {
        //https://fzapp.gjfzpt.cn/hicore/api/Information/querylatestcases?Page=1&Rows=2&Sort=releasetime&Order=desc
        com.demo.antizha.getDataByGet(
            "https://fzapp.gjfzpt.cn/hicore/api/Information/querylatestcases?Page=" + page.toString() + "&Rows=" + row.toString() + "&Sort=releasetime&Order=desc",
            callBackFunc = this::addNewCase
        )
    }

    private fun addNewCase(data: String) {
        val json = Klaxon().parse<NewCasePackage>(data)
        if (json != null && json.code == 0) {
            total = json.data.total
            refreshLayout.finishLoadMore()
            newCaseAdapter.addNewCase(json.data.rows)
            newCaseAdapter.notifyDataSetChanged()
            if (newCaseAdapter.itemCount >= total) {
                val morecase: View = root.findViewById(R.id.ly_morecase)
                morecase.visibility = View.VISIBLE
                refreshLayout.setEnableLoadMore(false)
            }
        }
    }
}