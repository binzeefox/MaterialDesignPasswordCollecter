package com.binzeefox.materialdesignpasswordcollecter.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by tong.xiwen on 2017/4/10.
 */
public class ToastUtil {

    private static Toast toast;

    public static void showToast(Context context, String text){
        if (toast == null){
            toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showBlankErorToast(Context context){
        showToast(context,"用户名或密码不能为空");
    }

}
