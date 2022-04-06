package com.zyf.androidlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.zyf.androidlearn.Bean.User;
import com.zyf.androidlearn.SQLite.MySQLiteOpenHelper;

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

            //更新数据库中
            long rowId =mySQLiteOpenHelper.updateDataName(user);
            if (rowId > 0){
                Toast.makeText(this,"更新成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"没有数据被更新",Toast.LENGTH_SHORT).show();
            }

            Intent intent=new Intent(changename.this,MainActivity.class);
            startActivity(intent);

        }

    }
}