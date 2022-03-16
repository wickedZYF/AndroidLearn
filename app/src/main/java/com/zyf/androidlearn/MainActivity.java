package com.zyf.androidlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        String name =nameEdt.getText().toString();
        String pwd =pwdEdt.getText().toString();
        ProgressBar proBar=findViewById(R.id.pro_bar);
        if (name.equals("")||pwd.equals("")){
            //如果为空，则提示
            //无焦点提示
                          //参数1 环境上下文     //参数2提示性文本    参数提示时间
            Toast.makeText(this, "姓名密码不能为空", Toast.LENGTH_SHORT).show();
        }else{
            //都不为空，则出现进度条
          proBar.setVisibility(View.VISIBLE);
          new Thread(){
              public void run(){
                  for (int i = 0; i <=100; i++) {
                      proBar.setProgress(i);
                      try {
                          Thread.sleep(8);
                      }catch (InterruptedException e){
                          e.printStackTrace();
                      }

                      if (proBar.getProgress()==100) {

                          Intent intent=new Intent();

                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); //登录后返回键失效

                          intent.setClass(MainActivity.this,viewpager.class);

                          startActivity(intent);

                      }

                  }
              }
          }.start();


        }

    }
}