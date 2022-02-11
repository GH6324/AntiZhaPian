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
import com.demo.antizha.UserInfoBean
import com.demo.antizha.databinding.ActivityIndustryListBinding
import com.demo.antizha.util.FileUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class IndustryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var content: TextView = view.findViewById(R.id.tv_content) as TextView
    var select: ImageView = view.findViewById(R.id.iv_select) as ImageView
}

class IndustryHolderAdapte(private var context: Context,
                           private var list: ArrayList<IndustryBean>) :
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
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, i: Int) {
        if (i >= 0) {
            holder.content.text = list[i].positionName
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

class IndustryBean(val positionId: Int, val positionName: String)
class IndustryBeanData(val code: Int, var data: ArrayList<IndustryBean>)

class IndustryActivity : BaseActivity() {
    private lateinit var infoBinding: ActivityIndustryListBinding
    private var industrys: ArrayList<IndustryBean> = ArrayList()
    override fun initPage() {
        infoBinding = ActivityIndustryListBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "行业"
        
        initIndustrys()
        infoBinding.rvList.layoutManager = LinearLayoutManager(this)
        val industrysAdapter = IndustryHolderAdapte(this, industrys)
        infoBinding.rvList.adapter = industrysAdapter

        val position = getIndustrysPos(UserInfoBean.professionName)
        industrysAdapter.select = position
        industrysAdapter.notifyItemChanged(position)
        if (position > 0)
            infoBinding.rvList.smoothScrollToPosition(position)
        industrysAdapter.setOnItemClickListener(object : IndustryHolderAdapte.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val industry: String = industrys[position].positionName
                if (industry != UserInfoBean.professionName) {
                    UserInfoBean.professionName = industry
                    UserInfoBean.commit()
                }
                finish()
            }
        })
        infoBinding.piTitle.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun initIndustrys() {
        val inputStream = FileUtil.openfile("positions.txt")
        val data: IndustryBeanData = Gson().fromJson(InputStreamReader(inputStream, "UTF-8"),
            object : TypeToken<IndustryBeanData>() {}.type)
        inputStream.close()
        industrys = data.data
    }

    private fun getIndustrysPos(pname: String): Int {
        for ((i, industry) in industrys.withIndex()) {
            if (industry.positionName == pname)
                return i
        }
        return 0
    }
}