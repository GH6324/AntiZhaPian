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
import com.demo.antizha.adapter.SocialAccAdapter
import com.demo.antizha.adapter.SocialAccBean
import com.demo.antizha.databinding.ActivitySocialAccountBinding


class SocialAccountActivity : BaseActivity() {

    private lateinit var infoBinding: ActivitySocialAccountBinding
    private var SocialAccounts: ArrayList<SocialAccBean> = ArrayList()
    private lateinit var socialAccAdapter: SocialAccAdapter
    private lateinit var startEdit: ActivityResultLauncher<Intent>

    override fun initPage() {
        infoBinding = ActivitySocialAccountBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        infoBinding.piTitle.tvTitle.text = "添加诈骗社交账号"
        infoBinding.tvSelectTip.text = "添加"
        infoBinding.lyComplete.tvCommitTip.text = "提示：最多可上传20条社交账号"
        infoBinding.lyComplete.btnCommit.text = "确定"
        getIntentData()
        infoBinding.recyclerview.setLayoutManager(LinearLayoutManager(this,
            RecyclerView.VERTICAL, false))
        socialAccAdapter = SocialAccAdapter(R.layout.item_social_acc, SocialAccounts)
        infoBinding.recyclerview.setAdapter(socialAccAdapter)
        socialAccAdapter.setOnItemChildClickListener(BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            val id: Int = view.getId()
            if (id == R.id.iv_delete) {
                SocialAccounts.removeAt(position)
                adapter.notifyDataSetChanged()
            } else if (id == R.id.iv_edit) {
                editAcc(position)
            }
        })

        infoBinding.piTitle.ivBack.setOnClickListener {
            setResult(RESULT_CANCELED, intent)
            finish()
        }
        infoBinding.llSelect.setOnClickListener {
            editAcc(-1)
        }
        infoBinding.lyComplete.btnCommit.setOnClickListener {
            val intent = Intent()
            intent.putParcelableArrayListExtra("accounts", SocialAccounts)
            setResult(RESULT_OK, intent)
            finish()
        }
        startEdit =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode != Activity.RESULT_OK)
                    return@registerForActivityResult
                if (result.data == null || result.data!!.extras == null)
                    return@registerForActivityResult
                val tag = result.data!!.extras!!.getInt("tag")
                val socialAccount = result.data!!.getParcelableExtra<SocialAccBean>("account")!!
                if (tag == -1)
                    SocialAccounts.add(socialAccount)
                else
                    SocialAccounts[tag] = socialAccount
                socialAccAdapter.notifyDataSetChanged()
            }
    }

    private fun getIntentData() {
        val list = intent.getParcelableArrayListExtra<SocialAccBean>("accounts")
        if (list != null) {
            SocialAccounts.addAll(list)
        }
    }

    fun editAcc(i: Int) {
        val intent = Intent(mActivity, SocialAccountEditActivity::class.java)
        intent.putExtra("pos", i)
        if (i != -1) {
            intent.putExtra("acc", SocialAccounts.get(i))
        }
        startEdit.launch(intent)
    }
}