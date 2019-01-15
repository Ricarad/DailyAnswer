package com.ricarad.app.dailyanswer.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.adapter.CommonAdapter;
import com.ricarad.app.dailyanswer.model.Question;
import com.ricarad.app.dailyanswer.model.User;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.ricarad.app.dailyanswer.common.Constant.ActivityFlag.ACTIVITY_FLAG;
import static com.ricarad.app.dailyanswer.common.Constant.ActivityFlag.MISTAKE_ACTIVITY_CODE;
import static com.ricarad.app.dailyanswer.common.Constant.ActivityFlag.RECORD_ACTIVITY_CODE;
import static com.ricarad.app.dailyanswer.common.Constant.USER;

public class RecordActivity extends Activity implements View.OnClickListener {

    @ViewInject(R.id.record_question_lv)
    private ListView recordListView;
    @ViewInject(R.id.record_back_iv)
    private ImageView back;
    @ViewInject(R.id.record_edit_iv)
    private ImageView edit;
    @ViewInject(R.id.record_empty_tv)
    private TextView emptyTv;

    private ArrayList<Question> questionList = new ArrayList<>();
    private User user;
    private CommonAdapter commonAdapter;
    private boolean isEdit = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_record);
        x.view().inject(this);
        edit.setOnClickListener(this);
        back.setOnClickListener(this);
        user = (User) getIntent().getSerializableExtra(USER);
        if (user == null) {
            finish();
            Toast.makeText(RecordActivity.this, "获取用户数据失败，请重试", Toast.LENGTH_SHORT).show();
        }
        initQuestionList(); //初始化问题列表
        recordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RecordActivity.this,ShowQuestionActivity.class);
                intent.putExtra(ACTIVITY_FLAG,RECORD_ACTIVITY_CODE);
                intent.putExtra(USER,user);
                intent.putExtra("questionList",questionList);
                intent.putExtra("currentIndex",position);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_back_iv:{
                isEdit = true;
                edit.setBackground(this.getResources().getDrawable(R.drawable.edit_mistake));
                CommonAdapter.isEditMode = false;
                commonAdapter.notifyDataSetChanged();
                finish();
            }break;
            case R.id.record_edit_iv: {
                if (!isEdit) {
                    //删除所选择的问题
                    isEdit = true;
                    edit.setBackground(this.getResources().getDrawable(R.drawable.edit_mistake));
                    CommonAdapter.isEditMode = false;
                    commonAdapter.notifyDataSetChanged();
                    final List<Question> deleteList = commonAdapter.getDeletes();
                    if (deleteList.size() != 0) {
                        BmobRelation deleteRelation = new BmobRelation();
                        Log.i("TGA","删除数目"+deleteList.size()+"");
                        for (Question question : deleteList) {
                            deleteRelation.remove(question);
                        }
                        user.setAnswerQuestion(deleteRelation);
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    questionList.removeAll(deleteList);
                                    deleteList.clear();
                                    commonAdapter.setDeleteQuestions(deleteList);
                                    commonAdapter.notifyDataSetChanged();
                                    Toast.makeText(RecordActivity.this, "删除成功！",Toast.LENGTH_SHORT).show();
                                    if (questionList.size() == 0){
                                        // 当没有错题时，显示另一个界面
                                        recordListView.setVisibility(View.GONE);
                                        emptyTv.setVisibility(View.VISIBLE);
                                        edit.setClickable(false);
                                    }
                                } else {
                                    Toast.makeText(RecordActivity.this, "删除失败！失败原因"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                } else {
                    //显示checkBox
                    isEdit = false;
                    edit.setBackground(this.getResources().getDrawable(R.drawable.edit_press_mistake));
                    CommonAdapter.isEditMode = true;
                    commonAdapter.notifyDataSetChanged();
                }
            }
            break;
        }
    }

    private void initQuestionList() {
        //查询该用户做错的所有题目
        BmobQuery<Question> query = new BmobQuery<>();
        query.addWhereRelatedTo("answerQuestion", new BmobPointer(user));
        query.findObjects(new FindListener<Question>() {
            @Override
            public void done(List<Question> list, BmobException e) {
                if (e == null) {
                    for (Question question : list) {
                        questionList.add(question);
                    }
                    if (questionList.size() == 0){
                        emptyTv.setVisibility(View.VISIBLE);
                        recordListView.setVisibility(View.GONE);
                        edit.setClickable(false);
                    }else {
                        commonAdapter = new CommonAdapter(RecordActivity.this,R.layout.common_question_list_item, questionList);
                        recordListView.setAdapter(commonAdapter);
                    }
                } else {
                    Toast.makeText(RecordActivity.this, "更新练习记录列表失败，失败原因" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
