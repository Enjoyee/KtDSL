package com.glimmer.enjoy.fund.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IFundService {
    @GET("http://fundgz.1234567.com.cn/js/{foundCode}.js")
    suspend fun getFundEstimates(
        @Path("foundCode") code: String,
        @Query("rt") timeStamp: String = System.currentTimeMillis().toString()
    ): String
}