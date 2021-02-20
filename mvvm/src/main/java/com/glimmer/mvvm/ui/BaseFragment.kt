package com.glimmer.mvvm.ui

import android.content.Context
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.core.util.containsValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.glimmer.mvvm.common.throttleLast
import com.glimmer.mvvm.config.BindingConfig
import com.glimmer.mvvm.view.IFragment
import com.glimmer.uutil.Clicker
import com.glimmer.uutil.KHandler
import com.glimmer.uutil.KLog
import java.util.concurrent.TimeUnit

abstract class BaseFragment : Fragment(), IFragment, Clicker {
    companion object {
        private const val DURATION_CLICK = 500L
    }

    /**==========================================================**/
    /**
     * 页面参数
     */
    internal val bindingConfig: BindingConfig.Info by lazy { createBindingInfo() }

    /**==========================================================**/
    /**
     * 防止内存泄漏Handler
     */
    val handler by lazy { KHandler(this) }
    val fragment: BaseFragment by lazy { this }
    lateinit var bindActivity: FragmentActivity

    /**
     * 是否加载
     */
    private var isLoaded = false

    /**
     * 忽略不需要过滤快速点击的view
     */
    private val ignoreMultiClickViewArr = SparseArray<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentContentView()
    }

    internal open fun setFragmentContentView() {
        val clazz = javaClass.superclass
        clazz?.let {
            val field = it.getDeclaredField("mContentLayoutId")
            field.isAccessible = true
            field.set(this, layoutId())
        } ?: throw ClassNotFoundException("fragment init layout error")
    }

    override fun onResume() {
        super.onResume()
        //增加了Fragment是否可见的判断
        if (!isLoaded && !isHidden) {
            onInit()
            isLoaded = true
            KLog.d(message = "lazyInit Fragment")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bindActivity = context as FragmentActivity
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            statusBarMode(isStatusBarFontDark())
        }
    }

    override fun onInit() {
        statusBarMode(isStatusBarFontDark())
        initData()
        initView()
        marginStatusBarView()?.apply { addViewMarginStatusBar(this) }
    }

    override fun initData() {
    }

    override fun initView() {
    }

    override fun onClick(v: View?) {
        v?.let {
            val contain = ignoreMultiClickViewArr.containsValue(it)
            if (contain) {
                viewClick(it)
            } else {
                throttleLast({
                    viewClick(it)
                }, clickDuration(), clickDurationTimeUnit())
            }
        }
    }

    /**==========================================================**/
    fun addViewsClick(viewArr: (() -> List<View>)) {
        viewArr.invoke().forEach {
            it.setOnClickListener(this)
        }
    }

    /**
     * 忽略过滤多次点击的view
     */
    fun filterMultiClickStrategy(viewArr: (() -> List<View>)) {
        viewArr.invoke().forEach {
            val contain = ignoreMultiClickViewArr.containsValue(it)
            if (!contain) {
                ignoreMultiClickViewArr.put(ignoreMultiClickViewArr.size(), it)
            }
        }
    }

    /**
     * 过滤点击的时长
     */
    open fun clickDuration() = DURATION_CLICK

    /**
     * 过滤点击的时长单位
     */
    open fun clickDurationTimeUnit() = TimeUnit.MILLISECONDS

    /**
     * 是否显示黑色文字
     */
    open fun isStatusBarFontDark() = false

    /**
     * 状态栏字体是否黑色模式
     */
    private fun statusBarMode(blackFont: Boolean) {
        (bindActivity as? BaseActivity)?.statusBarMode(blackFont)
    }

    /**
     * 下移状态栏高度的view
     */
    open fun marginStatusBarView(): View? = null

    /**
     * 是否设置view距离状态栏高度
     */
    private fun addViewMarginStatusBar(view: View) {
        (bindActivity as? BaseActivity)?.addViewMarginStatusBar(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    /**==========================================================**/
    abstract fun layoutId(): Int

    /**==========================================================**/
    abstract fun createBindingInfo(): BindingConfig.Info

    open fun viewClick(v: View) {}
}