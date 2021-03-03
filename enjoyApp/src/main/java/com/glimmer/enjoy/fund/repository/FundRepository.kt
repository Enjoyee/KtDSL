package com.glimmer.enjoy.fund.repository

import com.blankj.utilcode.util.GsonUtils
import com.glimmer.enjoy.fund.api.IFundService
import com.glimmer.enjoy.fund.bean.BeanTTFundInfo
import com.glimmer.requestdsl.request.RequestDSL
import com.glimmer.uutil.logD
import com.glimmer.uutil.logW
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*

enum class FundRepository {
    Instance;

    private var firstRefresh = true

    fun getFundDataList(fundCodes: List<String>): Flow<List<Any>> = flow {
        val fundDataList = arrayListOf<BeanTTFundInfo>()
        val tempDataList = arrayListOf<BeanTTFundInfo>()
        while (true) {
            // 是否需要刷新
            var needRefresh: Boolean
            // 当前时间
            val currentTime = System.currentTimeMillis()
            // 开市
            val calendar = Calendar.getInstance(Locale.CHINA)
            // 9:30
            calendar.set(Calendar.HOUR_OF_DAY, 9)
            calendar.set(Calendar.MINUTE, 20)
            val firstTime = calendar.timeInMillis
            // 11:30
            calendar.set(Calendar.HOUR_OF_DAY, 11)
            calendar.set(Calendar.MINUTE, 40)
            val secondTime = calendar.timeInMillis
            // 13:00
            calendar.set(Calendar.HOUR_OF_DAY, 13)
            calendar.set(Calendar.MINUTE, 10)
            val thirdTime = calendar.timeInMillis
            // 15:00
            calendar.set(Calendar.HOUR_OF_DAY, 15)
            calendar.set(Calendar.MINUTE, 10)
            val fourthTime = calendar.timeInMillis
            needRefresh = currentTime in firstTime..secondTime || currentTime in thirdTime..fourthTime
            "==》是否需要请求基金数据：$needRefresh".logW("yyyyy")

            if (needRefresh || firstRefresh) {
                firstRefresh = false
                "==》请求基金数据".logW("yyyyy")
                fundDataList.clear()
                // 请求数据
                val fundService = RequestDSL.createApiService(IFundService::class.java)
                // 数据转换
                fundCodes.forEach { code ->
                    coverBean(fundService.getFundEstimates(code), fundDataList)
                }
                // 数据净值排序
                fundDataList.sortBy { -it.estimates }

                val finalList = arrayListOf<BeanTTFundInfo>()
                fundDataList.forEach { currentFund ->
                    var index = -1
                    tempDataList.forEachIndexed { _index, lastFund ->
                        if (currentFund.code == lastFund.code) {
                            index = _index
                            return@forEachIndexed
                        }
                    }

                    val upStr: String
                    if (index != -1) {
                        val lastEstimates = tempDataList[index].estimates
                        val currentEstimates = currentFund.estimates
                        val result = lastEstimates - currentEstimates
                        upStr = when {
                            result > 0 -> {
                                "↓"
                            }
                            result < 0 -> {
                                "↑"
                            }
                            else -> {
                                "♨"
                            }
                        }
                    } else {
                        upStr = "♨"
                    }
                    finalList.add(BeanTTFundInfo(currentFund.code, currentFund.name, currentFund.time, currentFund.estimates, upStr))
                }

                tempDataList.clear()
                tempDataList.addAll(fundDataList)

                // 发射数据2045
                emit(finalList)
            }
            // 延时2min
            delay(2 * 60 * 1000)
        }
    }.flowOn(Dispatchers.IO)

    private fun coverBean(jsonStr: String, list: ArrayList<BeanTTFundInfo>) {
        "请求响应：$jsonStr".logD()
        val start = jsonStr.indexOf("{")
        val end = jsonStr.indexOf("}") + 1
        GsonUtils.fromJson(
            jsonStr.subSequence(start, end).toString(),
            BeanTTFundInfo::class.java
        ).apply { list.add(this) }
    }
}