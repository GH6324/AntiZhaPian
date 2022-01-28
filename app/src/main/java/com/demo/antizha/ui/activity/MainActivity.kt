package com.demo.antizha.ui.activity


import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.demo.antizha.Dp2Px
import com.demo.antizha.R
import com.demo.antizha.UserInfoBean
import com.demo.antizha.databinding.ActivityMainBinding
import com.demo.antizha.dp2px
import com.demo.antizha.ui.Hicore
import com.demo.antizha.ui.fragment.home.HomeFragment
import com.demo.antizha.ui.fragment.mine.MineFragment
import com.demo.antizha.ui.fragment.web.WebFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import qiu.niorgai.StatusBarCompat


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private var lastIndex = 0
    private var mFragments = ArrayList<Fragment>()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initPage() {
        dp2px = Dp2Px(Hicore.context)
        val SPLASH_TIME: Long = 5000
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //隐藏状态栏
        binding.root.windowInsetsController?.hide(WindowInsets.Type.statusBars())
        //刘海屏？反正测试的时候不加这些代码，状态栏隐藏后会有一块空白
        val lp = window.attributes
        lp.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.attributes = lp
        UserInfoBean.Init()
        binding.navView.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener  {
            override fun onNavigationItemSelected(item: MenuItem): Boolean
            {
                resetIcon(binding.navView)
                return when (item.itemId) {
                    R.id.navigation_home -> {
                        item.setIcon(R.mipmap.tab_home_seled)
                        binding.viewPager.setCurrentItem(0, false)
                        true
                    }
                    R.id.navigation_dashboard -> {
                        item.setIcon(R.mipmap.tab_xc_seled)
                        binding.viewPager.setCurrentItem(1, false)
                        true
                    }
                    R.id.navigation_notifications -> {
                        item.setIcon(R.mipmap.tab_mine_seled)
                        binding.viewPager.setCurrentItem(2, false)
                        true
                    }
                    else -> false
                }
            }
        })
        Handler(Looper.getMainLooper()).postDelayed({
            binding.splash.root.visibility = View.GONE
            binding.navView.visibility = View.VISIBLE
            binding.viewPager.visibility = View.VISIBLE
            StatusBarCompat.translucentStatusBar(this, true, true)
            binding.root.windowInsetsController?.show(WindowInsets.Type.statusBars())
        }, SPLASH_TIME)
        initData()
    }

    private fun initData() {
        mFragments.add(HomeFragment())
        mFragments.add(WebFragment())
        mFragments.add(MineFragment())
        binding.viewPager.run {
            isUserInputEnabled = false
            offscreenPageLimit = 2
            adapter = object: FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount(): Int = mFragments.size
                override fun createFragment(position: Int): Fragment = mFragments[position]
            }
        }
        binding.viewPager.setCurrentItem(0)
    }


    private fun resetIcon(navView: BottomNavigationView) {
        val home = navView.menu.findItem(R.id.navigation_home)
        val dashboard = navView.menu.findItem(R.id.navigation_dashboard)
        val mine = navView.menu.findItem(R.id.navigation_notifications)
        home.setIcon(R.mipmap.tab_home_unseled)
        dashboard.setIcon(R.mipmap.tab_xc_unseled)
        mine.setIcon(R.mipmap.tab_mine_unseled)
    }
}