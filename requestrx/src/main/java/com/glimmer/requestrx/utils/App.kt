package com.glimmer.requestrx.utils

import android.content.Context
import android.widget.Toast

fun Context.toast(msg: CharSequence?) {
    if (!msg.isNullOrEmpty()) {
        val toast = Toast.makeText(this.applicationContext, null, Toast.LENGTH_SHORT)
        toast.setText(msg)
        toast.show()
    }
}

fun Context.toast(resId: Int) {
    val toast = Toast.makeText(this.applicationContext, null, Toast.LENGTH_SHORT)
    toast.setText(resId)
    toast.show()
}