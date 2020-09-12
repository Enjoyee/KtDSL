package com.glimmer.enjoy.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.glimmer.dsl.adapter.BaseListAdapter
import com.glimmer.dsl.adapter.vh.BaseVH
import com.glimmer.enjoy.bean.Teacher

class PeopleAdapter : BaseListAdapter<BaseVH<Any, ViewDataBinding>>() {

    companion object {
        const val VH_TYPE_TEACHER = 1
        const val VH_TYPE_STUDENT = 2
    }

    override fun forViewType(bean: Any, position: Int): Int? {
        return when (bean) {
            is Teacher -> VH_TYPE_TEACHER
            else -> VH_TYPE_STUDENT
        }
    }

    override fun createVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        return when (viewType) {
            VH_TYPE_TEACHER -> TeacherVH(parent)
            else -> StudentVH(parent)
        }
    }
}