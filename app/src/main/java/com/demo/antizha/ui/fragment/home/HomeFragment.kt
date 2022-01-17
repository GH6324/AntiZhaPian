package com.demo.antizha.ui.fragment.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.demo.antizha.getDataByGet
import com.demo.antizha.optimizationTimeStr
import com.demo.antizha.ui.Hicore
import com.demo.antizha.ui.activity.PromosWebDetActivity
import com.demo.antizha.userInfoBean
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.RoundLinesIndicator
import com.youth.banner.util.BannerUtils


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

class NewCase(
    val id: String,
    val updateTime: String,
    val title: String,
    val author: String,
    val cdnCover: String,
    val localFilePath: String
)

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
        holder.time.text = list[i].author + " " + optimizationTimeStr(list[i].updateTime)
        holder.title.text = list[i].title
        Glide.with(view).load(list[i].cdnCover).into(holder.image)
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            val url: String = list[holder.adapterPosition].localFilePath
            val id: String = list[holder.adapterPosition].id
            override fun onClick(v: View?) {
                if (!Hicore.app.isDouble()) {
                    val intent = Intent(context, PromosWebDetActivity::class.java)
                    intent.putExtra("extra_web_title", "国家反诈中心")
                    val adcode =
                        if (TextUtils.isEmpty(userInfoBean.adcode)) "110105" else userInfoBean.adcode
                    intent.putExtra("extra_web_url", url + "&nodeId=" + adcode)
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

enum class ImageDataType {
    TYPE_RES,
    TYPE_URL
}

class BanderBean(val imageRes: Int, val imageUrl: String, val imageType: ImageDataType)
class BanderHolder(view: View) : RecyclerView.ViewHolder(view) {
    var imageView: ImageView

    init {
        imageView = view as ImageView
    }
}

class BanderAdapter(
    private var imageUrls: ArrayList<BanderBean>
) : BannerAdapter<BanderBean, BanderHolder>(imageUrls) {
    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BanderHolder {
        val imageView = ImageView(parent!!.context)
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        //通过裁剪实现圆角
        BannerUtils.setBannerRound(imageView, 20f)
        return BanderHolder(imageView)
    }

    override fun onBindView(holder: BanderHolder, data: BanderBean, position: Int, size: Int) {
        when (data.imageType) {
            ImageDataType.TYPE_RES -> holder.imageView.setImageResource(data.imageRes);
            ImageDataType.TYPE_URL -> Glide.with(holder.itemView).load(data.imageUrl)
                .into(holder.imageView)
        }
    }
}

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var root: View
    private lateinit var newCaseAdapter: NewCaseHolderAdapte
    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var banderAdapter: BanderAdapter
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
        initBanner()
        initTool()
        initNewCase()
        initRefreshLayout()
        return root
    }

    private fun initBanner() {
        val imageList = ArrayList<BanderBean>()
        imageList.add(BanderBean(Integer.valueOf(R.mipmap.banner1), "", ImageDataType.TYPE_RES))
        val banner: Banner<BanderBean, BanderAdapter> = root.findViewById(R.id.banner)
        banner.addBannerLifecycleObserver(this)
        banner.setBannerRound(20f)
        banner.setLoopTime(5000)
        banner.setIndicator(RoundLinesIndicator(Hicore.getContext()))
        banderAdapter = BanderAdapter(imageList)
        banner.setAdapter(banderAdapter)
        getNewBander()
    }

    private fun initTool() {
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
    }

    private fun initNewCase() {
        val newCaseRecycle: RecyclerView = root.findViewById(R.id.rcy_newcase)
        newCaseRecycle.layoutManager = LinearLayoutManager(root.context)
        this.newCaseAdapter =
            NewCaseHolderAdapte(root.context, root, newCaseRecycle, ArrayList<NewCase>())
        newCaseRecycle.adapter = newCaseAdapter
    }

    private fun initRefreshLayout() {
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
    }

    class NewCasePackage(val data: NewCaseData, val code: Int)
    class NewCaseData(val total: Int, var rows: ArrayList<NewCase>)

    private fun getNewCaseApi(page: Int, row: Int) {
        //https://fzapp.gjfzpt.cn/hicore/api/Information/querylatestcases?Page=1&Rows=2&Sort=releasetime&Order=desc
        getDataByGet(
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

    class NewBanderData(val data: ArrayList<NewBander>, val code: Int)
    class NewBander(val imgPath: String, val id: Long)

    private fun getNewBander() {
        getDataByGet("https://fzapp.gjfzpt.cn/hicore/api/Banner", callBackFunc = this::addNewBander)
    }

    private fun addNewBander(data: String) {
        val json = Klaxon().parse<NewBanderData>(data)
        if (json != null && json.code == 0) {
            val imageList = ArrayList<BanderBean>()
            for (row in json.data) {
                imageList.add(BanderBean(0, row.imgPath, ImageDataType.TYPE_URL))
            }
            banderAdapter.setDatas(imageList)
            banderAdapter.notifyDataSetChanged()
        }
    }
}