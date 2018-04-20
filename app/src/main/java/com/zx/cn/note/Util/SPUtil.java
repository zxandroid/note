package com.zx.cn.note.Util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zx.cn.note.base.BaseAplication;


/**
 * 对SharedPreference文件中的各种类型的数据进行存取操作
 * commit是同步，apply是异步先保存内存后异步保存到硬盘，无返回值
 */
public class SPUtil {

    private static SharedPreferences sp;
    public static int DEFAULT_INT = 0;
    public static long DEFAULT_LONG = 0l;
    public static String DEFAULT_STRING = "";
    public static boolean DEFAULT_BOOLEAN = false;
    public static Float DEFAULT_FLOAT = 0.0f;

    private static void init() {
        if (sp == null) {
            sp = PreferenceManager.getDefaultSharedPreferences(BaseAplication.getAppContext());
        }
    }

    //  ----------------------------int----------------------------------------------
    public static void setIntData(String key, int value) {
        if (sp == null) {
            init();
        }
        sp.edit().putInt(key, value).apply();
    }

    public static int getIntData(String key) {
        if (sp == null) {
            init();
        }
        return sp.getInt(key, DEFAULT_INT);
    }

    //  ----------------------------long----------------------------------------------
    public static void setlongData(String key, long value) {
        if (sp == null) {
            init();
        }
        sp.edit().putLong(key, value).apply();
    }

    public static long getlongData(String key) {
        if (sp == null) {
            init();
        }
        return sp.getLong(key, DEFAULT_LONG);
    }

    //  ----------------------------float----------------------------------------------
    public static void setFloatData(String key,
                                    float value) {
        if (sp == null) {
            init();
        }
        sp.edit().putFloat(key, value).apply();
    }

    public static Float getFloatData(String key) {
        if (sp == null) {
            init();
        }
        return sp.getFloat(key, DEFAULT_FLOAT);
    }

    //  ----------------------------Boolean----------------------------------------------
    public static void setBooleanData(String key,
                                      boolean value) {
        if (sp == null) {
            init();
        }
        sp.edit().putBoolean(key, value).apply();
    }

    public static Boolean getBooleanData(String key) {
        if (sp == null) {
            init();
        }
        return sp.getBoolean(key, DEFAULT_BOOLEAN);
    }


    //  ----------------------------String----------------------------------------------
    public static void setStringData(String key, String value) {
        if (sp == null) {
            init();
        }
        sp.edit().putString(key, value).apply();
    }

    public static String getStringData(String key) {
        if (sp == null) {
            init();
        }
        return sp.getString(key, DEFAULT_STRING);
    }



}