package com.hsf.utilstest.packet.utils;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtils {
    public static void runOnUi(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
