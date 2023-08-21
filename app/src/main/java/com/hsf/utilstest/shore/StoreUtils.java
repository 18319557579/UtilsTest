package com.hsf.utilstest.shore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public class StoreUtils {
    public static void launchAppDetail(Activity activity, String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)) return;

            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
