package com.glimmer.enjoy.bean

data class Teacher(var name: String, val age: Int) {
    fun fullText() = "老师姓名：$name，年龄：$age"
}