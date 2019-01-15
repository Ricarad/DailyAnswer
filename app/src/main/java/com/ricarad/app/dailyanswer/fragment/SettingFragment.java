package com.ricarad.app.dailyanswer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.activity.SettingMyCollectionActivity;
import com.ricarad.app.dailyanswer.activity.SettingMyPostActivity;
import com.ricarad.app.dailyanswer.activity.SettingMySettingsActivity;
import com.ricarad.app.dailyanswer.model.User;

public class SettingFragment extends Fragment implements View.OnClickListener{
    private LinearLayout ll_collection;//收藏布局
    private LinearLayout ll_creation;
    private LinearLayout ll_settings;
    private User user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment_guide,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ll_collection= getActivity().findViewById(R.id.setting_fragment_guide_collection);
        ll_collection.setOnClickListener(this);
        ll_creation = getActivity().findViewById(R.id.setting_fragment_guide_mycreation);
        ll_creation.setOnClickListener(this);
        ll_settings = getActivity().findViewById(R.id.setting_fragment_guide_settings);
        ll_settings.setOnClickListener(this);
        if (isAdded()) {  //判断Fragment已经依附Activity
            user = (User) getArguments().getSerializable("user");
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_fragment_guide_collection: {
                goToSettingMyCollect();
            }
                break;
            case R.id.setting_fragment_guide_mycreation:{
                goToSettingMyCreate();
                break;
            }
            case R.id.setting_fragment_guide_settings:{
                goToSettingMySettings();
                break;
            }
        }


    }
//前往收藏界面
    private void goToSettingMyCollect() {
        Intent intent = new Intent(getContext(), SettingMyCollectionActivity.class);
        if (isAdded()) {  //判断Fragment已经依附Activity
            User user = (User)getArguments().getSerializable("user");
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }
    //前往我的帖子界面
    private void goToSettingMyCreate() {
        Intent intent = new Intent(getContext(), SettingMyPostActivity.class);
        if (isAdded()) {  //判断Fragment已经依附Activity
            User user = (User)getArguments().getSerializable("user");
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }
  //前往设置界面
    private void goToSettingMySettings() {
        Intent intent = new Intent(getContext(), SettingMySettingsActivity.class);
        if (isAdded()) {  //判断Fragment已经依附Activity
            User user = (User)getArguments().getSerializable("user");
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }


}
