package com.glimmer.enjoy.list

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.glimmer.dsl.adapter.ext.attachPageAdapter
import com.glimmer.dsl.adapter.ext.submitPageDataSource
import com.glimmer.enjoy.BR
import com.glimmer.enjoy.R
import com.glimmer.enjoy.bean.Student
import com.glimmer.enjoy.databinding.ActivityPeopleListBinding
import com.glimmer.enjoy.databinding.ItemStudentBinding
import com.glimmer.mvvm.common.launch
import com.glimmer.mvvm.config.BindingConfig
import com.glimmer.mvvm.ui.MVVMActivity
import kotlinx.coroutines.flow.collectLatest
import kotlin.reflect.KClass

class PeopleListActivity : MVVMActivity<PeopleListVM, ActivityPeopleListBinding>() {

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
        dataBinding.rv.attachPageAdapter {
            listItem {
                layoutManager { GridLayoutManager(activity, 3) }

//                addItem<Teacher, ItemTeacherBinding>(R.layout.item_teacher) {
//                    isViewType { it is Teacher }
//                    spanSizeUp { 2 }
//                    bindVH {
//                        bindVariableData(BR.bean)
//                        bindClick(BR.clicker) { _, bean, _ ->
//                            bean.name = "点击了"
//                            refreshItem()
//                        }
//                    }
//                }

                addItem<Student, ItemStudentBinding>(R.layout.item_student) {
                    isViewType { it is Student }
                    spanSizeUp { 1 }
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
//        vm.peopleList.observe(this, {
//            dataBinding.rv.submitDataSource(it)
//        })
//
        launch {
            vm.loadPageList().collectLatest {
                dataBinding.rv.submitPageDataSource(it)
            }
        }
    }

}