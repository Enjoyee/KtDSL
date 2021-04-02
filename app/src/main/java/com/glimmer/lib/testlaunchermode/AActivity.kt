package com.glimmer.lib.testlaunchermode

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.glimmer.lib.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class AActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvTest.text = "AActivity"
        binding.tvTest.viewTreeObserver.removeOnGlobalLayoutListener {  }
        binding.tvTest.viewTreeObserver.addOnGlobalLayoutListener {

        }
        binding.tvTest.setOnClickListener {
//            startActivity(Intent(this, BActivity::class.java))
            shellExec()
        }
    }

    fun shellExec() {
        val mRuntime = Runtime.getRuntime()
        try {
            //Process中封装了返回的结果和执行错误的结果
            val mProcess = mRuntime.exec("ps")
            val mReader = BufferedReader(InputStreamReader(mProcess.inputStream))
            val mRespBuff = StringBuffer()
            val buff = CharArray(1024)
            var ch = 0
            while (mReader.read(buff).also { ch = it } != -1) {
                mRespBuff.append(buff, 0, ch)
            }
            mReader.close()
            print(mRespBuff.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    inner class MyHandler : Handler(Looper.getMainLooper()) {

    }
}
