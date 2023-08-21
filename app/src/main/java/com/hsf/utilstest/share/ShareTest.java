package com.hsf.utilstest.share;

import android.app.Activity;
import android.content.Intent;

public class ShareTest {
    public static void shareText(Activity activity, String shareContent) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        intent.setType("text/plain");
        activity.startActivity(intent);
    }

    /**
     * 系统分享，纯文本，但是多一个复制按钮
     */
    public static void shareText_withCopy(Activity activity, String shareContent) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        intent.setType("text/plain");
        activity.startActivity(Intent.createChooser(intent ,"分享"));
    }
}
