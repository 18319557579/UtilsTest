package com.hsf.utilstest;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.net.wifi.WifiManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.hsf.utilstest.clipboard.ClipboardUtil;
import com.hsf.utilstest.databinding.ActivityMainBinding;
import com.hsf.utilstest.first_enter.FirstEnterUtil;
import com.hsf.utilstest.huangming.DeviceInfoUtils;
import com.hsf.utilstest.huangming.GetPhoneInfo;
import com.hsf.utilstest.packet.facade.OuterDownloadCallback;
import com.hsf.utilstest.packet.facade.RxNet;
import com.hsf.utilstest.print.PrintLog;
import com.hsf.utilstest.various_id.IdUtils;
import com.hsf.utilstest.share.ShareTest;
import com.hsf.utilstest.shore.StoreUtils;
import com.hsf.utilstest.ui.ScreenTool;
import com.hsf.utilstest.ui.ScreenUtils;
import com.hsf.utilstest.ui.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnIsFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Daisy", "第一次：" + FirstEnterUtil.isFirst(MainActivity.this));
            }
        });

        binding.btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Map<String, String> map = new HashMap<>();
                map.put("1", "h");
                map.put("2", "s");
                map.put("3", "f");*/

/*                Log.d("Daisy", GetPhoneInfo.getAndroidId(MainActivity.this));
                Log.d("Daisy", DeviceInfoUtils.getPackageVersionName(MainActivity.this));
                Log.d("Daisy", "" + DeviceInfoUtils.getPackageVersionCode(MainActivity.this));
                Log.d("Daisy", Build.BRAND);
                Log.d("Daisy", DeviceInfoUtils.getAndroidVersion());
                Log.d("Daisy", GetPhoneInfo.getDeviceDefaultLanguage());
                Log.d("Daisy", DeviceInfoUtil.getPackageName(MainActivity.this));
                Log.d("Daisy", DeviceInfoUtil.getInfo(MainActivity.this));
                Log.d("Daisy", DeviceInfoUtil.netType(MainActivity.this));*/

                PrintLog.INSTANCE
                        .addParam("AndroidId", GetPhoneInfo.getAndroidId(MainActivity.this))
                        .addParam("Version Code", DeviceInfoUtils.getPackageVersionName(MainActivity.this))
                        .addParam("VersionName", DeviceInfoUtils.getPackageVersionCode(MainActivity.this))
                        .addParam("OS Type", Build.BRAND)
                        .addParam("OS Version", DeviceInfoUtils.getAndroidVersion())
                        .addParam("语言", GetPhoneInfo.getDeviceDefaultLanguage())
                        .addParam("包名", DeviceInfoUtil.getPackageName(MainActivity.this))
                        .addParam("sim国家", DeviceInfoUtil.getInfo(MainActivity.this))
                        .addParam("网络类型", DeviceInfoUtil.netType(MainActivity.this))
                        .print();


            }
        });

        binding.btnIdShow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                PrintLog.INSTANCE
                        .addParam("AndroidId", GetPhoneInfo.getAndroidId(MainActivity.this))
                        .addParam("IMEI", GetPhoneInfo.getCurrentImei(MainActivity.this))
                        .addParam("手机硬件序列号", GetPhoneInfo.getDeviceSerial())
                        .addParam("MacID", IdUtils.getMac(MainActivity.this))
                        .addParam("获取DeviceID", IdUtils.getDeviceId(MainActivity.this))
                        .print();

                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String adid = AdvertisingIdClient.getGoogleAdId(getApplicationContext());
                            Log.d("Daisy","获取adid:"+adid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        binding.btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] REQUEST_PERMISSIONS = {
                        Manifest.permission.READ_PHONE_STATE
                };
                ActivityCompat.requestPermissions(MainActivity.this, REQUEST_PERMISSIONS, 0);
            }
        });

        binding.btnClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clipContent = ClipboardUtil.getClipboardContent(MainActivity.this);
                Log.d("Daisy", "剪切板的内容：" + clipContent);
            }
        });

        binding.btnShareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareTest.shareText_withCopy(MainActivity.this, "我是黄绍飞Daisy");
            }
        });

        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.baidu.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        binding.btnJumpGooglePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //org.telegram.messenger
                StoreUtils.launchAppDetail(MainActivity.this, "com.ruguoapp.jike", "com.heytap.market");
            }
        });

        binding.btnHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //物理高度
                Log.d("Daisy", "屏幕高度1：" + GetPhoneInfo.getScreenHeight(MainActivity.this));
                //当前高度，不包括状态栏和导航栏
                Log.d("Daisy", "屏幕高度2：" + GetPhoneInfo.getDeviceHeight(MainActivity.this));


                Log.d("Daisy", "导航栏高度：" + ScreenUtils.getNavigationBarHeight(MainActivity.this));

                Log.d("Daisy", "判断是否有导航栏：" + ScreenUtils.hasNavBar(MainActivity.this));
                Log.d("Daisy", "判断是否有导航栏(新)：" + ScreenUtils.isNavigationBarExist(MainActivity.this));

                Log.d("Daisy", "获得状态栏高度：" + ScreenUtils.getStatusBarHeight(MainActivity.this));
            }
        });

        binding.btnChen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.setActivity(MainActivity.this);
                Utils.hideVirtualButton();

                ScreenTool.screenFull(MainActivity.this);
            }
        });


        binding.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxNet.downloadFacade("https://lottojackpotslots.s3.us-east-2.amazonaws.com/com.Lotto.Jackpot.Slots777/5.0.0/debug/subgame/loading.zip", "", "",
                        MainActivity.this, new OuterDownloadCallback() {
                            @Override
                            public void onSuccess() {
                                Log.d("Daisy", "下载成功了（最外面）");
                            }

                            @Override
                            public void onDownloading(long now, long max) {
                                Log.d("Daisy", "正在下载中（最外面）, 当前:" + now + ", 总量:" + max );
                            }

                            @Override
                            public void onFail() {
                                Log.d("Daisy", "下载失败了（最外面）");
                            }
                        });
            }
        });

    }

    public void getInfo(View view) {
        DeviceInfoUtil.getWholeInfo();

    }


    private void showSystemParameter() {
        String TAG = "Daisy";
        Log.e(TAG, "手机厂商：" + SystemUtil.getDeviceBrand());
        Log.e(TAG, "手机型号：" + SystemUtil.getSystemModel());
        Log.e(TAG, "手机当前系统语言：" + SystemUtil.getSystemLanguage());
        Log.e(TAG, "Android系统版本号：" + SystemUtil.getSystemVersion());
//        Log.e(TAG, "手机IMEI：" + SystemUtil.getIMEI(getApplicationContext()));
    }

    public static String getTotalRam(){
        String path = "/proc/meminfo";
        String ramMemorySize = null;
        int totalRam = 0 ;
        try{
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader, 4096);
            ramMemorySize = br.readLine().split("\\s+")[1];
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(ramMemorySize != null){
            totalRam = (int)Math.ceil((new Float(Float.valueOf(ramMemorySize) / (1024 * 1024)).doubleValue()));
        }

        return totalRam + "GB";
    }

    private static String getTotalRom() {
        File dataDir = Environment.getDataDirectory();
        StatFs stat = new StatFs(dataDir.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        long size = totalBlocks * blockSize;
        long GB = 1024 * 1024 * 1024;
        final long[] deviceRomMemoryMap = {2*GB, 4*GB, 8*GB, 16*GB, 32*GB, 64*GB, 128*GB, 256*GB, 512*GB, 1024*GB, 2048*GB};
        String[] displayRomSize = {"2GB","4GB","8GB","16GB","32GB","64GB","128GB","256GB","512GB","1024GB","2048GB"};
        int i;
        for(i = 0 ; i < deviceRomMemoryMap.length; i++) {
            if(size <= deviceRomMemoryMap[i]) {
                break;
            }
            if(i == deviceRomMemoryMap.length) {
                i--;
            }
        }
        return displayRomSize[i];
    }

    public static String getCpuName() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String cpuName = "";

        try {
            FileReader fileReader = new FileReader(str1);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((str2 = bufferedReader.readLine()) != null) {
                if (TextUtils.isEmpty(str2)) {
                    continue;
                }
                String[] arrayOfString = str2.split(":\\s+", 2);
                if (TextUtils.equals(arrayOfString[0].trim(), "Hardware")) {
                    cpuName = arrayOfString[1];
                    break;
                }
            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cpuName;
    }

    // 获取CPU名字
    public static String getCpuName2() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCpuName3() {
        String CPUInfo = "";
        String cpu_args_file = "/proc/cpuinfo";
        try {
            String rst = readFile(cpu_args_file);
            String[] lines = rst.split("\\n");
            CPUInfo = lines[0];  // 获得CPU型号
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CPUInfo;
    }

    public static String readFile(String absolutePath) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(absolutePath));
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            String result = new String(baos.toByteArray());
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                baos = null;
            }
            if (null != bis) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bis = null;
            }
        }
        return "";
    }

}