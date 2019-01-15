package com.ricarad.app.dailyanswer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CommonAdapter extends BaseAdapter {
    private int resourceId;
    private List<Question> questionList;
    private Context mContext;
    //记录checkbox的状态
    static List<Question> deleteQuestions = new ArrayList<>();
    public static Boolean isEditMode=false;
    public CommonAdapter(Context mContext, int resourceId, List<Question> questionList) {
        this.questionList = questionList;
        this.mContext = mContext;
        this.resourceId = resourceId;
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
    public View getView(final int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(resourceId,null);
        TextView titleTv = view.findViewById(R.id.common_list_item_tv);
        final CheckBox checkBox = view.findViewById(R.id.common_list_item_check);
        titleTv.setText((position+1)+"."+questionList.get(position).getTitle());
        if(isEditMode){
            checkBox.setFocusable(true);
            checkBox.setVisibility(View.VISIBLE);
        }else {
            checkBox.setFocusable(false);
            checkBox.setVisibility(View.GONE);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked()){
                    deleteQuestions.add(questionList.get(position));
                }else {
                    deleteQuestions.remove(questionList.get(position));
                }
            }
        });
        return view;
    }
    public List<Question> getDeletes() {
        return deleteQuestions;
    }

    public void setDeleteQuestions(List<Question> questions){
        deleteQuestions = questions;
    }


}
