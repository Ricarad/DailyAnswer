package com.ricarad.app.dailyanswer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.model.Mycollection;

import java.util.List;

/**
 * Created by shy on 2019/1/14.
 */

public class CollectionAdapter extends BaseAdapter {
    private Context mContext;
    private List<Mycollection> data;

    public CollectionAdapter(Context mContext, List<Mycollection> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.mycollection_list_item,null);
        ImageView mycollect_list_item_photo = view.findViewById(R.id.mycollect_list_item_photo);
        TextView mycollect_list_item_username = view.findViewById(R.id.mycollect_list_item_username);
        TextView mycollect_list_item_title = view.findViewById(R.id.mycollect_list_item_title);
        mycollect_list_item_photo.setImageResource(data.get(position).getUserimg());
        mycollect_list_item_username.setText(data.get(position).getUsername());
        mycollect_list_item_title.setText(data.get(position).getCollectedTopics());
        return view;
    }
}
