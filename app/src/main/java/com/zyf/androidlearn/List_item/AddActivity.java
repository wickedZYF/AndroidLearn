
package com.zyf.androidlearn.List_item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zyf.androidlearn.Adapter.MyAdapter2;
import com.zyf.androidlearn.Bean.Note;
import com.zyf.androidlearn.R;
import com.zyf.androidlearn.SQLite.NoteDbOpenHelper;
import com.zyf.androidlearn.utils.ToastUtil;

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
        if (TextUtils.isEmpty(title)) {
            ToastUtil.toastShort(this, "标题不能为空！");
            return;
        }

        Note note = new Note();

        note.setTitle(title);
        note.setContent(content);
        note.setCreatedTime(getCurrentTimeFormat());
        note.setName(name);

        //添加一行数据库的信息
        long row = mNoteDbOpenHelper.insertData(note);
        if (row != -1) {
            ToastUtil.toastShort(this,"添加成功！");
            this.finish();
        }else {
            ToastUtil.toastShort(this,"添加失败！");
        }

    }
    //定义时间格式，调用↑
    private String getCurrentTimeFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY年MM月dd HH:mm:ss");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }
}