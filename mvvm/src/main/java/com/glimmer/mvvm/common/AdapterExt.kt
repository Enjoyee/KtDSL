package com.glimmer.mvvm.common

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glimmer.mvvm.adapter.CommonListAdapterDsl

fun RecyclerView.bindAdapter(init: RecyclerViewSetup.() -> Unit) {
    val adapterDsl = RecyclerViewSetup(this)
    adapterDsl.init()
}

class RecyclerViewSetup internal constructor(private val recyclerView: RecyclerView) {
    private lateinit var items: MutableList<Any>
    private lateinit var adapter: CommonListAdapterDsl
    var context: Context = recyclerView.context

    fun dataSource(items: MutableList<Any>) {
        this.items = items
    }

    fun withLayoutManager(init: RecyclerViewSetup.() -> RecyclerView.LayoutManager) = apply { recyclerView.layoutManager = init() }

    fun adapter(init: CommonListAdapterDsl.() -> Unit) {
        this.adapter = CommonListAdapterDsl()
        init.invoke(adapter)
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
        recyclerView.adapter = adapter
        if (::items.isInitialized) {
            adapter.submitList(items)
        }
    }
}

fun RecyclerView.submitDataSource(items: MutableList<out Any>) {
    val list = arrayListOf<Any>().apply { addAll(items) }
    (adapter as? CommonListAdapterDsl)?.submitList(list)
}