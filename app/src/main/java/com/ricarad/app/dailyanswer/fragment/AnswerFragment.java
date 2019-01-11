package com.ricarad.app.dailyanswer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.activity.AnswerQuestionActivity;
import com.ricarad.app.dailyanswer.adapter.ToolBarAdapter;
import com.ricarad.app.dailyanswer.model.User;

public class AnswerFragment extends Fragment implements AdapterView.OnItemClickListener {


    private ImageView settingIv;
    private TextView numberTv; //显示刷题数的控件
    private TextView rightRatioTv;//显示正确率的控件
    private TextView daysTv;    //显示登录天数的控件
    private GridView toolBarGv;

    private User user;
    private String[] titleArr = new String[]{
            "随机练习","认真考试","练习记录","错题本","收藏夹"
    };
    private int[] imgArr = new int[]{R.drawable.answer_fragment_practice,R.drawable.answer_fragment_exam,
            R.drawable.answer_fragment_record, R.drawable.answer_fragment_mistakes, R.drawable.answer_fragment_collect};

    @Override
    public void onStart(){
        super.onStart();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.answer_fragment_guide,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        numberTv = getActivity().findViewById(R.id.answerfragment_number_tv);
        rightRatioTv = getActivity().findViewById(R.id.answerfragment_rightratio_tv);
        daysTv = getActivity().findViewById(R.id.answerfragment_days_tv);
        settingIv = getActivity().findViewById(R.id.answerfragment_setting_iv);
        toolBarGv = getActivity().findViewById(R.id.answerfragment_toolbar_gv);
        ToolBarAdapter toolBarAdapter = new ToolBarAdapter(getContext());
        toolBarGv.setAdapter(toolBarAdapter);
        toolBarGv.setOnItemClickListener(this);
        if (isAdded()) {  //判断Fragment已经依附Activity
            user = (User)getArguments().getSerializable("user");
           initView();
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(){
            numberTv.setText(user.getNumber().toString());
            daysTv.setText(user.getDays().toString());
            rightRatioTv.setText(user.getRightRatio().toString());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:{
                    Intent intent = new Intent(getContext(),AnswerQuestionActivity.class);
                    startActivity(intent);
                }break;
                case 1:{}break;
                case 2:{}break;
                case 3:{}break;
                case 4:{}break;

            }
    }
}
