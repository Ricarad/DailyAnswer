package com.ricarad.app.dailyanswer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ricarad.app.dailyanswer.R;

import java.util.List;

public class SlidingListAdapter extends BaseAdapter {

    private int resourceId;
    private List<String> questionTitleList;
    private Context mContext;

    public SlidingListAdapter( Context context,int resourceId, List<String> questionTitleList) {
        this.resourceId = resourceId;
        this.questionTitleList = questionTitleList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return questionTitleList.size();
    }

    @Override
    public Object getItem(int position) {
        return questionTitleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder=null;
        if(view==null){
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.left_slidingment_list_item,null);
            holder .contentTv = (TextView)view.findViewById(R.id.left_itemllist_tv);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.contentTv.setText(questionTitleList.get(position));
        return view;
    }

    class ViewHolder{
        TextView contentTv;
    }
}
