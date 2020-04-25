package com.android.myapplication.data
data class Response<out T>(val errorCode: Int, val errorMsg: String, val data: T)