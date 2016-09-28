package com.itheima.admin.qq50;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/9/20.
 */
public class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private SlideMenu mSlideMenu;

    public void setSlideMenu(SlideMenu slideMenu){
        this.mSlideMenu = slideMenu;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (mSlideMenu != null && mSlideMenu.mMenuViewState == SlideMenu.MenuViewState.OPEN) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSlideMenu != null && mSlideMenu.mMenuViewState == SlideMenu.MenuViewState.OPEN){
            return  true;
        }
        return super.onTouchEvent(event);
    }
}
