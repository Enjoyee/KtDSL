package com.glimmer.dsl.adapter.decroation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glimmer.uutil.getColorById

class GridDividerDecoration(
    context: Context,
    @ColorRes private val color: Int = android.R.color.darker_gray,
    private val dividerPx: Int = 1,
) : RecyclerView.ItemDecoration() {

    private val mPaint = Paint()
    private var needTopBottomEdgeDivider = true
    private var needLeftRightEdgeDivider = false

    init {
        mPaint.flags = Paint.ANTI_ALIAS_FLAG
        mPaint.color = context.getColorById(color)
    }

    fun needTopBottomEdgeDivider(need: Boolean) {
        needTopBottomEdgeDivider = need
    }

    fun needLeftRightEdgeDivider(need: Boolean) {
        needLeftRightEdgeDivider = need
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (parent.layoutManager == null) {
            return
        }
        drawVertical(c, parent)
        drawHorizontal(c, parent)
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val childCount: Int = parent.childCount
        for (index in 0 until childCount) {
            val child: View = parent.getChildAt(index)
            val itemPosition = parent.getChildAdapterPosition(child)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val top = child.top - params.topMargin
            val right: Int = left + dividerPx
            val bottom = child.bottom + params.bottomMargin

            // 最左侧分割线
            if (needLeftRightEdgeDivider && isFirstSpan(itemPosition, parent)) {
                val leftTemp = child.left + params.leftMargin - dividerPx
                val rightTemp = leftTemp + dividerPx
                canvas.drawRect(
                    leftTemp.toFloat(),
                    top.toFloat(),
                    rightTemp.toFloat(),
                    bottom.toFloat(),
                    mPaint
                )
            }

            // 最右侧分割线
            val isEdgeRow = isLastSpan(itemPosition, parent)
            if (isEdgeRow && !needLeftRightEdgeDivider) {
                continue
            }

            canvas.drawRect(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                bottom.toFloat(),
                mPaint
            )
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val childCount: Int = parent.childCount
        for (index in 0 until childCount) {
            val child: View = parent.getChildAt(index)
            val itemPosition = parent.getChildAdapterPosition(child)
            val params = child.layoutParams as RecyclerView.LayoutParams
            var left = child.left - params.leftMargin
            if (needLeftRightEdgeDivider) {
                left -= dividerPx
            }
            val top = child.bottom + params.bottomMargin
            val right = child.right + params.rightMargin + dividerPx
            val bottom: Int = top + dividerPx

            // 顶部分割线
            if (needTopBottomEdgeDivider && isFirstRow(itemPosition, parent)) {
                val topTemp = child.top + params.topMargin - dividerPx
                val bottomTemp = topTemp + dividerPx
                canvas.drawRect(
                    left.toFloat(),
                    topTemp.toFloat(),
                    right.toFloat(),
                    bottomTemp.toFloat(),
                    mPaint
                )
            }

            // 底部分割线
            val isEdgeSpan = isLastRow(itemPosition, parent) || index == childCount - 1
            if (isEdgeSpan && !needTopBottomEdgeDivider) {
                continue
            }

            canvas.drawRect(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                bottom.toFloat(),
                mPaint
            )
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)
        var left = 0
        var top = 0
        var right = dividerPx
        var bottom = dividerPx

        if (isFirstSpan(itemPosition, parent) && needLeftRightEdgeDivider) {
            left = dividerPx
        }
        if (isLastSpan(itemPosition, parent) && !needLeftRightEdgeDivider) {
            right = 0
        }

        if (isFirstRow(itemPosition, parent) && needTopBottomEdgeDivider) {
            top = dividerPx
        }
        if (isLastRow(itemPosition, parent) && !needTopBottomEdgeDivider) {
            bottom = 0
        }

        outRect.set(left, top, right, bottom)
    }

    private fun isFirstRow(itemPosition: Int, parent: RecyclerView): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            if (itemPosition < spanCount) return true
        }
        return false
    }

    private fun isLastRow(itemPosition: Int, parent: RecyclerView): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val itemCount: Int = layoutManager.getItemCount()
            val spanCount = layoutManager.spanCount
            if (itemPosition > itemCount - spanCount) return true
        }
        return false
    }

    private fun isFirstSpan(itemPosition: Int, parent: RecyclerView): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            if ((itemPosition) % spanCount == 0) return true
        }
        return false
    }

    private fun isLastSpan(itemPosition: Int, parent: RecyclerView): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            if ((itemPosition + 1) % spanCount == 0) return true
        }
        return false
    }

}