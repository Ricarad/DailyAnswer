package com.ricarad.app.dailyanswer.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.activity.SettingMyCollectionActivity;
import com.ricarad.app.dailyanswer.activity.SettingMyPostActivity;
import com.ricarad.app.dailyanswer.activity.SettingAboutUsActivity;
import com.ricarad.app.dailyanswer.model.User;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout collection;//收藏的梯子
    private RelativeLayout myPosts;//发布的帖子
    private RelativeLayout about;//关于我们
    private RelativeLayout version;//版本信息
    private ImageView headImg;//头像设置
    private TextView nickNameTv;//昵称
    private RelativeLayout quit;//退出
    private RelativeLayout changeNickName;//修改昵称
    private User user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment_guide, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        collection = getActivity().findViewById(R.id.setting_fragment_guide_collection);
        myPosts = getActivity().findViewById(R.id.setting_fragment_guide_mypost);
        about = getActivity().findViewById(R.id.setting_fragment_guide_about);
        version = getActivity().findViewById(R.id.setting_fragment_guide_version);
        headImg = getActivity().findViewById(R.id.setting_fragment_guide_headimg);
        nickNameTv = getActivity().findViewById(R.id.setting_fragment_guide_nickname);
        quit = getActivity().findViewById(R.id.setting_fragment_guide_quit);
        changeNickName = getActivity().findViewById(R.id.setting_fragment_guide_change_nickname);
        collection.setOnClickListener(this);
        myPosts.setOnClickListener(this);
        about.setOnClickListener(this);
        version.setOnClickListener(this);
        headImg.setOnClickListener(this);
        quit.setOnClickListener(this);
        changeNickName.setOnClickListener(this);
        if (isAdded()) {  //判断Fragment已经依附Activity
            user = (User) getArguments().getSerializable("user");
        }

        initView();
    }

    public void initView() {
        //TODO
        nickNameTv.setText(user.getNickName());
        if (user.getUserImg() != null) {
            user.getUserImg().download(new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), "加载头像失败" + e.getErrorCode() + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Bitmap bmp = BitmapFactory.decodeFile(s);
                        headImg.setImageBitmap(bmp);
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_fragment_guide_collection: {
                //TODO 进入收藏的帖子页面
            }
            break;
            case R.id.setting_fragment_guide_mypost: {
                //TODO 进入我发表的帖子页面
            }
            break;
            case R.id.setting_fragment_guide_about: {
                //TODO 进入关于我们界面
            }
            break;
            case R.id.setting_fragment_guide_version: {
                //TODO 软件版本界面
            }
            break;
            case R.id.setting_fragment_guide_headimg: {
                //TODO 点击头像可以从本地获取头像并更换
            }
            break;
            case R.id.setting_fragment_guide_quit: {
                //TODO 点击退出回到登录界面
            }
            break;
            case R.id.setting_fragment_guide_change_nickname: {
                //TODO 点击修改昵称
            }
            break;
        }

    }


    //前往收藏界面
    private void goToSettingMyCollect() {
        Intent intent = new Intent(getContext(), SettingMyCollectionActivity.class);
        if (isAdded()) {  //判断Fragment已经依附Activity
            User user = (User) getArguments().getSerializable("user");
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }

    //前往我的帖子界面
    private void goToSettingMyCreate() {
        Intent intent = new Intent(getContext(), SettingMyPostActivity.class);
        if (isAdded()) {  //判断Fragment已经依附Activity
            User user = (User) getArguments().getSerializable("user");
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }

    //前往设置界面
    private void goToSettingMySettings() {
        Intent intent = new Intent(getContext(), SettingAboutUsActivity.class);
        if (isAdded()) {  //判断Fragment已经依附Activity
            User user = (User) getArguments().getSerializable("user");
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }


}
