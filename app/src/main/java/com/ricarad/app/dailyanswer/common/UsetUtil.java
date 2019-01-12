package com.ricarad.app.dailyanswer.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ricarad.app.dailyanswer.model.User;

import java.lang.reflect.Type;

import static com.ricarad.app.dailyanswer.common.Constant.USER;

public class UsetUtil {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public  static User getUser(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(USER, null);
        Type type = new TypeToken<User>(){}.getType();
        User user = gson.fromJson(json,type);
        return user;
    }

    public static void saveUser(Context context,User user){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(USER, json);
    }
}
