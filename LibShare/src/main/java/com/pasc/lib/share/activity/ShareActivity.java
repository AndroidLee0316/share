package com.pasc.lib.share.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.pasc.lib.share.ShareManager;
import com.pasc.lib.share.callback.AuthActionCallBack;
import com.pasc.lib.share.callback.ShareActionCallBack;
import com.pasc.lib.share.config.ShareContent;
import com.pasc.lib.share.dialog.ShareLoadingDialog;
import com.pasc.lib.share.util.ShareUtils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import static com.pasc.lib.share.ShareManager.PLATFORM_COPY_LINK;
import static com.pasc.lib.share.ShareManager.PLATFORM_EMAIL;
import static com.pasc.lib.share.ShareManager.PLATFORM_MORE;
import static com.pasc.lib.share.ShareManager.PLATFORM_QQ;
import static com.pasc.lib.share.ShareManager.PLATFORM_QZONE;
import static com.pasc.lib.share.ShareManager.PLATFORM_SMS;
import static com.pasc.lib.share.ShareManager.PLATFORM_WX;
import static com.pasc.lib.share.ShareManager.PLATFORM_WX_CIRCLE;

/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2018/9/3
 * 分享activity 1、处理拦截 Tencent.onActivityResultData
 */
public class ShareActivity extends AppCompatActivity {
    public static final String KEY_SHARE_BEAN = "key_share_bean";
    public static final String KEY_PLATFORM = "key_platform";
    private ShareContent mShareBean;

    public static final String KEY_FUNCTION = "key_function";
    /**
     * 分享
     */
    public static final int FUNCTION_SHARE = 1;
    /**
     * 授权
     */
    public static final int FUNCTION_AUTH = 2;


    private static final String TAG = ShareActivity.class.getSimpleName();

    /**
     * 分享成功
     */
    public static final int SHARE_SUCCESS = 121;
    /**
     * 分享失败
     */
    public static final int SHARE_FAILED = 122;
    /**
     * 分享取消
     */
    public static final int SHARE_CANCEL = 123;

    private boolean mIsStartShareContent;
    /**
     * 为了兼容华为mate 7,8,9，在activity执行onPause之后，toast弹不出来的问题。使用该字段记录分享状态，然后再onResume中通知界面弹出toast
     */
    private int mShareState;

    @ShareManager.SharePlatform
    private int mPlatform;
    private ShareLoadingDialog mLoadingDialog;
    private Throwable mShareError;
    private int mFunctionType;

    private IUiListener iUiListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            if (mFunctionType == FUNCTION_AUTH) {
                //授权
                JSONObject obj = (JSONObject) response;
                AuthActionCallBack authActionCallBack = ShareManager.getInstance().getAuthActionCallBack();
                if (authActionCallBack != null) {
                    authActionCallBack.onComplete(mPlatform, obj.toString());
                }
                finish();
            } else {
                mShareState = SHARE_SUCCESS;
            }
        }

        @Override
        public void onError(UiError uiError) {
            if (mFunctionType == FUNCTION_AUTH) {
                //授权
                AuthActionCallBack authActionCallBack = ShareManager.getInstance().getAuthActionCallBack();
                if (authActionCallBack != null) {
                    authActionCallBack.onError(mPlatform, new Throwable(uiError.errorDetail));
                }
                finish();
            } else {
                mShareState = SHARE_FAILED;
                mShareError = new Throwable(uiError.errorDetail);
            }
        }

        @Override
        public void onCancel() {
            if (mFunctionType == FUNCTION_AUTH) {
                //授权
                AuthActionCallBack authActionCallBack = ShareManager.getInstance().getAuthActionCallBack();
                if (authActionCallBack != null) {
                    authActionCallBack.onCancel(mPlatform);
                }
                finish();
            } else {
                mShareState = SHARE_CANCEL;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout view = new FrameLayout(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(view);
        mShareBean = (ShareContent) getIntent().getSerializableExtra(KEY_SHARE_BEAN);
        mPlatform = getIntent().getIntExtra(KEY_PLATFORM, -1);
        mFunctionType = getIntent().getIntExtra(KEY_FUNCTION, -1);

        if (mPlatform == -1) {
            //没有传入第三方平台
            finish();
            return;
        }

        if (mFunctionType == FUNCTION_AUTH) {
            //授权登录
            if (mPlatform == PLATFORM_QQ) {
                //QQ
                ShareUtils.getInstance().authorizeForQQ(this, iUiListener);
            }
        } else {
            //分享内容到第三方
            startShare(mPlatform);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsStartShareContent) {
            handleShareResult();
            dismissLoading();
        }
        mIsStartShareContent = true;
    }

    /**
     * 分享
     * @param context
     * @param shareBean
     * @param platform
     */
    public static void startActivity(Context context, ShareContent shareBean, @ShareManager.SharePlatform int platform) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(KEY_PLATFORM, platform);
        intent.putExtra(KEY_SHARE_BEAN, shareBean);
        context.startActivity(intent);
    }

    /**
     * 授权
     * @param context
     * @param platform
     * @param functionType
     */
    public static void startActivity(Context context, @ShareManager.SharePlatform int platform, int functionType) {
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra(KEY_PLATFORM, platform);
        intent.putExtra(KEY_FUNCTION, functionType);
        context.startActivity(intent);
    }

    private void startShare(@ShareManager.SharePlatform int platform) {
        //微信分享|复制链接|更多分享不需要加loading框
        if (platform != PLATFORM_WX
                && platform != PLATFORM_WX_CIRCLE
                && platform != PLATFORM_COPY_LINK
                && platform != PLATFORM_MORE) {
            showLoading();
        }
        switch (platform) {
            case PLATFORM_QQ:
                ShareUtils.getInstance().shareToQQ(this, mShareBean, iUiListener);
                break;
            case PLATFORM_QZONE:
                ShareUtils.getInstance().shareToQZone(this, mShareBean, iUiListener);
                break;
            case PLATFORM_WX:
                ShareUtils.getInstance().shareToWechat(this, mShareBean);
                break;
            case PLATFORM_WX_CIRCLE:
                ShareUtils.getInstance().shareToWechatCircle(this, mShareBean);
                break;
            case PLATFORM_EMAIL:
                //邮件内容
                String emailContent = TextUtils.isEmpty(mShareBean.getEmailContent()) ? mShareBean.getContent()
                        + "。详情点击（" + mShareBean.getShareUrl() + "）" : mShareBean.getEmailContent();
                //邮件标题
                String emailTitle = TextUtils.isEmpty(mShareBean.getEmailTitle()) ? mShareBean.getTitle() : mShareBean.getEmailTitle();
                //邮件地址
                String emailAddress = TextUtils.isEmpty(mShareBean.getEmailAddress()) ? "" : mShareBean.getEmailAddress();
                ShareUtils.getInstance().shareToEmail(this, emailTitle, emailContent, emailAddress);
                break;
            case PLATFORM_SMS:
                //短信内容
                String smsContent = TextUtils.isEmpty(mShareBean.getSmsContent()) ? mShareBean.getContent() + mShareBean.getShareUrl() : mShareBean.getSmsContent();
                ShareUtils.getInstance().shareToSMS(this, smsContent);
                break;
            case PLATFORM_COPY_LINK:
                //要复制的Url
                String copyLinkUrl = TextUtils.isEmpty(mShareBean.getCopyLinkUrl()) ? mShareBean.getShareUrl() : mShareBean.getCopyLinkUrl();
                ShareUtils.getInstance().shareToCopyLink(this, copyLinkUrl);
                break;
            case PLATFORM_MORE:
                //更多分享的内容
                String moreContent = TextUtils.isEmpty(mShareBean.getMoreContent()) ? mShareBean.getContent() : mShareBean.getMoreContent();
                ShareUtils.getInstance().shareToMore(this, moreContent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    private void handleShareResult() {
        ShareActionCallBack shareActionCallBack = ShareManager.getInstance().getShareActionCallBack();
        switch (mShareState) {
            case SHARE_CANCEL:
                if (shareActionCallBack != null) {
                    shareActionCallBack.onCancel(mPlatform);
                }
                break;
            case SHARE_SUCCESS:
                if (shareActionCallBack != null) {
                    shareActionCallBack.onComplete(mPlatform);
                }
                break;
            case SHARE_FAILED:
                if (shareActionCallBack != null) {
                    shareActionCallBack.onError(mPlatform, mShareError);
                }
                break;
            default:
                break;
        }
    }

    public void showLoading() {
        mLoadingDialog = new ShareLoadingDialog(this);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(true);
        mLoadingDialog.show();
    }

    public void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, iUiListener);
    }
}
