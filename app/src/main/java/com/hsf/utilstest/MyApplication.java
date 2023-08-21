package com.hsf.utilstest;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Daisy", "已启动");
    }
}
