package com.glimmer.enjoy.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.glimmer.dsl.adapter.ext.attachAdapter
import com.glimmer.dsl.adapter.ext.attachPageAdapter
import com.glimmer.dsl.adapter.ext.submitDataSource
import com.glimmer.dsl.adapter.ext.submitPageDataSource
import com.glimmer.enjoy.BR
import com.glimmer.enjoy.R
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

    override fun vMClass(): KClass<PeopleListVM> = PeopleListVM::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun createBindingInfo(): BindingConfig.Info {
        return BindingConfig.create {
            layoutId(R.layout.activity_people_list)
            viewModel(BR.vm, vm)
            params(BR.clicker bind activity)
        }
    }

    override fun viewClick(v: View) {
        when (v) {
            dataBinding.btRefresh -> {
                lifecycleScope.launch {
                    vm.flo.collectLatest {
                        dataBinding.rv.submitPageDataSource(it)
                    }
                }
            }
            dataBinding.btAdd -> vm.add()
            dataBinding.btDel -> vm.del()
        }
    }

    override fun initData() {
        super.initData()
        testStr2 = "1233332"
    }

    override fun initView() {
        super.initView()
        // adapter 绑定
        dataBinding.rv.attachPageAdapter {
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
        vm.peopleList.observe(this) {
            dataBinding.rv.submitDataSource(it)
        }
//
        launch {
//            vm.flo.collectLatest {
//                dataBinding.rv.submitPageDataSource(it)
//            }
//            vm.loadPageList().collectLatest {
//                dataBinding.rv.submitPageDataSource(it)
//            }
        }
    }

}