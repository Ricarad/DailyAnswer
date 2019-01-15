package com.ricarad.app.dailyanswer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.adapter.PostAdapter;
import com.ricarad.app.dailyanswer.model.Post;
import com.ricarad.app.dailyanswer.model.Topic;
import com.ricarad.app.dailyanswer.model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class PostsActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView title_tv, collect_tv;
    private ListView posts_lv;
    private ImageView collect_iv, back_iv;
    private LinearLayout collect_ll, add_ll;

    private Topic topic;
    private User mUser;
    private List<Post>  postList;
    private PostAdapter postAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        topic = (Topic)getIntent().getSerializableExtra("topic");
        mUser = (User)getIntent().getSerializableExtra("user");
        fetchViews();
        title_tv.setText(topic.getTitle());
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);
        posts_lv.setAdapter(postAdapter);
        fetchPosts();
        checkCollect();
    }

    private boolean checkCollect(){
        return false;
    }

    private void collect(){

    }

    private void fetchPosts(){
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.setLimit(50);
        query.addWhereEqualTo("topic", topic);
        query.order("createdAt");
        query.include("author, topic");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null){
                    if (list.isEmpty()){
                    }else{
                        postList.clear();
                        postList.addAll(list);
                        postAdapter.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(PostsActivity.this, "获取帖子失败,"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.posts_back_iv:
                finish();
                break;
            case R.id.posts_collect_ll:
                collect();
                break;
        }
    }

    private void fetchViews(){
        title_tv = findViewById(R.id.posts_title_tv);
        collect_tv = findViewById(R.id.posts_collect_tv);
        posts_lv = findViewById(R.id.posts_posts_lv);
        collect_iv = findViewById(R.id.posts_collect_iv);
        collect_ll = findViewById(R.id.posts_collect_ll);
        add_ll = findViewById(R.id.posts_add_ll);
        back_iv = findViewById(R.id.posts_back_iv);

        back_iv.setOnClickListener(this);
        collect_ll.setOnClickListener(this);
        add_ll.setOnClickListener(this);
    }


}
