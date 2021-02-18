package com.glimmer.dsl.adapter.common

import androidx.paging.PagingSource

abstract class PagingDataSource<Key : Any, Value : Any> : PagingSource<Key, Value>() {
}