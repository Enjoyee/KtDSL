package com.glimmer.enjoy.bean

data class Student(val name: String, val age: Int)  {
    fun fullText() = "学生姓名：$name，年龄：$age"
}