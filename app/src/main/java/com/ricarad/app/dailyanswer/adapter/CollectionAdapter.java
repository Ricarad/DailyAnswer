package com.ricarad.app.dailyanswer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.model.Topic;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by shy on 2019/1/14.
 */

public class CollectionAdapter extends BaseAdapter {
    private Context mContext;
    private List<Topic> topicList;

    public CollectionAdapter(Context mContext, List<Topic> topicList) {
        this.mContext = mContext;
        this.topicList = topicList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.collect_topic_list_item,null);
        final ImageView mycollect_list_item_photo = view.findViewById(R.id.mycollect_list_item_photo);
        TextView mycollect_list_item_nickname = view.findViewById(R.id.mycollect_list_item_username);
        TextView mycollect_list_item_title = view.findViewById(R.id.mycollect_list_item_title);

        //设置头像
        if (topicList.get(position).getAuthor().getUserImg() != null){
            topicList.get(position).getAuthor().getUserImg().download(new DownloadFileListener() {
                @Override
                public void done(String imgPath, BmobException e) {
                    if (e != null){
                        Toast.makeText(mContext, "加载头像失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        Bitmap bmp = BitmapFactory.decodeFile(imgPath);
                        mycollect_list_item_photo.setImageBitmap(bmp);
                    }
                }
                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        }
        //加载昵称
        if (topicList.get(position).getAuthor().getNickName() != null){
            mycollect_list_item_nickname.setText(topicList.get(position).getAuthor().getNickName());
        }else{
            mycollect_list_item_nickname.setText(topicList.get(position).getAuthor().getUsername());
        }

        //设置标题
        mycollect_list_item_title.setText(topicList.get(position).getTitle());
        return view;
    }
}
