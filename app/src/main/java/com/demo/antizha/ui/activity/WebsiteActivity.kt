package com.demo.antizha.ui.activity

import android.content.Intent
import android.os.Build
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivityWeburlBinding
import com.demo.antizha.util.ToastUtil
import java.util.*


class WebsiteActivity : BaseActivity() {
    private lateinit var infoBinding: ActivityWeburlBinding
    private val etContents: LinkedList<EditText> = LinkedList<EditText>()
    private val ivClears: LinkedList<ImageView> = LinkedList<ImageView>()
    private val MAX_COUNT = 20

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initPage() {
        supportActionBar?.hide()
        infoBinding = ActivityWeburlBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "添加诈骗网址"
        infoBinding.lyComplete.tvCommitTip.text = "最多可添加" + MAX_COUNT.toString() + "条网址"
        infoBinding.lyComplete.btnCommit.text = "确定"
        infoBinding.vLine.setVisibility(View.GONE);
        infoBinding.flSelectHistory.setVisibility(View.GONE);
        initData()
        infoBinding.flSelect.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (!isFull()) {
                    addUrl("")
                }
            }
        })
        infoBinding.piTitle.ivBack.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
        infoBinding.lyComplete.btnCommit.setOnClickListener {
            val intent = Intent()
            intent.putStringArrayListExtra("url", getSelectUrl())
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun initData() {
        val array = intent.getStringArrayListExtra("url")
        if (array == null || array.size == 0) {
            for (i in 0..4)
                addUrl("")
        } else {
            val it: Iterator<String> = array.iterator()
            while (it.hasNext()) {
                addUrl(it.next())
            }
        }
    }

    private fun addUrl(url: String) {
        val inflate = View.inflate(mActivity, R.layout.recyclerview_url_select, null)
        val etContent = inflate.findViewById<EditText>(R.id.et_content)
        val ivClear = inflate.findViewById<ImageView>(R.id.iv_clear)
        etContent.filters = arrayOf<InputFilter>(LengthFilter(500))
        ivClear.setOnClickListener(OnClearClickListener())
        infoBinding.linearlayout.addView(inflate)
        this.etContents.add(etContent)
        this.ivClears.add(ivClear)
        if (!TextUtils.isEmpty(url)) {
            etContent.setText(url)
        }
    }

    fun isFull(): Boolean {
        if (etContents.size < MAX_COUNT) {
            return false
        }
        ToastUtil.showMessage("最多可添加" + MAX_COUNT.toString() + "条网址")
        return true
    }

    private fun getSelectUrl(): ArrayList<String> {
        val urls = ArrayList<String>()
        for (i in etContents.indices) {
            val url = etContents[i].text.toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(url) && !urls.contains(url)) {
                urls.add(url)
            }
        }
        return urls
    }

    inner class OnClearClickListener : View.OnClickListener {
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
                    infoBinding.linearlayout.removeViewAt(i)
                    break
                }
            }
        }
    }
}