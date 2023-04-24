package com.hsf.utilstest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class DeviceInfoUtil {
    public static void getWholeInfo() {
        Log.d("Daisy", "设备品牌：" + Build.BRAND);
        Log.d("Daisy", "设备型号：" + Build.MODEL);
        Log.d("Daisy", "系统版本：" + Build.VERSION.RELEASE);
        Log.d("Daisy", "运行内存：" + getTotalRam());
        Log.d("Daisy", "闪存大小：" + getTotalRom());
        Log.d("Daisy", "指令集：" + Arrays.toString(Build.SUPPORTED_ABIS));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d("Daisy", "处理器厂商与型号：" + Build.SOC_MANUFACTURER + " / " + Build.SOC_MODEL);
        }
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

    public static String getPackageName(Context context) {
        PackageManager manager = context.getPackageManager();

        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getInfo(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimCountryIso();
    }

    public static String GetNetState(Context context) {
        String netState = "null";
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected() && info.getState() == NetworkInfo.State.CONNECTED) {
                label18:
                switch(info.getType()) {
                    case 0:
                        switch(info.getSubtype()) {

                            case 1:
                            case 2:
                            case 4:
                                netState = "2g";
                                break label18;
                            case 3:
                            case 5:
                            case 6:
                            case 8:
                            case 12:
                                netState = "3g";
                            case 7:
                            case 9:
                            case 10:
                            case 11:
                            default:
                                break label18;
                            case 13:
                                netState = "4g";
                                break label18;
                            case 20:
                                netState = "5g";
                                break label18;
                        }
                    case 1:
                        netState = "wifi";
                }
            }
        }

        return netState;
    }

    public static String netType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null) {
            return "未联网";
        }
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return "wifi";
        } else {
            return "流量";
        }
    }
}
