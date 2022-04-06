package com.zyf.androidlearn.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
//封装一下弹框的功能，再有弹框直接调用就行 ————>AddActivity用了
    public static void toastShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
