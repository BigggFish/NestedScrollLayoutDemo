package com.bigggfish.nestscrollviewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by bigggfish on 2016/11/24.
 * description:tab指示器
 */
public class SimpleViewPagerIndicator extends LinearLayout {

    private static final int COLOR_TEXT_NORMAL = 0xFF000000;
    private static final int COLOR_INDICATOR_COLOR = Color.GREEN;

    private OnItemClickListener onItemClickListener;
    private String[] mTitles;
    private int mTabCount;
    private int mIndicatorColor = COLOR_INDICATOR_COLOR;
    private float mTranslationX;
    private Paint mPaint = new Paint();
    private int mTabWidth;

    private int mTouchSlop;
    private float mLastX;
    private float mLastY;

    public SimpleViewPagerIndicator(Context context) {
        this(context, null);
    }

    public SimpleViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(mIndicatorColor);
        mPaint.setStrokeWidth(9.0F);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTabWidth = w / mTabCount;
    }

    public void setTitles(String[] titles) {
        mTitles = titles;
        mTabCount = titles.length;
        generateTitleView();

    }

    public void setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        canvas.translate(mTranslationX, getHeight() - 2);
        canvas.drawLine(0, 0, mTabWidth, 0, mPaint);
        canvas.restore();
    }

    public void scroll(int position, float offset) {
        /**
         * <pre>
         *  0-1:position=0 ;1-0:postion=0;
         * </pre>
         */
        mTranslationX = getWidth() / mTabCount * (position + offset);
        invalidate();
    }

    private void generateTitleView() {
        if (getChildCount() > 0)
            this.removeAllViews();
        int count = mTitles.length;

        setWeightSum(count);
        for (int i = 0; i < count; i++) {
            TextView tv = new TextView(getContext());
            LayoutParams lp = new LayoutParams(0,
                    LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(COLOR_TEXT_NORMAL);
            tv.setText(mTitles[i]);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setLayoutParams(lp);
            addView(tv);
            final int position = i;
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

      /*  Log.e("SimpleVPIndicator", "onTouchEvent " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();

                //break;
                return false;

            case MotionEvent.ACTION_UP:
               *//* float x = event.getX();
                float y = event.getY();
                if (Math.abs(x - mLastX) < mTouchSlop && Math.abs(y - mLastY) < mTouchSlop) {
                    if (onItemClickListener != null) {
                        int position = (int) x / mTabWidth;
                        onItemClickListener.onItemClick(position == mTabCount ? position - 1 : position);
                        return true;
                    }
                }*//*
                break;
            case MotionEvent.ACTION_CANCEL:
                float x = event.getX();
                float y = event.getY();
                if (Math.abs(x - mLastX) < mTouchSlop && Math.abs(y - mLastY) < mTouchSlop) {
                    if (onItemClickListener != null) {
                        int position = (int) x / mTabWidth;
                        onItemClickListener.onItemClick(position == mTabCount ? position - 1 : position);
                        return true;
                    }
                }

                mLastX = x;
                mLastY = y;
                break;

        }
*/
        return super.onTouchEvent(event);
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
