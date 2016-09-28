package com.itheima.admin.flowlayout;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/9/24.
 */
public class FlowLayoutActivity extends Activity{

    private List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataCenter.addData(data);

        ScrollView scrollView =  new ScrollView(this);

        scrollView.setPadding(10,10,10,10);

        FLowLayout fLowLayout = new FLowLayout(this);


        for (int i = 0; i < data.size(); i++) {

            final TextView textView = new TextView(this);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setText(data.get(i));

            int red = 30 + new Random().nextInt(210);
            int green = 30 + new Random().nextInt(210);
            int blue = 30 + new Random().nextInt(210);

            //此颜色用于绘画textView的背景
            int rgb = Color.rgb(red,green,blue);
            Drawable drawableNormal = DrawableUtil.getGradientDrawable(rgb, 6);

            //创建选中的背景图片
            //偏白色
            int pressRgb = 0xffcecece;
            Drawable drawablePress = DrawableUtil.getGradientDrawable(pressRgb, 6);

            //生成选择器
            StateListDrawable stateListDrawable = DrawableUtil.getStateListDrawable(drawablePress, drawableNormal);

            textView.setBackgroundDrawable(stateListDrawable);

            textView.setPadding(5,5,5,5);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(FlowLayoutActivity.this, textView.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            fLowLayout.addView(textView);
        }

            scrollView.addView(fLowLayout);
            setContentView(scrollView);
    }
}
