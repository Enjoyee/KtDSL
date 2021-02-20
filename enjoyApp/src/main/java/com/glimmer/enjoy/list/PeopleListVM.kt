package com.glimmer.enjoy.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.glimmer.enjoy.bean.Student
import com.glimmer.enjoy.bean.Teacher
import com.glimmer.mvvm.viewmodel.BaseVM
import java.util.*

class PeopleListVM : BaseVM() {
    private val _peopleList = MutableLiveData<ArrayList<Any>>()
    val peopleList: LiveData<ArrayList<Any>> = _peopleList

    fun refresh() {
        val list = mockList()
        _peopleList.value = list
    }

    private fun mockList(): ArrayList<Any> {
        val list = arrayListOf<Any>()
        list.add(Student("学生1", Random().nextInt(100)))
        for (index in 0 until 20) {
            list.add(Teacher("王$index", Random().nextInt(100)))
        }
        return list
    }

    fun del() {
        _peopleList.value?.let {
            it.removeAt(it.size - 1)
            _peopleList.value = it
        }
    }

    fun add() {
        _peopleList.value?.let {
            if (Random().nextBoolean()) {
                it.add(Student("学生${Random().nextInt(100)}", Random().nextInt(100)))
            } else {
                it.add(Teacher("老师${Random().nextInt(100)}", Random().nextInt(100)))
            }

            _peopleList.value = it
        }
    }

    val studentList = Pager(
        PagingConfig(pageSize = 20),
    ) {
        StudentDataSource()
    }.flow.cachedIn(viewModelScope)

}