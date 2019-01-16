package com.ricarad.app.dailyanswer.activity;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class SettingMyCollectionActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView topics_lv;
    private TextView msg_tv;
    private List<Topic> topicList;
    private TopicAdapter topicAdapter;
    private User mUser;

    private ImageView exit_iv;

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

//    private ListView setting_my_collection_lv;
//    private ImageView setting_my_collection_back_iv;
//    private User user;
//
//
//    private ArrayList<Topic> collectedTopicList = new ArrayList<>();
//    private CollectionAdapter collectionAdapter;
//    //onCreate方法
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_setting_my_collection);
//        Bmob.initialize(this, BMOBAPPKEY);
//        user = (User) getIntent().getSerializableExtra("user");
//        setting_my_collection_back_iv = findViewById(R.id.setting_my_collection_back);
//        setting_my_collection_lv = findViewById(R.id.setting_my_collection_lv);
//        //ListView 匿名内部类
//        setting_my_collection_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                Intent intent = new Intent(SettingMyCollectionActivity.this, PostsActivity.class);
//                intent.putExtra("topic", collectedTopicList.get(i));
//                intent.putExtra("user", user);
//                startActivity(intent);
//            }
//        });
//        setting_my_collection_back_iv.setOnClickListener(this);
//        initCollectionList();
//    }
//
//    //监听事件
//    @Override
//    public void onClick(View view) {
//        switch(view.getId()) {
//            case R.id.setting_my_collection_back: {
//                finish();
//            }break;
//        }
//    }
//
//
//    //下载Bomb文件
//    private void downloadFile(BmobFile file){
//        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
//        File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
//        file.download(saveFile, new DownloadFileListener() {
//            @Override
//            public void onProgress(Integer integer, long l) {
//
//            }
//
//            @Override
//            public void done(String savePath,BmobException e) {
//                if(e==null){
//                    Toast.makeText(SettingMyCollectionActivity.this,"下载成功,保存路径:"+SettingMyCollectionActivity.this.getApplicationContext().getCacheDir()+"/bmob/",Toast.LENGTH_LONG).show();
//
//                }else{
//                   Toast.makeText(SettingMyCollectionActivity.this,"下载失败："+e.getErrorCode()+","+e.getMessage(),Toast.LENGTH_LONG);
//                }
//            }
//
//
//
//        });
//    }
//    //查询user收藏的主题信息
//    public void initCollectionList() {
//        BmobQuery<Topic> query = new BmobQuery<Topic>();
//        query.addWhereRelatedTo("collectedTopics",new BmobPointer(user));
//        query.findObjects(new FindListener<Topic>() {
//            @Override
//            public void done(List<Topic> list, BmobException e) {
//                if (e == null){
//                    if (list.size() != 0){
//                        for (Topic topic : list){
//                            collectedTopicList.add(topic);
//                        }
//
//                        collectionAdapter = new CollectionAdapter(SettingMyCollectionActivity.this,collectedTopicList);
//                        setting_my_collection_lv.setAdapter(collectionAdapter);
//
//                    }
//                }else {
//                    Toast.makeText(SettingMyCollectionActivity.this,
//                            "初始化收藏列表失败！失败原因"+e.getErrorCode()+":"+e.getMessage(),Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

}
