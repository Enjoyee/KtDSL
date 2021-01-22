package com.glimmer.enjoy.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.glimmer.enjoy.R

class DialogDSL {
    internal var title: String? = null
    internal var message: String? = null
    internal var positiveText: String? = null
    internal var positiveClick: ((View) -> Unit)? = null
    internal var negativeText: String? = null
    internal var negativeClick: ((View) -> Unit)? = null

    fun title(title: () -> String) {
        this.title = title.invoke()
    }

    fun message(message: () -> String) {
        this.message = message.invoke()
    }

    fun positiveText(positiveText: () -> String) {
        this.positiveText = positiveText.invoke()
    }

    fun positiveClick(positiveClick: (View) -> Unit) {
        this.positiveClick = positiveClick
    }

    fun negativeText(negativeText: () -> String) {
        this.negativeText = negativeText.invoke()
    }

    fun negativeClick(negativeClick: (View) -> Unit) {
        this.negativeClick = negativeClick
    }
}

fun FragmentActivity.showDSLDialog(dsl: DialogDSL.() -> Unit) {
    AlertDialogF().initParam(DialogDSL().also(dsl)).show(supportFragmentManager, "dialog")
}

class AlertDialogF : DialogFragment() {
    private lateinit var tvTitle: TextView
    private lateinit var tvMessage: TextView
    private lateinit var tvPositive: TextView
    private lateinit var tvNegative: TextView

    private lateinit var initParam: DialogDSL

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.dialog_fragment, container, false)
        tvTitle = contentView.findViewById(R.id.tvTitle)
        tvMessage = contentView.findViewById(R.id.tvMessage)
        tvPositive = contentView.findViewById(R.id.tvPositive)
        tvNegative = contentView.findViewById(R.id.tvNegative)
        return contentView
    }

    override fun onStart() {
        super.onStart()
        context?.apply {
            dialog?.window?.setLayout(
                (0.9 * resources.displayMetrics.widthPixels).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            tvTitle.text = initParam.title
            tvMessage.text = initParam.message
            tvPositive.text = initParam.positiveText
            tvPositive.setOnClickListener {
                initParam.positiveClick?.invoke(it)
                dismissAllowingStateLoss()
            }
            tvNegative.text = initParam.negativeText
            tvNegative.setOnClickListener {
                initParam.negativeClick?.invoke(it)
                dismissAllowingStateLoss()
            }
        }
    }

    fun initParam(initParam: DialogDSL): AlertDialogF {
        this.initParam = initParam
        return this
    }

}