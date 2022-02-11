package com.demo.antizha.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.antizha.databinding.ActivityMainBinding

class WelcomeActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}