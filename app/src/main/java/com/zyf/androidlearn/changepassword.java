package com.zyf.androidlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zyf.androidlearn.Bean.User;
import com.zyf.androidlearn.SQLite.MySQLiteOpenHelper;

public class changepassword extends AppCompatActivity {
    private EditText newuserName;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        initView();
        mySQLiteOpenHelper =new MySQLiteOpenHelper(this);
    }


    private void initView() {
        newuserName =findViewById(R.id.name3);

    }
    public void change2(View v){
        EditText pwdEdt1 =findViewById(R.id.pwd4);
        EditText pwdEdt2 =findViewById(R.id.pwd5);
        String pwd1 =pwdEdt1.getText().toString().trim();//trim（）去掉字符串前后的空格；多余的东西;
        String pwd2 =pwdEdt2.getText().toString().trim();//trim（）去掉字符串前后的空格；多余的东西;
        User user=new User();
        user.setPwd(pwd2);

        if (pwd1.equals("")||pwd2.equals("")){
            //如果为空，则提示
            //无焦点提示
            //参数1 环境上下文     //参数2提示性文本    参数提示时间
            Toast.makeText(this, "密码和重复密码位不能为空", Toast.LENGTH_SHORT).show();
        }else{
            if (pwd1.equals(pwd2)){

                //更新数据库中
                long rowId =mySQLiteOpenHelper.updateDataPassword(user);
                if (rowId > 0){
                    Toast.makeText(this,"密码修改成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"没有数据被更新",Toast.LENGTH_SHORT).show();
                }
                Intent intent=new Intent(changepassword.this,MainActivity.class);
                startActivity(intent);

            }else {
                Toast.makeText(this, "两次密码必须一致~", Toast.LENGTH_SHORT).show();
            }


        }
    }
}