package com.demo.antizha

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.navigation.fragment.NavHostFragment
import com.demo.antizha.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Handler;
import android.os.Looper


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val SPLASH_TIME:Long = 5000
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userInfoBean.Init(this)
        val navHostFragment =
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.itemBackground = null
        binding.navView.menu.forEach {
            val view = binding.navView.findViewById<View>(it.itemId)
            view.setOnLongClickListener { true }
        }

        binding.navView.setOnNavigationItemSelectedListener { item ->
            resetIcon(binding.navView)
            when (item.itemId) {
                R.id.navigation_home -> {
                    item.setIcon(R.mipmap.tab_home_seled)
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_dashboard -> {
                    item.setIcon(R.mipmap.tab_xc_seled)
                    navController.navigate(R.id.navigation_dashboard)
                    true
                }
                R.id.navigation_notifications -> {
                    item.setIcon(R.mipmap.tab_mine_seled)
                    navController.navigate(R.id.navigation_notifications)
                    true
                }
                else -> false
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            binding.splash.root.visibility = View.GONE
            binding.navView.visibility = View.VISIBLE
            binding.navHostFragment.visibility = View.VISIBLE
        }, SPLASH_TIME)
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