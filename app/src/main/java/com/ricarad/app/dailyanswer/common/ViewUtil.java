package com.ricarad.app.dailyanswer.common;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ricarad.app.dailyanswer.activity.AddTopicActivity.POST_TAG;

/**
 * Created by root on 2019-1-12.
 */

public class ViewUtil {
    public static String genRealativeTime(String updated) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date(System.currentTimeMillis());
        Date updatedDay;
        try {
            updatedDay = sdf.parse(updated);
        } catch (ParseException e) {
            e.printStackTrace();
            return updated;
        }
        int diff = (int)(today.getTime() - updatedDay.getTime());
        double diff_d = diff;
        if (diff < 1000*60*60){
            int mins = diff/(1000*60);
            if (mins == 0)
                return "刚刚";
            return mins + "分钟前";
        }else if (diff_d < 1000.0*60*60*24){
            double hours = diff/(1000*60*60);
            int tmp = (int)hours;
            return tmp + "小时前";
        }else if (diff_d < 1000.0*60*60*24*30){
            double days = diff/(1000.0*60*60*24);
            int tmp = (int)days;
            return tmp + "天前";
        }else {
            return updated;
        }
    }

}
