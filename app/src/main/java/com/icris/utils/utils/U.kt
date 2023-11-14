package com.icris.utils.utils

import android.util.Log

fun Any?.log(tag:String="DEBUG") = Log.d(tag, toString())