package com.hsf.utilstest.ui;

import static android.view.View.NO_ID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;

public class ScreenUtils {
    /**
     * 获得导航栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool",
                "android");
        Log.d("Daisy", "打印一下rid：" + rid);

        if (rid != 0) {
            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen",
                    "android");
            Log.d("Daisy", "打印一下resourceId：" + resourceId);

            return context.getResources().getDimensionPixelSize(resourceId);
        }

        return 0;
    }

    /**
     * 检查是否存在虚拟按键栏
     * 在fx5p上不管用
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else {
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Exception e) {
                Log.d("Daisy", e.toString());
            }
        }
        return sNavBarOverride;
    }

    private static final String NAVIGATION = "navigationBarBackground";

    /**
     * 管用
     * https://blog.csdn.net/lebulangzhen/article/details/120035635
     */
    // 该方法需要在View完全被绘制出来之后调用，否则判断不了
    public static boolean isNavigationBarExist(@NonNull Activity activity) {
        ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
        if (vp != null) {
            for (int i = 0; i < vp.getChildCount(); i++) {
                if (vp.getChildAt(i).getId() != NO_ID && NAVIGATION.equals(activity.getResources().getResourceEntryName(vp.getChildAt(i).getId()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }


}
