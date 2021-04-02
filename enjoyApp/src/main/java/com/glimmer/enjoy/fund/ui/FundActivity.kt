package com.glimmer.enjoy.fund.ui

import android.view.View
import com.glimmer.dsl.adapter.decroation.LinearLayoutDividerDecoration
import com.glimmer.dsl.adapter.ext.attachAdapter
import com.glimmer.dsl.adapter.ext.submitDataSource
import com.glimmer.enjoy.BR
import com.glimmer.enjoy.R
import com.glimmer.enjoy.databinding.ActivityFundBinding
import com.glimmer.enjoy.databinding.RvItemFundBinding
import com.glimmer.enjoy.fund.bean.BeanTTFundInfo
import com.glimmer.mvvm.config.BindingConfig
import com.glimmer.mvvm.ui.MVVMActivity
import com.glimmer.uutil.delegateString
import com.glimmer.uutil.logD
import kotlin.reflect.KClass

class FundActivity : MVVMActivity<FundVM, ActivityFundBinding>() {
    var s2 by delegateString("test", "默认值")

    override fun layoutId(): Int = R.layout.activity_fund

    override fun vMClass(): KClass<FundVM> = FundVM::class

    override fun createBindingInfo(): BindingConfig.Info = BindingConfig.create {
        viewModel(BR.vm, vm)
        params(BR.clicker bind activity)
    }

    override fun viewClick(v: View) {
        super.viewClick(v)
        when (v) {
            dataBinding.btnGetFundData -> {
                vm.getFundData().observe(this, {
                    "请求完成：${it}".logD()
                    dataBinding.rvFund.submitDataSource(it)
                })
            }
        }
    }

    override fun initData() {
        super.initData()
        initRv()
    }

    private fun initRv() {
        dataBinding.rvFund.apply {
            addItemDecoration(
//                GridDividerDecoration(activity, dividerPx = 10)
                LinearLayoutDividerDecoration(activity, color = R.color.colorAccentA)
            )
            attachAdapter {
//                layoutManager { GridLayoutManager(activity, 3) }
//            layoutManager { LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false) }
                listItem {
                    addItem<BeanTTFundInfo, RvItemFundBinding>(R.layout.rv_item_fund) {
                        createVH {
                            bindData(BR.rvBean)
                        }
                    }
                }
            }
        }
    }

    override fun dataObserver() {
        super.dataObserver()
        vm.getFundData().observe(this, {
            "请求完成：${it}".logD()
            dataBinding.rvFund.submitDataSource(it)
        })
    }

}