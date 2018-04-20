package com.zx.cn.note.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhang xiong on 2018/4/20.
 */

public class BaseAplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getAppContext() {
        return context;
    }
}
