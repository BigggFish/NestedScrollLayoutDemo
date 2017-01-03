package com.bigggfish.nestscrollviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.OverScroller;

/**
 * Created by bigggfish on 2016/12/5.
 * description:
 */
public class NestedScrollTopView extends LinearLayout {

    private OverScroller mScroller;
    private  int mTopViewHeight;
    public NestedScrollTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new OverScroller(context);
    }

    public NestedScrollTopView(Context context) {
        super(context);
        mScroller = new OverScroller(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mTopViewHeight = getMeasuredHeight();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }
}
