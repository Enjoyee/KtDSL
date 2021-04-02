package com.glimmer.lib

import android.app.ActivityManager
import android.app.ActivityManager.RunningTaskInfo
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


open class BaseAc : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        isApplicationBroughtToBackground(this)
    }

    override fun onStop() {
        super.onStop()
        isApplicationBroughtToBackground(this)
    }

    private var isBackToBackground = false

    private fun isApplicationBroughtToBackground(context: Context) {
        val am: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks: List<RunningTaskInfo> = am.getRunningTasks(1)
        if (tasks.isNotEmpty()) {
            val taskTopActivity = tasks[0].topActivity
            val taskTopActivityPackageName = taskTopActivity?.packageName
            val appPackageName = context.packageName

            if (isBackToBackground && taskTopActivityPackageName == appPackageName) {
                Log.e("oooooo", "后台回到前台")
            }

            isBackToBackground = taskTopActivityPackageName != appPackageName
            if (isBackToBackground) {
                Log.e("oooooo", "前台回到后台")
            }
        }
    }
}