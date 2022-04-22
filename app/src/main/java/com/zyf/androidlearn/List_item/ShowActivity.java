package com.zyf.androidlearn.List_item;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zyf.androidlearn.Bean.Note;
import com.zyf.androidlearn.R;
import com.zyf.androidlearn.SQLite.NoteDbOpenHelper;
import com.zyf.androidlearn.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.zyf.androidlearn.MainActivity.LoginUser;

public class ShowActivity extends AppCompatActivity {

    private Note note;
    private EditText etTitle,etContent;
    private TextView showUser;
    private NoteDbOpenHelper mNoteDbOpenHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        //隐藏ActionBar
        getSupportActionBar().hide();
        //设置页面全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        showUser = findViewById(R.id.ShowUser);


        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note");
        if (note != null) {
            etTitle.setText(note.getTitle());
            etContent.setText(note.getContent());
            showUser.setText("分享人："+note.getName());
        }
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);
    }



}