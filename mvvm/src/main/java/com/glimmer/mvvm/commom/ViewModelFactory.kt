package com.glimmer.mvvm.commom

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.glimmer.mvvm.Hammer
import com.glimmer.mvvm.bean.SharedData
import com.glimmer.mvvm.bean.SharedType
import com.glimmer.mvvm.view.IMvvmActivity
import com.glimmer.mvvm.view.IMvvmFragment
import com.glimmer.mvvm.view.IMvvmView
import com.glimmer.mvvm.viewmodel.BaseVM
import java.lang.reflect.Field

object ViewModelFactory {

    fun getViewModel(fragment: IMvvmFragment, field: Field): BaseVM {
        val viewModel = realCreateViewModel(field, fragment as Fragment)
        initSharedData(fragment, viewModel)
        return viewModel
    }

    fun getViewModel(activity: IMvvmActivity, field: Field): BaseVM {
        val viewModel = realCreateViewModel(field, activity as FragmentActivity)
        initSharedData(activity, viewModel)
        return viewModel
    }

    @Suppress("UNCHECKED_CAST")
    private fun realCreateViewModel(field: Field, owner: ViewModelStoreOwner): BaseVM {
        val viewModelClass = field.genericType as Class<BaseVM>
        return ViewModelProvider(owner).get(viewModelClass)
    }

    private fun initSharedData(view: IMvvmView, viewModel: BaseVM) {
        val shareDataObserver: Observer<SharedData> = Observer { sharedData ->
            sharedData?.run {
                when (type) {
                    is SharedType.Toast -> {
                        if (message.isNotEmpty()) {
                            view.showToast(message)
                        } else {
                            view.showToast(resId)
                        }
                    }

                    is SharedType.LoadingDialog -> view.getLoadingDialog()?.showLoadingDialog()

                    is SharedType.DismissLoadingDialog -> view.getLoadingDialog()?.dismissLoadingDialog()

                    is SharedType.LoadingLayout -> view.getStateView()?.showLoadingLayout()

                    is SharedType.EmptyLayout -> view.getStateView()?.showEmptyLayout()

                    is SharedType.ErrorLayout -> view.getStateView()?.showErrLayout()

                    is SharedType.ContentLayout -> view.getStateView()?.showContentLayout()
                }
            }
        }
        // 订阅通用 observer
        viewModel.sharedData.observe(view as LifecycleOwner, shareDataObserver)

        // 订阅 api 错误
        if (Hammer.mConfig.mShowApiErrToast.invoke()) {
            val apiExceptionObserver: Observer<Throwable> = Observer { err ->
                err.message?.let { view.showApiToast(it) }
            }
            viewModel.apiException.observe(view as LifecycleOwner, apiExceptionObserver)
        }
        // 订阅api loading
        if (Hammer.mConfig.mShowApiLoading.invoke()) {
            val apiLoadingObserver: Observer<Boolean> = Observer { showLoading ->
                if (showLoading) {
                    view.getLoadingDialog()?.showLoadingDialog()
                } else {
                    view.getLoadingDialog()?.dismissLoadingDialog()
                }
            }
            viewModel.apiLoading.observe(view as LifecycleOwner, apiLoadingObserver)
        }
    }

}