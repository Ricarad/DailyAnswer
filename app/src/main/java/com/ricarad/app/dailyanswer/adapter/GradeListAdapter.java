package com.ricarad.app.dailyanswer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.model.Question;

import java.util.HashMap;
import java.util.List;

import static com.ricarad.app.dailyanswer.common.Constant.QuestionResultType.EMPTY;
import static com.ricarad.app.dailyanswer.common.Constant.QuestionResultType.ERROR;
import static com.ricarad.app.dailyanswer.common.Constant.QuestionResultType.RIGHT;

public class GradeListAdapter extends BaseAdapter {

    private List<Question> questionList;
    private Context mContext;
    private HashMap<Question,Integer> questionResultMap;

    public GradeListAdapter(Context mContext, List<Question> questionList,  HashMap<Question, Integer> questionResultMap) {
        this.questionList = questionList;
        this.mContext = mContext;
        this.questionResultMap = questionResultMap;
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
        view = LayoutInflater.from(mContext).inflate(R.layout.grade_results_list_item,null);
        TextView titleTv = view.findViewById(R.id.grade_list_item_tv);
        titleTv.setText((position+1)+"."+questionList.get(position).getTitle());
        switch (questionResultMap.get(questionList.get(position))){
            case RIGHT:{
                    titleTv.setTextColor(mContext.getResources().getColor(R.color.cololSkyBlue));
            }break;
            case ERROR:{
                titleTv.setTextColor(mContext.getResources().getColor(R.color.colorSmallRed));
            }break;
            case EMPTY:{

            }break;
        }
        return view;
    }
}
