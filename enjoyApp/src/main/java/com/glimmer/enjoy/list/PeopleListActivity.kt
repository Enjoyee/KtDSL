package com.glimmer.enjoy.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.glimmer.dsl.adapter.ext.attachAdapter
import com.glimmer.dsl.adapter.ext.attachPageAdapter
import com.glimmer.dsl.adapter.ext.submitDataSource
import com.glimmer.dsl.adapter.ext.submitPageDataSource
import com.glimmer.enjoy.BR
import com.glimmer.enjoy.R
import com.glimmer.enjoy.bean.BeanCommonToolBar
import com.glimmer.enjoy.bean.Student
import com.glimmer.enjoy.bean.Teacher
import com.glimmer.enjoy.databinding.ActivityPeopleListBinding
import com.glimmer.enjoy.databinding.ItemStudentBinding
import com.glimmer.enjoy.databinding.ItemTeacherBinding
import com.glimmer.mvvm.common.launch
import com.glimmer.mvvm.config.BindingConfig
import com.glimmer.mvvm.ui.MVVMActivity
import com.glimmer.uutil.anno.LocalKV
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class PeopleListActivity : MVVMActivity<PeopleListVM, ActivityPeopleListBinding>() {
    @LocalKV(key = "1234", "默认")
    var testStr2: String? = null

    override fun layoutId(): Int = R.layout.activity_people_list

    override fun vMClass(): KClass<PeopleListVM> = PeopleListVM::class

    override fun createBindingInfo(): BindingConfig.Info {
        return BindingConfig.create {
            viewModel(BR.vm, vm)
            params(BR.clicker bind activity)
        }
    }

    override fun viewClick(v: View) {
        when (v) {
            dataBinding.btRefresh -> {
//                launch {
//                    vm.studentList.collectLatest {
//                        dataBinding.rv.submitPageDataSource(it)
//                    }
//                }
                vm.refresh()
            }
            dataBinding.btAdd -> vm.add()
            dataBinding.btDel -> vm.del()
        }
    }

    override fun initData() {
        super.initData()
        testStr2 = "1233332"
    }

    override fun isStatusBarFontDark(): Boolean = false

    override fun initView() {
        super.initView()
        // 标题
        vm.beanToolBar.value = BeanCommonToolBar().apply {
            leftText = "测试"
        }
        // adapter 绑定
        dataBinding.rv.attachAdapter {
            layoutManager { GridLayoutManager(activity, 3) }
            listItem {
                addItem<Teacher, ItemTeacherBinding>(R.layout.item_teacher) {
                    isViewType { it is Teacher }
                    spanSizeUp { 2 }
                    createVH {
                        bindData(BR.bean)
                        clicker(BR.clicker) { _, bean, _ ->
                            bean.name = "点击了"
                            refreshItem()
                        }
                    }
                }
                addItem<Student, ItemStudentBinding>(R.layout.item_student) {
                    isViewType { it is Student }
                    spanSizeUp { 1 }
                    createVH {
                        setData { bean, _ ->
                            vhDataBinding.tvStudentName.text = bean.fullText()
                        }
                    }
                }
            }
        }
    }

    override fun dataObserver() {
        super.dataObserver()
        vm.peopleList.observe(this, Observer { dataBinding.rv.submitDataSource(it) })
    }

}