package com.zyf.androidlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import static com.zyf.androidlearn.MainActivity.LoginUser;

public class changename extends AppCompatActivity {
    private EditText newuserName;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changename);

        //隐藏ActionBar
        getSupportActionBar().hide();
        //设置页面全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        initView();
        mySQLiteOpenHelper =new MySQLiteOpenHelper(this);
    }

    private void initView() {
        newuserName =findViewById(R.id.name3);

    }
    public void change(View v){


        //判断姓名密码是否为空
        EditText nameEdt =findViewById(R.id.name3);
        String name =nameEdt.getText().toString();

        String newName =newuserName.getText().toString().trim();//trim（）去掉字符串前后的空格；多余的东西

        User user=new User();
        user.setName(newName);

        if (name.equals("")){
            //如果为空，则提示
            //无焦点提示
            //参数1 环境上下文     //参数2提示性文本    参数提示时间
            Toast.makeText(this, "姓名密码不能为空", Toast.LENGTH_SHORT).show();
        }else {

            //2. 网络为连接则直接返回
            if(!NetStateUtil.checkNetworkState(changename.this))
                return;
            //3. 发起http请求
            OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

            FormEncodingBuilder builder = new FormEncodingBuilder();
            RequestBody requestBody = builder.add("oldname", LoginUser)
                    .add("newname",newName)
                    .build();                                          //需要传输的数据存入requestBody

            Request request = new Request.Builder()
                    .url("http://192.168.0.105:8080/cname")							 //需要的url
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
                            Toast.makeText(changename.this,"Fail",Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(changename.this,"修改成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();

                                intent.setClass(changename.this, MainActivity.class);

                                startActivity(intent);
                            }
                            else
                                Toast.makeText(changename.this,"修改失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

//            //更新数据库中
//            long rowId =mySQLiteOpenHelper.updateDataName(user);
//            if (rowId > 0){
//                Toast.makeText(this,"更新成功",Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(this,"没有数据被更新",Toast.LENGTH_SHORT).show();
//            }



            Intent intent=new Intent(changename.this,MainActivity.class);
            startActivity(intent);

        }

    }
}