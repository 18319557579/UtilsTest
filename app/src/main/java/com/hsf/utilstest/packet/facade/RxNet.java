package com.hsf.utilstest.packet.facade;

import android.app.Activity;
import android.text.TextUtils;


import com.hsf.utilstest.packet.callback.DownloadCallback;
import com.hsf.utilstest.packet.callback.DownloadListener;
import com.hsf.utilstest.packet.core.RetrofitFactory;
import com.hsf.utilstest.packet.utils.AntZipUtils;
import com.hsf.utilstest.packet.utils.CommonUtils;
import com.hsf.utilstest.packet.utils.HandlerUtils;
import com.hsf.utilstest.packet.utils.LogUtil;
import com.hsf.utilstest.packet.utils.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class RxNet {
    private static Disposable mDownloadTask;

    public static void downloadFacade(String url, String folder, String location, Activity activity, OuterDownloadCallback outerDownloadCallback) {
        //这是zip下载到的路径
        String path = activity.getFilesDir().getAbsolutePath()
                + File.separator + CommonUtils.getName(url);


        RxNet.download(url, path, new DownloadCallback() {
            @Override
            public void onStart(Disposable d) {
                mDownloadTask = d;
                LogUtil.d("onStart " + d);
            }

            @Override
            public void onProgress(long totalByte, long currentByte, int progress) {
                LogUtil.d("onProgress " + progress);
                LogUtil.d("max：" + CommonUtils.byteFormat(totalByte) + ", downloaded：" + CommonUtils.byteFormat(currentByte) +
                        ", progress：" + progress + "%");
                if (outerDownloadCallback != null) outerDownloadCallback.onDownloading(currentByte, totalByte);
            }

            @Override
            public void onFinish(File file, String message) {
                LogUtil.d(message + " onFinish " + file.getAbsolutePath());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            AntZipUtils.uncompressFile(path, activity.getFilesDir().getAbsolutePath() + File.separator, true);
                            ZipUtils.decompressFile(path, activity.getFilesDir().getAbsolutePath() + File.separator, true);
                            LogUtil.d("解压完成");

                            if (outerDownloadCallback != null) outerDownloadCallback.onSuccess();

                        } catch (Exception e) {
                            LogUtil.d("解压出现问题：" + e);
                        }
                    }
                }).start();



            }

            @Override
            public void onError(String msg) {
                LogUtil.d("onError " + msg);
                if (outerDownloadCallback != null) outerDownloadCallback.onFail();
            }
        });
    }

    public static void download(final String url, final String filePath, final DownloadCallback callback) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(filePath)) {
            if (null != callback) {
                callback.onError("url or path empty");
            }
            return;
        }

        File oldFile = new File(filePath);
        if (oldFile.exists()) {
            if (null != callback) {
                callback.onFinish(oldFile, "File already exists");
            }
            return;
        }

        DownloadListener listener = new DownloadListener() {
            @Override
            public void onStart(ResponseBody responseBody) {
                saveFile(responseBody, url, filePath, callback);
            }
        };

        RetrofitFactory.downloadFile(url, CommonUtils.getTempFile(url, filePath).length(), listener, new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                if (null != callback) {
                    callback.onStart(d);
                }
            }

            @Override
            public void onNext(@NonNull ResponseBody responseBody) {
                LogUtil.d("implemented onNext " + responseBody);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                LogUtil.d("onError " + e.getMessage());
                if (null != callback) {
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onComplete() {
                LogUtil.i("download onComplete ");
            }
        });
    }

    /**
     * 这里是最终成功/失败的出口 ？
     */
    private static void saveFile(final ResponseBody responseBody, String url, final String filePath, final DownloadCallback callback) {
        boolean downloadSuccess = true;
        final File tempFile = CommonUtils.getTempFile(url, filePath);
        try {
            writeFileToDisk(responseBody, tempFile.getAbsolutePath(), callback);
        } catch (Exception e) {
            e.printStackTrace();
            downloadSuccess = false;
            LogUtil.d("download failed：" + e);
        }

        LogUtil.d("download successfully？ " + downloadSuccess);

        if (downloadSuccess) {
            final boolean renameSuccess = tempFile.renameTo(new File(filePath));
            LogUtil.d("Whether the name change is successful：" + renameSuccess);
            HandlerUtils.runOnUi(new Runnable() {
                @Override
                public void run() {
                    if (null != callback && renameSuccess) {
                        callback.onFinish(new File(filePath), "The download really worked");
                    }
                }
            });
        }
    }

    private static void writeFileToDisk(ResponseBody responseBody, String filePah, final DownloadCallback callback) throws IOException {
        long totalByte = responseBody.contentLength();
        LogUtil.d("Length of content to download this time:" + totalByte);

        long downloadByte = 0;
        File file = new File(filePah);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        byte[] buffer = new byte[1024 * 4];
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
        long tempFileLen = file.length();
        LogUtil.d("The length of the temporary file:" + tempFileLen);

        randomAccessFile.seek(tempFileLen);

        LogUtil.d("Begins reading the corresponding input stream");
        while (true) {
            //这里将输入流读到缓冲区去
            int len = responseBody.byteStream().read(buffer);
            if (len == -1) {
                LogUtil.d("The input stream reads to the end");
                break;
            }
            randomAccessFile.write(buffer, 0, len);
            downloadByte += len;

            callbackProgress(tempFileLen + totalByte, tempFileLen + downloadByte, callback);
        }
        randomAccessFile.close();

    }

    private static void callbackProgress(final long totalByte, final long downloadedByte, final DownloadCallback callback) {
        HandlerUtils.runOnUi(new Runnable() {
            @Override
            public void run() {
                if (null != callback) {
                    callback.onProgress(totalByte, downloadedByte, (int)((downloadedByte * 100) / totalByte));
                }
            }
        });
    }
}
