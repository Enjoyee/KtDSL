package com.glimmer.lib.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class InnerScrollView extends ScrollView {
    private int lastY;

    public InnerScrollView(Context context) {
        super(context);
    }

    public InnerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InnerScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        postInvalidate();
        invalidate();
        int y = (int) ev.getY();
        switch (action & ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaY = y - lastY;
                boolean isScrollToTop = deltaY > 0 && !canScrollVertically(-1);
                boolean isScrollToBottom = deltaY < 0 && !canScrollVertically(1);
                if (isScrollToTop || isScrollToBottom) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }

        lastY = y;
        return super.dispatchTouchEvent(ev);
    }

}
