package com.glimmer.mvvm.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.glimmer.mvvm.common.CommonVH

class CommonListAdapterDsl : BaseListAdapter<CommonVH<Any, *>>() {

    fun <BEAN : Any, VB : ViewDataBinding> addItem(resourceId: Int, init: ViewHolderCreatorDsl<BEAN, VB>.() -> Unit) {
        val holder = ViewHolderCreatorDsl<BEAN, VB>(resourceId)
        holder.init()
        typeVHs.put(typeVHs.size(), holder)
    }

}

/**==========================================================**/

class ViewHolderCreatorDsl<BEAN : Any, VB : ViewDataBinding>(private val resourceId: Int) : ViewHolderCreator<BEAN, VB>() {
    internal lateinit var isViewType: ((Any) -> Boolean)
    private var setUpData: (CommonVH<BEAN, VB>.() -> Unit)? = null

    fun isViewType(viewType: (Any) -> Boolean) {
        isViewType = viewType
    }

    fun bindVH(setUpData: CommonVH<BEAN, VB>.() -> Unit) {
        this.setUpData = setUpData
    }

    private fun buildVH(parent: ViewGroup): CommonVH<BEAN, VB> {
        val vh = CommonVH<BEAN, VB>(parent, getLayoutId())
        setUpData?.let { vh.also(it) }
        return vh
    }

    override fun isForViewType(data: BEAN): Boolean = isViewType.invoke(data)

    override fun getLayoutId(): Int = resourceId

    override fun createViewHolder(parent: ViewGroup): BaseVH<BEAN, VB> = buildVH(parent)

}
