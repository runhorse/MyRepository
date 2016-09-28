package com.itheima.admin.gooview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 目的是为了让ImageView摆放到合适的位置
 * @author lxj
 *
 */
public class BubbleLayout extends FrameLayout{
	
	public BubbleLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public BubbleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public BubbleLayout(Context context) {
		super(context);
	}
	private float x,y;
	public void setBubblePosition(float x,float y){
		this.x = x;
		this.y = y;
	}
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		View view = getChildAt(0);
		int l = (int) (x-view.getMeasuredWidth()/2);
		int t = (int) (y-view.getMeasuredHeight()/2);
		view.layout(l, t,l+view.getMeasuredWidth(), t+view.getMeasuredHeight());
	}

}
