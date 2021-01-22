package com.glimmer.enjoy.fund.bean

import com.google.gson.annotations.SerializedName

data class BeanTTFundInfo(
    @SerializedName("fundcode")
    val code: String,
    val name: String,
    @SerializedName("gztime")
    val time: String,
    @SerializedName("gszzl")
    val estimates: Float, // 估值
) {
    val isDown: Boolean
        get() = estimates <= 0

    fun estimatesStr() = "$estimates%"

}
