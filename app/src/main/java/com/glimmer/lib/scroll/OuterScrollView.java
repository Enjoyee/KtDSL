package com.glimmer.lib.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class OuterScrollView extends ScrollView {
    public OuterScrollView(Context context) {
        super(context);
    }

    public OuterScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OuterScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OuterScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        final int action = ev.getAction();
//        // 不拦截 ACTION_DOWN 事件
//        return (action & ev.getActionMasked()) != MotionEvent.ACTION_DOWN;
//    }
}
