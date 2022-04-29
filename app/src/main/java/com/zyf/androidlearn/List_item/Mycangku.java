package com.zyf.androidlearn.List_item;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.zyf.androidlearn.utils.SpfUtil;
import com.zyf.androidlearn.viewpager;

import java.io.IOException;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zyf.androidlearn.MainActivity.LoginUser;

public class Mycangku extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private FloatingActionButton mBtnAdd;
    public static List<Note> mNotes;
    private MyAdapter2 mMyAdapter;

    private NoteDbOpenHelper mNoteDbOpenHelper;
    public static final int MODE_LINEAR = 0;
    public static final int MODE_GRID = 1;

    public static final String KEY_LAYOUT_MODE = "key_layout_mode";

    private int currentListLayoutMode = MODE_LINEAR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycangku);

        initView();
        initData();
        initEvent();
    }

    //Activity生命周期，添加完数据会自动再查，以便显示数据
    @Override
    protected void onResume() {
        super.onResume();
        refreshDataFromDb();
        setListLayout();
    }

    private void setListLayout() {
        currentListLayoutMode = SpfUtil.getIntWithDefault(this, KEY_LAYOUT_MODE, MODE_LINEAR);
        if (currentListLayoutMode == MODE_LINEAR) {
            setToLinearList();
        }else{
            setToGridList();
        }
    }

    private void refreshDataFromDb() {
        mNotes = getDataFromDB();
        //告诉我们的适配器MyAdapter2进行刷新
        //调用MyAdapter2中的refreshData方法把新数据传进去
        mMyAdapter.refreshData(mNotes);
    }

    private void initEvent() {
        mMyAdapter=new MyAdapter2(this,mNotes);

        mRecyclerView.setAdapter(mMyAdapter);

//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mMyAdapter.setViewType(MyAdapter2.TYPE_LINEAR_LAYOUT);//调用线性布局
        setListLayout();

    }

    private void initData() {
        mNotes =new ArrayList<>();
        mNoteDbOpenHelper =new NoteDbOpenHelper(this);
        

//        for (int i = 0; i < 30; i++) {
//            Note note=new Note();
//            note.setTitle("动漫：更衣室人偶资源"+i);
//            note.setContent("熊曰：哦啊哦啊哦噢噢噢哇啊撒阿瓦我我我我我");
//            note.setCreatedTime(getCurrentTimeFormat());
//            mNotes.add(note);
//        }
        mNotes = getDataFromDB();


    }
//从数据库中获取数据
    public List<Note> getDataFromDB() {
        String name=LoginUser;
        List<Note> notes=new ArrayList<>();

        //3. 发起http请求
        OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

        FormEncodingBuilder builder = new FormEncodingBuilder();
        RequestBody requestBody = builder.add("name", name)
                .build();                                          //需要传输的数据存入requestBody

        Request request = new Request.Builder()
                .url("http://192.168.0.105:8080/getUser")                     //需要的url
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {                 //开启异步进程
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //连接超时时的提醒
                        Toast.makeText(Mycangku.this,"Fail",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //成功获取响应时
                //不要toString
                String responseData = response.body().string();
                //解析，responseData，获取响应数据
                 List<Note> noteList = JSON.parseObject(responseData,List.class);      //将所接受的json数据转换成boolean对象

                System.out.println(noteList);

                SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
                //步骤2： 实例化SharedPreferences.Editor对象
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //步骤3：将获取过来的值放入文件
                Gson gson = new Gson();
                String str = gson.toJson(noteList);
                editor.putString("key_data",str);
                //步骤4：提交
                editor.commit();

            }
        });

        SharedPreferences sharedPreferences= getSharedPreferences("data", Context .MODE_PRIVATE);
        String listJson=sharedPreferences.getString("key_data","");
        if (!listJson.equals("")) {
            Gson gson = new Gson();
            notes = gson.fromJson(listJson, new TypeToken <List<Note>>() {
            }.getType());

        }
        System.out.println(notes);
        return notes;
//        return mNoteDbOpenHelper.queryUserFromDb(name);


    }




    //日期
    private String getCurrentTimeFormat(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYY年MM月dd HH:mm:ss");//定义日期格式
        Date date=new Date();
        return simpleDateFormat.format(date);
    }

    private void initView() {

        mRecyclerView=findViewById(R.id.rlv);
    }


    public void add(View view) {
        Intent intent = new Intent();

        intent.setClass(Mycangku.this, AddActivity.class);
//        Mycangku.this.finish();

        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override//每当输入文本改变，更新
            public boolean onQueryTextChange(String newText) {
                mNotes = mNoteDbOpenHelper.queryFromDbByTitle2(newText);  //调用数据库模糊查询进行查询
                mMyAdapter.refreshData(mNotes);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //搜索框右边的两个布局选项
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()) {
            //线性布局
            case R.id.menu_linear:
//                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//                mRecyclerView.setLayoutManager(linearLayoutManager);
//                mMyAdapter.setViewType(MyAdapter2.TYPE_LINEAR_LAYOUT);//设置线性布局
//                mMyAdapter.notifyDataSetChanged();

                setToLinearList();

                currentListLayoutMode =MODE_LINEAR;
                SpfUtil.saveInt(this,KEY_LAYOUT_MODE,MODE_LINEAR);

                return true;
            //网格布局
            case R.id.menu_grid:
                                                                                        //两列
//                GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
//                mRecyclerView.setLayoutManager(gridLayoutManager);
//                mMyAdapter.setViewType(MyAdapter2.TYPE_GRID_LAYOUT);//设置网格布局
//                mMyAdapter.notifyDataSetChanged();
                setToGridList();
                currentListLayoutMode =MODE_GRID;
                SpfUtil.saveInt(this,KEY_LAYOUT_MODE,MODE_GRID);

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private void setToLinearList() {
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mMyAdapter.setViewType(MyAdapter2.TYPE_LINEAR_LAYOUT);//设置线性布局
        mMyAdapter.notifyDataSetChanged();
    }


    private void setToGridList() {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMyAdapter.setViewType(MyAdapter2.TYPE_GRID_LAYOUT);//设置网格布局
        mMyAdapter.notifyDataSetChanged();
    }

    //每次每次打开就会知道你那种布局
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentListLayoutMode == MODE_LINEAR) {//如果当布局是线性，就把线性布局给找到
            MenuItem item = menu.findItem(R.id.menu_linear);
            item.setChecked(true);
        } else {   //反之就是网格布局
            menu.findItem(R.id.menu_grid).setChecked(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }


}