package com.ricarad.app.dailyanswer.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.entity.Result;
import com.qingmei2.rximagepicker.ui.SystemImagePicker;
import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.activity.AddTopicActivity;
import com.ricarad.app.dailyanswer.activity.RegisterActivity;
import com.ricarad.app.dailyanswer.activity.SettingMyCollectionActivity;
import com.ricarad.app.dailyanswer.activity.SettingMyPostActivity;
import com.ricarad.app.dailyanswer.activity.SettingAboutUsActivity;
import com.ricarad.app.dailyanswer.activity.SettingShowVersionActivity;
import com.ricarad.app.dailyanswer.model.User;

import java.io.File;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.reactivex.functions.Consumer;

import static com.ricarad.app.dailyanswer.common.Constant.BMOBAPPKEY;

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
            Uri imageUrl = Uri.parse(user.getUserImg().getFileUrl());
            Glide.with(this).load(imageUrl).into(headImg);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_fragment_guide_collection: {
                //进入收藏的帖子页面
                Intent intent = new Intent(getContext(), SettingMyCollectionActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
            break;
            case R.id.setting_fragment_guide_mypost: {
                //进入我发表的帖子页面
                Intent intent = new Intent(getContext(), SettingMyPostActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
            break;
            case R.id.setting_fragment_guide_about: {
                // 进入关于我们界面
                Intent intent = new Intent(getContext(), SettingAboutUsActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.setting_fragment_guide_version: {
                // 软件版本界面
                Intent intent = new Intent(getContext(), SettingShowVersionActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.setting_fragment_guide_headimg: {
                //点击头像可以从本地获取头像并更换
                SystemImagePicker systemImagePicker = RxImagePicker.INSTANCE.create();
                systemImagePicker.openGallery(getActivity()).subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) {
                        final String imgUrl = result.getUri().getPath();
                        BmobFile bmobFile = new BmobFile(new File(imgUrl));
                        final User tempUser = new User();
                        tempUser.setObjectId(user.getObjectId());
                        tempUser.setUserImg(bmobFile);
                        tempUser.getUserImg().uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    tempUser.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                File file = new File(imgUrl);
                                                final Bitmap cackeBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                                headImg.setImageBitmap(cackeBitmap);
                                                Toast.makeText(getActivity(), "头像修改成功", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getActivity(), "修改头像失败：" + e.getErrorCode() +
                                                        ":" + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), "修改头像失败：" + e.getErrorCode() +
                                            ":" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
            break;
            case R.id.setting_fragment_guide_quit: {
                //TODO 点击退出回到登录界面
                getActivity().finish();
            }
            break;
            case R.id.setting_fragment_guide_change_nickname: {
                //TODO 点击修改昵称
                final Dialog changeDialog = new Dialog(getActivity());
                changeDialog.setTitle("修改昵称");
                View cv = LayoutInflater.from(getActivity()).inflate(R.layout.setting_change_nickname_dialog, null);
                changeDialog.setContentView(cv);
                TextView okTv = cv.findViewById(R.id.setting_ok_change_nick_btn);
                TextView cancelTv = cv.findViewById(R.id.setting_cancel_change_nick_btn);
                final EditText newNickNameEt = cv.findViewById(R.id.setting_new_nickname_et);
                setEditTextInputSpace(newNickNameEt);
                setEditTextInputSpeChat(newNickNameEt);
                okTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String newNickName = newNickNameEt.getText().toString();
                        if (newNickName == null || newNickName.equals("")) {
                            Toast.makeText(getActivity(), "请输入正确的昵称格式！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        User tempUser = new User();
                        tempUser.setObjectId(user.getObjectId());
                        tempUser.setNickName(newNickName);
                        tempUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    nickNameTv.setText(newNickName);
                                    changeDialog.dismiss();
                                    Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "修改昵称失败！请重试" + e.getErrorCode() +
                                            e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                cancelTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeDialog.dismiss();
                    }
                });
                changeDialog.show();
            }
            break;
        }

    }


    /**
     * 禁止EditText输入空格和换行符
     *
     * @param editText EditText输入框
     */
    public static void setEditTextInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText EditText输入框
     */
    public static void setEditTextInputSpeChat(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }


}
