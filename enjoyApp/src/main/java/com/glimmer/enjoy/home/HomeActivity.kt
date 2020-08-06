package com.glimmer.enjoy.home

import androidx.appcompat.app.AppCompatActivity
import com.glimmer.enjoy.R
import com.glimmer.mvvm.annotation.BindViewModel
import com.glimmer.mvvm.view.IMvvmActivity
import com.glimmer.uutil.KHandler
import com.glimmer.uutil.launchActivity

class HomeActivity : AppCompatActivity(), IMvvmActivity {
    @BindViewModel
    lateinit var homeVM: HomeVM

    private val handler by lazy { KHandler(this) }

    override fun getLayoutId(): Int = R.layout.activity_home

    override fun initData() {
        super.initData()

        launchActivity<HomeActivity>()

        handler.postDelayed({
            homeVM.onToast()
        }, 5000)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
