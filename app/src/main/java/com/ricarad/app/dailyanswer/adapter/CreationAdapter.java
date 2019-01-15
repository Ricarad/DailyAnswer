package com.ricarad.app.dailyanswer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.model.Post;

import java.util.List;

/**
 * Created by shy on 2019/1/15.
 */

public class CreationAdapter extends BaseAdapter {
    private Context mContext;
    private List<Post> postList;
    public CreationAdapter(Context mContext,List<Post> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.create_post_list_item,null);
        TextView mycreate_list_item_title = view.findViewById(R.id.mycreate_list_item_title_tv);
        TextView mycreate_list_item_year_tv = view.findViewById(R.id.mycreate_list_item_year_tv);
        TextView mycreate_list_item_month_tv = view.findViewById(R.id.mycreate_list_item_month_tv);
        TextView mycreate_list_item_day_tv = view.findViewById(R.id.mycreate_list_item_day_tv);

        return view;
    }
}
