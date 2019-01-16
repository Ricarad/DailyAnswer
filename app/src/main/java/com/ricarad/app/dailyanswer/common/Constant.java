package com.ricarad.app.dailyanswer.common;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.ricarad.app.dailyanswer.model.Question;

public class Constant {

    public final static String BMOBAPPKEY = "e197fce2a9812ccbb9419f2193211af0";
    public final String[] postLabels= {
            "C++", "Java", "Python", "HTML", "JavaScript", "Android", "数据结构与算法"
    };

    public final static String USER = "user";
    public final static String USERID = "userId";
    public final static int LFILEPICKER_REQUEST_CODE = 0;
    public final static String LFILEPICKER_PATH = "paths";
    public static class ResultCode{
        public final static int REGISTER_CODE = 1;
        public final static int GUIDE_CODE = 2;
    }
    public static class ActivityFlag{
        public final static String ACTIVITY_FLAG = "ActivityFlag";
        public final static int GRADE_ACTIVITY_CODE = 1;//表明从GradeActivity跳转过来
        public final static int MISTAKE_ACTIVITY_CODE = 2;//表明从MistakeActivity跳转过来
        public final static int COLLECTED_ACTIVITY_CODE = 3;//表明从CollectedActivity跳转过来
        public final static int RECORD_ACTIVITY_CODE = 4;//表明从CollectedActivity跳转过来
    }

    public static class GradeType{
        public final static String ANSWER_TYPE = "answerQuestionType";
        public final static int PRACTICE_CODE = 0;//表明为练习
        public final static int EXAM_CODE = 1;//表明为考试
    }
    public static class QuestionResultType{
        public final static int RIGHT = 0;
        public final static int ERROR = 1;
        public final static int EMPTY = 2;
    }
    public static class QuestionType{
       public final static String JAVA = "JAVA";
       public final static String C = "C";
       public final static String PHP = "PHP";
       public final static String PYTHON = "PYTHON";
       public final static String ANDROID = "ANDROID";
       /**
        * 数据结构
        */
       public final static String DATASTRUCTURE = "dataStructure";
       /**
        * 算法
        */
       public final static String ALGORITHM = "algorithm";

   }
}
