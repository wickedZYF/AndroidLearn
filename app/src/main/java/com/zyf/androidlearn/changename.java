package com.zyf.androidlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class changename extends AppCompatActivity {
    private EditText newuserName;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changename);

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

        if (name.equals("")){
            //如果为空，则提示
            //无焦点提示
            //参数1 环境上下文     //参数2提示性文本    参数提示时间
            Toast.makeText(this, "姓名密码不能为空", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent=new Intent(changename.this,MainActivity.class);
            startActivity(intent);

        }

    }
}