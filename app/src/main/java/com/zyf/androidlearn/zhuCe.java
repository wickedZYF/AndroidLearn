package com.zyf.androidlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.zyf.androidlearn.Bean.User;
import com.zyf.androidlearn.SQLite.MySQLiteOpenHelper;

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

     //uid生成方法
        String uid = createCardId();

        User user=new User();

        user.setName(name);
        user.setPwd(pwd1);
        user.setCardId(uid);


            if (name.equals("")||pwd1.equals("")||pwd2.equals("")){
                //如果为空，则提示
                //无焦点提示
                //参数1 环境上下文     //参数2提示性文本    参数提示时间
                Toast.makeText(this, "姓名密码重复密码位不能为空", Toast.LENGTH_SHORT).show();
            }else {
                if (pwd1.equals(pwd2)){
                    //调用数据库name列，查看是否有重复名称
                    Cursor c=mySQLiteOpenHelper.queryFromDbByName(name);
                    if(c!=null && c.getCount() >= 1){
                               Toast.makeText(this, "该用户已存在", Toast.LENGTH_SHORT).show();
                                c.close();
                    }else {
                        //插入数据库中
                        long rowId =mySQLiteOpenHelper.insertData(user);
                        if (rowId!=-1){
                            Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();

                            intent.setClass(zhuCe.this, MainActivity.class);

                            startActivity(intent);
                        }else {
                            Toast.makeText(this,"注册失败",Toast.LENGTH_SHORT).show();
                        }
                    }

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



