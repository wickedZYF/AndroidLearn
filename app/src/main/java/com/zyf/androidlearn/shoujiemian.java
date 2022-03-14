package com.zyf.androidlearn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.zyf.androidlearn.R;
import com.zyf.androidlearn.Bean;

import java.util.ArrayList;
import java.util.List;

public class shoujiemian extends AppCompatActivity {
   //创建data列表和list匹配
    private List<Bean> data=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoujiemian);

        //在每项列表中添加文本
        for (int i = 0; i < 100; i++) {
            Bean bean=new Bean();
            bean.setTitle(""+i);
            data.add(bean);
        }
        //找到xml中的ListView项
        ListView listView=findViewById(R.id.lv);


    }

}