package com.glimmer.mvvm.delegate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.glimmer.mvvm.annotation.BindViewModel
import com.glimmer.mvvm.commom.ViewModelFactory
import com.glimmer.mvvm.view.IMvvmFragment
import java.lang.reflect.Field

class MvvmFragmentDelegateImpl(fm: FragmentManager, private val fragment: Fragment) : FragmentDelegateImpl(fm, fragment), IMvvmFragment {

    override fun getLayoutId(): Int = 0

    private val iMvmFragment = fragment as IMvvmFragment

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        initViewModel()
        super.onViewCreated(v, savedInstanceState)
        iMvmFragment.dataObserver()
    }

    /**
     *  根据 @BindViewModel 注解, 查找注解标示的变量（ViewModel）
     *  并且 创建 ViewModel 实例, 注入到变量中
     */
    private fun initViewModel() {
        fragment.javaClass.fields
            .filter { it.isAnnotationPresent(BindViewModel::class.java) }
            .getOrNull(0)
            ?.apply {
                isAccessible = true
                set(fragment, getViewModel(this))
            }
    }

    private fun getViewModel(field: Field): ViewModel {
        return ViewModelFactory.getViewModel(iMvmFragment, field)
    }
}