package com.demo.antizha.ui.activity

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.demo.antizha.Dp2Px
import com.demo.antizha.R
import com.demo.antizha.databinding.ActivityMainBinding
import com.demo.antizha.dp2px
import com.demo.antizha.ui.fragment.home.HomeFragment
import com.demo.antizha.ui.fragment.mine.MineFragment
import com.demo.antizha.ui.fragment.web.WebFragment
import com.demo.antizha.userInfoBean
import com.google.android.material.bottomnavigation.BottomNavigationView
import qiu.niorgai.StatusBarCompat


class MainActivity : BaseActivity() {
    private var lastIndex = 0
    private var mFragments = ArrayList<Fragment>()
    @RequiresApi(Build.VERSION_CODES.R)
    override fun initPage() {
        dp2px = Dp2Px(applicationContext)
        val SPLASH_TIME: Long = 5000
        supportActionBar?.hide()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //隐藏状态栏
        binding.root.windowInsetsController?.hide(WindowInsets.Type.statusBars())
        //刘海屏？反正测试的时候不加这些代码，状态栏隐藏后会有一块空白
        val lp = window.attributes
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.attributes = lp

        userInfoBean.Init(this)
        binding.navView.setOnNavigationItemSelectedListener { item ->
            resetIcon(binding.navView)
            when (item.itemId) {
                R.id.navigation_home -> {
                    item.setIcon(R.mipmap.tab_home_seled)
                    setFragmentPosition(0)
                    true
                }
                R.id.navigation_dashboard -> {
                    item.setIcon(R.mipmap.tab_xc_seled)
                    setFragmentPosition(1)
                    true
                }
                R.id.navigation_notifications -> {
                    item.setIcon(R.mipmap.tab_mine_seled)
                    setFragmentPosition(2)
                    true
                }
                else -> false
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            binding.splash.root.visibility = View.GONE
            binding.navView.visibility = View.VISIBLE
            binding.llFrameLayout.visibility = View.VISIBLE
            StatusBarCompat.translucentStatusBar(this, true, true)
            binding.root.windowInsetsController?.show(WindowInsets.Type.statusBars())
        }, SPLASH_TIME)
        initData()
    }

    private fun initData() {
        mFragments.add(HomeFragment())
        mFragments.add(WebFragment())
        mFragments.add(MineFragment())
        setFragmentPosition(0)
    }

    private fun setFragmentPosition(position: Int) {
        val ft = supportFragmentManager.beginTransaction()
        val currentFragment = mFragments[position]
        val lastFragment = mFragments[lastIndex]
        lastIndex = position
        ft.hide(lastFragment)
        if (!currentFragment.isAdded) {
            supportFragmentManager.beginTransaction().remove(currentFragment).commit()
            ft.add(R.id.ll_frameLayout, currentFragment)
        }
        ft.show(currentFragment)
        ft.commitAllowingStateLoss()
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