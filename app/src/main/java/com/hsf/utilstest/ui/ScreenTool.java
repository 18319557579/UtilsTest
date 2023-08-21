package com.hsf.utilstest.ui;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class ScreenTool {
    public static void  screenFull(Activity activity) {

        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        try {
            Field field = lp.getClass().getField("layoutInDisplayCutoutMode");
            //Field constValue = lp.getClass().getDeclaredField("LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER");
            Field constValue = lp.getClass().getDeclaredField("LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER");
            field.setInt(lp, constValue.getInt(null));

            // https://developer.android.com/training/system-ui/immersive
            int flag = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            flag |= View.class.getDeclaredField("SYSTEM_UI_FLAG_IMMERSIVE_STICKY").getInt(null);
            View view = activity.getWindow().getDecorView();
            view.setSystemUiVisibility(flag);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
