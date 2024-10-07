package com.pingan.nt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pasc.lib.share.ShareManager;
import com.pasc.lib.share.callback.AuthActionCallBack;

/**
 * Copyright (C) 2019 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2019/3/19
 */
public class GetInfoSampleActivity extends AppCompatActivity {

    //授权回调信息，可以在这里获取基本的授权返回的信息
    private AuthActionCallBack authActionCallBack = new AuthActionCallBack() {
        @Override
        public void onComplete(int platform, String authJson) {
            //输出所有授权信息
            Log.d("cdx", authJson);
            if (platform == ShareManager.PLATFORM_QQ)
                ResultActivity.startActivity(GetInfoSampleActivity.this, "获取QQ用户资料成功，结果是：" + authJson);
        }

        @Override
        public void onCancel(int platform) {
            if (platform == ShareManager.PLATFORM_QQ)
                ResultActivity.startActivity(GetInfoSampleActivity.this, "获取QQ用户资料取消");
        }

        @Override
        public void onError(int platform, Throwable var3) {
            if (platform == ShareManager.PLATFORM_QQ)
                ResultActivity.startActivity(GetInfoSampleActivity.this, "获取QQ用户资料失败，失败原因：" + var3.getMessage());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_demo);

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


        findViewById(R.id.wx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GetInfoSampleActivity.this, "南通百通没有开通微信快捷登录，不能获取微信用户资料", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareManager.getInstance().authorizeForQQ(GetInfoSampleActivity.this, authActionCallBack);
            }
        });

        TextView title = findViewById(R.id.toolbar_title);
        title.setText("获取用户资料");

    }
}
