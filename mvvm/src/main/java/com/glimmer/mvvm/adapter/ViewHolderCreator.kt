package com.glimmer.mvvm.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

abstract class ViewHolderCreator<BEAN : Any, VB : ViewDataBinding> {
    abstract fun isForViewType(data: BEAN): Boolean
    abstract fun getLayoutId(): Int
    abstract fun createViewHolder(parent: ViewGroup): BaseVH<BEAN, VB>
}