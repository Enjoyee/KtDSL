package com.glimmer.enjoy.home

import androidx.appcompat.app.AppCompatActivity
import com.glimmer.enjoy.R
import com.glimmer.mvvm.annotation.BindViewModel
import com.glimmer.mvvm.view.IMvvmActivity

class HomeActivity : AppCompatActivity(), IMvvmActivity {
    @BindViewModel
    lateinit var homeVM: HomeVM

    override fun getLayoutId(): Int = R.layout.activity_home

    override fun initData() {
        super.initData()
        homeVM.onToast()
    }

}
