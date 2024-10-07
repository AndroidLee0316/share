package com.pasc.lib.share.dialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.pasc.lib.base.util.DensityUtils;
import com.pasc.lib.share.R;
import com.pasc.lib.share.ShareManager;
import com.pasc.lib.share.activity.ShareActivity;
import com.pasc.lib.share.adapter.ShareAdapter;
import com.pasc.lib.share.callback.ShareActionCallBack;
import com.pasc.lib.share.callback.ShareActionListener;
import com.pasc.lib.share.config.DialogConfig;
import com.pasc.lib.share.config.ShareContent;

import java.util.List;

/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2018/9/3
 * 分享的dialog
 */
public class ShareDialog extends Dialog {
    private View mView;
    private ConstraintLayout mContentArea;
    private RecyclerView mRecyclerView;
    private TextView mCancelBtn;
    private TextView mShareTitle;

    private ShareContent mShareBean;
    private Activity mActivity;

    private View.OnClickListener onCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismissDialog(-1);
        }
    };
    private List<DialogConfig.PlatformInfo> mPlatInfoList;

    private void initView() {
        mView = findViewById(R.id.dialog_share_touch_area);
        mContentArea = findViewById(R.id.dialog_share_content_area);
        mRecyclerView = findViewById(R.id.dialog_share_gr);
        mCancelBtn = findViewById(R.id.dialog_cancel_btn);
        mShareTitle = findViewById(R.id.tv_share_tip);

        DialogConfig dialogConfig = ShareManager.getInstance().getDialogConfig();
        if (dialogConfig != null) {
            String colorForTvTitle = dialogConfig.getColorForTvTitle();
            int textSizeForTvCancel = dialogConfig.getTextSizeForTvCancel();
            int textSizeForTvTitle = dialogConfig.getTextSizeForTvTitle();
            String colorForTvCancel = dialogConfig.getColorForTvCancel();

            if (colorForTvTitle != null &&
                    colorForTvTitle.startsWith("#")) {
                mShareTitle.setTextColor(Color.parseColor(colorForTvTitle));
            }
            if (colorForTvCancel != null &&
                    colorForTvCancel.startsWith("#")) {
                mCancelBtn.setTextColor(Color.parseColor(colorForTvCancel));
            }

            if (textSizeForTvTitle != 0) {
                mShareTitle.setTextSize(textSizeForTvTitle);
            }

            if (textSizeForTvCancel != 0) {
                mCancelBtn.setTextSize(textSizeForTvCancel);
            }
        }
    }


    public ShareDialog(@NonNull Activity activity, ShareContent shareBean, List<DialogConfig.PlatformInfo> platInfoList) {
        super(activity, R.style.ShareLibTranslucent);
        this.mShareBean = shareBean;
        this.mActivity = activity;
        this.mPlatInfoList = platInfoList;
    }

    /**
     * 发送分享
     */
    private void sendShare(@ShareManager.SharePlatform int platform) {
        ShareActivity.startActivity(mActivity, mShareBean, platform);
        dismissDialog(0);
        ShareActionCallBack shareActionCallBack = ShareManager.getInstance().getShareActionCallBack();
        if (shareActionCallBack != null && shareActionCallBack instanceof ShareActionListener) {
            ((ShareActionListener) shareActionCallBack).onPlatformClick(platform);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_lib_dialog_share);
        initView();
        setLayoutManagerStyle();
        setListener();
    }

    @NonNull
    private void setLayoutManagerStyle() {
        int mode = mPlatInfoList.size() > 4 ? ShareAdapter.MODE_LINEAR_LAYOUT : ShareAdapter.MODE_GRID_LAYOUT;
        ShareAdapter adapter = new ShareAdapter(mode, mPlatInfoList);
        mRecyclerView.setAdapter(adapter);
        if (mode == ShareAdapter.MODE_LINEAR_LAYOUT) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), mPlatInfoList.size()));
        }
        adapter.setOnItemClickListener(new ShareAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, @ShareManager.SharePlatform int platform) {
                sendShare(platform);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isShowing()) {
            dismissDialog(-1);
        } else {
            super.onBackPressed();
        }
    }

    private void setListener() {
        mView.setOnClickListener(onCancelClickListener);
        mCancelBtn.setOnClickListener(onCancelClickListener);
    }


    private void dismissDialog(final int code) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mContentArea, "translationY", 0, DensityUtils.dp2px(135));
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.4F, 0.0F);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(animatorUpdateListener);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator, valueAnimator);
        animatorSet.setDuration(200)
                .addListener(new SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dismiss();
                        ShareActionCallBack shareActionCallBack = ShareManager.getInstance().getShareActionCallBack();
                        if (code == -1 && shareActionCallBack != null) {
                            shareActionCallBack.onCancel(ShareManager.PLATFORM_NONE);
                        }
                    }
                });
        animatorSet.start();
    }

    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float alpha = (float) animation.getAnimatedValue();
            String alphaHex = Integer.toHexString((int) (255 * alpha));
            String color = "#" + (alphaHex.length() < 2 ? "0" + alphaHex : alphaHex) + "000000";
            mView.setBackgroundColor(Color.parseColor(color));
        }
    };

    @Override
    public void show() {
        super.show();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mContentArea, "translationY", DensityUtils.dp2px(135), 0);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0F, 0.4F);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(animatorUpdateListener);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator, valueAnimator);
        animatorSet.setDuration(200)
                .start();
    }

    private class SimpleAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}