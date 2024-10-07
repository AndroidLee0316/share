package com.pingan.nt;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/*
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2018/9/5
 * @des
 * @modify
 */
public class ResultActivity extends AppCompatActivity {
    public static final String KEY_RESULT = "key_result";
    public static final String KEY_TYPE = "key_type";
    private TextView mResult;

    public static void startActivity(Context context, String result) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(KEY_RESULT, result);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        //获取到ToolBar的id
        Toolbar mToolBar = findViewById(R.id.toolbar);
        mToolBar.setTitle("");
        //设置ToolBar
        setSupportActionBar(mToolBar);
        //设置NavigationIcon的监听事件
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mResult = findViewById(R.id.result);
        mResult.setText(getIntent().getStringExtra(KEY_RESULT));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
