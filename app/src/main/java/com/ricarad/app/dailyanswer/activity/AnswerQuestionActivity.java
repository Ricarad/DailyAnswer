package com.ricarad.app.dailyanswer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ricarad.app.dailyanswer.R;
import com.ricarad.app.dailyanswer.model.Question;
import com.ricarad.app.dailyanswer.model.User;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

public class AnswerQuestionActivity extends AppCompatActivity {

    private List<Question> questionList ;//题目集合
    @ViewInject(R.id.answer_select_radiogroup)
    private RadioGroup radioSelect;
    @ViewInject(R.id.answer_Aselect_rb)
    private RadioButton itemA;
    @ViewInject(R.id.answer_Bselect_rb)
    private RadioButton itemB;
    @ViewInject(R.id.answer_Cselect_rb)
    private RadioButton itemC;
    @ViewInject(R.id.answer_Dselect_rb)
    private RadioButton itemD;


    private ListView questionSlideList; //滑动侧边栏的listview
    private int currentIndex = 0;
    private boolean isShowResult = true;//是否显示答案
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        //设置SlideMenu功能
        SlidingMenu slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        // slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        //SlidingMenu划出时主页面显示的剩余宽度
        //slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //设置SlidingMenu菜单的宽度
        slidingMenu.setBehindWidthRes(R.dimen.slidingmenu_offset_large);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        slidingMenu.attachToActivity(AnswerQuestionActivity.this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        slidingMenu.setMenu(R.layout.left_slidingmenu_layout);
        //View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.question_slide_view, null);
        View view = slidingMenu.getMenu();
    }
}
