package com.glimmer.enjoy.home

import android.view.View
import com.glimmer.enjoy.BR
import com.glimmer.enjoy.R
import com.glimmer.enjoy.bean.BeanCommonToolBar
import com.glimmer.enjoy.databinding.ActivityHomeBinding
import com.glimmer.enjoy.list.PeopleListActivity
import com.glimmer.mvvm.config.BindingConfig
import com.glimmer.mvvm.ui.MvvmActivity
import com.glimmer.uutil.launchActivity
import com.glimmer.uutil.logE
import kotlin.reflect.KClass

class HomeActivity : MvvmActivity<HomeVM, ActivityHomeBinding>() {

    override fun vMClass(): KClass<HomeVM> = HomeVM::class

    override fun createBindingInfo(): BindingConfig.Info {
        return BindingConfig.create {
            layoutId(R.layout.activity_home)
            viewModel(BR.vm, vm)
            params(BR.clicker bind activity)
        }
    }

    override fun initView() {
        super.initView()
        addViewMarginStatusBar(dataBinding.ivBack)
        dataBinding.toolbar
        filterMultiClickStrategy { listOf(dataBinding.tvTest2) }
        statusBarMode(true)
//        vm.beanToolBar.value = BeanToolBar(centerTitle = "测试标题12334", leftText = "左边", leftColor = R.color.colorPrimaryDark, bgColor = R.color.colorAccent, leftIcon = R.drawable.ic_back, rightText = "右边")
        vm.beanToolBar.value = BeanCommonToolBar().apply {
            leftIcon = R.drawable.ic_back
            centerTitle = "测试标题"
        }

        "测试".logE("测试稿")
    }

    override fun toolBarLeftClick(v: View) {
        super.toolBarLeftClick(v)
    }

    override fun viewClick(v: View) {
        super.viewClick(v)
        when (v) {
            // 防重复点击
            dataBinding.tvTest -> {
                vm.change()
                launchActivity<PeopleListActivity>()
            }
            // 普通
            dataBinding.tvTest2 -> launchActivity<PeopleListActivity>()
        }
    }

}
