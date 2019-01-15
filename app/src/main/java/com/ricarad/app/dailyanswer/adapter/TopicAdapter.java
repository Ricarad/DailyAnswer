package com.ricarad.app.dailyanswer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.activity.PostsActivity;
import com.ricarad.app.dailyanswer.common.ViewUtil;
import com.ricarad.app.dailyanswer.model.Topic;
import com.ricarad.app.dailyanswer.model.User;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

import static com.ricarad.app.dailyanswer.activity.AddTopicActivity.POST_TAG;

/**
 * Created by root on 2019-1-14.
 */

public class TopicAdapter extends BaseAdapter {
    private Context mContext;
    private List<Topic> topicList;
    private User mUser;

    public TopicAdapter(Context mContext, List<Topic> topicList, User mUser) {
        this.mContext = mContext;
        this.topicList = topicList;
        this.mUser = mUser;
    }

    @Override
    public int getCount() {
        return topicList.size();
    }

    @Override
    public Object getItem(int position) {
        return topicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = LayoutInflater.from(mContext).from(mContext).inflate(R.layout.discuss_topic_list_item, null);
        final ImageView potrait_iv = view.findViewById(R.id.discuss_item_potrait_iv);
        TextView nickname_tv = view.findViewById(R.id.discuss_item_nickname_tv);
        TextView time_tv = view.findViewById(R.id.discuss_item_time_tv);
        TextView title_tv = view.findViewById(R.id.discuss_item_title_tv);
        TextView count_tv = view.findViewById(R.id.discuss_item_count_tv);

        //设置头像
        if (topicList.get(position).getAuthor().getUserImg() != null){
            topicList.get(position).getAuthor().getUserImg().download(new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e != null){
                        Toast.makeText(mContext, "加载头像失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        Bitmap bmp = BitmapFactory.decodeFile(s);
                        potrait_iv.setImageBitmap(bmp);
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        }
        //加载昵称
        if (topicList.get(position).getAuthor().getNickName() != null){
            nickname_tv.setText(topicList.get(position).getAuthor().getNickName());
        }else{
            nickname_tv.setText(topicList.get(position).getAuthor().getUsername());
        }
        //设置时间
        time_tv.setText(ViewUtil.genRealativeTime(topicList.get(position).getUpdatedAt()));
        //设置标题
        title_tv.setText(topicList.get(position).getTitle());
        //设置回复数
        count_tv.setText(topicList.get(position).getReplyCount().toString());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostsActivity.class);
                intent.putExtra("topic", topicList.get(position));
                intent.putExtra("user", mUser);
                mContext.startActivity(intent);
            }
        });
        return view;
    }
}
