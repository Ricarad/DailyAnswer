package com.ricarad.app.dailyanswer.activity;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.adapter.PostAdapter;
import com.ricarad.app.dailyanswer.adapter.TopicAdapter;
import com.ricarad.app.dailyanswer.model.Post;
import com.ricarad.app.dailyanswer.model.Topic;
import com.ricarad.app.dailyanswer.model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SettingMyPostActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView posts_lv;
    private TextView msg_tv;
    private List<Post> postList;
    private PostAdapter postAdapter;
    private User mUser;

    private ImageView exit_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting_my_post);
        //Bmob.initialize(this, BMOBAPPKEY);

        posts_lv = findViewById(R.id.setting_my_post_lv);
        msg_tv = findViewById(R.id.setting_my_post_hint_tv);
        exit_iv = findViewById(R.id.setting_my_post_exit_iv);

        mUser = (User)getIntent().getSerializableExtra("user");
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList, mUser);
        posts_lv.setAdapter(postAdapter);
        fetchAllPosts();//获得用户收藏的帖子并显示出来

        //设置监听
        exit_iv.setOnClickListener(this);
    }

    private void fetchAllPosts() {
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.setLimit(50);
        query.addWhereRelatedTo("publishedPosts", new BmobPointer(mUser));
        query.order("-updatedAt");
        query.include("author");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null){
                    if (list.isEmpty()){
                        posts_lv.setVisibility(View.GONE);
                        msg_tv.setVisibility(View.VISIBLE);
                        //Toast.makeText(SettingMyCollectionActivity.this, "你tm还没有帖子", Toast.LENGTH_SHORT).show();
                    }else{
                        posts_lv.setVisibility(View.VISIBLE);
                        msg_tv.setVisibility(View.GONE);
                        postList.clear();
                        postList.addAll(list);
                        postAdapter.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(SettingMyPostActivity.this, "获取帖子失败,"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchAllPosts();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_my_post_exit_iv:
                finish();
                break;
        }
    }


}
