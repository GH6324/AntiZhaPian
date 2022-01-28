package com.demo.antizha.ui.activity
//选择行业窗口
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivityIndustryListBinding
import com.demo.antizha.*

class IndustryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var content: TextView = view.findViewById(R.id.tv_content) as TextView
    var select: ImageView = view.findViewById(R.id.iv_select) as ImageView
}

class IndustryHolderAdapte(private var context: Context, private var list: ArrayList<String>) :
    RecyclerView.Adapter<IndustryViewHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener
    var select: Int = -1

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    init {
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, i: Int) {
        if (i >= 0) {
            holder.content.text = list[i]
            holder.select.visibility = if (i == select) View.VISIBLE else View.INVISIBLE
            holder.itemView.setOnClickListener {
                onItemClickListener.onItemClick(
                    holder.itemView,
                    i
                )
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): IndustryViewHolder {
        return IndustryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_industry, viewGroup, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class IndustryActivity : BaseActivity() {
    override fun initPage() {
        val industryListBinding = ActivityIndustryListBinding.inflate(layoutInflater)
        setContentView(industryListBinding.root)
        industryListBinding.piTitle.tvTitle.text = "行业"

        val industrys = arrayListOf(
            "衣、林、牧、渔业", "金融、保险、投资", "房地产业", "信息传输、软件和信息技术服务业",
            "教育、学生", "文化、体育和娱乐业", "批发和零售业", "建筑业", "住宿和餐饮业",
            "制造业", "交通运输、仓储和邮政业", "科学研究和技术服务业", "卫生和社会工作",
            "居民服务、修理和其他服务业", "水利、环境和公共设施管理业", "电力、热力、燃气及水生产和供应业",
            "采矿业", "公共管理、社会保障和社会组织", "旅游、购物、休闲", "机械设备、通用零部件",
            "家具、生活用品、食品", "工艺品、礼品", "新闻、出版、科研", "广告、会展、商务办公、咨询",
            "贸易、市场", "党政机关、社会团体", "离退休人员", "国际组织", "其他行业"
        )
        val industrysRecycle: RecyclerView = industryListBinding.rvList
        industrysRecycle.layoutManager = LinearLayoutManager(this)
        val industrysAdapter = IndustryHolderAdapte(this, industrys)
        industrysRecycle.adapter = industrysAdapter

        val position = industrys.indexOf(UserInfoBean.professionName)
        industrysAdapter.select = position
        industrysAdapter.notifyItemChanged(position)
        if (position > 0)
            industrysRecycle.smoothScrollToPosition(position)
        industrysAdapter.setOnItemClickListener(object : IndustryHolderAdapte.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val industry: String = industrys[position]
                if (industry != UserInfoBean.professionName) {
                    UserInfoBean.professionName = industry
                    UserInfoBean.commit()
                }
                finish()
            }
        })
        industryListBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
    }
}