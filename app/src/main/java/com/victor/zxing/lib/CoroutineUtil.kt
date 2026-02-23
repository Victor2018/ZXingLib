package com.victor.zxing.lib

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun runOnMain(block: suspend CoroutineScope.() -> Unit) = CoroutineScope(Dispatchers.Main).launch {
    try {
      block()
    } catch (e: Exception) {
      e.printStackTrace()
    }
}

fun runOnIO(block: suspend CoroutineScope.() -> Unit) = CoroutineScope(Dispatchers.IO).launch {
    try {
      block()
    } catch (e: Exception) {
      e.printStackTrace()
    }
}
