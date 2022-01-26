package com.demo.antizha.ui.activity

import android.content.Intent
import android.os.Build
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivityCallBinding
import java.io.Serializable

class CallBean : Serializable {
    var crime_time: String? = null
    var duration: Long = 0
    var isInput = false
    var isSelect = false
    var number: String? = null
    var plcae: String? = null
    var suspectInfoID: String? = null
    var type = 0

    constructor() {}
    constructor(str: String?, str2: String?, str3: String?, i: Int, z: Boolean) {
        number = str
        crime_time = str2
        plcae = str3
        type = i
        isSelect = z
        isInput = !isSelect
    }

    constructor(str: String?, str2: String?, str3: String?, i: Int, z: Boolean, j: Long) {
        number = str
        crime_time = str2
        plcae = str3
        type = i
        isSelect = z
        duration = j
        isInput = !isSelect
    }
}

class CallActivity : BaseActivity() {
    private lateinit var infoBinding: ActivityCallBinding
    private var etContents: ArrayList<EditText> = ArrayList()
    private var ivClears: ArrayList<ImageView> = ArrayList()
    private val selectCallBeans: ArrayList<CallBean> = ArrayList()
    private val inputCallBeans: ArrayList<CallBean> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initPage() {
        supportActionBar?.hide()
        infoBinding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "添加诈骗电话"
        infoBinding.lySelect.tvSelectTip.setText("选择通话记录")
        infoBinding.lySelect.tvInputTip.setText("手动输入")
        infoBinding.lyComplete.tvCommitTip.text = "最多可选择" + 20 + "条举报电话"
        initData()
        infoBinding.piTitle.ivBack.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
        infoBinding.lyComplete.btnCommit.setOnClickListener {
            val callBeans = ArrayList<CallBean>()
            inputCallBeans.clear()
            val hashSet: HashSet<String> = HashSet()
            for (i in 0 until etContents.size) {
                val call = etContents[i].getText().toString()
                if (!TextUtils.isEmpty(call)) {
                    hashSet.add(call)
                }
            }
            for (call in hashSet) {
                val callBean = CallBean(call, "", "未知", -1, false)
                callBean.isInput = true
                inputCallBeans.add(callBean)
            }
            callBeans.addAll(selectCallBeans)
            callBeans.addAll(inputCallBeans)
            val intent = Intent()
            intent.putExtra("call", callBeans as Serializable)
            setResult(RESULT_OK, intent)
            finish()
        }
        infoBinding.lySelect.llSelect.setOnClickListener {
            //需要权限
        }
        infoBinding.lySelect.llInput.setOnClickListener {
            inputNumber("")
        }
    }

    fun initData() {
        var list: ArrayList<CallBean>?
        try {
            list = intent.getSerializableExtra("call") as ArrayList<CallBean>?
        } catch (e: Exception) {
            e.printStackTrace()
            list = null
        }
        val selectLogs = ArrayList<CallBean>()
        val inputLogs = ArrayList<CallBean>()
        if (list != null && list.size > 0) {
            for (callBean in list) {
                if (callBean.isInput) {
                    inputLogs.add(callBean)
                } else {
                    selectLogs.add(callBean)
                }
            }
        }
        if (selectLogs.size > 0) {
            selectCallBeans.addAll(selectLogs)
            //this.CallBeanAdapter.notifyDataSetChanged()
        }
        if (inputLogs.size > 0) {
            inputCallBeans.addAll(inputLogs)
            for (callBean in inputCallBeans) {
                inputNumber(callBean.number!!)
            }
        }
    }

    private fun inputNumber(str: String) {
        val inflate = View.inflate(this, R.layout.recyclerview_url_select, null)
        val etContent = inflate.findViewById<EditText>(R.id.et_content)
        val ivClear = inflate.findViewById<ImageView>(R.id.iv_clear)
        etContent.hint = "请输入电话号码"
        etContent.filters = arrayOf<InputFilter>(LengthFilter(25))
        etContent.keyListener = DigitsKeyListener.getInstance("1234567890+")
        etContent.inputType = 2
        infoBinding.llInputPart.addView(inflate)
        etContents.add(etContent)
        ivClears.add(ivClear)
        if (!TextUtils.isEmpty(str)) {
            etContent.setText(str)
        }
        ivClear.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (view == null)
                    return
                if (etContents.size == 1) {
                    etContents[0].setText("")
                    return
                }
                for ((i, ivClear) in ivClears.withIndex()) {
                    if (ivClear === view) {
                        etContents.removeAt(i)
                        ivClears.removeAt(i)
                        infoBinding.llInputPart.removeViewAt(i)
                        break
                    }
                }
            }
        })
    }
}