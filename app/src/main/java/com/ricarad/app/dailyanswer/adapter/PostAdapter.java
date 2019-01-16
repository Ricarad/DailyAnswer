package com.ricarad.app.dailyanswer.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.activity.PostsActivity;
import com.ricarad.app.dailyanswer.common.ViewUtil;
import com.ricarad.app.dailyanswer.model.PComment;
import com.ricarad.app.dailyanswer.model.Post;
import com.ricarad.app.dailyanswer.model.Topic;
import com.ricarad.app.dailyanswer.model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK;

/**
 * Created by root on 2019-1-15.
 */

public class PostAdapter extends BaseAdapter {
    Context mContext;
    List<Post> postList;
    User mUser;

    public PostAdapter(Context mContext, List<Post> postList, User user) {
        this.mContext = mContext;
        this.postList = postList;
        this.mUser = user;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = LayoutInflater.from(mContext).from(mContext).inflate(R.layout.posts_post_list_item, null);
        final ImageView potrait_iv = view.findViewById(R.id.post_item_potrait_iv);
        TextView nickname_tv = view.findViewById(R.id.post_item_nickname_tv);
        TextView time_tv = view.findViewById(R.id.post_item_time_tv);
        final TextView count_tv = view.findViewById(R.id.post_item_review_tv);
        WebView webView = view.findViewById(R.id.post_item_content_wv);

        //设置头像
        if (postList.get(position).getAuthor().LOCALPATH.equals("")){
            if (postList.get(position).getAuthor().getUserImg() != null){
                postList.get(position).getAuthor().getUserImg().download(new DownloadFileListener() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e != null){
                            Toast.makeText(mContext, "加载头像失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            Bitmap bmp = BitmapFactory.decodeFile(s);
                            potrait_iv.setImageBitmap(bmp);
                            postList.get(position).getAuthor().LOCALPATH = s;
                        }
                    }

                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                });
            }
        }else{
            Bitmap bmp = BitmapFactory.decodeFile(postList.get(position).getAuthor().LOCALPATH);
            potrait_iv.setImageBitmap(bmp);
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
        webSettings.setCacheMode(LOAD_CACHE_ELSE_NETWORK);
        webView.setBackgroundColor(0);
        webView.loadData(postList.get(position).getContent(), "text/html; charset=UTF-8","utf-8");

        count_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog cdlg = new Dialog(mContext);
                cdlg.setTitle("文明上网，理性发言");
                final View cv = LayoutInflater.from(mContext).inflate(R.layout.comment_dlg_layout, null);
                cdlg.setContentView(cv);
                final Button ccommit_btn = cv.findViewById(R.id.comment_dlg_commit_btn);
                final EditText ccontent_et = cv.findViewById(R.id.comment_dlg_content_et);
                //从数据库获得coments
                BmobQuery<PComment> query = new BmobQuery<PComment>();
                query.addWhereEqualTo("post", new BmobPointer(postList.get(position)));
                query.include("author");
                query.order("-createdAt");
                query.findObjects(new FindListener<PComment>() {
                    @Override
                    public void done(List<PComment> list, BmobException e) {
                        if (e == null){
                            //没问题
                            if (!list.isEmpty()){
                                //已经有评论了
                                List<PComment> commentList = new ArrayList<>();
                                commentList.addAll(list);
                                CommentAdapter commentAdapter = new CommentAdapter(mContext, commentList);
                                ListView comment_lv = cv.findViewById(R.id.comment_dlg_lv);
                                comment_lv.setAdapter(commentAdapter);
                                ccontent_et.setHint("输入评论内容");
                            }else{ //还没有评论
                                ccontent_et.setHint("快来抢沙发吧~");
                            }//给提交设置监听
                            ccommit_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //提交评论
                                    if (!ccontent_et.getText().toString().isEmpty()){
                                        //向数据库提交
                                        PComment pComment = new PComment();
                                        pComment.setAuthor(mUser);
                                        pComment.setContent(ccontent_et.getText().toString());
                                        pComment.setPost(postList.get(position));
                                        pComment.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null){
                                                    Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                                                    final Post tmpPost = postList.get(position);
                                                    tmpPost.setReplyCount(tmpPost.getReplyCount() + 1);
                                                    postList.set(position, tmpPost);
                                                    tmpPost.update(new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            if (e == null){
                                                                Topic tmpTopic = postList.get(position).getTopic();
                                                                tmpTopic.setReplyCount(tmpTopic.getReplyCount() + 1);
                                                                tmpTopic.update(new UpdateListener() {
                                                                    @Override
                                                                    public void done(BmobException e) {
                                                                        if (e != null){
                                                                            Toast.makeText(mContext, "检测到网络波动", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                    notifyDataSetChanged();
                                                    cdlg.dismiss();
                                                }else{
                                                    Toast.makeText(mContext, "提交失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(mContext, "评论不能为空", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{//获取数据失败
                            Toast.makeText(mContext, "获取评论失败", Toast.LENGTH_SHORT).show();
                            cdlg.dismiss();
                        }
                    }
                });
                cdlg.show();
            }
        });

        return view;
    }
}
