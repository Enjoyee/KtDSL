package com.glimmer.enjoy.fund.repository

import com.blankj.utilcode.util.GsonUtils
import com.glimmer.enjoy.fund.api.IFundService
import com.glimmer.enjoy.fund.bean.BeanTTFundInfo
import com.glimmer.requestdsl.request.RequestDSL
import com.glimmer.uutil.logD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

enum class FundRepository {
    instance;

    fun getFundDataList(fundCodes: List<String>): Flow<List<Any>> = flow {
        val fundDataList = arrayListOf<BeanTTFundInfo>()
        val fundService = RequestDSL.createApiService(IFundService::class.java)
        fundCodes.forEach { code ->
            coverBean(fundService.getFundEstimates(code), fundDataList)
        }
        emit(fundDataList)
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