package com.demo.antizha

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.navigation.fragment.NavHostFragment
import com.demo.antizha.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.demo.antizha.ui.home.HomeFragment
import com.demo.antizha.ui.dashboard.DashboardFragment
import com.demo.antizha.ui.mine.MineFragment

class MainActivity : AppCompatActivity() {
    private var lastIndex = 0
    private var mFragments = ArrayList<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        dp2px = Dp2Px(getApplicationContext())
        val SPLASH_TIME:Long = 5000
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        }, SPLASH_TIME)
        initData()
    }
    private fun initData() {
        mFragments.add(HomeFragment())
        mFragments.add(DashboardFragment())
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