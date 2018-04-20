package com.zx.cn.note.Util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.zx.cn.note.BuildConfig;

/**
 * Created by zhang xiong on 2018/4/19.
 */

public  class LogUtil  {

    /**
     * release 时不会打印log
     */
    private static final String build_type="debug";

    public static void d(String tag, String msg){
        if(BuildConfig.BUILD_TYPE.equals(build_type)){
            Log.d(tag,msg);
        }
    }

    public static void d(String msg){
        if(BuildConfig.BUILD_TYPE.equals(build_type)){
            Log.d(getTag(),msg);
        }
    }

    public static void i(String tag, String msg){
        if(BuildConfig.BUILD_TYPE.equals(build_type)){
            Log.i(tag,msg);
        }
    }
    public static void i(String msg){
        if(BuildConfig.BUILD_TYPE.equals(build_type)){
            Log.i(getTag(),msg);
        }
    }
    public static void e(String tag, String msg){
        if(BuildConfig.BUILD_TYPE.equals(build_type)){
            Log.e(tag,msg);
        }
    }
    public static void e(String msg){
        if(BuildConfig.BUILD_TYPE.equals(build_type)){
            Log.e(getTag(),msg);
        }
    }

    public static void logToast(Context context, String msg){
        if(BuildConfig.BUILD_TYPE.equals(build_type)){
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取有类名与方法名的logString
     * @return 类名和方法名
     */
    public static String getTag() {
        /**
         * Throwable().getStackTrace()获取的是程序运行的堆栈信息，也就是程序运行到此处的流程，以及所有方法的信息
         * 这里我们为什么取2呢？0是代表createMessage方法信息，1是代表上一层方法的信息，这里我们
         * 取它是上两层方法的信息
         */
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[2];
        String fullClassName = stackTraceElement.getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        return className + "." + stackTraceElement.getMethodName();
    }
}
