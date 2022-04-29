package com.zyf.androidlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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
import java.util.Random;

public class zhuCe extends AppCompatActivity {

    private EditText userName,password1,password2;

    private MySQLiteOpenHelper mySQLiteOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhu_ce);

        //隐藏ActionBar
        getSupportActionBar().hide();
        //设置页面全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        initView();
        mySQLiteOpenHelper =new MySQLiteOpenHelper(this);

    }

    private void initView() {
         userName =findViewById(R.id.name2);
         password1 =findViewById(R.id.pwd2);
         password2 =findViewById(R.id.pwd3);
    }

    public void zhuce(View v){

        String name =userName.getText().toString().trim();//trim（）去掉字符串前后的空格；多余的东西
        String pwd1 =password1.getText().toString().trim();
        String pwd2 =password2.getText().toString().trim();

//     //uid生成方法
//        String uid = createCardId();
//
//        User user=new User();
//
//        user.setName(name);
//        user.setPwd(pwd1);
//        user.setCardId(uid);

        if (name.equals("")||pwd1.equals("")||pwd2.equals("")){
            //如果为空，则提示
            //无焦点提示
            //参数1 环境上下文     //参数2提示性文本    参数提示时间
            Toast.makeText(this, "姓名密码重复密码位不能为空", Toast.LENGTH_SHORT).show();
        }else {
            if (pwd1.equals(pwd2)){

                //2. 网络为连接则直接返回
                if(!NetStateUtil.checkNetworkState(zhuCe.this))
                    return;
                //3. 发起http请求
                OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

                FormEncodingBuilder builder = new FormEncodingBuilder();
                RequestBody requestBody = builder.add("name", name)
                        .add("pwd",pwd2)
                        .build();                                          //需要传输的数据存入requestBody

                Request request = new Request.Builder()
                        .url("http://192.168.0.105:8080/register")							 //需要的url
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
                                Toast.makeText(zhuCe.this,"Fail",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        //成功获取响应时
                        //不要toString
                        String responseData = response.body().string();
                        //解析，responseData，获取响应数据
                        final boolean data = JSON.parseObject(responseData,boolean.class);      //将所接受的json数据转换成boolean对象

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                //显示回调函数
//                                Toast.makeText(zhuCe.this,"data"+data,Toast.LENGTH_SHORT).show();
                                if(data == true){
                                    Toast.makeText(zhuCe.this,"注册成功",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();

                                    intent.setClass(zhuCe.this, MainActivity.class);

                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(zhuCe.this,"账号已被注册",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }else {
                Toast.makeText(this, "两次密码必须一致~", Toast.LENGTH_SHORT).show();
            }
        }





    }








    /**
     * 为用户创建8位uid
     * @return
     */
    private String createCardId() {
        String cardId="";
        while (true) {
            Random r =new Random();
            for (int i = 0; i < 8; i++) {
                cardId +=r.nextInt(10);
            }
                                                         //筛出来的的8位uid
            Cursor c=mySQLiteOpenHelper.queryFromDbByCardId(cardId);
            if(c.moveToFirst()==false){
                c.close();
                break;
            }
            c.close();
        }
        return cardId;
    }
    }



