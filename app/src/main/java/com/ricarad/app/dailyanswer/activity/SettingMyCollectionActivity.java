package com.ricarad.app.dailyanswer.activity;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.adapter.CollectionAdapter;
import com.ricarad.app.dailyanswer.adapter.TopicAdapter;
import com.ricarad.app.dailyanswer.model.Topic;
import com.ricarad.app.dailyanswer.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

import static com.ricarad.app.dailyanswer.common.Constant.BMOBAPPKEY;

public class SettingMyCollectionActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    private ListView topics_lv;
    private TextView msg_tv;
    private List<Topic> topicList;
    private TopicAdapter topicAdapter;
    private User mUser;

    private ImageView exit_iv;

    private static final int SCROLL_MIN_DISTANCE = 80;// 移动最小距离
    private GestureDetector mygesture;//手势探测器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting_my_collection);
        //Bmob.initialize(this, BMOBAPPKEY);

        topics_lv = findViewById(R.id.setting_my_collection_lv);
        msg_tv = findViewById(R.id.setting_my_collection_hint_tv);
        exit_iv = findViewById(R.id.setting_my_collection_exit_iv);

        mUser = (User)getIntent().getSerializableExtra("user");
        topicList = new ArrayList<>();
        topicAdapter = new TopicAdapter(this, topicList, mUser);
        topics_lv.setAdapter(topicAdapter);
        fetchAllTopics();//获得用户收藏的帖子并显示出来

        //设置监听
        exit_iv.setOnClickListener(this);
        //设置点击进入贴吧
        topics_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SettingMyCollectionActivity.this, PostsActivity.class);
                intent.putExtra("topic", topicList.get(position));
                intent.putExtra("user", mUser);
                startActivity(intent);
            }
        });
        mygesture = new GestureDetector(this,myGestureListener);
    }

    private void fetchAllTopics() {
        BmobQuery<Topic> query = new BmobQuery<Topic>();
        query.setLimit(50);
        query.addWhereRelatedTo("collectedTopics", new BmobPointer(mUser));
        query.order("-updatedAt");
        query.include("author");
        query.findObjects(new FindListener<Topic>() {
            @Override
            public void done(List<Topic> list, BmobException e) {
                if (e == null){
                    if (list.isEmpty()){
                        topics_lv.setVisibility(View.GONE);
                        msg_tv.setVisibility(View.VISIBLE);
                        //Toast.makeText(SettingMyCollectionActivity.this, "你tm还没有收藏帖子", Toast.LENGTH_SHORT).show();
                    }else{
                        topics_lv.setVisibility(View.VISIBLE);
                        msg_tv.setVisibility(View.GONE);
                        topicList.clear();
                        topicList.addAll(list);
                        topicAdapter.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(SettingMyCollectionActivity.this, "获取帖子失败,"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchAllTopics();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_my_collection_exit_iv:
                finish();
            break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mygesture.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mygesture.onTouchEvent(ev)){
            return mygesture.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
    private GestureDetector.OnGestureListener myGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > SCROLL_MIN_DISTANCE) {
                finish();
            }
            return false;
        }
    };

}
