package com.demo.antizha.interfaces

import okhttp3.Headers

interface IApiResult {
    fun callBack(data: String, headers: Headers?)
}