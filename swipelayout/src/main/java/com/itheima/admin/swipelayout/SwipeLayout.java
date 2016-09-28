package com.itheima.admin.swipelayout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SwipeLayout extends FrameLayout {

    private View mContentView;
    private View mDeleteView;
    private int mContetnWidth;
    private int mContentHeight;
    private int mDeletedWidth;
    private int mDeleteHeight;
    private ViewDragHelper mViewDragHelper;

    public SwipeLayout(Context context) {
        this(context,null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, callback);
    }


    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mDeletedWidth;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            if (child == mContentView){
                if (left > 0){
                    left = 0;
                }
                if (left < -mDeletedWidth) {
                    left = -mDeletedWidth;
                }
            }else {
                if (left > mContetnWidth) {
                    left = mContetnWidth;
                }
                if (left < mContetnWidth - mDeletedWidth) {
                    left = mContetnWidth - mDeletedWidth;
                }
            }
            return left;
        }


        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            if (changedView == mContentView) {
                int newLeft = mDeleteView.getLeft() + dx;
                mDeleteView.layout(newLeft,0,newLeft+mDeletedWidth,mDeleteHeight);
            }
            if (changedView == mDeleteView) {
                int newLeft = mContentView.getLeft() + dx;
                mContentView.layout(newLeft,0, newLeft+mContetnWidth,mContentHeight);
            }

        }


        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (mContentView.getLeft() < -mDeletedWidth/2){
                //left
                mViewDragHelper.smoothSlideViewTo(mContentView,-mDeletedWidth,0);
                ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
            }else {
                //right
                mViewDragHelper.smoothSlideViewTo(mContentView,0,0);
                ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
            }
        }

    };


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mDeleteView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mContetnWidth = mContentView.getMeasuredWidth();
        mContentHeight = mContentView.getMeasuredHeight();

        mDeletedWidth = mDeleteView.getMeasuredWidth();
        mDeleteHeight = mDeleteView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mContentView.layout(0,0,mContetnWidth,mContentHeight);
        mDeleteView.layout(mContetnWidth,0,mContetnWidth+mDeletedWidth,mDeleteHeight);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
