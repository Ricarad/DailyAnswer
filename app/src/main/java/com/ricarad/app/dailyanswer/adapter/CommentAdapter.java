package com.ricarad.app.dailyanswer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.model.PComment;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by root on 2019-1-15.
 */

public class CommentAdapter extends BaseAdapter {
    private Context mContext;
    private List<PComment> commentList;
    String[] rainbow =  {"#ffd4d4", "#fff1d4", "#b5ffe3", "#ddb5ff", "#ffb5b5"};

    public CommentAdapter(Context mContext, List<PComment> commentList) {
        this.mContext = mContext;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = LayoutInflater.from(mContext).from(mContext).inflate(R.layout.comment_list_item, null);
        view.setBackgroundColor(Color.parseColor(rainbow[position % rainbow.length]));
        final ImageView potrait_iv = view.findViewById(R.id.comment_item_potrait_iv);
        TextView nickname_tv = view.findViewById(R.id.comment_item_nickname_tv);
        TextView time_tv = view.findViewById(R.id.comment_item_time_tv);
        TextView content_tv = view.findViewById(R.id.comment_item_content_tv);

        //设置头像
        if (commentList.get(position).getAuthor().getUserImg() != null){
            Glide.with(mContext).load(Uri.parse(commentList.get(position).getAuthor().getUserImg().
                    getFileUrl())).placeholder(R.drawable.logo).into(potrait_iv);
        }
//        if (commentList.get(position).getAuthor().LOCALPATH.equals("")){
//            if (commentList.get(position).getAuthor().getUserImg() != null){
//                commentList  .get(position).getAuthor().getUserImg().download(new DownloadFileListener() {
//                    @Override
//                    public void done(String s, BmobException e) {
//                        if (e != null){
//                            Toast.makeText(mContext, "加载头像失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }else{
//                            Bitmap bmp = BitmapFactory.decodeFile(s);
//                            potrait_iv.setImageBitmap(bmp);
//                            commentList.get(position).getAuthor().LOCALPATH = s;
//                        }
//                    }
//
//                    @Override
//                    public void onProgress(Integer integer, long l) {
//
//                    }
//                });
//            }
//        }else{
//            Bitmap bmp = BitmapFactory.decodeFile(commentList.get(position).getAuthor().LOCALPATH);
//            potrait_iv.setImageBitmap(bmp);
//        }
        //加载昵称
        if (commentList.get(position).getAuthor().getNickName() != null){
            nickname_tv.setText(commentList.get(position).getAuthor().getNickName());
        }else{
            nickname_tv.setText(commentList.get(position).getAuthor().getUsername());
        }
        //设置时间
        time_tv.setText(commentList.get(position).getCreatedAt());
        //设置内容
       content_tv.setText(commentList.get(position).getContent());
       return view;
    }
}
