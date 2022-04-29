
package com.zyf.androidlearn.List_item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zyf.androidlearn.Adapter.MyAdapter2;
import com.zyf.androidlearn.Bean.Note;
import com.zyf.androidlearn.MainActivity;
import com.zyf.androidlearn.R;
import com.zyf.androidlearn.SQLite.NoteDbOpenHelper;
import com.zyf.androidlearn.utils.NetStateUtil;
import com.zyf.androidlearn.utils.ToastUtil;
import com.zyf.androidlearn.zhuCe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zyf.androidlearn.MainActivity.LoginUser;

public class AddActivity extends AppCompatActivity {

    private EditText etTitle,etContent;
    private NoteDbOpenHelper mNoteDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //隐藏ActionBar
        getSupportActionBar().hide();
        //设置页面全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);



        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);

    }


    public void add2(View view) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        String name=LoginUser;
        String created_time= getCurrentTimeFormat();
        if (TextUtils.isEmpty(title)) {
            ToastUtil.toastShort(this, "标题不能为空！");
            return;
        }

        Note note = new Note();

        note.setTitle(title);
        note.setContent(content);
        note.setCreateTime(getCurrentTimeFormat());
        note.setName(name);

        //2. 网络为连接则直接返回
        if(!NetStateUtil.checkNetworkState(AddActivity.this))
            return;
        //3. 发起http请求
        OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

        FormEncodingBuilder builder = new FormEncodingBuilder();
        RequestBody requestBody = builder.add("title", title)
                .add("content",content)
                .add("create_time",created_time)
                .add("name",name)
                .build();                                          //需要传输的数据存入requestBody

        Request request = new Request.Builder()
                .url("http://192.168.0.105:8080/addNote")							 //需要的url
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
                        Toast.makeText(AddActivity.this,"Fail",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AddActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();

                            intent.setClass(AddActivity.this, Mycangku.class);
                            AddActivity.this.finish();

                            startActivity(intent);
                        }
                        else
                            Toast.makeText(AddActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

//        //添加一行数据库的信息
//        long row = mNoteDbOpenHelper.insertData(note);
//        if (row != -1) {
//            ToastUtil.toastShort(this,"添加成功！");
//            this.finish();
//        }else {
//            ToastUtil.toastShort(this,"添加失败！");
//        }

    }
    //定义时间格式，调用↑
    private String getCurrentTimeFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY年MM月dd HH:mm:ss");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }
}