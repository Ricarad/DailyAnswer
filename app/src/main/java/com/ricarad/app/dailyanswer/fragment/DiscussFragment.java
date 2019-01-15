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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.activity.AddTopicActivity;
import com.ricarad.app.dailyanswer.adapter.TopicAdapter;
import com.ricarad.app.dailyanswer.model.Topic;
import com.ricarad.app.dailyanswer.model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class DiscussFragment extends Fragment implements View.OnClickListener{
    private ListView topics_lv;
    private TextView msg_tv;
    private List<Topic> topicList;
    private TopicAdapter topicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discuss_fragment_guide,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout add_ll = getActivity().findViewById(R.id.discuss_add_ll);
        topics_lv = getActivity().findViewById(R.id.discuss_topics_lv);
        msg_tv = getActivity().findViewById(R.id.discuss_msg_tv);

        add_ll.setOnClickListener(this);

        topicList = new ArrayList<>();
        topicAdapter = new TopicAdapter(getContext(), topicList, (User)getActivity().getIntent().getSerializableExtra("user"));
        topics_lv.setAdapter(topicAdapter);
        fetchAllTopics();
    }

    private void fetchAllTopics() {
        BmobQuery<Topic> query = new BmobQuery<Topic>();
        query.setLimit(50);
        query.order("-updatedAt");
        query.include("author");
        query.findObjects(new FindListener<Topic>() {
            @Override
            public void done(List<Topic> list, BmobException e) {
                   if (e == null){
                       if (list.isEmpty()){
                           topics_lv.setVisibility(View.GONE);
                           msg_tv.setVisibility(View.VISIBLE);
                           msg_tv.setText("抱歉，暂时没有帖子，快去发帖吧！");
                       }else{
                           topics_lv.setVisibility(View.VISIBLE);
                           msg_tv.setVisibility(View.GONE);
                           topicList.clear();
                           topicList.addAll(list);
                           topicAdapter.notifyDataSetChanged();
                       }
                   }else{
                       Toast.makeText(getContext(), "获取帖子失败,"+e.getMessage(), Toast.LENGTH_LONG).show();
                   }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded()){
            fetchAllTopics();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.discuss_add_ll:
                goToAddTopic();
                break;
        }
    }

    private void goToAddTopic(){
        Intent intent = new Intent(getContext(), AddTopicActivity.class);

        if (isAdded()) {  //判断Fragment已经依附Activity
            User user = (User)getArguments().getSerializable("user");
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }
}
