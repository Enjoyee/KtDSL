package com.glimmer.lib

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference

class BBActivity : AppCompatActivity() {
    private val handler = MyHandler(Looper.getMainLooper(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    internal class MyHandler(looper: Looper, activity: BBActivity) : Handler(looper) {
        private val activityWeakReference: WeakReference<BBActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val activity = activityWeakReference.get()
            activity?.test()
        }
    }

    fun test() {}

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}