package com.ricarad.app.dailyanswer.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.adapter.SlidingListAdapter;
import com.ricarad.app.dailyanswer.model.Question;
import com.ricarad.app.dailyanswer.model.User;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.ricarad.app.dailyanswer.common.Constant.BMOBAPPKEY;
import static com.ricarad.app.dailyanswer.common.Constant.GradeType.ANSWERTYPE;
import static com.ricarad.app.dailyanswer.common.Constant.GradeType.EXAM_CODE;
import static com.ricarad.app.dailyanswer.common.Constant.GradeType.PRACTICE_CODE;
import static com.ricarad.app.dailyanswer.common.Constant.USER;

public class AnswerQuestionActivity extends Activity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, PopupMenu.OnMenuItemClickListener {

    @ViewInject(R.id.answer_select_radiogroup)
    private RadioGroup radioSelectRg;
    @ViewInject(R.id.answer_Aselect_rb)
    private RadioButton itemARb;
    @ViewInject(R.id.answer_Bselect_rb)
    private RadioButton itemBRb;
    @ViewInject(R.id.answer_Cselect_rb)
    private RadioButton itemCRb;
    @ViewInject(R.id.answer_Dselect_rb)
    private RadioButton itemDRb;

    @ViewInject(R.id.answer_back_iv)
    private ImageView back;
    @ViewInject(R.id.answer_menu_iv)
    private ImageView menu;
    @ViewInject(R.id.answer_questionTitle_tv)
    private TextView questionTitleTv;
    @ViewInject(R.id.answer_pervious_img)
    private ImageView previous;
    @ViewInject(R.id.answer_next_img)
    private ImageView next;
    @ViewInject(R.id.answer_analysis_tv)
    private TextView analysisTv;
    @ViewInject(R.id.answer_result_tv)
    private TextView resultTv;

    private ListView questionSlideListView; //滑动侧边栏的listview
    private ArrayList<Question> questionList = new ArrayList<>();//题目集合
    private ArrayList<Integer> answerList = new ArrayList<Integer>(); //在选择每道题目后，将选择的选项Id存放在该集合里的对应位置
    private ArrayList<String> titleList = new ArrayList<String>();//每道题的题目集合，添加了标号
    private SlidingListAdapter slidingListAdapter;
    private int currentIndex = 0;
    private boolean isShowResult = true;//是否显示答案
    private User user;
    private int answerType;//是练习还是考试

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_answer_question);
        user = (User) getIntent().getSerializableExtra("user");
        answerType = getIntent().getIntExtra(ANSWERTYPE, 0);  //获取当前答题的模式，默认是练习
        Bmob.initialize(this, BMOBAPPKEY);
        x.view().inject(this);
        initView();
        initQuestionList();
        questionSlideListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectItemId = radioSelectRg.getCheckedRadioButtonId();
                answerList.set(currentIndex, selectItemId);
                Question question = questionList.get(position);
                setView(position, question);
                currentIndex = position;
                int nSelectItemId = answerList.get(currentIndex);
                if (nSelectItemId == -1) {
                    radioSelectRg.clearCheck();
                } else {
                    radioSelectRg.check(nSelectItemId);
                }
            }
        });
    }

    public void initQuestionList() {
        final ProgressDialog pgd = ProgressDialog.show(AnswerQuestionActivity.this, "请稍等", "正在加载题目");
        BmobQuery<Question> query = new BmobQuery<>();
        query.findObjects(new FindListener<Question>() {
            @Override
            public void done(List<Question> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0) {

                        int i = 1;
                        for (Question question : list) {
                            questionList.add(question);
                            answerList.add(-1);
                            titleList.add(i + "." + question.getTitle());
                            i++;
                        }
                        slidingListAdapter = new SlidingListAdapter(AnswerQuestionActivity.this,
                                R.layout.activity_answer_question, titleList);
                        questionSlideListView.setAdapter(slidingListAdapter);
                        slidingListAdapter.notifyDataSetChanged();
                        setView(currentIndex, questionList.get(0));
                        pgd.dismiss();
                    }
                } else {
                    pgd.dismiss();
                    Toast.makeText(AnswerQuestionActivity.this,
                            "初始化题目列表失败，请退出重试" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setView(int i, Question question) {
        i = i + 1;
        questionTitleTv.setText(i + "." + question.getTitle());
        itemARb.setText("A." + question.getItemA());
        itemBRb.setText("B." + question.getItemB());
        itemCRb.setText("C." + question.getItemC());
        itemDRb.setText("D." + question.getItemD());
        resultTv.setText("正确答案为：" + question.getAnswer());
        analysisTv.setText("问题解析：" + question.getAnalysis());
    }

    public void initView() {
        //设置SlideMenu功能
        SlidingMenu slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        // slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        //SlidingMenu划出时主页面显示的剩余宽度
        //slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //设置SlidingMenu菜单的宽度
        slidingMenu.setBehindWidthRes(R.dimen.slidingmenu_offset_toolarge);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(AnswerQuestionActivity.this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        slidingMenu.setMenu(R.layout.left_slidingmenu_layout);
        //View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.question_slide_view, null);
        View view = slidingMenu.getMenu();
        questionSlideListView = view.findViewById(R.id.left_questionlist_lv);
        findViewById(R.id.answer_result_analysis_layout).setVisibility(View.INVISIBLE);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        back.setOnClickListener(this);
        menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.answer_next_img: {
                int selectItemId = radioSelectRg.getCheckedRadioButtonId();
                answerList.set(currentIndex, selectItemId);
                //保存上一题的选项之后，显示下一题
                currentIndex++;
                if (currentIndex < questionList.size()) {
                    Question question = questionList.get(currentIndex);
                    setView(currentIndex, question);
                    int nSelectItemId = answerList.get(currentIndex);
                    if (nSelectItemId == -1) {
                        radioSelectRg.clearCheck();
                    } else {
                        radioSelectRg.check(nSelectItemId);
                    }
                } else {
                    currentIndex--;
                    Toast.makeText(AnswerQuestionActivity.this, "当前已是最后一题", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.answer_pervious_img: {
                int selectItemId = radioSelectRg.getCheckedRadioButtonId();
                answerList.set(currentIndex, selectItemId);
                //保存这一题的选项之后，显示上一题
                currentIndex--;
                if (currentIndex >= 0) {
                    Question question = questionList.get(currentIndex);
                    setView(currentIndex, question);
                    int pSelectItemId = answerList.get(currentIndex);
                    if (pSelectItemId == -1) {
                        radioSelectRg.clearCheck();
                    } else {
                        radioSelectRg.check(pSelectItemId);
                    }
                } else {
                    currentIndex++;
                    Toast.makeText(AnswerQuestionActivity.this, "当前已是第一题", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case R.id.answer_back_iv: {
                if (answerType == PRACTICE_CODE) {
                    for (int i = 0; i < questionList.size(); i++) {
                        if (answerList.get(i) != -1) {
                            Question question = questionList.get(i);
                            BmobRelation relation = new BmobRelation();
                            relation.add(question);
                            User tempUser = new User();
                            tempUser.setObjectId(user.getObjectId());
                            tempUser.setAnswerQuestion(relation);
                            tempUser.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(AnswerQuestionActivity.this, "已将做过的题目保存至最近练习记录", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AnswerQuestionActivity.this, "保存做题日志失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    finish();
                } else if (answerType == EXAM_CODE) {
                    //TODO 完善考试代码
                }
            }
            break;
            case R.id.answer_menu_iv: {
                showPopupMenu(v);
            }
            break;
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.answer_question_menu_item, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(AnswerQuestionActivity.this);
        //显示弹出菜单
        popup.show();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int selectItemId = radioSelectRg.getCheckedRadioButtonId();
        answerList.set(currentIndex, selectItemId);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.answer_showresult_menuitem: {
                if (answerType == EXAM_CODE) {
                    Toast.makeText(AnswerQuestionActivity.this, "考试无法查看答案，请交卷后查看", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isShowResult) {
                        findViewById(R.id.answer_result_analysis_layout).setVisibility(View.INVISIBLE);
                        isShowResult = true;
                    } else {
                        findViewById(R.id.answer_result_analysis_layout).setVisibility(View.VISIBLE);
                        isShowResult = false;
                    }
                }
            }
            break;
            case R.id.answer_finish_menuitem: {
                Intent intent = new Intent(AnswerQuestionActivity.this, GradeActivity.class);
                int selectItemId = radioSelectRg.getCheckedRadioButtonId();
                answerList.set(currentIndex, selectItemId);
                intent.putExtra("questionList", questionList);
                intent.putExtra("answerList", answerList);
                intent.putExtra(ANSWERTYPE, answerType);
                intent.putExtra(USER, user);
                startActivity(intent);
                finish();
            }
            break;
            case R.id.answer_collect_menuitem: {
                Question question = questionList.get(currentIndex);
                BmobRelation relation = new BmobRelation();
                relation.add(question);
                User tempUser = new User();
                tempUser.setObjectId(user.getObjectId());
                tempUser.setCollectedQuestion(relation);
                tempUser.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(AnswerQuestionActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AnswerQuestionActivity.this, "收藏失败，失败原因：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            break;
        }
        return false;
    }
}
