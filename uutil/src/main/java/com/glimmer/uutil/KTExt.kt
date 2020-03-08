package com.glimmer.uutil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.net.URLDecoder
import java.net.URLEncoder

/*====================================================================================================*/
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

/*====================================================================================================*/
fun Activity.launchActivity(cls: Class<*>) {
    startActivity(Intent(this, cls))
}

fun Activity.launchActivity(cls: Class<*>, extra: Parcelable?, extraName: String) {
    val intent = Intent(this, cls)
    extra?.apply {
        intent.putExtra(extraName, this)
    }
    startActivity(intent)
}

fun Activity.launchActivityForResult(cls: Class<*>, requestCode: Int) {
    startActivityForResult(Intent(this, cls), requestCode)
}

fun Activity.launchActivityForResult(cls: Class<*>, extra: Parcelable?, extraName: String, requestCode: Int) {
    val intent = Intent(this, cls)
    extra?.apply {
        intent.putExtra(extraName, this)
    }
    startActivityForResult(intent, requestCode)
}

/*====================================================================================================*/
fun Context.getColorById(@ColorRes color: Int) = ContextCompat.getColor(this, color)

/*==============================================*/
fun String.encode(): String = URLEncoder.encode(this, "utf-8")

fun String.decode(): String = URLDecoder.decode(this, "utf-8")

/*====================================================================================================*/
fun TextView.str() = this.text.toString()


