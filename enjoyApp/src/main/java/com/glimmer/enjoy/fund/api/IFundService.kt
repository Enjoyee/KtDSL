package com.glimmer.enjoy.fund.api

import okhttp3.MultipartBody
import retrofit2.http.*

interface IFundService {
    @GET("http://fundgz.1234567.com.cn/js/{foundCode}.js")
    suspend fun getFundEstimates(
        @Path("foundCode") code: String,
        @Query("rt") timeStamp: String = System.currentTimeMillis().toString()
    ): String

    @Headers("token:88oe67tfek8wkc8g0ogk4448o", "User-Agent:System/Android7.1.1 SystemModel/OPPO A83 NetWorkType/1 MifengdaoNews/1.0.0")
    @Multipart
    @POST("https://api.course.mifengdao.com/v1/uploadImg")
    suspend fun testUpload(@Part file: MultipartBody.Part): String

}