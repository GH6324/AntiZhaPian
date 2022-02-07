package com.demo.antizha.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.demo.antizha.R
import com.demo.antizha.adapter.SmsBean
import com.demo.antizha.adapter.SmsDeleteAdapter
import com.demo.antizha.databinding.ActivityCallBinding
import com.hjq.toast.ToastUtils


class SmsActivity : BaseActivity(), SmsDeleteAdapter.ItemClickListener {
    private lateinit var infoBinding: ActivityCallBinding
    private val smss: ArrayList<SmsBean> = ArrayList()
    private lateinit var smsDeleteAdapter: SmsDeleteAdapter
    private lateinit var startEdit: ActivityResultLauncher<Intent>
    private val MaxCount = 20

    override fun initPage() {
        infoBinding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "添加诈骗短信"
        infoBinding.lySelect.tvSelectTip.text = "选择短信"
        infoBinding.lySelect.tvInputTip.text = "手动输入"
        infoBinding.lyComplete.tvCommitTip.text = "最多可选择" + MaxCount + "条短信"
        initActivityResultLauncher()
        getIntentData()
        val lym = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        infoBinding.recyclerview.setLayoutManager(lym)
        smsDeleteAdapter = SmsDeleteAdapter(R.layout.recyclerview_sms_record_select, smss)
        smsDeleteAdapter.bindToRecyclerView(infoBinding.recyclerview)
        infoBinding.recyclerview.setAdapter(smsDeleteAdapter)
        smsDeleteAdapter.setOnItemChildClickListener(BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            if (view.getId() == R.id.iv_clear) {
                smss.removeAt(position)
                adapter.notifyDataSetChanged()
            }
        })
        smsDeleteAdapter.setOnItemClickListener(this)
        infoBinding.piTitle.ivBack.setOnClickListener {
            onBackPressed()
        }
        infoBinding.lyComplete.btnCommit.setOnClickListener {
            onBackPressed()
        }
        infoBinding.lySelect.llInput.setOnClickListener {
            if (smss.size == MaxCount) {
                ToastUtils.show("最多可选择" + MaxCount + "条短信")
                return@setOnClickListener
            }
            val intent = Intent(this, SmsAddActivity::class.java)
            intent.putExtra("sms", SmsBean())
            startEdit.launch(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        intent.putParcelableArrayListExtra("sms", smss)
        super.onBackPressed()
    }

    fun getIntentData() {
        var list = intent.getParcelableArrayListExtra<SmsBean>("sms")
        if (list != null) {
            smss.addAll(list)
        }
    }

    override fun onItemClickListener(i: Int, list: List<SmsBean>) {
        val smsBean = list[i]
        if (smsBean.isInput) {
            smsBean.index = i
            val intent = Intent(this, SmsAddActivity::class.java)
            intent.putExtra("sms", smsBean)
            startEdit.launch(intent)
        }
    }

    fun initActivityResultLauncher() {
        startEdit =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode != Activity.RESULT_OK)
                    return@registerForActivityResult
                if (result.data == null || result.data!!.extras == null)
                    return@registerForActivityResult
                val sms = result.data!!.extras!!.getParcelable<SmsBean>("sms")
                if (sms != null) {
                    if (sms.index == -1)
                        smss.add(sms)
                    else
                        smss[sms.index] = sms
                    smsDeleteAdapter.notifyDataSetChanged()
                }
            }
    }
}