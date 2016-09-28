package com.itheima.admin.news360demo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2016/9/1.
 */
public class News360Adapter extends BaseAdapter {

    private final List<News360Bean.DataBean> list;
    private final Context context;

    public News360Adapter(Context context, List<News360Bean.DataBean> list) {
     this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public News360Bean.DataBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        News360Bean.DataBean item = getItem(position);
        int size = item.getImgs().size();
        if (size == 3){
            return 0;
        }else if (size == 0){
            return  1;
        }else {
            return 2;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        News360Bean.DataBean item = getItem(position);
        ViewHolder holder = null;
        switch (itemViewType) {
            case 0:    //三张图片类型
                if (convertView == null){
                    convertView = View.inflate(context,R.layout.list_text3,null);
                    holder = new ViewHolder();
                    holder.img1 = (ImageView) convertView.findViewById(R.id.image1);
                    holder.img2 = (ImageView) convertView.findViewById(R.id.image2);
                    holder.img3 = (ImageView) convertView.findViewById(R.id.image3);
                    holder.tv = (TextView) convertView.findViewById(R.id.text);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tv.setText(item.getTitle());

                Glide.with(context).load(item.getImgs().get(0)).into(holder.img1);
                Glide.with(context).load(item.getImgs().get(1)).into(holder.img2);
                Glide.with(context).load(item.getImgs().get(2)).into(holder.img3);


                return convertView;

            case 1:  //没有图片类型
                if (convertView == null){
                    convertView = View.inflate(context,R.layout.list_text2,null);
                    holder = new ViewHolder();
                    holder.tv = (TextView) convertView.findViewById(R.id.text);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tv.setText(item.getTitle());
                return convertView;

            case 2: //一张图片类型
                if (convertView == null){
                    convertView = View.inflate(context,R.layout.list_text2,null);
                    holder = new ViewHolder();
                    holder.img1 = (ImageView) convertView.findViewById(R.id.image1);
                    holder.tv = (TextView) convertView.findViewById(R.id.text);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tv.setText(item.getTitle());

                Glide.with(context).load(item.getImgs().get(0)).into(holder.img1);

                return convertView;


        }
        return convertView;
    }


   private class ViewHolder{
       TextView tv;
       ImageView img1;
       ImageView img2;
       ImageView img3;
   }


}
