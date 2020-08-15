package com.glimmer.mvvm.ui

import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.glimmer.mvvm.view.IMvvmActivity
import com.glimmer.mvvm.viewmodel.BaseVM
import kotlin.reflect.KClass

abstract class MvvmActivity<VM : BaseVM, DB : ViewDataBinding> : BaseActivity(), IMvvmActivity {
    /**==========================================================**/
    val dataBinding: DB by lazy { DataBindingUtil.setContentView<DB>(this, bindingConfig.layout) }
    val vm: VM by lazy { ViewModelProvider(this, defaultViewModelProviderFactory).get(vMClass().java) }

    /**==========================================================**/
    override fun setActivityContentView() {
    }

    override fun onInit() {
        // 绑定布局vm
        bindingConfig.viewModel.invoke()?.let {vm->
            dataBinding.setVariable(bindingConfig.vmVariableId.invoke(), vm)
        }
        // 绑定xml其他参数
        bindingConfig.bindingParams.forEach { key, value ->
            dataBinding.setVariable(key, value)
        }
        dataBinding.lifecycleOwner = this
        super.onInit()
        dataObserver()
    }

    /**==========================================================**/
    abstract fun vMClass(): KClass<VM>

}