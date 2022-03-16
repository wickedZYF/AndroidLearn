package com.zyf.androidlearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class viewpager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);


        //创建存放viewpager的list
        List<View> viewList =new ArrayList<>();
        LayoutInflater lf=getLayoutInflater().from(this);
        MyAdapter myAdapter = new MyAdapter(viewList);

        View view1=lf.inflate(R.layout.activity_shoujiemian,null);  //将activity_shoujiemian和activity_function的界面
        View view2=lf.inflate(R.layout.activity_function,null);     //放入viewpager
        viewList.add(view1);
        viewList.add(view2);

        ViewPager viewPager=findViewById(R.id.vp);   //定位到activity_viewpager


        viewPager.setAdapter(myAdapter);



    }
}