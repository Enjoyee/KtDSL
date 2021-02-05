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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

enum class FundRepository {
    instance;

    fun getFundDataList(fundCodes: List<String>): Flow<List<Any>> = flow {
        val fundDataList = arrayListOf<BeanTTFundInfo>()
//        val fundService = RequestDSL.createApiService(IFundService::class.java)
//        fundCodes.forEach { code ->
//            coverBean(fundService.getFundEstimates(code), fundDataList)
//        }
        for (index in 1 until 12) {
            fundDataList.add(BeanTTFundInfo("1243${index}", "发财${index}", "200${index}", index.toFloat()))
        }

        emit(fundDataList)
    }.flowOn(Dispatchers.IO)

    fun testUpload(file: File): Flow<Any> = flow {
        val fundService = RequestDSL.createApiService(IFundService::class.java)
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        MultipartBody.Part.create(requestBody)
        val ss = fundService.testUpload(MultipartBody.Part.createFormData("file", file.name, requestBody))
        emit(ss)
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