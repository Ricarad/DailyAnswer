package com.ricarad.app.dailyanswer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.model.Question;

import java.util.List;

public class SldingListAdapter extends BaseAdapter {

    private int resourceId;
    private List<Question> questionList;
    private Context mContext;

    public SldingListAdapter(int resourceId, List<Question> questionList, Context context) {
        this.resourceId = resourceId;
        this.questionList = questionList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public Object getItem(int position) {
        return questionList.get(position);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.left_slidingment_listitem,null);
            holder .contentTv = (TextView)view.findViewById(R.id.left_itemllist_tv);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
       Question question = questionList.get(position);
        holder.contentTv.setText(question.getTitle());
        return view;
    }

    class ViewHolder{
        TextView contentTv;
    }
}
