package com.ricarad.app.dailyanswer.common;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.ricarad.app.dailyanswer.model.Question;

public class Constant {
    public final static int REGISTER_CODE = 1;
    public final static int GUIDEVIEW_CODE = -1;
    public final static String BMOBAPPKEY = "e197fce2a9812ccbb9419f2193211af0";
    public final String[] postLabels= {
            "C++", "Java", "Python", "HTML", "JavaScript", "Android", "数据结构与算法"
    };

    public final static String USER = "user";

   class QuestionType{
       public final static String JAVA = "java";
       public final static String C = "c";
       public final static String PHP = "php";
       public final static String PYTHON = "python";
       public final static String ANDROID = "android";
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
