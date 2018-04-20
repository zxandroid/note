package com.zx.cn.note.Util;

import android.view.View;

/**
 * Created by zhang xiong on 2018/4/20.
 */

public class CheckUtil {
    public static boolean isEmpty(String str){
        if(str!=null&&str.trim().length()>0){
            return false;
        }else{
            return true;
        }
    }

    public static boolean isVisible(View view){
        if(view.getVisibility()==View.VISIBLE){
            return true;
        }else {
            return false;
        }
    }
}
