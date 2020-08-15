package com.glimmer.enjoy.bean

open class People(var name: String, var age: Int, val type: Int) {
    fun fullText() = "学生姓名：$name，年龄：$age, 类型：$type"
}