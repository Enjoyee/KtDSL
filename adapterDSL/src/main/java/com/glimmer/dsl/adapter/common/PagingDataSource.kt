package com.glimmer.dsl.adapter.common

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.glimmer.uutil.logD


abstract class PagingDataSource : PagingSource<Int, Any>() {
    companion object {
        // 开始页码
        private const val STARTING_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Any>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        val page = params.key ?: firstPageIndex()
        return try {
            LoadResult.Page(
                data = fetchData(page),
                prevKey = if (page == firstPageIndex()) null else (page - 1),
                nextKey = if (page >= getMaxPage()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    open fun firstPageIndex() = STARTING_PAGE_INDEX

    abstract fun fetchData(page: Int): List<Any>

    abstract fun getMaxPage(): Int

}