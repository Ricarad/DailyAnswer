package com.ricarad.app.dailyanswer.application;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import org.xutils.x;

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        MultiDex.install(this);
    }
}
