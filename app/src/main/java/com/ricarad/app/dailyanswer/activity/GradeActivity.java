package com.ricarad.app.dailyanswer.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricarad.app.dailyanswer.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class GradeActivity extends Activity {

   @ViewInject(R.id.grade_back_iv)
    private ImageView back;
   @ViewInject(R.id.grade_rightratio_tv)
   private TextView rightRatioTv;
   @ViewInject(R.id.grade_totlanum_tv)
   private TextView totolNumberTv;
   @ViewInject(R.id.grade_rightnum_tv)
   private TextView rightNumberTv;
   @ViewInject(R.id.grade_analysis_mistake_tv)
   private TextView analysisMistakesTv;
   @ViewInject(R.id.grade_analysis_all_tv)
   private TextView analysisAllTv;
   @ViewInject(R.id.grade_recomplete_tv)
   private TextView reCompleteTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_grade);
        x.view().inject(this);
    }
}
