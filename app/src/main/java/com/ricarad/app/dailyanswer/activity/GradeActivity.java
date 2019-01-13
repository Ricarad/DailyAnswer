package com.ricarad.app.dailyanswer.activity;

import android.app.Activity;
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
import com.ricarad.app.dailyanswer.adapter.GradeListAdapter;
import com.ricarad.app.dailyanswer.model.Question;
import com.ricarad.app.dailyanswer.model.User;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.ricarad.app.dailyanswer.common.Constant.GradeType.ANSWERTYPE;
import static com.ricarad.app.dailyanswer.common.Constant.GradeType.EXAM_CODE;
import static com.ricarad.app.dailyanswer.common.Constant.GradeType.PRACTICE_CODE;
import static com.ricarad.app.dailyanswer.common.Constant.QuestionResultType.EMPTY;
import static com.ricarad.app.dailyanswer.common.Constant.QuestionResultType.ERROR;
import static com.ricarad.app.dailyanswer.common.Constant.QuestionResultType.RIGHT;
import static com.ricarad.app.dailyanswer.common.Constant.USER;

public class GradeActivity extends Activity implements View.OnClickListener,ListView.OnItemClickListener {

    @ViewInject(R.id.grade_back_iv)
    private ImageView back;
    @ViewInject(R.id.grade_rightratio_tv)
    private TextView rightRatioTv;
    @ViewInject(R.id.grade_totlanum_tv)
    private TextView totalNumberTv;
    @ViewInject(R.id.grade_rightnum_tv)
    private TextView rightNumberTv;
    @ViewInject(R.id.grade_analysis_mistake_tv)
    private TextView analysisMistakesTv;
    @ViewInject(R.id.grade_analysis_all_tv)
    private TextView analysisAllTv;
    @ViewInject(R.id.grade_recomplete_tv)
    private TextView reCompleteTv;
    @ViewInject(R.id.grade_results_lv)
    private ListView questionResultListView;
    private GradeListAdapter gradeListAdapter;
    private ArrayList<Question> questionList;
    private ArrayList<Integer> answerList;
    private ArrayList<Question> rightQuestionList;//回答正确的题目
    private ArrayList<Question> errorQuestionList;//回答错误的题目
    private ArrayList<Question> emptyQuestionList = new ArrayList<>();
    private User user;
    private int answerType = 0;//表明是考试还是练习 0为练习结果 1为考试结果

    private HashMap<Question,Integer> questionResultType = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_grade);
        x.view().inject(this);
        back.setOnClickListener(this);
        reCompleteTv.setOnClickListener(this);
        questionList = (ArrayList<Question>) getIntent().getSerializableExtra("questionList");
        answerList = getIntent().getIntegerArrayListExtra("answerList");
        user = (User) getIntent().getSerializableExtra(USER);
        answerType = getIntent().getIntExtra(ANSWERTYPE,0); //获取当前答题结果的模式，默认是练习
        if (questionList == null || answerList == null || user == null || answerList.size() != questionList.size()) {
            Toast.makeText(this, "数据加载发生错误！", Toast.LENGTH_SHORT).show();
            finish();
        }
        initView();
        gradeListAdapter = new GradeListAdapter(this,questionList,questionResultType);
        questionResultListView.setAdapter(gradeListAdapter);
        questionResultListView.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Question question = questionList.get(position);
    }
    private void initView() {
        errorQuestionList = new ArrayList<>();
        rightQuestionList = new ArrayList<>();
        int totalNumber = questionList.size();
        int rightNumber = 0;
        BmobRelation totalRelation = new BmobRelation();
        for (int i = 0; i < questionList.size(); i++) {
            String rightResult = questionList.get(i).getAnswer();
            totalRelation.add(questionList.get(i));
            int selectId = answerList.get(i);
            switch (selectId) {
                case R.id.answer_Aselect_rb: {
                    if (rightResult.equals("A")) {
                        rightNumber++;
                        rightQuestionList.add(questionList.get(i));
                        questionResultType.put(questionList.get(i),RIGHT);
                    } else {
                        errorQuestionList.add(questionList.get(i));
                        questionResultType.put(questionList.get(i),ERROR);
                    }
                }
                break;
                case R.id.answer_Bselect_rb: {
                    if (rightResult.equals("B")) {
                        rightNumber++;
                        rightQuestionList.add(questionList.get(i));
                        questionResultType.put(questionList.get(i),RIGHT);
                    } else {
                        errorQuestionList.add(questionList.get(i));
                        questionResultType.put(questionList.get(i),ERROR);
                    }
                }
                break;
                case R.id.answer_Cselect_rb: {
                    if (rightResult.equals("C")) {
                        rightNumber++;
                        rightQuestionList.add(questionList.get(i));
                        questionResultType.put(questionList.get(i),RIGHT);
                    } else {
                        errorQuestionList.add(questionList.get(i));
                        questionResultType.put(questionList.get(i),ERROR);
                    }
                }
                break;
                case R.id.answer_Dselect_rb: {
                    if (rightResult.equals("D")) {
                        rightNumber++;
                        rightQuestionList.add(questionList.get(i));
                        questionResultType.put(questionList.get(i),RIGHT);
                    } else {
                        errorQuestionList.add(questionList.get(i));
                        questionResultType.put(questionList.get(i),ERROR);
                    }
                }
                break;
                case -1:{
                    emptyQuestionList.add(questionList.get(i));
                    errorQuestionList.add(questionList.get(i));
                    questionResultType.put(questionList.get(i),EMPTY);
                }break;
            }
        }

        totalNumberTv.setText("总题目数" + totalNumber);
        rightNumberTv.setText("正确题目数" + rightNumber);
        Integer rightRatio = rightNumber * 100 / totalNumber;
        rightRatioTv.setText(rightRatio.toString());
        User tempUser = new User();
        tempUser.setObjectId(user.getObjectId());
        Log.i("TGA","更新前的总题目数量"+user.getNumber()+"");
        tempUser.setNumber(user.getNumber() + totalNumber);
        Log.i("TGA","更新后的总题目数量"+tempUser.getNumber()+"");
        tempUser.setRightNumber(user.getRightNumber() + rightNumber);//计算回答正确的题目数
        tempUser.setRightRatio((double) (user.getRightNumber() * 100 / user.getNumber()));//计算新的正确率
        tempUser.setAnswerQuestion(totalRelation);
        Log.i("TGA","答对题目的总次数"+tempUser.getRightNumber()+"");
        Log.i("TGA","答题目的正确率"+tempUser.getRightRatio()+"");
        //先从错题列表中移除这些回答正确的问题
        BmobRelation errorRelation = new BmobRelation();
        if (rightQuestionList.size() != 0) {
            for (Question question : rightQuestionList) {
                errorRelation.remove(question);
            }
            tempUser.setMistakeQuestion(errorRelation);
            tempUser.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                    } else {
                        Toast.makeText(GradeActivity.this, "code:400，更新用户信息失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        //再从错题列表中添加错题  前后两个BmobRelation不能相同
        errorRelation = new BmobRelation();
        if (errorQuestionList.size() != 0 ) {
            for (Question question : errorQuestionList) {
                errorRelation.add(question);
            }
            tempUser.setMistakeQuestion(errorRelation);
            tempUser.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                    } else {
                        Toast.makeText(GradeActivity.this, "code:401，更新用户信息失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grade_analysis_mistake_tv:{

            }break;
            case R.id.grade_analysis_all_tv:{

            }break;
            case R.id.grade_recomplete_tv:{

            }break;
            case R.id.grade_back_iv:{
                finish();
            }break;
        }
    }


}
