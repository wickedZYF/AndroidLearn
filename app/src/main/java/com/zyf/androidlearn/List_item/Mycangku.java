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

    //Activity??????????????????????????????????????????????????????????????????
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
        //????????????????????????MyAdapter2????????????
        //??????MyAdapter2??????refreshData???????????????????????????
        mMyAdapter.refreshData(mNotes);
    }

    private void initEvent() {
        mMyAdapter=new MyAdapter2(this,mNotes);

        mRecyclerView.setAdapter(mMyAdapter);

//        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mMyAdapter.setViewType(MyAdapter2.TYPE_LINEAR_LAYOUT);//??????????????????
        setListLayout();

    }

    private void initData() {
        mNotes =new ArrayList<>();
        mNoteDbOpenHelper =new NoteDbOpenHelper(this);
        

//        for (int i = 0; i < 30; i++) {
//            Note note=new Note();
//            note.setTitle("??????????????????????????????"+i);
//            note.setContent("???????????????????????????????????????????????????????????????");
//            note.setCreatedTime(getCurrentTimeFormat());
//            mNotes.add(note);
//        }
        mNotes = getDataFromDB();


    }
//???????????????????????????
    public List<Note> getDataFromDB() {
        String name=LoginUser;
        List<Note> notes=new ArrayList<>();

        //3. ??????http??????
        OkHttpClient okHttpClient = new OkHttpClient();     //??????OkHttpClient??????

        FormEncodingBuilder builder = new FormEncodingBuilder();
        RequestBody requestBody = builder.add("name", name)
                .build();                                          //???????????????????????????requestBody

        Request request = new Request.Builder()
                .url("http://192.168.0.105:8080/getUser")                     //?????????url
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {                 //??????????????????
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //????????????????????????
                        Toast.makeText(Mycangku.this,"Fail",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //?????????????????????
                //??????toString
                String responseData = response.body().string();
                //?????????responseData?????????????????????
                 List<Note> noteList = JSON.parseObject(responseData,List.class);      //???????????????json???????????????boolean??????

                System.out.println(noteList);

                SharedPreferences sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
                //??????2??? ?????????SharedPreferences.Editor??????
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //??????3????????????????????????????????????
                Gson gson = new Gson();
                String str = gson.toJson(noteList);
                editor.putString("key_data",str);
                //??????4?????????
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




    //??????
    private String getCurrentTimeFormat(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYY???MM???dd HH:mm:ss");//??????????????????
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

            @Override//?????????????????????????????????
            public boolean onQueryTextChange(String newText) {
                mNotes = mNoteDbOpenHelper.queryFromDbByTitle2(newText);  //???????????????????????????????????????
                mMyAdapter.refreshData(mNotes);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //????????????????????????????????????
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()) {
            //????????????
            case R.id.menu_linear:
//                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
//                mRecyclerView.setLayoutManager(linearLayoutManager);
//                mMyAdapter.setViewType(MyAdapter2.TYPE_LINEAR_LAYOUT);//??????????????????
//                mMyAdapter.notifyDataSetChanged();

                setToLinearList();

                currentListLayoutMode =MODE_LINEAR;
                SpfUtil.saveInt(this,KEY_LAYOUT_MODE,MODE_LINEAR);

                return true;
            //????????????
            case R.id.menu_grid:
                                                                                        //??????
//                GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
//                mRecyclerView.setLayoutManager(gridLayoutManager);
//                mMyAdapter.setViewType(MyAdapter2.TYPE_GRID_LAYOUT);//??????????????????
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
        mMyAdapter.setViewType(MyAdapter2.TYPE_LINEAR_LAYOUT);//??????????????????
        mMyAdapter.notifyDataSetChanged();
    }


    private void setToGridList() {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMyAdapter.setViewType(MyAdapter2.TYPE_GRID_LAYOUT);//??????????????????
        mMyAdapter.notifyDataSetChanged();
    }

    //?????????????????????????????????????????????
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentListLayoutMode == MODE_LINEAR) {//??????????????????????????????????????????????????????
            MenuItem item = menu.findItem(R.id.menu_linear);
            item.setChecked(true);
        } else {   //????????????????????????
            menu.findItem(R.id.menu_grid).setChecked(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }


}