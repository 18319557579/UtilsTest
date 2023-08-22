package com.hsf.utilstest;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Daisy", "已启动");
        Log.d("Daisy", "我就是我");
        Log.d("Daisy", "未启动");

        Log.d("Little", "另一个启动");
    }
}
