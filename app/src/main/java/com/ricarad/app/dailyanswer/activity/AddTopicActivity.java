package com.ricarad.app.dailyanswer.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.entity.Result;
import com.qingmei2.rximagepicker.ui.SystemImagePicker;
import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.common.PostUtil;
import com.ricarad.app.dailyanswer.model.Post;
import com.ricarad.app.dailyanswer.model.Topic;
import com.ricarad.app.dailyanswer.model.User;


import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import jp.wasabeef.richeditor.RichEditor;

public class AddTopicActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{
    Button commit_btn;
    EditText title_et;
    RichEditor content_re;
    ImageView bold_iv, italic_iv, underline_iv, img_iv, undo_iv, redo_iv, exit_iv;
    SystemImagePicker imagePicker;

    boolean isBold = false, isItalic = false, isUnderline = false;
    User mUser;
    String postId = null;
    Post post;
    Topic topic;
    boolean continueFlag;
    boolean hasFailed;

    public static String POST_TAG = "Add Topic ========>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        //获得当前登录的用户
        Intent intent = getIntent();
        mUser = (User)intent.getSerializableExtra("user");
        //初始化样式
        fetchViews();
        setRichEditorStyles();
        //设置监听器
        commit_btn.setOnTouchListener(this);
        commit_btn.setOnClickListener(this);
        bold_iv.setOnClickListener(this);
        italic_iv.setOnClickListener(this);
        underline_iv.setOnClickListener(this);
        undo_iv.setOnClickListener(this);
        redo_iv.setOnClickListener(this);
        img_iv.setOnClickListener(this);
        exit_iv.setOnClickListener(this);
        //实例化图片选择器
        imagePicker = RxImagePicker.INSTANCE.create();
    }

    private void fetchViews(){
        commit_btn = findViewById(R.id.topic_commit_btn);
        title_et = findViewById(R.id.topic_title_et);
        content_re = findViewById(R.id.topic_content_re);
        bold_iv = findViewById(R.id.topic_bold_iv);
        italic_iv = findViewById(R.id.topic_italic_iv);
        underline_iv = findViewById(R.id.topic_underline_iv);
        undo_iv = findViewById(R.id.topic_undo_iv);
        redo_iv = findViewById(R.id.topic_redo_iv);
        img_iv = findViewById(R.id.topic_img_iv);
        exit_iv = findViewById(R.id.topic_exit_iv);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            //发布按钮变色
            case R.id.topic_commit_btn:
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.topic_bold_iv:
                pickBold();
                break;
            case R.id.topic_italic_iv:
                pickItalic();
                break;
            case R.id.topic_underline_iv:
                pickUnderline();
                break;
            case R.id.topic_undo_iv:
                content_re.undo();
                break;
            case R.id.topic_redo_iv:
                content_re.redo();
                break;
            case R.id.topic_img_iv:
                insertOneImg();//选择图片插入帖子内容
                break;
            case R.id.topic_commit_btn:
                commitTopic();
                break;
            case R.id.topic_exit_iv:
                askExit();
                break;
        }
    }

    private void commitTopic() {
        Log.i(POST_TAG, "2");
        final String title = title_et.getText().toString();
        if (title.isEmpty()){
            showErrMessage("标题不能为空");
            return;
        }
        //发表帖子
        final String content = content_re.getHtml();
        if (content == null || content.isEmpty()){
            showErrMessage("内容不能为空");
            return;
        }
        post = new Post();
        post.setAuthor(mUser);
        post.setReplyCount(0);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("请稍等");
        pd.show();
        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    postId = s;Log.i(POST_TAG, "1");

                    topic = new Topic();
                    topic.setTitle(title);
                    topic.setHostPostId(postId);
                    topic.setAuthor(mUser);
                    topic.setReplyCount(0);
                    topic.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {

                                Log.i(POST_TAG, "4");
                                final PostUtil postUtil = new PostUtil(AddTopicActivity.this, postId);
                                String ripeHtml = postUtil.getHtml(content);
                                if (ripeHtml == null){
                                    //上传图片失败，直接结束
                                    pd.dismiss();
                                    showErrMessage("图片上传失败，请检查网络");
                                    return;
                                }
                                post.setContent(ripeHtml);
                                post.setTopic(topic);
                                Log.i(POST_TAG, "5");
                                post.update(postId, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        Log.i(POST_TAG, "6");
                                        pd.dismiss();
                                        if (e == null) {
                                            BmobRelation relation = new BmobRelation();
                                            relation.add(post);
                                            mUser.setPublishedPosts(relation);
                                            mUser.update(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null){
                                                        Toast.makeText(AddTopicActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }else{
                                                        showErrMessage("发布失败，请检查网络");
                                                    }
                                                }
                                            });
                                        } else {
                                            showErrMessage("发布失败，请检查网络");
                                        }
                                    }
                                });
                            } else {
                                Log.i(POST_TAG, "3");
                                pd.dismiss();
                                showErrMessage("发布失败，请检查网络");
                            }
                        }
                    });
                }else {
                    pd.dismiss();
                    showErrMessage("发布失败，请检查网络");
                }
            }
        });
    }

    private void showErrMessage(String msg){
//        Snackbar snackbar =  Snackbar.make( findViewById(R.id.topic_root_ll), msg, Snackbar.LENGTH_LONG);
//        snackbar.setActionTextColor(getResources().getColor(R.color.colorPostErr));
//        snackbar.show();
        Toast.makeText(AddTopicActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void insertOneImg() {
        if (!content_re.isFocused())
            content_re.focusEditor();
        try{
            imagePicker.openGallery(this).subscribe(new io.reactivex.functions.Consumer<Result>() {
                @Override
                public void accept(Result result) throws Exception {
                    content_re.insertImage("file://" + result.getUri().getPath(), "加载中");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void pickBold(){
        if(isBold){
            isBold = false;
            content_re.setBold();
            bold_iv.setImageResource(R.drawable.editor_bold_btn_off);
        }else{
            isBold = true;
            content_re.setBold();
            bold_iv.setImageResource(R.drawable.editor_bold_btn_on);
        }
    }

    private void pickItalic(){
        if(isItalic){
            isItalic = false;
            content_re.setItalic();
            italic_iv.setImageResource(R.drawable.editor_italic_btn_off);
        }else{
            isItalic = true;
            content_re.setItalic();
            italic_iv.setImageResource(R.drawable.editor_italic_btn_on);
        }
    }

    private void pickUnderline(){
        if(isUnderline){
            isUnderline = false;
            content_re.setUnderline();
            underline_iv.setImageResource(R.drawable.editor_underline_btn_off);
        }else{
            isUnderline = true;
            content_re.setUnderline();
            underline_iv.setImageResource(R.drawable.editor_underline_btn_on);
        }
    }

    private void setRichEditorStyles(){
        content_re.setBackgroundColor(Color.parseColor("#fcfcfc"));
        content_re.setEditorFontSize(22);
        content_re.setEditorFontColor(Color.parseColor("#121212"));
        content_re.setPlaceholder("尽情发挥吧...");
        content_re.setPadding(3,3,3,3);
    }

    private void askExit() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setIcon(android.R.drawable.stat_sys_warning);
        dlg.setMessage("已编辑的内容不会被保存，确定要退出吗");
        dlg.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dlg.setNegativeButton("取消", null);
        dlg.show();
    }
}
