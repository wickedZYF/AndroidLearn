package com.zyf.androidlearn;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zyf.androidlearn.Bean.User;
import com.zyf.androidlearn.SQLite.MySQLiteOpenHelper;
import com.zyf.androidlearn.utils.NetStateUtil;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private MySQLiteOpenHelper mySQLiteOpenHelper;
//定义一个LoginUser记住当前登录成功的用户对象
    public static String LoginUser;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //隐藏ActionBar
        getSupportActionBar().hide();
        //设置页面全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mySQLiteOpenHelper =new MySQLiteOpenHelper(this);

//        TextView tv_one=findViewById(R.id.tv_one);
//        tv_one.setText("WickedWickedWickedWickedWickedWicked");

        //找到button这个view
        Button button= findViewById(R.id.zhuCe1);
        //设置点击事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到zhuCe界面
                Intent intent=new Intent(MainActivity.this,zhuCe.class);
                startActivity(intent);
            }
        });
    }





    public void login(View v){
        //判断姓名密码是否为空
        EditText nameEdt =findViewById(R.id.name1);
        EditText pwdEdt =findViewById(R.id.pwd1);
        String name =nameEdt.getText().toString().trim();
        String pwd =pwdEdt.getText().toString().trim();
        ProgressBar proBar=findViewById(R.id.pro_bar);
        if (name.equals("")||pwd.equals("")){
            //如果为空，则提示
            //无焦点提示
                          //参数1 环境上下文     //参数2提示性文本    参数提示时间
            Toast.makeText(this, "姓名密码不能为空", Toast.LENGTH_SHORT).show();
        }else{

            //2. 网络为连接则直接返回
            if(!NetStateUtil.checkNetworkState(MainActivity.this))
                return;

            //3. 发起http请求
            OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

            FormEncodingBuilder builder = new FormEncodingBuilder();
            RequestBody requestBody = builder.add("name", name)
                    .add("pwd",pwd)
                    .build();                                          //需要传输的数据存入requestBody

            Request request = new Request.Builder()
                    .url("http://192.168.0.105:8080/login")							 //需要的url
                    .post(requestBody)                      //注册时需要向服务端上传数据，所以使用post
                    .build();

            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {						//开启异步进程
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //连接超时时的提醒
                            Toast.makeText(MainActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    final String responseData = response.body().string();                         //响应数据
                    final User user = JSON.parseObject(responseData, User.class);      //将所接受的json数据转换成User对象

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //显示回调函数
                            if(user!=null)      //找到这个user的时候页面跳转
                            {
                                LoginUser=name;
                                //则出现进度条
                                proBar.setVisibility(View.VISIBLE);
                                new Thread() {
                                    public void run() {
                                        for (int i = 0; i <= 100; i++) {
                                            proBar.setProgress(i);
                                            try {
                                                Thread.sleep(8);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                            if (proBar.getProgress() == 100) {

                                                Intent intent = new Intent();

                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //登录后返回键失效

                                                intent.setClass(MainActivity.this, viewpager.class);

                                                intent.putExtra("name",user.getName());
                                                intent.putExtra("cardId",user.getCardId());

                                                startActivity(intent);

                                            }

                                        }
                                    }
                                }.start();

                            }
                            else                //错误提示
                                Toast.makeText(MainActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            });


        }

    }
}

//则出现进度条
//                    proBar.setVisibility(View.VISIBLE);
//                    new Thread() {
//                        public void run() {
//                            for (int i = 0; i <= 100; i++) {
//                                proBar.setProgress(i);
//                                try {
//                                    Thread.sleep(8);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//
//                                if (proBar.getProgress() == 100) {
//
//                                    Intent intent = new Intent();
//
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //登录后返回键失效
//
//                                    intent.setClass(MainActivity.this, viewpager.class);
//
//                                    startActivity(intent);
//
//                                }
//
//                            }
//                        }
//                    }.start();


//                                Intent intent = new Intent();
//                                intent.setClass(MainActivity.this, viewpager.class);
//                                intent.putExtra("name",user.getName());
//                                startActivity(intent);