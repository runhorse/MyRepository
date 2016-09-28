package com.itheima.admin.qq50;

import android.animation.FloatEvaluator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FloatEvaluator floatEvaluator = new FloatEvaluator();

        ListView lv_main = (ListView) findViewById(R.id.main_listview);
        ListView lv_menu = (ListView) findViewById(R.id.menu_listview);

        SlideMenu slideMenu = (SlideMenu) findViewById(R.id.slidemenu);
        final ImageView head = (ImageView) findViewById(R.id.iv_head);

        MyLinearLayout myLinearLayout = (MyLinearLayout) findViewById(R.id.my_layout);
        if (myLinearLayout != null) {
            myLinearLayout.setSlideMenu(slideMenu);
        }


        if (lv_main != null) {
            lv_main.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Constant.NAMES));
        }

        if (lv_menu != null) {
            lv_menu.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Constant.sCheeseStrings){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView tv = (TextView) super.getView(position, convertView, parent);

                    tv.setTextColor(Color.WHITE);

                    return tv;
                }
            });
        }


        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, Constant.NAMES[position], Toast.LENGTH_SHORT).show();
            }
        });

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, Constant.sCheeseStrings[position], Toast.LENGTH_SHORT).show();
            }
        });


        if (slideMenu != null) {
            slideMenu.setOnDragStateChangeListner(

                    new SlideMenu.OnDragStateChangeListner() {
                        @Override
                        public void onOpen() {

                        }

                        @Override
                        public void onClose() {
                            ViewCompat.animate(head)
                                    .translationX(80)
                                    .setDuration(1000)
                                    .setInterpolator(new CycleInterpolator(5))
                                    .start();
                        }

                        @Override
                        public void onDraging(float fraction) {
                            ViewCompat.setAlpha(head,floatEvaluator.evaluate(fraction,1f,0f));
                        }
                    }
            );
        }


    }
}
