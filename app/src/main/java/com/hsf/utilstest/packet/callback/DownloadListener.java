package com.hsf.utilstest.packet.callback;

import okhttp3.ResponseBody;

public interface DownloadListener {
    void onStart(ResponseBody responseBody);
}
