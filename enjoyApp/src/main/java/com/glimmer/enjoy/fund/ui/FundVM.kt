package com.glimmer.enjoy.fund.ui

import androidx.lifecycle.asLiveData
import com.glimmer.enjoy.fund.repository.FundRepository
import com.glimmer.mvvm.viewmodel.BaseVM
import com.glimmer.uutil.logD
import kotlinx.coroutines.flow.onStart

class FundVM : BaseVM() {
    private val fundCodeList = listOf("001838", "002190", "161725", "160643", "000336", "161726", "519019")

    fun getFundData() = FundRepository.instance
        .getFundDataList(fundCodeList)
        .onStart { "请求天天基金数据...".logD() }
        .asLiveData()

}