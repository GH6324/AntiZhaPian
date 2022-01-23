package com.demo.antizha.ui.fragment.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.antizha.R
import com.demo.antizha.UserInfoBean
import com.demo.antizha.getDataByGet
import com.demo.antizha.optimizationTimeStr
import com.demo.antizha.ui.Hicore
import com.demo.antizha.ui.IClickListener
import com.demo.antizha.ui.activity.*
import com.demo.antizha.util.DialogUtils
import com.google.gson.Gson
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.RoundLinesIndicator
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.util.BannerUtils


class ToolBean(val id: Int, val name: String, val imageId: Int)

class ToolViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var name: TextView = view.findViewById(R.id.tv_name) as TextView
    var image: ImageView = view.findViewById(R.id.iv_icon) as ImageView
}

class ToolHolderAdapte(private var homeFragment: HomeFragment,
                       private var context: Context,
                       private var list: ArrayList<ToolBean>) :
    RecyclerView.Adapter<ToolViewHolder>() {

    init {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ToolViewHolder, i: Int) {
        holder.name.text = list[i].name
        holder.image.setImageResource(list[i].imageId)
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.R)
            override fun onClick(v: View?) {
                if (!Hicore.app.isDouble()) {
                    if (list.size <= 0)
                        return
                    val adapterPosition = holder.adapterPosition
                    if (adapterPosition < 0)
                        return
                    val toolBean = list[adapterPosition]
                    when (toolBean.id) {
                        0 -> {
                            if (!UserInfoBean.isVerified())
                                DialogUtils.showNormalDialog(context,
                                    "请先进行实名认证",
                                    "",
                                    "取消",
                                    "身份验证",
                                    homeFragment as IClickListener)
                            else
                                homeFragment.startActivity(Intent(homeFragment.activity,
                                    ReportNewActivity::class.java))
                        }
                        1 -> {
                            if (!UserInfoBean.isVerified())
                                DialogUtils.showNormalDialog(context,
                                    "请先进行实名认证",
                                    "",
                                    "取消",
                                    "身份验证",
                                    homeFragment as IClickListener)
                            else
                                homeFragment.startActivity(Intent(homeFragment.activity,
                                    ReporterAidActivity::class.java))
                        }
                        2 -> {
                            val intentInfo =
                                Intent(homeFragment.activity, WarnSettingActivity::class.java)
                            homeFragment.startActivity(intentInfo)
                        }
                        3 -> {
                            if (!UserInfoBean.isVerified())
                                DialogUtils.showNormalDialog(context,
                                    "请先进行实名认证",
                                    "",
                                    "取消",
                                    "身份验证",
                                    homeFragment as IClickListener)
                            else
                                homeFragment.startActivity(Intent(homeFragment.activity,
                                    CheckIDActivity::class.java))
                        }
                    }
                }
            }
        })

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
    var time: TextView = view.findViewById(R.id.iv_topic_time) as TextView
    var title: TextView = view.findViewById(R.id.iv_topic_tit) as TextView
    var image: ImageView = view.findViewById(R.id.iv_topic_pic) as ImageView
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
                        if (TextUtils.isEmpty(UserInfoBean.adcode)) "110105" else UserInfoBean.adcode
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

enum class BanderType {
    TYPE_RES,
    TYPE_URL
}

class BanderBean(
    val imageRes: Int,
    val imagePath: String,
    val url: String,
    val title: String,
    val imageType: BanderType
)

class BanderHolder(view: View) : RecyclerView.ViewHolder(view) {
    var imageView: ImageView = view as ImageView

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
            BanderType.TYPE_RES -> holder.imageView.setImageResource(data.imageRes)
            BanderType.TYPE_URL -> Glide.with(holder.itemView).load(data.imagePath)
                .into(holder.imageView)
        }
    }
}

class HomeFragment : Fragment(), IClickListener {

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
        initWarnCheck()
        initNewCase()
        initRefreshLayout()
        return root
    }

    private fun initBanner() {
        val imageList = ArrayList<BanderBean>()
        imageList.add(BanderBean(Integer.valueOf(R.mipmap.banner1),
            "", "", "", BanderType.TYPE_RES))
        val banner: Banner<BanderBean, BanderAdapter> = root.findViewById(R.id.banner)
        banner.addBannerLifecycleObserver(this)
        banner.setBannerRound(20f)
        banner.setLoopTime(5000)
        banner.indicator = RoundLinesIndicator(Hicore.getContext())
        banderAdapter = BanderAdapter(imageList)
        banner.setAdapter(banderAdapter)
        val mOnWebListener = object : OnBannerListener<BanderBean> {
            override fun OnBannerClick(data: BanderBean, position: Int) {
                if (TextUtils.isEmpty(data.url))
                    return
                val intent = Intent(context, PromosWebDetActivity::class.java)
                intent.putExtra("extra_web_title", data.title)
                val adcode =
                    if (TextUtils.isEmpty(UserInfoBean.adcode)) "110105" else UserInfoBean.adcode
                intent.putExtra("extra_web_url", data.url + "&nodeId=" + adcode)
                startActivity(intent)
            }
        }
        banner.setOnBannerListener(mOnWebListener)
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
            val toolBean = ToolBean(i, text, toolimage[i])
            toolBeans.add(toolBean)
        }

        val toolRecycle: RecyclerView = root.findViewById(R.id.rcy_tool)
        toolRecycle.layoutManager = GridLayoutManager(root.context, 4)
        val toolAdapter = ToolHolderAdapte(this, root.context, toolBeans)
        toolRecycle.adapter = toolAdapter
    }

    private fun initWarnCheck() {
        val frameVirusCheck: FrameLayout = root.findViewById(R.id.fl_virus_check)
        val frameFruadCheck: FrameLayout = root.findViewById(R.id.fl_fruad_check)
        frameVirusCheck.setOnClickListener(View.OnClickListener { _ ->
            startActivity(Intent(activity, VirusKillingActivity::class.java))
        })
        frameFruadCheck.setOnClickListener(View.OnClickListener { _ ->
            startActivity(Intent(activity, CheckFraudActivity::class.java))
        })
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

    override fun cancelBtn() {

    }

    override fun clickOKBtn() {
        val intent = Intent(activity, PersonalInfoAddActivity::class.java)
        intent.putExtra("from_page_type", PersonalInfoAddActivity.pageBase)
        startActivity(intent)
    }

    class NewCasePackage(val data: NewCaseData?, val code: Int)
    class NewCaseData(val total: Int, var rows: ArrayList<NewCase>)

    private fun getNewCaseApi(page: Int, row: Int) {
        //https://fzapp.gjfzpt.cn/hicore/api/Information/querylatestcases?Page=1&Rows=2&Sort=releasetime&Order=desc
        getDataByGet(
            "https://fzapp.gjfzpt.cn/hicore/api/Information/querylatestcases?Page=" + page.toString() + "&Rows=" + row.toString() + "&Sort=releasetime&Order=desc",
            addHead = true, callBackFunc = this::addNewCase
        )
    }

    private fun addNewCase(data: String) {
        if (data[0] != '{')
            return
        val json = Gson().fromJson(data, NewCasePackage::class.java)
        if (json != null && json.code == 0 && json.data != null) {
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

    class NewBanderData(val data: ArrayList<NewBander>?, val code: Int)
    class NewBander(
        val createTime: String?,
        val extraID: Long?,
        val id: Long,
        val imgPath: String,
        val isShow: Int?,
        val openType: Int?,
        val sort: Int?,
        val title: String?,
        val updateTime: String?,
        val url: String?
    )

    private fun getNewBander() {
        if (TextUtils.isEmpty(UserInfoBean.acctoken)) {
            var s =
                """{"data":[{"title":null,"url":null,"openType":0,"imgPath":"https://oss.gjfzpt.cn/preventfraud-static/h5/files/banners/306dd120fd9cb87af9a8fbcd6d0790c7.png","sort":2,"isShow":1,"extraID":null,"startTime":null,"endTime":null,"name":null,"description":null,"id":222537046026227713,"createTime":"2021-08-06 10:52:50","updateTime":"2021-08-06 10:52:50","nodeID":0}],"code":0,"msg":"成功"}"""
            addNewBander(s)
        } else
            getDataByGet("https://fzapp.gjfzpt.cn/hicore/api/Banner",
                addHead = true,
                callBackFunc = this::addNewBander)
    }

    private fun addNewBander(data: String) {
        if (data[0] != '{')
            return
        val json = Gson().fromJson(data, NewBanderData::class.java)
        if (json != null && json.code == 0 && json.data != null && json.data.size > 0) {
            val imageList = ArrayList<BanderBean>()
            for (row in json.data) {
                imageList.add(
                    BanderBean(
                        0,
                        row.imgPath,
                        if (row.url == null) "" else row.url,
                        if (row.title == null) "" else row.title,
                        BanderType.TYPE_URL
                    )
                )
            }
            banderAdapter.setDatas(imageList)
            banderAdapter.notifyDataSetChanged()
        }
    }
}