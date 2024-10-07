package com.pasc.lib.share;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.IntDef;
import android.widget.Toast;

import com.pasc.lib.share.activity.ShareActivity;
import com.pasc.lib.share.callback.AuthActionCallBack;
import com.pasc.lib.share.callback.ShareActionCallBack;
import com.pasc.lib.share.config.AppSecretConfig;
import com.pasc.lib.share.config.DialogConfig;
import com.pasc.lib.share.config.ShareContent;
import com.pasc.lib.share.config.SharePlatformConfig;
import com.pasc.lib.share.dialog.ShareDialog;
import com.pasc.lib.share.util.ShareUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2018/9/3
 * 分享Manager
 */
public class ShareManager {
    /** 微信授权请求标识 */
    public static String SEND_AUTH_REQ_STATE;
    /** 微信绑定请求标识 */
    public static final String SEND_BIND_REQ_STATE = "send_bind_req_state";

    /** 第三方平台 微信 */
    public static final int PLATFORM_WX = 0x01;
    /** 第三方平台 QQ */
    public static final int PLATFORM_QQ = 0x02;
    /** 第三方平台 微信朋友圈 */
    public static final int PLATFORM_WX_CIRCLE = 0x04;
    /** 第三方平台 QQ空间 */
    public static final int PLATFORM_QZONE = 0x08;
    /** 第三方平台 短信分享 */
    public static final int PLATFORM_SMS = 0x10;
    /** 第三方平台 邮件分享 */
    public static final int PLATFORM_EMAIL = 0x20;
    /** 第三方平台 复制链接 */
    public static final int PLATFORM_COPY_LINK = 0x40;
    /** 第三方平台 更多分享 */
    public static final int PLATFORM_MORE = 0x80;

    /** 不存在的分享方式 */
    public static final int PLATFORM_NONE = 0;

    private static final int PLATFORM_ALL = PLATFORM_WX | PLATFORM_QQ
            | PLATFORM_WX_CIRCLE | PLATFORM_QZONE | PLATFORM_EMAIL | PLATFORM_SMS | PLATFORM_COPY_LINK | PLATFORM_MORE;

    private ShareActionCallBack mShareActionCallBack;
    private AuthActionCallBack mAuthActionCallBack;
    private SharePlatformConfig mSharePlatformConfig;
    private DialogConfig mDialogConfig;

    /** 第三方申请的appkey */
    private AppSecretConfig mAppSecretConfig;


    public static ShareManager getInstance() {
        return SingletonHolder.G_INSTANCE;
    }

    /**
     * 设置底部分享弹窗的配置
     */
    public ShareManager setDialogConfig(DialogConfig dialogConfig) {
        this.mDialogConfig = dialogConfig;
        return this;
    }


    /**
     * 静态内部类,只有在装载该内部类时才会去创建单例对象
     */
    private static class SingletonHolder {
        private static final ShareManager G_INSTANCE = new ShareManager();
    }

    // 初始化分享模块(SDK)
    public void init(Application context, AppSecretConfig config) {
        if (config == null) {
            throw new NullPointerException("此处AppSecretConfig不能为null");
        }
        mAppSecretConfig = config;
        ShareUtils.getInstance().regToQQ(context);
        ShareUtils.getInstance().regToWX(context);
    }


    /**
     * 设置支持的分享平台
     */
    public ShareManager setPlatformConfig(SharePlatformConfig sharePlatformConfig) {
        mSharePlatformConfig = sharePlatformConfig;
        return this;
    }

    /**
     * 分享 ---- 默认支持定义的所有分享支持
     *
     * @param shareContent 分享所需相关信息封装实体
     * @param activity     调用分享的activity
     */
    public void shareContent(Activity activity, ShareContent shareContent) {
        int flags = (mSharePlatformConfig == null || mSharePlatformConfig.getPlatformFlag() == 0)
                ? PLATFORM_ALL : mSharePlatformConfig.getPlatformFlag();
        shareContent(activity, flags, shareContent, null);
    }

    /**
     * 分享 ---- 默认支持定义的所有分享支持
     *
     * @param shareContent 分享所需相关信息封装实体
     * @param activity     调用分享的activity
     * @param callBack     分享状态回调（微信分享不支持callBack回调分享状态，微信分享状态回调WxEntryActivity的onResp方法中）
     */
    public void shareContent(Activity activity, ShareContent shareContent, ShareActionCallBack callBack) {
        int flags = mSharePlatformConfig == null ? PLATFORM_ALL : mSharePlatformConfig.getPlatformFlag();
        shareContent(activity, flags, shareContent, callBack);
    }

    /**
     * 分享
     *
     * @param flags        需要支持的分享平台
     *                     如支持微信和QQ分享传 ShareManager.PLATFORM_WX|ShareManager.PLATFORM_QQ，其他类似
     * @param shareContent 分享所需相关信息封装实体
     * @param activity     调用分享的activity
     * @param callBack     分享状态回调
     */
    private void shareContent(Activity activity, int flags, ShareContent shareContent, ShareActionCallBack callBack) {
        this.mShareActionCallBack = callBack;
        List<DialogConfig.PlatformInfo> platInfos = ShareUtils.getInstance().buildPlatDatas(flags, activity);
        if (platInfos.size() < 1) {
            Toast.makeText(activity, "暂无可用的分享方式", Toast.LENGTH_SHORT).show();
            return;
        }
        ShareDialog shareDialog = new ShareDialog(activity, shareContent, platInfos);
        shareDialog.show();
    }

    /**
     * QQ授权登录
     *
     * @param activity           调用QQ授权的activity
     * @param authActionCallBack 授权结果回调
     */
    public void authorizeForQQ(Activity activity, AuthActionCallBack authActionCallBack) {
        this.mAuthActionCallBack = authActionCallBack;
        ShareActivity.startActivity(activity, PLATFORM_QQ, ShareActivity.FUNCTION_AUTH);
    }

    /**
     * 微信授权登录
     *
     * @param activity 调用微信授权的activity
     */
    public void authorizeForWechat(Activity activity) {
        ShareUtils.getInstance().authorizeForWechat(activity);
    }

    /**
     * 绑定到微信
     *
     * @param activity 调用微信绑定的activity
     */
    public void bindForWechat(Activity activity) {
        ShareUtils.getInstance().bindForWechat(activity);
    }

    public AuthActionCallBack getAuthActionCallBack() {
        return mAuthActionCallBack;
    }

    public AppSecretConfig getAppSecretConfig() {
        return mAppSecretConfig;
    }

    public ShareActionCallBack getShareActionCallBack() {
        return mShareActionCallBack;
    }


    public DialogConfig getDialogConfig() {
        return mDialogConfig;
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PLATFORM_EMAIL, PLATFORM_QQ,
            PLATFORM_QZONE, PLATFORM_WX,
            PLATFORM_WX_CIRCLE, PLATFORM_NONE,
            PLATFORM_SMS, PLATFORM_COPY_LINK, PLATFORM_MORE})
    public @interface SharePlatform {
    }

}
