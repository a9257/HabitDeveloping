package com.example.habittest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {
    private Utils() {
    }

    // 新浪微博是否安装
    public static boolean isSinaWeiboInstalled(Context context) {
        return isPackageInstalled(context, "com.sina.weibo");
    }

    // 包名对应的App是否安装
    public static boolean isPackageInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null)
            return false;
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        for (PackageInfo info : packageInfoList) {
            if (info.packageName.equals(packageName))
                return true;
        }
        return false;
    }

    public static String date2String(Date date) {
        Calendar calendar = Calendar.getInstance();   //实例化calendar类
        calendar.setTime(date);         ///更改为calendar类型
        int year = calendar.get(Calendar.YEAR);   //单独获取年份
        int month1 = calendar.get(Calendar.MONTH) + 1;    ////获取月份，格式存在不统一 06||6
        String month = String.format("%02d", month1);    ///更改月份类型
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);  ///获取日，格式不统一 06||6
        String day = String.format("%02d", day1);        //更改日期类型
        String date_s = year + month + day;     ///合成8位String

        return date_s;
    }

    public static String date2Week(Date date) {
        String[] weekdays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;
        return weekdays[w];
    }
}