package com.demo.antizha.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.demo.antizha.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        ViewModelProvider(this).get(HomeViewModel::class.java).also { homeViewModel = it }
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val dynamicId = arrayOf(R.id.dynamic01, R.id.dynamic02, R.id.dynamic03, R.id.dynamic04,
            R.id.dynamic05, R.id.dynamic06, R.id.dynamic07, R.id.dynamic08, R.id.dynamic09,
            R.id.dynamic10, R.id.dynamic11, R.id.dynamic12, R.id.dynamic13, R.id.dynamic14,
            R.id.dynamic15, R.id.dynamic16, R.id.dynamic17, R.id.dynamic18, R.id.dynamic19)
        val dateList = generateDate(dynamicId.size)
        for (i in 0..dynamicId.size - 1){
            root.findViewById<TextView>(dynamicId[i]).text = dateList[i]
        }

        val toolText = arrayOf("我要举报", "举报助手", "来电预警", "身份核实")
        val imageResource = arrayOf(R.drawable.iv_home_report, R.drawable.iv_home_case, R.drawable.iv_home_warn, R.drawable.iv_home_id_check)
        val toolId = arrayOf(R.id.tool1, R.id.tool2, R.id.tool3, R.id.tool4)
        for (i in 0..3){
            val ImageView1:ImageView = root.findViewById<LinearLayout>(toolId[i]).findViewById<ImageView>(R.id.ll_itool);
            ImageView1.setImageResource(imageResource[i])
            val TextView1: TextView = root.findViewById<LinearLayout>(toolId[i]).findViewById<TextView>(R.id.ll_ttool);
            TextView1.text = toolText[i]
        }

        return root
    }

    @SuppressLint("SimpleDateFormat")
    private fun generateDate(count:Int):ArrayList<String>{
        val dateList = ArrayList<String>()
        val time = Calendar.getInstance().time
        val calendar = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        val head = "国家反诈中心 "
        calendar.time = time
        for (index in 1..count){
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - (1..3).random())
            val date1 = df.format(calendar.time)
            //add("$head$date1 ${randomFormat((21..23).random())}:${randomFormat((0..59).random())}:${randomFormat((0..59).random())}")
            dateList.add("$head$date1")
        }
        return dateList
    }

    private fun randomFormat(time:Int):String{
        if (time < 10){
            return "0$time"
        }
        return time.toString()
    }

}