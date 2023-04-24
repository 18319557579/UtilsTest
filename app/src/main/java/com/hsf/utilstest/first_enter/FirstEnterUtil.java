package com.hsf.utilstest.first_enter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class FirstEnterUtil {
    @SuppressLint("ApplySharedPref")
    public static boolean isFirst(Context context) {
        SharedPreferences sp = context.getSharedPreferences("first_store", Context.MODE_PRIVATE);
        boolean isFirstEnter = sp.getBoolean("is_first", true);
        SharedPreferences.Editor editor = sp.edit();

        if (isFirstEnter) {
            editor.putBoolean("is_first", false);
            editor.commit();
            return true;
        }
        return false;
    }
}
