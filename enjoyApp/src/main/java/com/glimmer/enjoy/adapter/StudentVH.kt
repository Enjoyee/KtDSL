package com.glimmer.enjoy.adapter

import android.view.ViewGroup
import com.glimmer.dsl.adapter.vh.BaseVH
import com.glimmer.dsl.adapter.vh.createVHBinding
import com.glimmer.enjoy.R
import com.glimmer.enjoy.bean.Student
import com.glimmer.enjoy.databinding.ItemStudentBinding

class StudentVH(viewGroup: ViewGroup) : BaseVH<Student, ItemStudentBinding>(createVHBinding(viewGroup, R.layout.item_student)) {
    override fun bindData(bean: Student, position: Int) {

    }
}