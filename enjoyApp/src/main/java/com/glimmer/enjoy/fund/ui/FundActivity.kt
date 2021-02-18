package com.glimmer.enjoy.fund.ui

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glimmer.dsl.adapter.decroation.GridDividerDecoration
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
import com.glimmer.uutil.logD
import kotlin.reflect.KClass

class FundActivity : MVVMActivity<FundVM, ActivityFundBinding>() {

    override fun vMClass(): KClass<FundVM> = FundVM::class

    override fun createBindingInfo(): BindingConfig.Info = BindingConfig.create {
        layoutId(R.layout.activity_fund)
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
        dataBinding.rvFund.addItemDecoration(
            GridDividerDecoration(activity, dividerPx = 10)
//            LinearLayoutDividerDecoration(activity, dividerPx = 10)
        )
        dataBinding.rvFund.attachAdapter {
            layoutManager { GridLayoutManager(activity, 3) }
//            layoutManager { LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false) }
            listItem {
                addItem<BeanTTFundInfo, RvItemFundBinding>(R.layout.rv_item_fund) {
                    createVH { bindData(BR.rvBean) }
                }
            }
        }
    }

}