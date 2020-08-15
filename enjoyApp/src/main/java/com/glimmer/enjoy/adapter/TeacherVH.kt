package com.glimmer.enjoy.adapter

import android.view.ViewGroup
import com.glimmer.enjoy.R
import com.glimmer.enjoy.bean.Teacher
import com.glimmer.enjoy.databinding.ItemTeacherBinding
import com.glimmer.mvvm.adapter.BaseVH
import com.glimmer.mvvm.common.vhBinding

class TeacherVH(viewGroup: ViewGroup) : BaseVH<Teacher, ItemTeacherBinding>(vhBinding(viewGroup, R.layout.item_teacher)) {
    override fun bindData(bean: Teacher, position: Int) {

    }
}