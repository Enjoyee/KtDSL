package com.glimmer.enjoy.list

import com.glimmer.dsl.adapter.common.PagingDataSource
import com.glimmer.enjoy.bean.Student
import com.glimmer.uutil.logD
import java.util.*

class StudentDataSource : PagingDataSource() {

    private fun mockStudentList(): ArrayList<Student> {
        val list = arrayListOf<Student>()
        for (index in 0 until 20) {
            list.add(Student("${Random().nextInt(100)}", Random().nextInt(100)))
        }
        return list
    }

    override fun firstPageIndex(): Int = 1

    override fun fetchData(page: Int): List<Any> {
        return mockStudentList()
    }

    override fun getMaxPage(): Int = 10

}