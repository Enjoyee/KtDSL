package com.glimmer.enjoy.fund.ui

import androidx.lifecycle.asLiveData
import com.glimmer.enjoy.fund.repository.FundRepository
import com.glimmer.mvvm.viewmodel.BaseVM
import com.glimmer.uutil.logD
import kotlinx.coroutines.flow.onStart
import java.io.File

class FundVM : BaseVM() {
    private val fundCodeList = listOf("001838", "002190", "161725", "160643", "000336", "161726", "519019")

    fun getFundData() = FundRepository.instance
        .getFundDataList(fundCodeList)
        .onStart { "请求天天基金数据...".logD() }
        .asLiveData()

    fun test() = FundRepository.instance
        .testUpload(File("/storage/emulated/0/DCIM/Camera/IMG_20210129_113834.jpg"))
        .onStart { "上传...".logD() }
        .asLiveData()
}