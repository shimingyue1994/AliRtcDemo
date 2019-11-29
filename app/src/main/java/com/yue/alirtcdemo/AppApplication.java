package com.yue.alirtcdemo;

import android.app.Application;

import com.lzy.okgo.OkGo;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
    }
}
