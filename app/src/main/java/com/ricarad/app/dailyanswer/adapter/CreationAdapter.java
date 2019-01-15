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
import com.ricarad.app.dailyanswer.model.Topic;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by shy on 2019/1/15.
 */

public class CreationAdapter extends BaseAdapter {
    private Context mContext;
    private List<Mycollection> data;
    public CreationAdapter(Context mContext,List<Mycollection> data) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.mycreate_list_item,null);
        TextView mycreate_list_item_title = view.findViewById(R.id.mycreate_list_item_title_tv);
        TextView mycreate_list_item_year_tv = view.findViewById(R.id.mycreate_list_item_year_tv);
        TextView mycreate_list_item_month_tv = view.findViewById(R.id.mycreate_list_item_month_tv);
        TextView mycreate_list_item_day_tv = view.findViewById(R.id.mycreate_list_item_day_tv);
        mycreate_list_item_title.setText(data.get(position).getCollectedTopics());
        mycreate_list_item_year_tv.setText("2019年");
        mycreate_list_item_month_tv.setText("01月");
        mycreate_list_item_day_tv.setText("15日");
        return view;
    }
}
