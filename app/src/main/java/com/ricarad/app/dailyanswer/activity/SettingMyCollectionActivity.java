package com.ricarad.app.dailyanswer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.adapter.CollectionAdapter;
import com.ricarad.app.dailyanswer.fragment.SettingFragment;
import com.ricarad.app.dailyanswer.model.Mycollection;
import com.ricarad.app.dailyanswer.model.Topic;
import com.ricarad.app.dailyanswer.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class SettingMyCollectionActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView setting_my_collection_lv;
    private ImageView setting_my_collection_return_iv;
    private User user;

    //onCreate方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_my_collection);
        user = (User) getIntent().getSerializableExtra("user");
        setting_my_collection_return_iv = findViewById(R.id.setting_my_collection_return);
        setting_my_collection_lv = findViewById(R.id.setting_my_collection_lv);
        QueryCollection();
        Mycollection n1 = new Mycollection(R.drawable.logo,"shy","aaaaa");

        List<Mycollection> data = new ArrayList<Mycollection>();
        data.add(n1);
        CollectionAdapter adapter = new CollectionAdapter(this,data);
        setting_my_collection_lv.setAdapter(adapter);
        setting_my_collection_return_iv.setOnClickListener(this);
    }
    //监听事件
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.setting_my_collection_return: {
                finish();
            }break;
        }
    }


    //下载Bomb文件
    private void downloadFile(BmobFile file){
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        file.download(saveFile, new DownloadFileListener() {


            @Override
            public void onProgress(Integer integer, long l) {

            }

            @Override
            public void done(String savePath,BmobException e) {
                if(e==null){
                    Toast.makeText(SettingMyCollectionActivity.this,"下载成功,保存路径:"+SettingMyCollectionActivity.this.getApplicationContext().getCacheDir()+"/bmob/",Toast.LENGTH_LONG).show();

                }else{
                   Toast.makeText(SettingMyCollectionActivity.this,"下载失败："+e.getErrorCode()+","+e.getMessage(),Toast.LENGTH_LONG);
                }
            }



        });
    }
    //查询user收藏的主题信息
    public void QueryCollection() {
        BmobQuery<User> query = new BmobQuery<User>();
        query.getObject(user.getObjectId(), new QueryListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if(e==null){
                    //获得当前用户收藏的主贴的信息
                    user.getCollectedTopics();


                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }


        });
    }

}
