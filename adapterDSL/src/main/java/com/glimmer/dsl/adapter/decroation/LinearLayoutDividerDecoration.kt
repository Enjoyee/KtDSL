package com.glimmer.dsl.adapter.decroation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glimmer.uutil.density
import com.glimmer.uutil.getColorById
import kotlin.math.roundToInt

class LinearLayoutDividerDecoration(
    private val context: Context,
    @ColorRes private val color: Int = android.R.color.darker_gray,
    private val dividerPx: Int = 1
) : RecyclerView.ItemDecoration() {

    private val mBounds = Rect()
    private val mMaiginBounds = Rect()
    private val mPaint = Paint()
    private var mNeedFirstDivider = true
    private var mNeedLastDivider = true

    init {
        mPaint.flags = Paint.ANTI_ALIAS_FLAG
        mPaint.color = context.getColorById(color)
    }

    fun setTopBottomDividerMargin(topDp: Int = 0, bottomDp: Int = 0) {
        mMaiginBounds.top = (topDp * context.density).roundToInt()
        mMaiginBounds.bottom = (bottomDp * context.density).roundToInt()
    }

    fun setLeftRightDividerMargin(leftDp: Int = 0, rightDp: Int = 0) {
        mMaiginBounds.left = (leftDp * context.density).roundToInt()
        mMaiginBounds.right = (rightDp * context.density).roundToInt()
    }

    fun needFirstDivider(need: Boolean) {
        mNeedFirstDivider = need
    }

    fun needLastDivider(need: Boolean) {
        mNeedLastDivider = need
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (parent.layoutManager == null) {
            return
        }

        val orientation = getOrientation(parent)
        if (orientation == RecyclerView.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val child = parent.getChildAt(index)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            var bottom = mBounds.bottom + child.translationY.roundToInt()
            var top = bottom - dividerPx
            val finalLeft = left + mMaiginBounds.left
            val finalRight = right + mMaiginBounds.right

            // 最后一条分割线跟底部分割线
            if (mNeedLastDivider || !isLastItem(parent)) {
                canvas.drawRect(finalLeft.toFloat(), top.toFloat(), finalRight.toFloat(), bottom.toFloat(), mPaint)
            }

            // 第一条分割线
            if (mNeedFirstDivider && isFirstItem(parent, child)) {
                bottom = child.top + child.translationY.roundToInt()
                top = bottom - dividerPx
                canvas.drawRect(finalLeft.toFloat(), top.toFloat(), finalRight.toFloat(), bottom.toFloat(), mPaint)
            }
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(
                parent.paddingLeft, top,
                parent.width - parent.paddingRight, bottom
            )
        } else {
            top = 0
            bottom = parent.height
        }
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val child = parent.getChildAt(index)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            var right = mBounds.right + child.translationX.roundToInt()
            var left = right - dividerPx
            val finalTop = top + mMaiginBounds.top
            val finalBottom = bottom + mMaiginBounds.bottom

            // 最后一条分割线跟右侧分割线
            if (mNeedLastDivider || !isLastItem(parent)) {
                canvas.drawRect(left.toFloat(), finalTop.toFloat(), right.toFloat(), finalBottom.toFloat(), mPaint)
            }

            // 第一条分割线
            if (mNeedFirstDivider && isFirstItem(parent, child)) {
                right = child.left + child.translationX.roundToInt()
                left = right - dividerPx
                canvas.drawRect(left.toFloat(), finalTop.toFloat(), right.toFloat(), finalBottom.toFloat(), mPaint)
            }
        }
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val orientation = getOrientation(parent)
        if (orientation == LinearLayout.VERTICAL) {
            var bottom = dividerPx
            if (!mNeedLastDivider && isLastItem(parent)) {
                bottom = 0
            }
            outRect.bottom = bottom

            // 第一个item的顶部分割线
            if (mNeedFirstDivider && isFirstItem(parent, view)) {
                outRect.top = dividerPx
            }
        } else {
            var right = dividerPx
            if (!mNeedLastDivider && isLastItem(parent)) {
                right = 0
            }
            outRect.right = right

            // 第一个item的最左侧分割线
            if (mNeedFirstDivider && isFirstItem(parent, view)) {
                outRect.left = dividerPx
            }
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        var orientation = RecyclerView.VERTICAL
        (parent.layoutManager as? LinearLayoutManager)?.let {
            orientation = it.orientation
        }
        return orientation
    }

    private fun isFirstItem(parent: RecyclerView, child: View): Boolean {
        return parent.getChildAdapterPosition(child) == 0
    }

    private fun isLastItem(parent: RecyclerView): Boolean {
        (parent.layoutManager as? LinearLayoutManager)?.apply {
            return itemCount - 1 == findLastCompletelyVisibleItemPosition()
        }
        return false
    }

}