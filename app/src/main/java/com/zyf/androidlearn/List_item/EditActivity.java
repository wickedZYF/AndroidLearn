package com.zyf.androidlearn.List_item;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.zyf.androidlearn.Bean.Note;
import com.zyf.androidlearn.R;
import com.zyf.androidlearn.SQLite.NoteDbOpenHelper;
import com.zyf.androidlearn.utils.NetStateUtil;
import com.zyf.androidlearn.utils.ToastUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.zyf.androidlearn.List_item.Mycangku.mNotes;
import static com.zyf.androidlearn.MainActivity.LoginUser;


public class EditActivity extends AppCompatActivity {

    private Note note;
    private EditText etTitle,etContent;
    private NoteDbOpenHelper mNoteDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //隐藏ActionBar
        getSupportActionBar().hide();
        //设置页面全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);

        initData();

    }

    private void initData() {
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note");
        if (note != null) {
            etTitle.setText(note.getTitle());
            etContent.setText(note.getContent());
        }
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);
    }

    public void save(View view) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        String name=LoginUser;
        String created_time= getCurrentTimeFormat();
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note");
        String id=note.getId();




        if (TextUtils.isEmpty(title)) {
            ToastUtil.toastShort(this, "标题不能为空！");
            return;
        }




        //2. 网络为连接则直接返回
        if(!NetStateUtil.checkNetworkState(EditActivity.this))
            return;
        //3. 发起http请求
        OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

        FormEncodingBuilder builder = new FormEncodingBuilder();
        RequestBody requestBody = builder.add("id",id)
                .add("newtitle", title)
                .add("newcontent",content)
                .add("newcreateTime",created_time)
                .build();                                          //需要传输的数据存入requestBody

        Request request = new Request.Builder()
                .url("http://192.168.0.105:8080/uNote")							 //需要的url
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
                        Toast.makeText(EditActivity.this,"Fail",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(EditActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();

                            intent.setClass(EditActivity.this, Mycangku.class);
                            EditActivity.this.finish();
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(EditActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

//        long rowId = mNoteDbOpenHelper.updateData(note);  //调用更新数据库的功能
//        if (rowId != -1) {
//            ToastUtil.toastShort(this, "修改成功！");
//            this.finish();
//        }else{
//            ToastUtil.toastShort(this, "修改失败！");
//        }
    }
  

    private String getCurrentTimeFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY年MM月dd日 HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }
}