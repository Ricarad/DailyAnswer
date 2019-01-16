package com.ricarad.app.dailyanswer.fragment;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class DiscussFragment extends Fragment implements View.OnClickListener{
    private ListView topics_lv;
    private TextView msg_tv;
    private List<Topic> topicList;
    private List<Topic> bakList;
    private TopicAdapter topicAdapter;
    private EditText input_et;
    private ImageView search_btn;

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
        input_et = getActivity().findViewById(R.id.discuss_input_et);
        search_btn = getActivity().findViewById(R.id.discuss_search_iv);

        add_ll.setOnClickListener(this);
        search_btn.setOnClickListener(this);

        topicList = new ArrayList<>();
        bakList = new ArrayList<>();
        topicAdapter = new TopicAdapter(getContext(), topicList, (User)getActivity().getIntent().getSerializableExtra("user"));
        topics_lv.setAdapter(topicAdapter);
        fetchAllTopics();

        input_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    topicList.clear();
                    topicList.addAll(bakList);
                    topicAdapter.notifyDataSetChanged();
                }
            }
        });
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
                           topicList.clear();  bakList.clear();
                           topicList.addAll(list);  bakList.addAll(list);
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
            case R.id.discuss_search_iv:
                searchIt();
                break;
        }
    }

    private void searchIt() {
        topicList.clear();
        topicList.addAll(bakList);

        String input = input_et.getText().toString();
        if (input.isEmpty()){
            Toast.makeText(getContext(), "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] inputs = input.split(" ");
        boolean hasIt = false;
        Iterator<Topic> it = topicList.iterator();
        List<Topic> results = new ArrayList<>();
        for (String s: inputs){
            while (it.hasNext()){
                Topic tmp = it.next();
                if (tmp.getTitle().contains(s) || tmp.getAuthor().getNickName().contains(s)){
                    results.add(tmp);
                    it.remove();
                }
            }
        }
        if (results.isEmpty())
            Toast.makeText(getContext(), "抱歉，没有找到相关的内容", Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(getContext(), "找到" + results.size() + 1 + "条结果", Toast.LENGTH_SHORT).show();
            topicList.clear();
            topicList.addAll(results);
            search_btn.requestFocus();
        }

        topicAdapter.notifyDataSetChanged();
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
