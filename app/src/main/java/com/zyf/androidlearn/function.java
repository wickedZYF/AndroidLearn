package com.zyf.androidlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.zyf.androidlearn.MainActivity.LoginUser;

public class function extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        //找到button这个view
        Button button1= findViewById(R.id.button2);
        //设置点击事件
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到changename

                Intent intent=new Intent(function.this,changename.class);
                startActivity(intent);
            }
        });

        //找到button这个view
        Button button2= findViewById(R.id.button3);
        //设置点击事件
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到changepassword
                Intent intent=new Intent(function.this,changepassword.class);
                startActivity(intent);
            }
        });




    }
}