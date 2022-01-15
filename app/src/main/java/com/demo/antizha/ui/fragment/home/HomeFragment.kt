package com.demo.antizha.ui.fragment.home

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

class ToolBean(val name: String, val imageId: Int)

class ToolViewHolder : RecyclerView.ViewHolder {
    constructor(view: View) : super(view) {
        name = view.findViewById(R.id.tv_name) as TextView
        image = view.findViewById(R.id.iv_icon) as ImageView
    }

    var name: TextView
    var image: ImageView
}

class ToolHolderAdapte : RecyclerView.Adapter<ToolViewHolder> {
    private var context: Context
    private var list: ArrayList<ToolBean>

    constructor(context: Context, list: ArrayList<ToolBean>) {
        this.context = context
        this.list = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ToolViewHolder, i: Int) {
        holder.name.text = list.get(i).name
        holder.image.setImageResource(list.get(i).imageId)
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

class NewCase(val updateTime: String, val title: String, val author: String, val cdnCover: String)

class NewCaseViewHolder : RecyclerView.ViewHolder {
    constructor(view: View) : super(view) {
        time = view.findViewById(R.id.iv_topic_time) as TextView
        title = view.findViewById(R.id.iv_topic_tit) as TextView
        image = view.findViewById(R.id.iv_topic_pic) as ImageView
    }

    var time: TextView
    var title: TextView
    var image: ImageView
}

class NewCaseHolderAdapte : RecyclerView.Adapter<NewCaseViewHolder> {
    private var context: Context
    private var list: ArrayList<NewCase>
    private var view: View
    private var recycle: androidx.recyclerview.widget.RecyclerView

    constructor(
        context: Context,
        view: View,
        recycle: androidx.recyclerview.widget.RecyclerView,
        list: ArrayList<NewCase>
    ) {
        this.context = context
        this.list = list
        this.view = view
        this.recycle = recycle
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: NewCaseViewHolder, i: Int) {
        holder.time.text = list.get(i).author + " " + list.get(i).updateTime.substring(0, 10)
        holder.title.text = list.get(i).title
        Glide.with(view).load(list.get(i).cdnCover).into(holder.image)
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
        this.recycle.getLayoutParams()?.height =
            context.resources.displayMetrics.density.let { (100 * list.size * it + 0.5).toInt() }
    }

    fun addNewCase(newCases: ArrayList<NewCase>) {
        list.addAll(newCases)
        this.recycle.getLayoutParams()?.height =
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
    ): View? {
        ViewModelProvider(this).get(HomeViewModel::class.java).also { homeViewModel = it }
        root = inflater.inflate(R.layout.fragment_home, container, false)

        val toolText = arrayOf("我要举报", "举报助手", "来电预警", "身份核实")
        val toolimage = arrayOf(
            R.drawable.iv_home_report,
            R.drawable.iv_home_case,
            R.drawable.iv_home_warn,
            R.drawable.iv_home_id_check
        )
        var toolBeans = ArrayList<ToolBean>()
        for ((i, text) in toolText.withIndex()) {
            val toolBean = ToolBean(text, toolimage[i])
            toolBeans.add(toolBean)
        }

        val toolRecycle: RecyclerView = root.findViewById<RecyclerView>(R.id.rcy_tool);
        toolRecycle.setLayoutManager(GridLayoutManager(root.getContext(), 4))
        val toolAdapter = ToolHolderAdapte(root.getContext(), toolBeans)
        toolRecycle.setAdapter(toolAdapter)

        val newCaseRecycle: RecyclerView = root.findViewById<RecyclerView>(R.id.rcy_newcase);
        newCaseRecycle.setLayoutManager(LinearLayoutManager(root.getContext()))
        this.newCaseAdapter =
            NewCaseHolderAdapte(root.getContext(), root, newCaseRecycle, ArrayList<NewCase>())
        newCaseRecycle.setAdapter(newCaseAdapter)

        refreshLayout = root.findViewById<SmartRefreshLayout>(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(ClassicsHeader(root.getContext()))
        refreshLayout.setRefreshFooter(BallPulseFooter(root.getContext()))
        refreshLayout.setOnRefreshListener { //下拉刷新
            refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
        }
        refreshLayout.setOnLoadMoreListener { //上拉加载更多
            refreshLayout.finishLoadMore(5000/*,false*/);//传入false表示加载失败
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
    class NewCaseData(val total: Int, val totalPages: Int, var rows: ArrayList<NewCase>)

    fun getNewCaseApi(page: Int, row: Int) {
        //https://fzapp.gjfzpt.cn/hicore/api/Information/querylatestcases?Page=1&Rows=2&Sort=releasetime&Order=desc
        com.demo.antizha.getDataByGet(
            "https://fzapp.gjfzpt.cn/hicore/api/Information/querylatestcases?Page=" + page.toString() + "&Rows=" + row.toString() + "&Sort=releasetime&Order=desc",
            callBackFunc = this::AddNewCase
        )
    }

    fun AddNewCase(data: String) {
        val json = Klaxon().parse<NewCasePackage>(data)
        if (json != null && json.code == 0) {
            total = json.data.total
            refreshLayout.finishLoadMore()
            newCaseAdapter.addNewCase(json.data.rows)
            newCaseAdapter.notifyDataSetChanged()
            if (newCaseAdapter.itemCount >= total) {
                val morecase: View = root.findViewById<View>(R.id.ly_morecase);
                morecase.visibility = View.VISIBLE
                refreshLayout.setEnableLoadMore(false)
            }
        }
    }
}