package com.zyf.androidlearn.List_item;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zyf.androidlearn.MainActivity;
import com.zyf.androidlearn.R;
import com.zyf.androidlearn.viewpager;

public class Mycangku extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycangku);
    }

    public void add(View view) {
        Intent intent = new Intent();

        intent.setClass(Mycangku.this, AddActivity.class);

        startActivity(intent);
    }
}