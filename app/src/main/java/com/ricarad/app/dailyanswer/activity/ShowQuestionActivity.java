package com.ricarad.app.dailyanswer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.model.Question;
import com.ricarad.app.dailyanswer.model.User;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.ricarad.app.dailyanswer.common.Constant.ActivityFlag.ACTIVITY_FLAG;
import static com.ricarad.app.dailyanswer.common.Constant.ActivityFlag.COLLECTED_ACTIVITY_CODE;
import static com.ricarad.app.dailyanswer.common.Constant.ActivityFlag.GRADE_ACTIVITY_CODE;
import static com.ricarad.app.dailyanswer.common.Constant.ActivityFlag.MISTAKE_ACTIVITY_CODE;
import static com.ricarad.app.dailyanswer.common.Constant.ActivityFlag.RECORD_ACTIVITY_CODE;
import static com.ricarad.app.dailyanswer.common.Constant.BMOBAPPKEY;
import static com.ricarad.app.dailyanswer.common.Constant.USER;

public class ShowQuestionActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    @ViewInject(R.id.show_question_title_tv)
    private TextView titleTv;
    @ViewInject(R.id.show_question_back_iv)
    private ImageView back;
    @ViewInject(R.id.show_question_right_iv)
    private ImageView rightBtn;
    @ViewInject(R.id.show_question_questionTitle_tv)
    private TextView questionTitleTv;
    @ViewInject(R.id.show_question_select_radiogroup)
    private RadioGroup radioSelectRg;
    @ViewInject(R.id.show_question_Aselect_rb)
    private RadioButton itemARb;
    @ViewInject(R.id.show_question_Bselect_rb)
    private RadioButton itemBRb;
    @ViewInject(R.id.show_question_Cselect_rb)
    private RadioButton itemCRb;
    @ViewInject(R.id.show_question_Dselect_rb)
    private RadioButton itemDRb;
    @ViewInject(R.id.show_question_analysis_tv)
    private TextView analysisTv;
    @ViewInject(R.id.show_question_result_tv)
    private TextView resultTv;

    private ArrayList<Question> questionList = new ArrayList<>();//
    private Question gradeQuestion; //当从GradeActivity跳转过来时 启用question
    private int currentIndex = 0;
    private User user;
    private static final int SCROLL_MIN_DISTANCE = 50;// 移动最小距离
    private static int activityFlag = 1; //默认是从GradeActivity转过来
    private GestureDetector mygesture;//手势探测器

    private Boolean showResult = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_question);
        x.view().inject(this);
        Bmob.initialize(this, BMOBAPPKEY);
        activityFlag = getIntent().getIntExtra(ACTIVITY_FLAG, 1);
        user = (User) getIntent().getSerializableExtra(USER);
        back.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        //构建手势探测器
        mygesture = new GestureDetector(this, myGestureListener);
        switch (activityFlag) {
            case GRADE_ACTIVITY_CODE: {
                initViewFromGradeActivity();
            }
            break;
            case MISTAKE_ACTIVITY_CODE: {
                initViewFromMistakeActivity();
            }
            break;
            case COLLECTED_ACTIVITY_CODE:{
                initViewFromCollectedActivity();
            }break;
            case RECORD_ACTIVITY_CODE:{
                initViewFromRecordActivity();
            }break;
        }
    }

    //如果是从GradeActivity传递过来，则默认只有一道题
    public void initViewFromGradeActivity() {
        gradeQuestion = (Question) getIntent().getSerializableExtra("question");
        int answerId = getIntent().getIntExtra("answerId", R.id.answer_Aselect_rb);//默认选项是A
        titleTv.setText("题目回顾");
        setView(0, gradeQuestion);
        //先将正确选项标为红色
        switch (gradeQuestion.getAnswer()) {
            case "A": {
                radioSelectRg.check(itemARb.getId());
                itemARb.setTextColor(this.getResources().getColor(R.color.cololSkyBlue));
            }
            break;
            case "B": {
                radioSelectRg.check(itemBRb.getId());
                itemBRb.setTextColor(this.getResources().getColor(R.color.cololSkyBlue));
            }
            break;
            case "C": {
                radioSelectRg.check(itemCRb.getId());
                itemCRb.setTextColor(this.getResources().getColor(R.color.cololSkyBlue));
            }
            break;
            case "D": {
                radioSelectRg.check(itemDRb.getId());
                itemDRb.setTextColor(this.getResources().getColor(R.color.cololSkyBlue));
            }
            break;
        }
        //判断用户选的选项，选对了就把该选项标为蓝色，选错则标为红色，没选则不标颜色
        switch (answerId) {
            case R.id.answer_Aselect_rb: {
                if (gradeQuestion.getAnswer().equals("A")) {
                    itemARb.setTextColor(this.getResources().getColor(R.color.cololSkyBlue));
                } else {
                    itemARb.setTextColor(this.getResources().getColor(R.color.colorSmallRed));
                }
            }
            break;
            case R.id.answer_Bselect_rb: {
                if (gradeQuestion.getAnswer().equals("B")) {
                    itemARb.setTextColor(this.getResources().getColor(R.color.cololSkyBlue));
                } else {
                    itemBRb.setTextColor(this.getResources().getColor(R.color.colorSmallRed));
                }
            }
            break;
            case R.id.answer_Cselect_rb: {
                if (gradeQuestion.getAnswer().equals("C")) {
                    itemCRb.setTextColor(this.getResources().getColor(R.color.cololSkyBlue));
                } else {
                    itemCRb.setTextColor(this.getResources().getColor(R.color.colorSmallRed));
                }
            }
            break;
            case R.id.answer_Dselect_rb: {
                if (gradeQuestion.getAnswer().equals("D")) {
                    itemDRb.setTextColor(this.getResources().getColor(R.color.cololSkyBlue));
                } else {
                    itemDRb.setTextColor(this.getResources().getColor(R.color.colorSmallRed));
                }
            }
            break;
            case -1: {

            }
            break;
        }
    }
    public void initViewFromMistakeActivity() {
        questionList = (ArrayList<Question>) getIntent().getSerializableExtra("questionList");
        currentIndex = getIntent().getIntExtra("currentIndex", 0);//默认是0
        titleTv.setText("错题");
        setView(currentIndex, questionList.get(currentIndex));
    }
    public void initViewFromCollectedActivity(){
        questionList = (ArrayList<Question>) getIntent().getSerializableExtra("questionList");
        currentIndex = getIntent().getIntExtra("currentIndex", 0);//默认是0
        titleTv.setText("收藏夹");
        rightBtn.setBackground(this.getResources().getDrawable(R.drawable.show_result));
        resultTv.setVisibility(View.GONE);
        analysisTv.setVisibility(View.GONE);
        setView(currentIndex,questionList.get(currentIndex));
    }
    public void initViewFromRecordActivity(){
        questionList = (ArrayList<Question>) getIntent().getSerializableExtra("questionList");
        currentIndex = getIntent().getIntExtra("currentIndex", 0);//默认是0
        titleTv.setText("练习记录");
        setView(currentIndex, questionList.get(currentIndex));
    }

    //将数据显示在界面上，适用于所有跳转来的Activity
    public void setView(int i, Question question) {
        i = i + 1;
        questionTitleTv.setText(i + "." + question.getTitle());
        itemARb.setText("A." + question.getItemA());
        itemBRb.setText("B." + question.getItemB());
        itemCRb.setText("C." + question.getItemC());
        itemDRb.setText("D." + question.getItemD());
        resultTv.setText("正确答案为：" + question.getAnswer());
        analysisTv.setText("问题解析：" + question.getAnalysis());
        if (activityFlag == MISTAKE_ACTIVITY_CODE  || activityFlag == RECORD_ACTIVITY_CODE){
            switch (question.getAnswer()){
                case "A":{
                    radioSelectRg.check(R.id.show_question_Aselect_rb);
                }break;
                case "B":{
                    radioSelectRg.check(R.id.show_question_Bselect_rb);
                }break;
                case "C":{
                    radioSelectRg.check(R.id.show_question_Cselect_rb);
                }break;
                case "D":{
                    radioSelectRg.check(R.id.show_question_Dselect_rb);
                }break;
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_question_back_iv: {
                finish();
            }
            break;
            case R.id.show_question_right_iv: {

                if (activityFlag == GRADE_ACTIVITY_CODE) {
                    BmobRelation relation = new BmobRelation();
                    relation.add(gradeQuestion);
                    User tempUser = new User();
                    tempUser.setObjectId(user.getObjectId());
                    tempUser.setCollectedQuestion(relation);
                    tempUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(ShowQuestionActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ShowQuestionActivity.this, "收藏失败，失败原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (activityFlag == MISTAKE_ACTIVITY_CODE  ||  activityFlag == RECORD_ACTIVITY_CODE) {
                    BmobRelation relation = new BmobRelation();
                    relation.add(questionList.get(currentIndex));
                    User tempUser = new User();
                    tempUser.setObjectId(user.getObjectId());
                    tempUser.setCollectedQuestion(relation);
                    tempUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(ShowQuestionActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ShowQuestionActivity.this, "收藏失败，失败原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (activityFlag == COLLECTED_ACTIVITY_CODE){
                    if (!showResult){
                        showResult = true;
                        rightBtn.setBackground(this.getResources().getDrawable(R.drawable.show_result_press));
                        resultTv.setVisibility(View.VISIBLE);
                        analysisTv.setVisibility(View.VISIBLE);
                        switch (questionList.get(currentIndex).getAnswer()){
                            case "A": {
                                radioSelectRg.check(itemARb.getId());
                            }
                            break;
                            case "B": {
                                radioSelectRg.check(itemBRb.getId());
                            }
                            break;
                            case "C": {
                                radioSelectRg.check(itemCRb.getId());
                            }
                            break;
                            case "D": {
                                radioSelectRg.check(itemDRb.getId());
                            }
                            break;
                        }
                    }else {
                        showResult = false;
                        rightBtn.setBackground(this.getResources().getDrawable(R.drawable.show_result));
                        radioSelectRg.clearCheck();
                        resultTv.setVisibility(View.GONE);
                        analysisTv.setVisibility(View.GONE);
                    }
                }

            }
            break;
        }
    }

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
            if (activityFlag == GRADE_ACTIVITY_CODE) {
                return false;
            }
            //左滑
            if (e1.getX() - e2.getX() > SCROLL_MIN_DISTANCE) {
                currentIndex++;
                if (currentIndex < questionList.size()) {
                    setView(currentIndex, questionList.get(currentIndex));
                } else {
                    currentIndex = 0;
                    setView(currentIndex, questionList.get(currentIndex));
                }
            } else if (e2.getX() - e1.getX() > SCROLL_MIN_DISTANCE) {
                currentIndex--;
                if (currentIndex >= 0) {
                    setView(currentIndex, questionList.get(currentIndex));
                } else {
                    currentIndex = questionList.size() - 1;
                    setView(currentIndex, questionList.get(currentIndex));
                }
            }
            return false;
        }
    };



}
