package com.demo.antizha.ui.fragment.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.antizha.*
import com.demo.antizha.UnsafeOkHttpClient.getDataByGet
import com.demo.antizha.interfaces.IApiResult
import com.demo.antizha.interfaces.IClickListener
import com.demo.antizha.md.JniHandStamp
import com.demo.antizha.newwork.DictionaryUtils
import com.demo.antizha.ui.HiCore
import com.demo.antizha.ui.activity.*
import com.demo.antizha.util.AppUtil
import com.demo.antizha.util.DialogUtils
import com.demo.antizha.util.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.RoundLinesIndicator
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.util.BannerUtils
import okhttp3.Headers
import java.io.InputStreamReader


class ToolBean(val id: Int, val name: String, val imageId: Int)

class ToolViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var name: TextView = view.findViewById(R.id.tv_name) as TextView
    var image: ImageView = view.findViewById(R.id.iv_icon) as ImageView
}

class ToolHolderAdapter(private var homeFragment: HomeFragment,
                        private var context: Context,
                        private var list: ArrayList<ToolBean>) :
    RecyclerView.Adapter<ToolViewHolder>() {

    override fun onBindViewHolder(holder: ToolViewHolder, i: Int) {
        holder.name.text = list[i].name
        holder.image.setImageResource(list[i].imageId)
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!HiCore.app.isDouble()) {
                    if (list.size <= 0)
                        return
                    val adapterPosition = holder.absoluteAdapterPosition
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
    var cdnCover: String,
    var localFilePath: String
)

class NewCaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var time: TextView = view.findViewById(R.id.iv_topic_time) as TextView
    var title: TextView = view.findViewById(R.id.iv_topic_tit) as TextView
    var image: ImageView = view.findViewById(R.id.iv_topic_pic) as ImageView
}

class NewCaseHolderAdapter(
    private var context: Context,
    private var view: View,
    private var recycle: RecyclerView,
    private var list: ArrayList<NewCase>
) : RecyclerView.Adapter<NewCaseViewHolder>() {

    override fun onBindViewHolder(holder: NewCaseViewHolder, i: Int) {
        holder.time.text = "${list[i].author}  ${optimizationTimeStr(list[i].updateTime)}"
        holder.title.text = list[i].title
        Glide.with(view).load(list[i].cdnCover).into(holder.image)
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            val url: String = list[holder.absoluteAdapterPosition].localFilePath
            val id: String = list[holder.absoluteAdapterPosition].id
            override fun onClick(v: View?) {
                if (!HiCore.app.isDouble()) {
                    val intent = Intent(context, PromosWebDetActivity::class.java)
                    intent.putExtra("extra_web_title", "国家反诈中心")
                    val adcode =
                        if (TextUtils.isEmpty(UserInfoBean.adcode)) "110105" else UserInfoBean.adcode
                    intent.putExtra("extra_web_url", "$url&nodeId=$adcode")
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
        recycle.layoutParams?.height =
            context.resources.displayMetrics.density.let { (100 * list.size * it + 0.5).toInt() }
    }

    fun addNewCase(newCases: ArrayList<NewCase>) {
        list.addAll(newCases)
        recycle.layoutParams?.height =
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
    imageUrls: ArrayList<BanderBean>
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
    private lateinit var mActivity: Activity
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var root: View
    private lateinit var newCaseAdapter: NewCaseHolderAdapter
    private lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var banderAdapter: BanderAdapter
    public var pageIndex = 0
    private var pagecount = 2

    class OnGetLatestCase : IApiResult {
        var saveFile: String
        var homeFragment: HomeFragment

        constructor(saveFile: String, homeFragment: HomeFragment) {
            this.saveFile = saveFile
            this.homeFragment = homeFragment
        }

        override fun callBack(data: String, headers: Headers?) {
            homeFragment.addLatestCase(data, saveFile, headers)
        }
    }

    class OnGetLatestCaseV2 : IApiResult {
        var saveFile: String
        var homeFragment: HomeFragment

        constructor(saveFile: String, homeFragment: HomeFragment) {
            this.saveFile = saveFile
            this.homeFragment = homeFragment
        }

        override fun callBack(data: String, headers: Headers?) {
            homeFragment.addLatestCaseV2(data, saveFile)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this)[HomeViewModel::class.java].also { homeViewModel = it }
        root = inflater.inflate(R.layout.fragment_home, container, false)
        initBanner()
        initTool()
        initWarnCheck()
        initNewCase()
        initRefreshLayout()
        initNoteList()
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    private fun initBanner() {
        val imageList = ArrayList<BanderBean>()
        imageList.add(BanderBean(Integer.valueOf(R.mipmap.banner1),
            "", "", "", BanderType.TYPE_RES))
        val banner: Banner<BanderBean, BanderAdapter> = root.findViewById(R.id.banner)
        banner.addBannerLifecycleObserver(this)
        banner.setBannerRound(20f)
        banner.setLoopTime(5000)
        banner.indicator = RoundLinesIndicator(HiCore.getContext())
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
        loadBander()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initTool() {
        val toolText = arrayOf("我要举报", "举报助手", "来电预警", "身份核实")
        val toolImage = arrayOf(
            R.drawable.iv_home_report,
            R.drawable.iv_home_case,
            R.drawable.iv_home_warn,
            R.drawable.iv_home_id_check
        )
        val toolBeans = ArrayList<ToolBean>()
        for ((i, text) in toolText.withIndex()) {
            val toolBean = ToolBean(i, text, toolImage[i])
            toolBeans.add(toolBean)
        }

        val toolRecycle: RecyclerView = root.findViewById(R.id.rcy_tool)
        toolRecycle.layoutManager = GridLayoutManager(root.context, 4)
        val toolAdapter = ToolHolderAdapter(this, root.context, toolBeans)
        toolRecycle.adapter = toolAdapter
        toolAdapter.notifyDataSetChanged()
    }

    private fun initWarnCheck() {
        val frameVirusCheck: FrameLayout = root.findViewById(R.id.fl_virus_check)
        val frameFruadCheck: FrameLayout = root.findViewById(R.id.fl_fruad_check)
        frameVirusCheck.setOnClickListener(View.OnClickListener {
            if (AppUtil.checkPermission(mActivity, true))
                startActivity(Intent(activity, VirusKillingActivity::class.java))
        })
        frameFruadCheck.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, CheckFraudActivity::class.java))
        })
    }

    private fun initNewCase() {
        val newCaseRecycle: RecyclerView = root.findViewById(R.id.rcy_newcase)
        newCaseRecycle.layoutManager = LinearLayoutManager(root.context)
        newCaseAdapter =
            NewCaseHolderAdapter(root.context, root, newCaseRecycle, ArrayList())
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
            if (pageIndex < pagecount)
                getNewCaseApi(pageIndex + 1)
        }
        getNewCaseApi(1)
    }

    private fun initNoteList() {
        val flNoteView: FrameLayout = root.findViewById(R.id.fl_note_view)
        flNoteView.setOnClickListener {
            startActivity(Intent(activity, NoteListActivity::class.java))
        }
    }

    override fun cancelBtn() {

    }

    override fun clickOKBtn() {
        val intent = Intent(activity, PersonalInfoAddActivity::class.java)
        intent.putExtra("from_page_type", PersonalInfoAddActivity.pageBase)
        startActivity(intent)
    }

    class NewCaseData(var rows: ArrayList<NewCase>)
    class NewCasePackage(val data: NewCaseData?, val code: Int)

    private fun getNewCaseApi(page: Int) {
        if (DictionaryUtils.step < 2) {
            Handler(Looper.getMainLooper()).postDelayed({
                getNewCaseApi(page)
            }, 500)
            return
        }
        //https://fzapp.gjfzpt.cn/hicore/api/Information/querylatestcases?Page=1&Rows=2&Sort=releasetime&Order=desc
        getDataByGet(
            BuildConfig.RELEASE_OSS_DOWNLOAD + "h5/news/index/index-${page}.json",
            addHead = true, OnGetLatestCase("newcase${page}.txt", this))
    }

    private fun getNewCaseApiV2(page: Int) {
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["Sort"] = "releasetime"
        hashMap["Rows"] = "10"
        hashMap["Order"] = "desc"
        hashMap["Page"] = "${page}"
        UnsafeOkHttpClient.getDataByPost(
            BuildConfig.RELEASE_API_URL + "/api/Information/querylatestcasesv2",
            bodyMap = JniHandStamp.princiHttp(hashMap),
            addHead = true,
            OnGetLatestCaseV2("latestcase${page}.txt", this)
        )
    }

    fun addLatestCaseV2(data: String, saveFile: String) {
        val text = JniHandStamp.getSData(data)
        if (TextUtils.isEmpty(text))
            return
        if (text[0] != '{')
            return
        val json = Gson().fromJson(text, NewCasePackage::class.java)
        if (json != null && json.code == 0 && json.data != null) {
            if (!TextUtils.isEmpty(saveFile))
                saveBuff2File(text, saveFile)
            refreshLayout.finishLoadMore()
            val oldCount = newCaseAdapter.itemCount
            newCaseAdapter.addNewCase(json.data.rows)
            newCaseAdapter.notifyItemRangeInserted(oldCount, json.data.rows.size)
            pageIndex++
            if (pageIndex >= pagecount) {
                val morecase: View = root.findViewById(R.id.ly_morecase)
                morecase.visibility = View.VISIBLE
                refreshLayout.setEnableLoadMore(false)
            }
        }

    }

    private fun getNewCaseList(data: String): ArrayList<NewCase> {
        if (TextUtils.isEmpty(data))
            return ArrayList<NewCase>()
        if (data[0] != '[')
            return ArrayList<NewCase>()
        return Gson().fromJson<ArrayList<NewCase>>(data,
            object : TypeToken<ArrayList<NewCase>>() {}.type)
    }

    private fun addLatestCase(data: String, saveFile: String, headers: Headers?) {
        //服务器发送的数据明明没有压缩，却故意设置一个错误的zip标记，
        //让客户端无法正确处理，然后去调用新的接口，这是个什么处理方式？
        //以上是根据2.0.1代码猜的，他并没有处理age字段，
        //而是直接在onError或解析出的数值为空里处理
        val newCaseList = getNewCaseList(data)
        if (newCaseList.size == 0) {
            getNewCaseApiV2(pageIndex + 1)
            return
        }
        for (NewCase in newCaseList) {
            if (NewCase.cdnCover.substring(0, 4) != "http")
                NewCase.cdnCover = BuildConfig.RELEASE_OSS_DOWNLOAD + "h5/" + NewCase.cdnCover
            if (NewCase.localFilePath.substring(0, 4) != "http")
                NewCase.localFilePath =
                    BuildConfig.RELEASE_OSS_DOWNLOAD + "h5/" + NewCase.localFilePath
        }
        if (newCaseList != null) {
            if (!TextUtils.isEmpty(saveFile))
                saveBuff2File(data, saveFile)
            refreshLayout.finishLoadMore()
            val oldCount = newCaseAdapter.itemCount
            newCaseAdapter.addNewCase(newCaseList)
            newCaseAdapter.notifyItemRangeInserted(oldCount, newCaseList.size)
            pageIndex++
            if (pageIndex >= pagecount) {
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
        var imgPath: String,
        val isShow: Int?,
        val openType: Int?,
        val sort: Int?,
        val title: String?,
        val updateTime: String?,
        val url: String?
    )

    private fun loadBander() {
        if (DictionaryUtils.step < 2) {
            Handler(Looper.getMainLooper()).postDelayed({
                loadBander()
            }, 500)
            return
        }
        val inStream = Utils.openfile("bander.txt")
        val inputReader = InputStreamReader(inStream, charset("UTF_8"))
        val buff = inputReader.readText()
        inStream.close()
        addNewBander(buff)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addNewBander(data: String) {
        if (data[0] != '{')
            return
        val json = Gson().fromJson(data, NewBanderData::class.java)
        if (json != null && json.code == 0 && json.data != null && json.data.size > 0) {
            val imageList = ArrayList<BanderBean>()
            for (row in json.data) {
                if (row.imgPath.substring(0, 4) != "http")
                    row.imgPath = BuildConfig.RELEASE_OSS_DOWNLOAD + "h5/" + row.imgPath
                imageList.add(
                    BanderBean(
                        0,
                        row.imgPath,
                        row.url ?: "",
                        row.title ?: "",
                        BanderType.TYPE_URL
                    )
                )
            }
            banderAdapter.setDatas(imageList)
            banderAdapter.notifyDataSetChanged()
        }
    }
}