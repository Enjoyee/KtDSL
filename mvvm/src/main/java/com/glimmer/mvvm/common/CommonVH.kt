package com.glimmer.mvvm.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.glimmer.mvvm.adapter.BaseVH

class CommonVH<BEAN : Any, VB : ViewDataBinding>(viewGroup: ViewGroup, @LayoutRes viewLayout: Int, viewDataBinding: VB = vhBinding(viewGroup, viewLayout)) : BaseVH<BEAN, VB>(viewDataBinding), Clicker {
    private val dataBinding = viewDataBinding
    private var beanVariableId: Int? = null
    private lateinit var bean: BEAN
    private var itemPosition: Int = 0
    private var setUp: ((bean: BEAN, position: Int, dataBinding: VB) -> Unit)? = null
    private var clicker: ((View, BEAN, Int) -> Unit)? = null

    private fun bindVariable(variableId: Int?, value: Any) {
        variableId?.let { dataBinding.setVariable(it, value) }
    }

    override fun bindData(bean: BEAN, position: Int) {
        this.bean = bean
        this.itemPosition = position
        bindVariable(beanVariableId, bean)
        setUp?.invoke(bean, position, dataBinding)
    }

    fun bindData(setUp: ((bean: BEAN, position: Int, dataBinding: VB) -> Unit)?) {
        this.setUp = setUp
    }

    fun bindVariableData(beanVariableId: Int, setUp: ((bean: BEAN, position: Int, dataBinding: VB) -> Unit)? = null) {
        this.beanVariableId = beanVariableId
        this.setUp = setUp
    }

    override fun onClick(v: View?) {
        v?.let { clicker?.invoke(it, bean, itemPosition) }
    }

    fun bindClick(clickerVariableId: Int, clicker: ((View, BEAN, Int) -> Unit)?) {
        bindVariable(clickerVariableId, this)
        this.clicker = clicker
    }

    fun refreshItem(bean: BEAN = this.bean) {
        bindVariable(beanVariableId, bean)
    }

}

/**
 * ==========================================================================================
 */
fun <VB : ViewDataBinding> vhBinding(viewGroup: ViewGroup, @LayoutRes viewLayout: Int) = requireNotNull(DataBindingUtil.bind<VB>(inflateView(viewGroup, viewLayout))) { "cannot find the matched layout." }

fun inflateView(viewGroup: ViewGroup, @LayoutRes viewLayout: Int): View {
    val layoutInflater = LayoutInflater.from(viewGroup.context)
    return layoutInflater.inflate(viewLayout, viewGroup, false)
}
