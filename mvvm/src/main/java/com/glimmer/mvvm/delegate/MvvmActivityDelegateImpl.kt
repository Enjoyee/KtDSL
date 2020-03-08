package com.glimmer.mvvm.delegate

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.glimmer.mvvm.annotation.BindViewModel
import com.glimmer.mvvm.commom.ViewModelFactory
import com.glimmer.mvvm.view.IMvvmActivity
import java.lang.reflect.Field

class MvvmActivityDelegateImpl(private val activity: Activity) : ActivityDelegateImpl(activity), IMvvmActivity {

    override fun getLayoutId(): Int = 0

    private val iMvvmActivity = activity as IMvvmActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        initViewModel()
        super.onCreate(savedInstanceState)
        iMvvmActivity.dataObserver()
    }

    /**
     *  根据 @BindViewModel 注解, 查找注解标示的变量（ViewModel）
     *  并且 创建 ViewModel 实例, 注入到变量中
     */
    private fun initViewModel() {
        activity.javaClass.fields
            .filter { it.isAnnotationPresent(BindViewModel::class.java) }
            .getOrNull(0)
            ?.apply {
                isAccessible = true
                set(activity, getViewModel(this))
            }
    }

    private fun getViewModel(field: Field): ViewModel {
        return ViewModelFactory.getViewModel(iMvvmActivity, field)
    }

}