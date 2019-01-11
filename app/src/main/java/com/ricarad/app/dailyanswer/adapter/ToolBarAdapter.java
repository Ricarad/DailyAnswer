package com.ricarad.app.dailyanswer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricarad.app.dailyanswer.R;

import java.util.ArrayList;
import java.util.List;

public class ToolBarAdapter extends BaseAdapter {
    private Context context;
    private List<ToolItem> toolItemList;

    private String[] titleArr = new String[]{
            "随机练习","认真考试","练习记录","错题本","收藏夹"
    };
    private int[] imgArr = new int[]{R.drawable.answer_fragment_practice,R.drawable.answer_fragment_exam,
    R.drawable.answer_fragment_record, R.drawable.answer_fragment_mistakes, R.drawable.answer_fragment_collect};



    public ToolBarAdapter(Context context){
        this.context = context;
        toolItemList = new ArrayList<>();
        for (int i = 0;i<5;i++){
            ToolItem toolItem = new ToolItem(titleArr[i],imgArr[i]);
            toolItemList.add(toolItem);
        }
    }
    @Override
    public int getCount() {
        if(toolItemList != null){
            return toolItemList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return toolItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.toolitem_answer_fragment, null);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.toolitem_img_iv);
            //设置显示图片
            viewHolder.iv.setBackgroundResource(imgArr[position]);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.toolitem_title_tv);
            //设置标题
            viewHolder.tv.setText(titleArr[position]);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    class ViewHolder{
        ImageView iv;
        TextView tv;
    }
    class ToolItem{
        private String name;
        private int imgId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getImgId() {
            return imgId;
        }

        public void setImgId(int imgId) {
            this.imgId = imgId;
        }

        public ToolItem(String name, int imgId) {
            this.name = name;
            this.imgId = imgId;
        }
    }
}
