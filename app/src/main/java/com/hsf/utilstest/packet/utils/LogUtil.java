package com.hsf.utilstest.packet.utils;

import android.util.Log;


public class LogUtil {
    private LogUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static final String TAG = "Daisy";

    /**
     * 下面四个是默认tag的函数
     *
     * @param msg
     */
    public static void i(String msg) {
        if (canPrint())
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (canPrint()) {
            LogUtil.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (canPrint()) {
            Log.e(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (canPrint()) {
            Log.v(TAG, msg);
        }
    }

    /**
     * 下面是传入自定义tag的函数
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (canPrint()) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (canPrint()) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (canPrint()) {
            Log.i(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (canPrint()) {
            Log.i(tag, msg);
        }
    }
    
    public static boolean canPrint() {
        return true;
    }
}
