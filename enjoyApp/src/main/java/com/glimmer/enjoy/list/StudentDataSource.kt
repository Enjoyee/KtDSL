package com.glimmer.enjoy.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.glimmer.enjoy.bean.Student
import java.util.*

private const val STARTING_PAGE_INDEX = 1

class StudentDataSource : PagingSource<Int, Any>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return LoadResult.Page(data = mockStudentList(), prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1, nextKey = if (page >= 10) null else page + 1)
    }

    private fun mockStudentList(): ArrayList<Student> {
        val list = arrayListOf<Student>()
        for (index in 0 until 20) {
            list.add(Student("${Random().nextInt(100)}", Random().nextInt(100)))
        }
        return list
    }

    override fun getRefreshKey(state: PagingState<Int, Any>): Int? = state.anchorPosition

}