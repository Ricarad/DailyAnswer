package com.ricarad.app.dailyanswer.common;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.widget.Toast;

import com.ricarad.app.dailyanswer.activity.RegisterActivity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ricarad.app.dailyanswer.activity.AddTopicActivity.POST_TAG;
import static com.ricarad.app.dailyanswer.common.Constant.FILE_MAX_LENGTH;

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

    public static boolean isImgTooBig(String imgUrl){
        File file = new File(imgUrl);
        if (file.length() >= FILE_MAX_LENGTH){
            return true;
        }else
            return false;
    }

    public static String getRealPathFromUri(Context context, Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 11) {
            // SDK < Api11
            return getRealPathFromUri_BelowApi11(context, uri);
        }
        if (sdkVersion < 19) {
            // SDK > 11 && SDK < 19
            return getRealPathFromUri_Api11To18(context, uri);
        }
        // SDK > 19
        return getRealPathFromUri_AboveApi19(context, uri);
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = { MediaStore.Images.Media.DATA };
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = { id };

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };

        CursorLoader loader = new CursorLoader(context, uri, projection, null,
                null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }
}
