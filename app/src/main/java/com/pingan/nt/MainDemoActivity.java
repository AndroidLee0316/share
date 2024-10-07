package com.pingan.nt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Copyright (C) 2019 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2019/3/19
 */
public class MainDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_demo);

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享
                startActivity(new Intent(MainDemoActivity.this, ShareSampleActivity.class));
            }
        });
        findViewById(R.id.auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //第三方登陆
                startActivity(new Intent(MainDemoActivity.this, AuthSampleActivity.class));
            }
        });
        findViewById(R.id.user_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户资料
                startActivity(new Intent(MainDemoActivity.this, GetInfoSampleActivity.class));
            }
        });

    }
}
