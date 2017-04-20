package com.binzeefox.materialdesignpasswordcollecter.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.widget.Toast;
import com.binzeefox.materialdesignpasswordcollecter.db.Account;
import org.litepal.crud.DataSupport;

/**
 * Created by tong.xiwen on 2017/4/10.
 */
public class ToastUtil {

    private static Toast toast;
    private static AlertDialog.Builder dialog;

    /**
     * 显示Toast
     * @param context context
     * @param text Toast文字
     */
    public static void showToast(Context context, String text){
        if (toast == null){
            toast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    /**
     * 显示预设Toast
     * @param context context
     */
    public static void showBlankErrorToast(Context context){
        showToast(context,"用户名或密码不能为空");
    }



}
