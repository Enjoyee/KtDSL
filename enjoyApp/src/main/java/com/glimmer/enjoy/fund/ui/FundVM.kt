package com.glimmer.enjoy.fund.ui

import androidx.lifecycle.asLiveData
import com.glimmer.enjoy.fund.repository.FundRepository
import com.glimmer.mvvm.viewmodel.BaseVM
import com.glimmer.uutil.logD
import kotlinx.coroutines.flow.onStart

class FundVM : BaseVM() {
    private val fundCodeList = listOf("162605", "161725", "000336", "163402", "002190", "004997", "165520")

    fun getFundData() = FundRepository.Instance
        .getFundDataList(fundCodeList)
        .onStart { "请求天天基金数据...".logD() }
        .asLiveData()

}