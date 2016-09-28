package com.itheima.admin.qq50;

import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/9/20.
 */
public class SlideMenu extends FrameLayout {

    private View           mMenuView;
    private View           mMainView;
    private ViewDragHelper mViewDragHelper;
    private int            mDragRange;
    private FloatEvaluator mFloatEvaluator;
    private ArgbEvaluator  mArgbEvaluator;
    private OnDragStateChangeListner listner;

    public SlideMenu(Context context) {
        this(context, null);
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mViewDragHelper = ViewDragHelper.create(this, mCallBack);
        mFloatEvaluator = new FloatEvaluator();
        mArgbEvaluator = new ArgbEvaluator();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mMainView = getChildAt(1);

    }

    private ViewDragHelper.Callback mCallBack = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mMainView || child == mMenuView;
        }

        //set slide range
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mMainView) {
                if (left > mDragRange) {
                    left = mDragRange;
                } else if (left < 0) {
                    left = 0;
                }
            }
            return left;
        }

        //should return positive
        @Override
        public int getViewHorizontalDragRange(View child) {
            return mDragRange;
        }

        //set move togethor
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == mMenuView) {
                mMenuView.layout(0, 0, mMenuView.getRight(), mMenuView.getBottom());
                //let mainMenu move together
                int newLeft = mMainView.getLeft() + dx;
                if (newLeft < 0) {
                    newLeft = 0;
                } else if (newLeft > mDragRange) {
                    newLeft = mDragRange;
                }
                mMainView.layout(newLeft, 0, mMainView.getWidth() + newLeft, mMainView.getBottom());
            }


            float fraction = mMainView.getLeft() * 1f / mDragRange;
            executAnim(fraction);

            if (mMainView.getLeft() == 0){
                mMenuViewState = MenuViewState.CLOSE;
                if (listner != null){
                    listner.onClose();
                }
            }else if (mMainView.getLeft() == mDragRange){
                mMenuViewState = MenuViewState.OPEN;
                if (listner != null) {
                    listner.onOpen();
                }
            }else {
                mMenuViewState = MenuViewState.DRAGING;
                if (listner != null) {
                    listner.onDraging(fraction);
                }
            }

        }


        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (mMainView.getLeft() > mDragRange / 2) {
                //open:scroll to right
                mViewDragHelper.smoothSlideViewTo(mMainView, mDragRange, 0);
                ViewCompat.postInvalidateOnAnimation(SlideMenu.this);

            } else {
                //close: scroll to left
                mViewDragHelper.smoothSlideViewTo(mMainView, 0, 0);
                ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
            }
        }


    };

    private void executAnim(float fraction) {

        ViewCompat.setScaleY(mMainView, mFloatEvaluator.evaluate(fraction, 1f, .8f));
        ViewCompat.setScaleX(mMainView, mFloatEvaluator.evaluate(fraction, 1f, .8f));

        ViewCompat.setScaleY(mMenuView, mFloatEvaluator.evaluate(fraction, .4f, 1f));
        ViewCompat.setScaleX(mMenuView, mFloatEvaluator.evaluate(fraction, .4f, 1f));
        ViewCompat.setTranslationX(mMenuView, mFloatEvaluator.evaluate(fraction, -mDragRange / 2, 0));
        ViewCompat.setAlpha(mMenuView, mFloatEvaluator.evaluate(fraction, 0.2f, 1.0f));
        getBackground().setColorFilter((Integer) mArgbEvaluator.evaluate(fraction, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = mViewDragHelper.shouldInterceptTouchEvent(ev);

        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int menuWidth = mMenuView.getMeasuredWidth();
        int mainWidth = mMainView.getMeasuredWidth();

        mDragRange = menuWidth;
    }


    public void setOnDragStateChangeListner(OnDragStateChangeListner listner){
        this.listner = listner;
    }

    public interface OnDragStateChangeListner {
        void onOpen();

        void onClose();

        void onDraging(float fraction);
    }


    public MenuViewState mMenuViewState = MenuViewState.CLOSE;

    public enum MenuViewState {
        OPEN,CLOSE,DRAGING;
    }

}
