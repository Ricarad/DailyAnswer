package com.ricarad.app.dailyanswer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;

import org.xutils.view.annotation.ViewInject;

public class AnswerFragment extends Fragment {

    @ViewInject(R.id.answerfragment_setting_iv)
    private ImageView settingIv;
    @ViewInject(R.id.answerfragment_number_tv)
    private TextView numberTv;
    @ViewInject(R.id.answerfragment_rightratio_tv)
    private TextView rightRatioTv;
    @ViewInject(R.id.answerfragment_days_tv)
    private TextView daysTv;
    @ViewInject(R.id.answerfragment_toolbar_gv)
    private GridView toolBarGv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.answer_fragment_guide,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
