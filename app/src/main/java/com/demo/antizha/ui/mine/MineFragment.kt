package com.demo.antizha.ui.mine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.text.TextUtils;
import android.widget.LinearLayout
import com.demo.antizha.*

class MineFragment : Fragment() {

    private lateinit var mineViewModel: MineViewModel
    private lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mineViewModel =
            ViewModelProvider(this).get(MineViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_mine, container, false)

        val personalInfo: TextView = root.findViewById(R.id.tv_explain)
         personalInfo.setOnClickListener {
            val intentInfo = Intent(activity, MinePersonalActivity::class.java)
            startActivity(intentInfo)
        }
        return root
    }
    override fun onResume()
    {
        super.onResume()
        val phoneNumber: TextView = root.findViewById(R.id.tv_phone)
        if (!TextUtils.isEmpty(userInfoBean.mobileNumber)) {
            phoneNumber.text = getString(R.string.title_mine) + " " + userInfoBean.mobileNumber.substring(0, 3) +
                    "******" + userInfoBean.mobileNumber.substring(userInfoBean.mobileNumber.length - 2)
        }
        else {
            phoneNumber.text = generatePhoneNumber()
        }
        val ver: LinearLayout = root.findViewById(R.id.ll_version)
        ver.visibility = if (TextUtils.isEmpty(userInfoBean.name)) View.VISIBLE else View.GONE
    }
    private fun generatePhoneNumber(): String {   //手机号生成
        val head = getString(R.string.title_mine)
        val a = listOf(
            131,
            132,
            133,
            134,
            135,
            136,
            137,
            138,
            139,
            177,
            151,
            152,
            153,
            155,
            156
        ).random().toString()
        val b = "******"
        val c = (10..99).random().toString()
        return "$head  $a$b$c"
    }
}