package com.glimmer.enjoy.list

import android.view.View
import androidx.lifecycle.observe
import com.glimmer.enjoy.BR
import com.glimmer.enjoy.R
import com.glimmer.enjoy.bean.Student
import com.glimmer.enjoy.bean.Teacher
import com.glimmer.enjoy.databinding.ActivityPeopleListBinding
import com.glimmer.enjoy.databinding.ItemStudentBinding
import com.glimmer.enjoy.databinding.ItemTeacherBinding
import com.glimmer.mvvm.common.bindAdapter
import com.glimmer.mvvm.common.submitDataSource
import com.glimmer.mvvm.config.BindingConfig
import com.glimmer.mvvm.ui.MvvmActivity
import kotlin.reflect.KClass

class PeopleListActivity : MvvmActivity<PeopleListVM, ActivityPeopleListBinding>() {

    override fun vMClass(): KClass<PeopleListVM> = PeopleListVM::class

    override fun createBindingInfo(): BindingConfig.Info {
        return BindingConfig.create {
            layoutId(R.layout.activity_people_list)
            viewModel(BR.vm, vm)
            params(BR.clicker bind activity)
        }
    }

    override fun viewClick(v: View) {
        when (v) {
            dataBinding.btRefresh -> vm.refresh()
            dataBinding.btAdd -> vm.add()
            dataBinding.btDel -> vm.del()
        }
    }

    override fun initView() {
        super.initView()
        // adapter 绑定
        dataBinding.rv.bindAdapter {
            adapter {
                addItem<Teacher, ItemTeacherBinding>(R.layout.item_teacher) {
                    isViewType { it is Teacher }
                    bindVH {
                        bindVariableData(BR.bean)
                        bindClick(BR.clicker) { _, bean, _ ->
                            bean.name = "点击了"
                            refreshItem()
                        }
                    }
                }

                addItem<Student, ItemStudentBinding>(R.layout.item_student) {
                    isViewType { it is Student }
                    bindVH {
                        bindData { bean, _, dataBinding ->
                            dataBinding.tvStudentName.text = bean.fullText()
                        }
                    }
                }
            }
        }


    }

    override fun dataObserver() {
        super.dataObserver()
        vm.peopleList.observe(this) {
            dataBinding.rv.submitDataSource(it)
//            adapter.submitList(it)
//            adapter2.submitList(it)
        }
    }

}