package com.example.retrofitdemo;

import android.app.Application;

import com.example.retrofitdemo.common.utils.CrashHandler;

/**
 * Created by wangyuhang on 16/12/16.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}
