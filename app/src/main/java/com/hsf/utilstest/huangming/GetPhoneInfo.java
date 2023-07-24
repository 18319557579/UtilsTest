package com.hsf.utilstest.huangming;
import android.Manifest;
import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GetPhoneInfo {

    static final int INTERNAL_STORAGE = 0;
    static final int EXTERNAL_STORAGE = 1;


    int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;//屏幕高度(不包括刘海屏的高度)
    int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;//屏幕宽度

    /**
     *获取设备宽度(px)
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void getPhoneInfo(Context context) {
        Log.d("lylx", "设备宽度：" + getDeviceWidth(context));
        Log.d("lylx", "设备高度：" + getScreenHeight(context));
        Log.d("lylx", "设备的唯一标识IMEI：" +getCurrentImei(context));
        Log.d("lylx", "厂商名：" + getDeviceManufacturer());
        Log.d("lylx", "产品名：" + getDeviceProduct());
        Log.d("lylx", "手机品牌：" + getDeviceBrand());
        Log.d("lylx", "手机型号：" + getDeviceModel());
        Log.d("lylx", "手机主板名：" + getDeviceBoard());
        Log.d("lylx", "手机设备名：" + getDeviceDevice());
        Log.d("lylx", "手机硬件序列号：" + getDeviceSerial());
        Log.d("lylx", "手机Android 系统SDK：" + getDeviceSDK());
        Log.d("lylx", "系统SDK版本：" + getAndroidVersion());
        Log.d("lylx", "手机Android 版本：" + getDeviceAndroidVersion());
        Log.d("lylx", "当前手机系统语言：" + getDeviceDefaultLanguage());
        Log.d("lylx", "当前系统上的语言列表（Locale列表）：" + getDeviceSupportLanguage());
        Log.d("lylx", "内存RAM：" + getRAMInfo(context));
        Log.d("lylx", "内存RAM：" + getTotalRam(context));
        Log.d("lylx", "存储ROM：" + getTotalRom());
        Log.d("lylx", "手机CPU硬件型号：" + getPhoneCPU());
        Log.d("lylx", "安卓id：" + getAndroidId(context));

    }

    /**
     *获取手机androidId
     */
    public static String getAndroidId(Context context){
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;
    }

    /**
     *获取手机CPU硬件型号
     */
    public static String getPhoneCPU(){
        return Build.HARDWARE;
    }

    /**
     *获取设备宽度(px)
     */
    public static int getDeviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     *获取设备高度(px)
     *获取的是屏幕当前高度，不包括状态栏和导航栏。findx5p分辨率为3216*1400，当用手势导航栏时，
     * 这里得到的是3088，当用按键导航栏时，这里得到的是2912
     */
    public static int getDeviceHeight(Context context) {//高度不包括状态栏
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 屏幕高度
     * @return the height of screen, in pixel
     * 获取的高度为屏幕的物理高度，findx5p分辨率为3216*1400，这里得到的高度为3216
     */
    public static int getScreenHeight(Context context) {//获取高度为实际高度（包括了状态栏高度）
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }

    /**
     * 屏幕宽度
     * @return the width of screen, in pixel
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkPermission(Context ctx, String permission) {
        return (new ContextWrapper(ctx)).checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
    /**
     *获取设备的唯一标识，需要“android.permission.READ_Phone_STATE”权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getCurrentImei(Context context) {
        String imei = "";
        if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                //imei = tm.getDeviceId();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei =tm.getImei();
                } else {
                    imei = tm.getDeviceId();
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            } finally {
                if (TextUtils.isEmpty(imei)) {
                    imei = "";
                }
            }
        }
        return imei;
    }

    /**
     *获取厂商名
     */
    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     *获取产品名
     */
    public static String getDeviceProduct() {
        return Build.PRODUCT;
    }

    /**
     *获取手机品牌
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     *获取手机型号
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     *获取手机主板名
     */
    public static String getDeviceBoard() {
        return Build.BOARD;
    }

    /**
     *获取手机设备名
     */
    public static String getDeviceDevice() {
        return Build.DEVICE;
    }

    /**
     *获取手机硬件序列号
     */
    public static String getDeviceSerial() {
        return Build.SERIAL;
    }

    /**
     *获取手机Android 系统SDK
     * @return
     */
    public static int getDeviceSDK() {
        return Build.VERSION.SDK_INT;
    }

    /**
     *获取手机Android 版本
     * @return
     */
    public static String getDeviceAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     *获取当前手机系统语言
     * @return
     */
    public static String getDeviceDefaultLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     *获取当前系统上的语言列表（Locale列表）
     * @return
     */
    public static Locale[] getDeviceSupportLanguage() {
        return Locale.getAvailableLocales();
    }

    /**
     *判断SD是否挂载
     * @return
     */
    public static boolean isSDCardMount() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    //获取手机运行内存
    public static String getTotalRam(Context context){
        String path = "/proc/meminfo";
        if (!isPathExist(path)){
            Log.d("lylx",path + "路径不存在");
            return "0GB";
        }
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

        long availableSize = 0;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        availableSize = memoryInfo.availMem;

        //return totalRam + "GB";
        return "可用/总共：" + Formatter.formatFileSize(context,availableSize)
                + "/" + totalRam + "GB";
    }

    //获取手机存储内存
    public static String getTotalRom() {
        File dataDir = Environment.getDataDirectory();
        StatFs stat = new StatFs(dataDir.getPath());
        long blockSize = stat.getBlockSizeLong();  //文件系统上块的大小（以字节为单位）
        long availableBlocks = stat.getAvailableBlocksLong();  //文件系统上可用的块的数目
        long totalBlocks = stat.getBlockCountLong(); //文件系统上的块总数
        long size = totalBlocks * blockSize;
        long availableSize = availableBlocks * blockSize;
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
        if (i==11) {
            return String.format("可用/总共：%.2fGB/%.2fTB", (float)availableSize/(GB*1024),(float)size/(GB*1024));
        }
        return String.format("可用/总共：%.0fGB/%s",(float)availableSize/GB,displayRomSize[i]);
    }

    /**
     *获取手机RAM信息
     * @return
     */
    public static String getRAMInfo(Context context) {
        long totalSize = 0;
        long availableSize = 0;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        totalSize = memoryInfo.totalMem;
        availableSize = memoryInfo.availMem;

        return "可用/总共：" + Formatter.formatFileSize(context,availableSize)
            + "/" + Formatter.formatFileSize(context,totalSize);
    }

    /**
     * 是否为鸿蒙系统
     *
     * @return true为鸿蒙系统
     */
    public static boolean isHarmonyOs() {
        try {
            Class<?> buildExClass = Class.forName("com.huawei.system.BuildEx");
            Object osBrand = buildExClass.getMethod("getOsBrand").invoke(buildExClass);
            return "Harmony".equalsIgnoreCase(osBrand.toString());
        } catch (Throwable x) {
            return false;
        }
    }

    /**
     * 获取鸿蒙系统版本号
     *
     * @return 版本号
     */
    public static String getHarmonyVersion() {
        return getProp("hw_sc.build.platform.version", "");
    }

    private static String getProp(String property, String defaultValue) {
        try {
            Class spClz = Class.forName("android.os.SystemProperties");
            Method method = spClz.getDeclaredMethod("get", String.class);
            String value = (String) method.invoke(spClz, property);
            if (TextUtils.isEmpty(value)) {
                return defaultValue;
            }
            return value;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    // 获取CPU名字
    public static String getCpuName() {
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

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getPackageVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0); //PackageManager.GET_CONFIGURATIONS
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static int getPackageVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 判断路径是否存在
     *
     * @param path 需要判断的路径
     * @return true 是存在，false 是不存在
     */
    public static boolean isPathExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    //获取手机安卓版本或鸿蒙版本
    public static String getAndroidVersion(){
        if(isHarmonyOs()){
            return "HarmonyOS版本 " + getHarmonyVersion();
        }else {
            return "Android版本 " + Build.VERSION.RELEASE;
        }
    }





}
