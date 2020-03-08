package com.glimmer.mvvm.bean

data class SharedData(val message: String = "", val resId: Int = 0, val type: SharedType) {
    constructor(message: String, type: SharedType) : this(message, 0, type)
    constructor(resId: Int, type: SharedType) : this("", resId, type)
    constructor(type: SharedType) : this("", 0, type)
}