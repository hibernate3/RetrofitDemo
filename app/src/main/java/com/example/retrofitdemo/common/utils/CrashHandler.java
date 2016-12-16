package com.example.retrofitdemo.common.utils;


import android.content.Context;
import android.util.Log;

/**
 * Created by wangyuhang on 16/12/16.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler INSTANCE = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultUEH;
    private Context mContext;

    private CrashHandler() {
        mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static CrashHandler getInstance(){
        return INSTANCE;
    }

    public void init(Context ctx){
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = ctx;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e("CrashHandler", ex.getMessage(), ex);
        mDefaultUEH.uncaughtException(thread, ex);
    }
}
