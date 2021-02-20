package com.glimmer.enjoy.home

import android.view.View
import com.glimmer.enjoy.BR
import com.glimmer.enjoy.R
import com.glimmer.enjoy.bean.BeanCommonToolBar
import com.glimmer.enjoy.databinding.ActivityHomeBinding
import com.glimmer.enjoy.list.PeopleListActivity
import com.glimmer.enjoy.test.showDSLDialog
import com.glimmer.mvvm.config.BindingConfig
import com.glimmer.mvvm.ui.MVVMActivity
import com.glimmer.uutil.launchActivity
import kotlin.reflect.KClass

class HomeActivity : MVVMActivity<HomeVM, ActivityHomeBinding>() {

    override fun layoutId(): Int = R.layout.activity_home

    override fun vMClass(): KClass<HomeVM> = HomeVM::class

    override fun createBindingInfo(): BindingConfig.Info {
        return BindingConfig.create {
            viewModel(BR.vm, vm)
            params(BR.clicker bind activity)
        }
    }

    override fun marginStatusBarView(): View = dataBinding.ivBack

    override fun isStatusBarFontDark(): Boolean = true

    override fun initView() {
        super.initView()
        dataBinding.toolbar
        filterMultiClickStrategy { listOf(dataBinding.tvTest2) }
//        vm.beanToolBar.value = BeanToolBar(centerTitle = "测试标题12334", leftText = "左边", leftColor = R.color.colorPrimaryDark, bgColor = R.color.colorAccent, leftIcon = R.drawable.ic_back, rightText = "右边")
        vm.beanToolBar.value = BeanCommonToolBar().apply {
            leftIcon = R.drawable.ic_back
            centerTitle = "测试标题"
        }
    }

    override fun viewClick(v: View) {
        super.viewClick(v)
        when (v) {
            // 防重复点击
            dataBinding.tvTest -> {
                vm.change()
                launchActivity<PeopleListActivity>()

                showDSLDialog {
                    title { "标题" }
                    message { "弹窗内容" }
                    positiveText { "确定" }
                    positiveClick { println("点击确定") }
                    negativeText { "取消" }
                    negativeClick { println("点击取消") }
                }

            }
            // 普通
            dataBinding.tvTest2 -> launchActivity<PeopleListActivity>()
        }
    }

}
