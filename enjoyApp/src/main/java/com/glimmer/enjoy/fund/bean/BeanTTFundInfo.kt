package com.glimmer.enjoy.fund.bean

import com.google.gson.annotations.SerializedName

data class BeanTTFundInfo(
    @SerializedName("fundcode")
    val code: String,
    val name: String,
    @SerializedName("gztime")
    val time: String,
    @SerializedName("gszzl")
    val estimates: Float, // 估值,
    val upStr: String? = "♨"
) {
    val isDown: Boolean
        get() = estimates <= 0

    val nameStr: String
        get() = "» $name"

    val estimatesStr: String
        get() = "$estimates%"

    val timeStr: String
        get() = "【 ${time.replace("2021-", "")} 】"

    override fun toString(): String {
        return "BeanTTFundInfo(code='$code', name='$name', time='$time', estimates=$estimates)"
    }

}
