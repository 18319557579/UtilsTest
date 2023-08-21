package com.hsf.utilstest.packet.facade;

public interface OuterDownloadCallback {
    void onSuccess();
    void onDownloading(long now, long max);
    void onFail();
}
