package com.pasc.lib.share.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.pasc.lib.share.R;

/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2018/9/3
 * 分享loading框
 */
public class ShareLoadingDialog extends AppCompatDialog {

    private ImageView mLoadingIcon;
    private Activity mActivity;

    public ShareLoadingDialog(Activity activity) {
        super(activity, R.style.ShareLibLoadingDialog);
        mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_lib_loading_layout);
        mLoadingIcon = findViewById(R.id.share_lib_loading_icon);
    }

    @Override
    public void show() {
        super.show();
        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatMode(Animation.RESTART);
        animation.setRepeatCount(-1);
        animation.setInterpolator(new LinearInterpolator());
        mLoadingIcon.startAnimation(animation);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mActivity != null) {
            mActivity.finish();
        }
    }

    @Override
    protected void onStop() {
        mLoadingIcon.clearAnimation();
        super.onStop();
    }
}
