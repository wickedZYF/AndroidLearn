package com.zyf.androidlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zyf.androidlearn.SQLite.MySQLiteOpenHelper;

public class MainActivity extends AppCompatActivity {


    private MySQLiteOpenHelper mySQLiteOpenHelper;
//定义一个静态的Uesr记住当前登录成功的用户对象
    public static String LoginUser;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

            //调用数据库name列，查看是否有重复名称
            Cursor c1=mySQLiteOpenHelper.queryFromDbByName(name);
            if(c1!=null && c1.getCount() >= 1){
                //调用数据库password列，查看是否有重复密码
                Cursor c2=mySQLiteOpenHelper.queryFromDbByPassword(pwd);
                if (c2!=null && c2.getCount() >= 1) {

                    LoginUser =name;
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

                                    startActivity(intent);

                                }

                            }
                        }
                    }.start();
                    c2.close();
                }else {
                    Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                }


                c1.close();
            }else {
                Toast.makeText(this, "不存该用户", Toast.LENGTH_SHORT).show();
            }


        }

    }
}

