package com.pasc.lib.share.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.util.BitmapUtils;
import com.pasc.lib.net.download.DownLoadManager;
import com.pasc.lib.net.download.DownloadInfo;
import com.pasc.lib.net.download.DownloadObserver;
import com.pasc.lib.share.R;
import com.pasc.lib.share.ShareManager;
import com.pasc.lib.share.config.AppSecretConfig;
import com.pasc.lib.share.config.DialogConfig;
import com.pasc.lib.share.config.ShareContent;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.pasc.lib.base.util.BitmapUtils.Bitmap2Bytes;
import static com.pasc.lib.base.util.BitmapUtils.zoomImage;

/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2018/10/3
 * 分享工具类
 */
public class ShareUtils {
    private static final String QQ_PACKAGE = "com.tencent.mobileqq";
    private static final String TIM_PACKAGE = "com.tencent.tim";
    private static String DEFAULT_PICTURE_URL = "http://iobs.pingan.com.cn/download/szsc-smt-app-dmz-prd/53e03a14-b997-4d39-9541-52c07afaaccc_1537008766995";

    private ShareUtils() {
    }

    private IWXAPI mWxApi;
    private Tencent mTencent;

    public static ShareUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 静态内部类,只有在装载该内部类时才会去创建单例对象
     */
    private static class SingletonHolder {
        private static final ShareUtils INSTANCE = new ShareUtils();
    }

    /**
     * 要分享必须先注册(微信)
     */
    public void regToWX(Context context) {
        AppSecretConfig appSecretConfig = ShareManager.getInstance().getAppSecretConfig();
        mWxApi = WXAPIFactory.createWXAPI(context, appSecretConfig.getWechatAppId());
        mWxApi.registerApp(appSecretConfig.getWechatAppId());
    }

    /**
     * 要分享必须先注册Tencent
     */
    public void regToQQ(Context context) {
        AppSecretConfig appSecretConfig = ShareManager.getInstance().getAppSecretConfig();
        mTencent = Tencent.createInstance(appSecretConfig.getQqAppId(), context);
    }

    /**
     * 分享消息到好友
     */
    public void shareToQQ(final Context context, ShareContent shareContent, final IUiListener iUiListener) {
        final Bundle params = new Bundle();
        ShareContent.Platform platformForQQ = shareContent.getPlatformForQQ();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        // 标题
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareContent.getTitle());
        // 摘要
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, getShareContent(platformForQQ, shareContent));
        // 内容地址
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, getShareUrl(platformForQQ, shareContent));
        String pictureUrl = DEFAULT_PICTURE_URL;
        if (!TextUtils.isEmpty(shareContent.getImageUrl()) && shareContent.getImageUrl().startsWith("http")) {
            pictureUrl = shareContent.getImageUrl();
        }

        // 网络图片地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                pictureUrl);
        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其它附加功能");
        // 分享操作要在主线程中完成
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ((Activity) context, params, iUiListener);
            }
        });
    }

    /**
     * 分享消息到QQ空间
     */
    public void shareToQZone(final Context context, ShareContent shareContent, final IUiListener iUiListener) {
        final Bundle params = new Bundle();
        ShareContent.Platform platformForQzone = shareContent.getPlatformForQzone();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        // 标题
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareContent.getTitle());
        // 摘要
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, getShareContent(platformForQzone, shareContent));
        // 内容地址
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, getShareUrl(platformForQzone, shareContent));
        ArrayList<String> imgUrlList = new ArrayList<>();
        String pictureUrl = DEFAULT_PICTURE_URL;
        if (shareContent.getImageUrl() != null && shareContent.getImageUrl().startsWith("http")) {
            pictureUrl = shareContent.getImageUrl();
        }
        imgUrlList.add(pictureUrl);
        // 图片地址
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrlList);
        // 分享操作要在主线程中完成
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQzone((Activity) context, params, iUiListener);
            }
        });
    }


    /**
     * 分享内容到微信好友
     */
    public void shareToWechat(final Context context, ShareContent shareContent) {
        ShareContent.Platform platformForWeChatCircle = shareContent.getPlatformForWeChatCircle();
        sendMessage2WX(context, SendMessageToWX.Req.WXSceneSession,
                getShareUrl(platformForWeChatCircle, shareContent), shareContent.getTitle(), getShareContent(platformForWeChatCircle, shareContent),
                shareContent.getImageUrl());
    }

    /**
     * 分享内容到朋友圈
     */
    public void shareToWechatCircle(final Context context, ShareContent shareContent) {
        ShareContent.Platform platformForWeChat = shareContent.getPlatformForWeChat();
        sendMessage2WX(context, SendMessageToWX.Req.WXSceneTimeline,
                getShareUrl(platformForWeChat, shareContent), shareContent.getTitle(), getShareUrl(platformForWeChat, shareContent),
                shareContent.getImageUrl());
    }

    /**
     * 直接调起短信分享
     *
     * @param context    上下文
     * @param smsContent 短信内容
     */
    public void shareToSMS(Context context, String smsContent) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", smsContent);
        context.startActivity(mIntent);
    }

    /**
     * 邮件分享
     *
     * @param context 上下文
     * @param title   邮件主题
     * @param content 邮件内容
     * @param address 邮件地址
     */
    public void shareToEmail(Context context, String title, String content, String address) {
        Uri uri = Uri.parse("mailto:" + address);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        // 设置对方邮件地址
        emailIntent.putExtra(Intent.EXTRA_EMAIL, address);
        // 设置标题内容
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        // 设置邮件文本内容
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(emailIntent, "分享到"));
    }

    /**
     * 直接调起更多分享----Android原生分享
     *
     * @param context 上下文
     * @param content 分享内容
     */
    public void shareToMore(Context context, String content) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        // 设置文本格式
        sendIntent.setType("text/plain");
        // 设置邮件文本内容
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(sendIntent, "分享到"));
    }

    /**
     * 复制链接
     *
     * @param activity 上下文
     * @param linkUrl  链接
     */
    public void shareToCopyLink(Activity activity, String linkUrl) {
        try {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", linkUrl);
            // 将ClipData内容放到系统剪贴板里。
            if (cm != null) {
                cm.setPrimaryClip(mClipData);
                showCopyLinkToast(activity, activity.getString(R.string.share_lib_dialog_copy_link_success));
            }
            activity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCopyLinkToast(Context context, CharSequence message) {
        final Toast currentToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        final View toastLayout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.share_lib_toast_layout, null);
        final TextView toastTextView = toastLayout.findViewById(R.id.share_lib_toast_text);
        toastTextView.setText(message);
        currentToast.setView(toastLayout);
        currentToast.setGravity(Gravity.CENTER, 0, 0);
        currentToast.show();
    }

    /**
     * QQ授权登录
     *
     * @param activity 调用QQ授权的activity
     */
    public void authorizeForQQ(Activity activity, IUiListener iUiListener) {
        mTencent.login(activity, "all", iUiListener);
    }

    /**
     * 微信授权登录
     *
     * @param activity 调用微信授权的activity
     */
    public void authorizeForWechat(Activity activity) {
        ShareManager.SEND_AUTH_REQ_STATE = UUID.randomUUID().toString();
        authForWx(ShareManager.SEND_AUTH_REQ_STATE);
    }

    /**
     * 微信绑定
     *
     * @param activity 调用微信绑定的activity
     */
    public void bindForWechat(Activity activity) {
        authForWx(ShareManager.SEND_BIND_REQ_STATE);
    }

    private void authForWx(String reqState) {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = reqState;
        mWxApi.sendReq(req);
    }

    private void sendMessage2WX(final Context context, final int type, final String url,
                                final String title, final String description, final String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            shareData(context, type, url, title, description, null);
            return;
        }
        DownLoadManager downInstance = DownLoadManager.getDownInstance();
        DownloadObserver downloadObserver = new DownloadObserver() {
            @Override
            public void onDownloadStateProgressed(final DownloadInfo downloadInfo) {
                if (downloadInfo.downloadState == DownloadInfo.STATE_FINISH) {
                    //下载成功
                    String filePath = downloadInfo.getFilePath(context);
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    shareData(context, type, url, title, description, bitmap);
                } else if (downloadInfo.downloadState == DownloadInfo.STATE_ERROR || downloadInfo.downloadState == DownloadInfo.STATE_PAUSED) {
                    shareData(context, type, url, title, description, null);
                }
            }
        };
        Context proxyContext = AppProxy.getInstance().getContext();
        downInstance.init(proxyContext, 1, 2, 0);
        File cacheFile;
        File externalCacheDir = proxyContext.getExternalCacheDir();
        if (externalCacheDir != null) {
            cacheFile = externalCacheDir;
        } else {
            cacheFile = proxyContext.getCacheDir();
        }
        String cachePath = cacheFile.getAbsolutePath();
        DownloadInfo downloadInfo = new DownloadInfo(imageUrl, md5(imageUrl), cachePath, false);
        downInstance.startDownload(downloadInfo, downloadObserver);
    }

    private void shareData(Context context, int type, String url, final String title,
                           final String description, Bitmap bitmap) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;

        if (bitmap == null) {
            bitmap = BitmapUtils.drawableToBitamp(context.getResources().getDrawable(R.drawable.share_lib_default_share_img));
        }
        try {
            Bitmap thumBmp = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
            msg.thumbData = Bitmap2Bytes(imageZoom(thumBmp));
        } catch (Exception e) {
            e.printStackTrace();
            msg.thumbData = null;
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        mWxApi.sendReq(req);
    }

    private Bitmap imageZoom(Bitmap bitMap) {
        //图片允许最大空间  单位：KB
        double maxSize = 32;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length / 1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
                    bitMap.getHeight() / Math.sqrt(i));
        }
        return bitMap;
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    public IWXAPI getWxApi() {
        return mWxApi;
    }

    public Tencent getTencent() {
        return mTencent;
    }

    /**
     * 计算字符串的MD5值
     */
    private static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                stringBuilder.append(temp);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return string;
        }
    }

    public List<DialogConfig.PlatformInfo> buildPlatDatas(int flags, Context context) {
        List<DialogConfig.PlatformInfo> platInfoList = new ArrayList<>();
        DialogConfig dialogConfig = ShareManager.getInstance().getDialogConfig();
        DialogConfig.PlatformInfo platInfo = null;
        //微信
        if (ShareUtils.getInstance().getWxApi().isWXAppInstalled() &&
                ((flags & ShareManager.PLATFORM_WX) == ShareManager.PLATFORM_WX)) {
            if (dialogConfig != null) {
                platInfo = dialogConfig.getWxInfo();
            }
            addPlatformInfoToList(ShareManager.PLATFORM_WX,
                    R.drawable.pasc_share_lib_ic_share_wx,
                    context.getString(R.string.share_lib_dialog_wx),
                    platInfoList, platInfo);
        }
        //朋友圈
        if (ShareUtils.getInstance().getWxApi().isWXAppInstalled() &&
                ((flags & ShareManager.PLATFORM_WX_CIRCLE) == ShareManager.PLATFORM_WX_CIRCLE)) {
            if (dialogConfig != null) {
                platInfo = dialogConfig.getWxCircleInfo();
            }
            addPlatformInfoToList(ShareManager.PLATFORM_WX_CIRCLE,
                    R.drawable.pasc_share_lib_ic_share_wx_circle,
                    context.getString(R.string.share_lib_dialog_wx_circle),
                    platInfoList, platInfo);
        }
        //qq
        if (isQQInstalled(context) &&
                ((flags & ShareManager.PLATFORM_QQ) == ShareManager.PLATFORM_QQ)) {
            if (dialogConfig != null) {
                platInfo = dialogConfig.getQQInfo();
            }
            addPlatformInfoToList(ShareManager.PLATFORM_QQ,
                    R.drawable.pasc_share_lib_ic_share_qq,
                    context.getString(R.string.share_lib_dialog_qq),
                    platInfoList, platInfo);
        }
        //qq空间
        if (isQQInstalled(context) &&
                ((flags & ShareManager.PLATFORM_QZONE) == ShareManager.PLATFORM_QZONE)) {
            if (dialogConfig != null) {
                platInfo = dialogConfig.getQzoneInfo();
            }
            addPlatformInfoToList(ShareManager.PLATFORM_QZONE,
                    R.drawable.pasc_share_lib_ic_share_qzone,
                    context.getString(R.string.share_lib_dialog_qq_zone),
                    platInfoList, platInfo);
        }
        //短信
        if ((flags & ShareManager.PLATFORM_SMS) == ShareManager.PLATFORM_SMS) {
            if (dialogConfig != null) {
                platInfo = dialogConfig.getSMSInfo();
            }
            addPlatformInfoToList(ShareManager.PLATFORM_SMS,
                    R.drawable.pasc_share_lib_ic_share_sms,
                    context.getString(R.string.share_lib_dialog_sms),
                    platInfoList, platInfo);
        }
        //邮箱
        if ((flags & ShareManager.PLATFORM_EMAIL) == ShareManager.PLATFORM_EMAIL) {
            if (dialogConfig != null) {
                platInfo = dialogConfig.getEmailInfo();
            }
            addPlatformInfoToList(ShareManager.PLATFORM_EMAIL,
                    R.drawable.pasc_share_lib_ic_share_email,
                    context.getString(R.string.share_lib_dialog_email),
                    platInfoList, platInfo);
        }
        //复制链接
        if ((flags & ShareManager.PLATFORM_COPY_LINK) == ShareManager.PLATFORM_COPY_LINK) {
            if (dialogConfig != null) {
                platInfo = dialogConfig.getCopyLinkInfo();
            }
            addPlatformInfoToList(ShareManager.PLATFORM_COPY_LINK,
                    R.drawable.pasc_share_lib_ic_share_copy_link,
                    context.getString(R.string.share_lib_dialog_copy_link),
                    platInfoList, platInfo);
        }
        //更多（本地分享）
        if ((flags & ShareManager.PLATFORM_MORE) == ShareManager.PLATFORM_MORE) {
            if (dialogConfig != null) {
                platInfo = dialogConfig.getMoreInfo();
            }
            addPlatformInfoToList(ShareManager.PLATFORM_MORE,
                    R.drawable.pasc_share_lib_ic_share_more,
                    context.getString(R.string.share_lib_dialog_more),
                    platInfoList, platInfo);
        }
        return platInfoList;
    }

    private void addPlatformInfoToList(@ShareManager.SharePlatform int platform, int defaultIconResId,
                                       String defaultPlatformName, List<DialogConfig.PlatformInfo> platInfoList,
                                       DialogConfig.PlatformInfo platformInfo) {
        DialogConfig.PlatformInfo platInfo = new DialogConfig.PlatformInfo();
        if (platformInfo != null) {
            platInfo = platformInfo;
            platInfo.setPlatform(platform);
            if (platInfo.getIconResId() == 0) {
                platInfo.setIconResId(defaultIconResId);
            }
            if (TextUtils.isEmpty(platInfo.getPlatName())) {
                platInfo.setPlatName(defaultPlatformName);
            }
        } else {
            platInfo.setPlatform(platform);
            platInfo.setPlatName(defaultPlatformName);
            platInfo.setIconResId(defaultIconResId);
        }
        platInfoList.add(platInfo);
    }

    public boolean isQQInstalled(Context context) {
        PackageManager manager = context.getPackageManager();
        List list = manager.getInstalledPackages(0);
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                String packageName = ((PackageInfo) list.get(i)).packageName;
                if (QQ_PACKAGE.equals(packageName) || TIM_PACKAGE.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getShareContent(ShareContent.Platform platform, ShareContent shareContent) {
        return platform != null && !TextUtils.isEmpty(platform.getContent()) ? platform.getContent() : shareContent.getContent();
    }

    private String getShareUrl(ShareContent.Platform platform, ShareContent shareContent) {
        return platform != null && !TextUtils.isEmpty(platform.getShareUrl()) ? platform.getShareUrl() : shareContent.getShareUrl();
    }

}
