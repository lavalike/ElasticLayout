package com.wangzhen.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity
 * Created by wangzhen on 2019/2/22.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_web).setOnClickListener(this);
        findViewById(R.id.btn_scroll).setOnClickListener(this);
        findViewById(R.id.btn_list).setOnClickListener(this);
        findViewById(R.id.btn_recycler).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_web:
                startActivity(new Intent(this, WebActivity.class));
                break;
            case R.id.btn_scroll:
                startActivity(new Intent(this, ScrollActivity.class));
                break;
            case R.id.btn_list:
                startActivity(new Intent(this, ListActivity.class));
                break;
            case R.id.btn_recycler:
                startActivity(new Intent(this, RecyclerActivity.class));
                break;
        }
    }
}
