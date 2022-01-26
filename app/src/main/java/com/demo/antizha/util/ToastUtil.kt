package com.demo.antizha.util

import android.annotation.SuppressLint
import android.widget.Toast
import com.demo.antizha.ui.Hicore


object ToastUtil {
    private var currentToast: Toast? = null

    @SuppressLint("WrongConstant")
    fun showMessage(str: String) {
        if (Hicore.app != null && !Hicore.app.isDouble()) {
            currentToast = Toast.makeText(Hicore.app, str, Toast.LENGTH_SHORT)
            currentToast?.setGravity(17, 0, 0)
            currentToast?.show()
        }
    }
}