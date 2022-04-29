package com.zyf.androidlearn.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetStateUtil {

    static String NETWORK_STATE_ERROR = "网络连接错误";
    public static boolean checkNetworkState(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null){        //网络连接异常
            Toast.makeText(context,NETWORK_STATE_ERROR,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
