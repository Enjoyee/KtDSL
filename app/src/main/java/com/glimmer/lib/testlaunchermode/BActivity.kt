package com.glimmer.lib.testlaunchermode

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.glimmer.lib.databinding.ActivityTestScrollBinding

class BActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestScrollBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestScrollBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}