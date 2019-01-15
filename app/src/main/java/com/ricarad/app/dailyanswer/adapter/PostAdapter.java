package com.ricarad.app.dailyanswer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.activity.PostsActivity;
import com.ricarad.app.dailyanswer.common.ViewUtil;
import com.ricarad.app.dailyanswer.model.Post;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by root on 2019-1-15.
 */

public class PostAdapter extends BaseAdapter {
    Context mContext;
    List<Post> postList;

    public PostAdapter(Context mContext, List<Post> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = LayoutInflater.from(mContext).from(mContext).inflate(R.layout.posts_post_list_item, null);
        final ImageView potrait_iv = view.findViewById(R.id.post_item_potrait_iv);
        TextView nickname_tv = view.findViewById(R.id.post_item_nickname_tv);
        TextView time_tv = view.findViewById(R.id.post_item_time_tv);
        TextView count_tv = view.findViewById(R.id.post_item_review_tv);
        WebView webView = view.findViewById(R.id.post_item_content_wv);

        //设置头像
        if (postList.get(position).getAuthor().getUserImg() != null){
            postList.get(position).getAuthor().getUserImg().download(new DownloadFileListener() {
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
        if (postList.get(position).getAuthor().getNickName() != null){
            nickname_tv.setText(postList.get(position).getAuthor().getNickName());
        }else{
            nickname_tv.setText(postList.get(position).getAuthor().getUsername());
        }
        //设置时间
        time_tv.setText(postList.get(position).getCreatedAt());
        //设置回复数
        count_tv.setText("回复(" + postList.get(position).getReplyCount() + ")");
        //设置内容
        WebSettings webSettings = webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDefaultFontSize(23);
        webView.setBackgroundColor(0);
        webView.loadData(postList.get(position).getContent(), "text/html; charset=UTF-8","utf-8");
        return view;
    }
}
