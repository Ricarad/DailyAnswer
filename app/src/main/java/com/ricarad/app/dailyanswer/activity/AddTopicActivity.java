package com.ricarad.app.dailyanswer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.model.User;

import jp.wasabeef.richeditor.RichEditor;

public class AddTopicActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{
    Button commit_btn;
    EditText title_et;
    RichEditor content_re;
    ImageView bold_iv, italic_iv, underline_iv, img_iv, undo_iv, redo_iv;

    boolean isBold = false, isItalic = false, isUnderline = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        //获得当前登录的用户
        Intent intent = getIntent();
        User user = (User)intent.getSerializableExtra("user");
        //初始化样式
        fetchViews();
        setRichEditorStyles();
        //设置监听器
        commit_btn.setOnTouchListener(this);
        bold_iv.setOnClickListener(this);
        italic_iv.setOnClickListener(this);
        underline_iv.setOnClickListener(this);
        undo_iv.setOnClickListener(this);
        redo_iv.setOnClickListener(this);
        img_iv.setOnClickListener(this);
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
                insertOneImg();
                break;
        }
    }

    private void insertOneImg() {

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

}
