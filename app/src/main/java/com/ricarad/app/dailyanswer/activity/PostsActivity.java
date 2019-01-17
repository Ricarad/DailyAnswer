package com.ricarad.app.dailyanswer.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.qingmei2.rximagepicker.core.RxImagePicker;
import com.qingmei2.rximagepicker.entity.Result;
import com.qingmei2.rximagepicker.ui.SystemImagePicker;
import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.adapter.PostAdapter;
import com.ricarad.app.dailyanswer.common.PostUtil;
import com.ricarad.app.dailyanswer.model.PComment;
import com.ricarad.app.dailyanswer.model.Post;
import com.ricarad.app.dailyanswer.model.Topic;
import com.ricarad.app.dailyanswer.model.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import jp.wasabeef.richeditor.RichEditor;

import static com.ricarad.app.dailyanswer.activity.AddTopicActivity.POST_TAG;
import static com.ricarad.app.dailyanswer.common.Constant.LFILEPICKER_PATH;
import static com.ricarad.app.dailyanswer.common.Constant.LFILEPICKER_REQUEST_CODE;

public class PostsActivity extends AppCompatActivity implements  View.OnTouchListener,View.OnClickListener {
    private TextView title_tv, collect_tv;
    private ListView posts_lv;
    private ImageView collect_iv, back_iv;
    private LinearLayout collect_ll, add_ll;

    Button commit_btn, leave_btn;
    RichEditor content_re;
    ImageView bold_iv, italic_iv, underline_iv, img_iv, undo_iv, redo_iv, exit_iv;
    SystemImagePicker imagePicker;
    Dialog pickDialog;
    String imgUrl;

    boolean isBold = false, isItalic = false, isUnderline = false;
    Dialog contentDlg;

    private Topic topic;
    private User mUser;
    private Post post;
    private PComment comment;
    private List<Post> postList;
    private PostAdapter postAdapter;

    private static final int SCROLL_MIN_DISTANCE = 80;// 移动最小距离
    private GestureDetector mygesture;//手势探测器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        topic = (Topic) getIntent().getSerializableExtra("topic");
        mUser = (User) getIntent().getSerializableExtra("user");
        fetchViews();
        //实例化图片选择器
        imagePicker = RxImagePicker.INSTANCE.create();
        title_tv.setText(topic.getTitle());
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList, mUser);
        posts_lv.setAdapter(postAdapter);
        fetchPosts();
        checkCollect();
        mygesture = new GestureDetector(this,myGestureListener);
    }

    //提交回帖
    private void commitPost() {
        final String content = content_re.getHtml();
        if (content == null || content.isEmpty()) {
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        post = new Post();
        post.setAuthor(mUser);
        post.setTopic(topic);
        post.setReplyCount(0);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("提交中");
        pd.show();
        post.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i(POST_TAG, "1");
                    final PostUtil postUtil = new PostUtil(PostsActivity.this, post.getObjectId());
                    String ripeHtml = postUtil.getHtml(content);
                    if (ripeHtml == null) {
                        //上传图片失败，直接结束
                        pd.dismiss();
                        showErrMessage("图片上传失败，请检查网络");
                        return;
                    }
                    post.setContent(ripeHtml);
                    Log.i(POST_TAG, "5");
                    post.update(post.getObjectId(), new UpdateListener() {
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
                                        if (e == null) {
                                            topic.setReplyCount(topic.getReplyCount() + 1);
                                            topic.update(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    Toast.makeText(PostsActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                                    pd.dismiss();
                                                    fetchPosts();
                                                    contentDlg.dismiss();
                                                }
                                            });
                                        } else {
                                            showErrMessage("发布失败，请检查网络");
                                        }
                                    }
                                });
                            } else {
                                showErrMessage("发布失败，请检查网络");
                            }
                        }
                    });
                }
            }
        });
    }


    private void showErrMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void addPost() {
        contentDlg = new Dialog(this);
        contentDlg.setCanceledOnTouchOutside(false);
        contentDlg.setTitle("编辑回帖内容");
        View view = LayoutInflater.from(PostsActivity.this).inflate(R.layout.post_dialog_layout, null);
        contentDlg.setContentView(view);

        commit_btn = view.findViewById(R.id.post_dlg_ok_btn);
        content_re = view.findViewById(R.id.post_dlg_content_re);
        bold_iv = view.findViewById(R.id.post_dlg_bold_iv);
        italic_iv = view.findViewById(R.id.post_dlg_italic_iv);
        underline_iv = view.findViewById(R.id.post_dlg_underline_iv);
        undo_iv = view.findViewById(R.id.post_dlg_undo_iv);
        redo_iv = view.findViewById(R.id.post_dlg_redo_iv);
        img_iv = view.findViewById(R.id.post_dlg_img_iv);
        leave_btn = view.findViewById(R.id.post_dlg_leave_btn);
        setRichEditorStyles();;

        commit_btn.setOnClickListener(this);
        bold_iv.setOnClickListener(this);
        italic_iv.setOnClickListener(this);
        underline_iv.setOnClickListener(this);
        undo_iv.setOnClickListener(this);
        redo_iv.setOnClickListener(this);
        img_iv.setOnClickListener(this);
        leave_btn.setOnClickListener(this);
        contentDlg.show();
    }

    private void checkCollect() {
        BmobQuery<Topic> query = new BmobQuery<Topic>();
        query.addWhereRelatedTo("collectedTopics", new BmobPointer(mUser));
        query.findObjects(new FindListener<Topic>() {
            @Override
            public void done(List<Topic> list, BmobException e) {
                if (e == null) {
                    boolean hasIt = false;
                    for (Topic t : list)
                        if (topic.getObjectId().equals(t.getObjectId())) {
                            hasIt = true;
                            break;
                        }
                    if (!hasIt) {
                        //没收藏
                        collect_iv.setImageResource(R.drawable.post_uncollected);
                    } else {
                        //收藏了
                        collect_iv.setImageResource(R.drawable.post_collected);
                        collect_tv.setText("已收藏");
                    }
                } else {
                    Toast.makeText(PostsActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void collect() {
        BmobQuery<Topic> query = new BmobQuery<Topic>();
        query.addWhereRelatedTo("collectedTopics", new BmobPointer(mUser));
        query.findObjects(new FindListener<Topic>() {
            @Override
            public void done(List<Topic> list, BmobException e) {
                if (e == null) {
                    boolean hasIt = false;
                    for (Topic t : list)
                        if (topic.getObjectId().equals(t.getObjectId())) {
                            hasIt = true;
                            break;
                        }
                    if (!hasIt) {
                        //没收藏   现在收藏
                        BmobRelation relation = new BmobRelation();
                        relation.add(topic);
                        mUser.setCollectedTopics(relation);
                        mUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(PostsActivity.this, "已收藏，可在个人中心查看", Toast.LENGTH_SHORT).show();
                                    collect_iv.setImageResource(R.drawable.post_collected);
                                    collect_tv.setText("已收藏");

                                } else {
                                    Toast.makeText(PostsActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        //收藏了  现在取消收藏
                        BmobRelation relation = new BmobRelation();
                        relation.remove(topic);
                        mUser.setCollectedTopics(relation);
                        mUser.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(PostsActivity.this, "已从收藏夹删除", Toast.LENGTH_SHORT).show();
                                    collect_iv.setImageResource(R.drawable.post_uncollected);
                                    collect_tv.setText("收藏");
                                } else {
                                    Toast.makeText(PostsActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(PostsActivity.this, "获取收藏夹失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchPosts() {
        BmobQuery<Post> query = new BmobQuery<Post>();
        query.setLimit(50);
        query.addWhereEqualTo("topic", topic);
        query.order("createdAt");
        query.include("author,topic");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    if (list.isEmpty()) {
                    } else {
                        postList.clear();
                        Iterator<Post> it = list.iterator();
                        while (it.hasNext()){
                            String postContent = it.next().getContent();
                            if (postContent == null || postContent.isEmpty()){
                                it.remove();
                            }
                        }
                        postList.addAll(list);
                        postAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(PostsActivity.this, "获取帖子失败," + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.posts_back_iv:
                finish();
                break;
            case R.id.posts_collect_ll:
                collect();
                break;
            case R.id.posts_add_ll:
                addPost();
                break;

            case R.id.post_dlg_bold_iv:
                pickBold();
                break;
            case R.id.post_dlg_italic_iv:
                pickItalic();
                break;
            case R.id.post_dlg_underline_iv:
                pickUnderline();
                break;
            case R.id.post_dlg_undo_iv:
                content_re.undo();
                break;
            case R.id.post_dlg_redo_iv:
                content_re.redo();
                break;
            case R.id.post_dlg_img_iv:
                insertOneImg();//选择图片插入帖子内容
                break;
            case R.id.post_dlg_ok_btn:
                commitPost();
                break;
            case R.id.post_dlg_leave_btn:
                askExit();
                break;
        }
    }

    private void fetchViews() {
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

    private void askExit() {
        String content = content_re.getHtml();
        if (content != null && !content.isEmpty()){
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setIcon(android.R.drawable.stat_sys_warning);
            dlg.setMessage("已编辑的内容不会被保存，确定要退出吗");
            dlg.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    contentDlg.dismiss();
                }
            });
            dlg.setNegativeButton("取消", null);
            dlg.show();
        }else{
            contentDlg.dismiss();
        }
    }

    private void insertOneImg() {
        if (!content_re.isFocused())
            content_re.focusEditor();
        try{
            //start
            pickDialog = new Dialog(this);
            pickDialog.setTitle("请选择获取图片的方式");
            View pv = LayoutInflater.from(this).inflate(R.layout.pick_img_ways_dialog, null);
            pickDialog.setContentView(pv);
            final RadioGroup pickRg = pv.findViewById(R.id.pick_img_ways_rg);
            final RadioButton pickLocalPathRb = pv.findViewById(R.id.pick_img_from_localpath_rb);
            final RadioButton pickGalleryRb = pv.findViewById(R.id.pick_img_from_gallery_rb);
            pickRg.check(pickLocalPathRb.getId());
            TextView okTv = pv.findViewById(R.id.pick_img_ok_tv);
            TextView cancelTv = pv.findViewById(R.id.pick_img_cancel_tv);
            pickDialog.show();
            okTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectItemId = pickRg.getCheckedRadioButtonId();
                    if (selectItemId == pickLocalPathRb.getId()) {
                        new LFilePicker().withActivity(PostsActivity.this).withRequestCode(LFILEPICKER_REQUEST_CODE)
                                .withTitle("选择本地图片").withMutilyMode(false)
                                .withFileFilter(new String[]{".png", ".jpg", ".jpeg", ".ico", ".PNG", ".JPG", ".JPEG", ".ICO"})
                                .withIconStyle(Constant.ICON_STYLE_BLUE).start();
                        pickDialog.dismiss();
                    } else if (selectItemId == pickGalleryRb.getId()) {
                        imagePicker.openGallery(PostsActivity.this).subscribe(new io.reactivex.functions.Consumer<Result>() {
                            @Override
                            public void accept(Result result) throws Exception {
                                if (result == null){
                                    return;
                                }
                                content_re.insertImage("file://" + result.getUri().getPath(), "加载中");
                                pickDialog.dismiss();
                            }
                        });
                    } else {
                        pickDialog.dismiss();
                    }
                }
            });
            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickDialog.dismiss();
                }
            });
            //end
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //获得图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LFILEPICKER_REQUEST_CODE: {
                if (data == null)
                    return;
                List<String> list = data.getStringArrayListExtra(LFILEPICKER_PATH);
                if (list == null)
                    return;
                if (list.size() > 0) {
                    imgUrl = list.get(0);
                    if (imgUrl == null)
                        return;
                    content_re.insertImage("file://" + imgUrl, "加载中");
                }
            }
        }
    }

    private void pickBold() {
        if (isBold) {
            isBold = false;
            content_re.setBold();
            bold_iv.setImageResource(R.drawable.editor_bold_btn_off);
        } else {
            isBold = true;
            content_re.setBold();
            bold_iv.setImageResource(R.drawable.editor_bold_btn_on);
        }
    }

    private void pickItalic() {
        if (isItalic) {
            isItalic = false;
            content_re.setItalic();
            italic_iv.setImageResource(R.drawable.editor_italic_btn_off);
        } else {
            isItalic = true;
            content_re.setItalic();
            italic_iv.setImageResource(R.drawable.editor_italic_btn_on);
        }
    }

    private void pickUnderline() {
        if (isUnderline) {
            isUnderline = false;
            content_re.setUnderline();
            underline_iv.setImageResource(R.drawable.editor_underline_btn_off);
        } else {
            isUnderline = true;
            content_re.setUnderline();
            underline_iv.setImageResource(R.drawable.editor_underline_btn_on);
        }
    }

    private void setRichEditorStyles() {
        content_re.setBackgroundColor(Color.parseColor("#fcfcfc"));
        content_re.setEditorFontSize(22);
        content_re.setEditorFontColor(Color.parseColor("#121212"));
        content_re.setPlaceholder("尽情发挥吧...");
        content_re.setPadding(3, 3, 3, 3);
        content_re.focusEditor();
    }

    private GestureDetector.OnGestureListener myGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > SCROLL_MIN_DISTANCE) {
                finish();
            }
            return false;
        }
    };
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mygesture.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mygesture.onTouchEvent(ev)){
            return mygesture.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}
