package com.pingan.nt;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.pasc.lib.share.ShareManager;
import com.pasc.lib.share.callback.ShareActionCallBack;
import com.pasc.lib.share.config.DialogConfig;
import com.pasc.lib.share.config.ShareContent;
import com.pasc.lib.share.config.SharePlatformConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2019 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2019/3/19
 */
public class ShareSampleActivity extends AppCompatActivity {

    List<AppCompatCheckBox> checkBoxList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_plat);

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

        AppCompatCheckBox wx = findViewById(R.id.ck_wx);
        AppCompatCheckBox wxc = findViewById(R.id.ck_wxc);
        AppCompatCheckBox qzone = findViewById(R.id.ck_qzone);
        AppCompatCheckBox qq = findViewById(R.id.ck_qq);
        AppCompatCheckBox sms = findViewById(R.id.ck_sms);
        AppCompatCheckBox email = findViewById(R.id.ck_email);
        AppCompatCheckBox copylink = findViewById(R.id.ck_copylink);
        AppCompatCheckBox more = findViewById(R.id.ck_more);

        checkBoxList.add(wx);
        checkBoxList.add(wxc);
        checkBoxList.add(qzone);
        checkBoxList.add(qq);
        checkBoxList.add(sms);
        checkBoxList.add(email);
        checkBoxList.add(copylink);
        checkBoxList.add(more);

        findViewById(R.id.start_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义分享平台
                SharePlatformConfig.Builder platformBuilder = new SharePlatformConfig.Builder();
                //开始分享
                for (AppCompatCheckBox checkBox : checkBoxList) {
                    switch (checkBox.getId()) {
                        case R.id.ck_wx:
                            if (checkBox.isChecked()) {
                                platformBuilder.setWX();
                            }
                            break;

                        case R.id.ck_wxc:
                            if (checkBox.isChecked()) {
                                platformBuilder.setWxCircle();
                            }
                            break;
                        case R.id.ck_qzone:
                            if (checkBox.isChecked()) {
                                platformBuilder.setQZONE();
                            }
                            break;
                        case R.id.ck_qq:
                            if (checkBox.isChecked()) {
                                platformBuilder.setQQ();
                            }
                            break;
                        case R.id.ck_sms:
                            if (checkBox.isChecked()) {
                                platformBuilder.setSMS();
                            }
                            break;
                        case R.id.ck_email:
                            if (checkBox.isChecked()) {
                                platformBuilder.setEmail();
                            }
                            break;
                        case R.id.ck_copylink:
                            if (checkBox.isChecked()) {
                                platformBuilder.setCopyLink();
                            }
                            break;
                        case R.id.ck_more:
                            if (checkBox.isChecked()) {
                                platformBuilder.setMore();
                            }
                            break;
                    }
                }
                shareContentMutilPlatforms(platformBuilder);
            }
        });
    }


    private void shareContentMutilPlatforms(SharePlatformConfig.Builder platformBuilder) {
        ShareContent shareContent = getShareContent();
        if (shareContent == null) {
            return;
        }
        DialogConfig.Builder builderForDialogConfig = getDialogConfigBuilder();
        ShareManager.getInstance()
                .setPlatformConfig(platformBuilder.build())
                //设置分享弹窗样式
//                .setDialogConfig(builderForDialogConfig.build())
                .shareContent(ShareSampleActivity.this, shareContent, new ShareActionCallBack() {
                    @Override
                    public void onComplete(int platform) {
                        handleShareCallBackData(platform, 0);
                    }

                    @Override
                    public void onCancel(int platform) {
                        handleShareCallBackData(platform, 1);
                    }

                    @Override
                    public void onError(int platform, Throwable var3) {
                        handleShareCallBackData(platform, 2);
                    }
                });
    }


    private ShareContent getShareContent() {
//        JSONObject jsonObject = null;
//        try {
//            jsonObject= new JSONObject("{\n" +
//                    "\t\"image\": \"http:\\/\\/isz-cloud.yun.city.pingan.com\\/cfs\\/nas\\/view?bucket=szsc-smt-app-dmz-prd&attKey=bd2efdb2d9224ab29a14c0ddd114f5bd\",\n" +
//                    "\t\"title\": \"杨洪常委赴中山大学・深圳及中山大学附属第七医院检查项目建设及安全文明施工工作\",\n" +
//                    "\t\"content\": \"4月12日下午,市委常委杨洪前往市建筑工务署组织建设的中山大学・深圳和中山大学附属第七医院项目调研。...\",\n" +
//                    "\t\"shareUrl\": \"https:\\/\\/smt-web-stg.pingan.com.cn\\/sz\\/app\\/feature\\/news-toutiao\\/?vt=share_h5#\\/list\\/details\\/focusSZ\\/2ef0fa36fe4d464c91fe18f56b7f5860?\",\n" +
//                    "\t\"shareTypes\": [{\n" +
//                    "\t\t\"platformID\": 1\n" +
//                    "\t}, {\n" +
//                    "\t\t\"platformID\": 2\n" +
//                    "\t}, {\n" +
//                    "\t\t\"platformID\": 3\n" +
//                    "\t}, {\n" +
//                    "\t\t\"platformID\": 4\n" +
//                    "\t}]\n" +
//                    "}");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        ShareContent.Builder contentBuilder = new ShareContent.Builder();
        contentBuilder.setTitle("分享的标题")
                .setContent("分享的描述")
                .setShareUrl("https://www.baidu.com")
                .setImageUrl("https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=3b44ffc10746f21fd6345853c6256b31/8c1001e93901213f5480ffe659e736d12f2e955d.jpg")
//                .setImageUrl(jsonObject.optString("image"))

                //短信内容分享定制
                .setSmsContent("短信分享的内容")
                //邮件分享内容定制
                .setEmailContent("邮件分享的内容")
                //邮件分享标题定制
                .setEmailTitle("邮箱标题内容")
                //邮件分享地址
                .setEmailAddress("975590887@qq.com")
                //复制链接定制
                .setCopyLinkUrl("https://www.baidu.com")
                //更多分享的内容定制
                .setMoreContent("更多分享的内容");
//                //qq内容分享定制,参数1：分享内容，参数2：分享地址url
//                .setPlatformForQQ(new ShareContent.Platform("我们都是好孩子", "https://www.baidu.com"))
//                //qq空间内容分享定制,参数1：分享内容，参数2：分享地址url
//                .setPlatformForQzone(new ShareContent.Platform(trim1, trim2))
//                //微信内容分享定制,参数1：分享内容，参数2：分享地址url
//                .setPlatformForWeChatCircle(new ShareContent.Platform(trim1, trim2))
//                //微信朋友圈内容分享定制,参数1：分享内容，参数2：分享地址url
//                .setPlatformForWeChat(new ShareContent.Platform(trim1, trim2));

        return contentBuilder.build();
    }

    @NonNull
    private DialogConfig.Builder getDialogConfigBuilder() {
        DialogConfig.Builder builderForDialogConfig = new DialogConfig.Builder();
        builderForDialogConfig
                //设置取消按钮文字大小，单位px
                .setTextSizeForTvCancel(17)
                //设置取消按钮文字颜色，格式：#FF0000
                .setColorForTvCancel("#FF0000")
                //设置分享平台文字大小，单位px
                .setTextSizeForTvPlatformName(12)
                //设置分享平台文字颜色，格式：#FF0000
                .setColorForTvPlatformName("#00FF00")
                //设置分享弹窗标题文字大小，单位px
                .setTextSizeForTvTitle(13)
                //设置分享弹窗标题文字颜色，格式：#FF0000
                .setColorForTvTitle("#0000FF")
                //设置微信图标、文字
                .setWxInfo(new DialogConfig.PlatformInfo().setPlatName("微信").setIconResId(R.mipmap.ssdk_oks_classic_wechat))
                //设置微信朋友圈图标、文字
                .setWxCircleInfo(new DialogConfig.PlatformInfo().setPlatName("微信朋友圈").setIconResId(R.mipmap.classic_wechatmoments))
                //设置QQ图标、文字
                .setQQInfo(new DialogConfig.PlatformInfo().setPlatName("QQ").setIconResId(R.mipmap.ssdk_oks_classic_qq))
                //设置QQ空间图标、文字
                .setQzoneInfo(new DialogConfig.PlatformInfo().setPlatName("QQ空间").setIconResId(R.mipmap.ssdk_oks_classic_qzone))
                //设置短信图标、文字
                .setSMSInfo(new DialogConfig.PlatformInfo().setPlatName("短信").setIconResId(R.mipmap.ssdk_oks_classic_shortmessage))
                //设置复制链接图标、文字
                .setCopyLinkInfo(new DialogConfig.PlatformInfo().setPlatName("复制链接").setIconResId(R.mipmap.ssdk_oks_classic_shortmessage))
                //设置更多分享图标、文字
                .setMoreInfo(new DialogConfig.PlatformInfo().setPlatName("更多").setIconResId(R.mipmap.ssdk_oks_classic_shortmessage));
        return builderForDialogConfig;
    }

    private void handleShareCallBackData(int platformFlag, int shareState) {
        String platformName = buildPlatformName(platformFlag);
        String shareStateStr = buildShareStateStr(shareState);
        String result = shareStateStr + "，平台是：" + platformName;
        Toast.makeText(ShareSampleActivity.this, result, Toast.LENGTH_LONG).show();
//        ResultActivity.startActivity(this, result);
    }

    @NonNull
    private String buildPlatformName(int platformFlag) {
        String platformName = "";
        if (platformFlag == ShareManager.PLATFORM_EMAIL) {
            platformName = "邮箱";
        } else if (platformFlag == ShareManager.PLATFORM_WX) {
            platformName = "微信好友\n\n(备注：微信4.7.2及以上，微信不再回调正确分享状态，\"分享取消\"与\"分享成功\"都会提示\"分享成功\",具体可查看：https://open.weixin.qq.com/cgi-bin/announce?action=getannouncement&key=11534138374cE6li&version=&lang=zh_CN&token=)";
        } else if (platformFlag == ShareManager.PLATFORM_WX_CIRCLE) {
            platformName = "朋友圈\n\n(备注：微信4.7.2及以上，微信不再回调正确分享状态，\"分享取消\"与\"分享成功\"都会提示\"分享成功\",具体可查看：https://open.weixin.qq.com/cgi-bin/announce?action=getannouncement&key=11534138374cE6li&version=&lang=zh_CN&token=)";
        } else if (platformFlag == ShareManager.PLATFORM_QQ) {
            platformName = "QQ";
        } else if (platformFlag == ShareManager.PLATFORM_QZONE) {
            platformName = "QQ空间";
        } else if (platformFlag == ShareManager.PLATFORM_SMS) {
            platformName = "短信";
        } else if (platformFlag == ShareManager.PLATFORM_COPY_LINK) {
            platformName = "复制链接";
        } else if (platformFlag == ShareManager.PLATFORM_MORE) {
            platformName = "更多";
        } else if (platformFlag == ShareManager.PLATFORM_NONE) {
            platformName = "点击取消分享按钮或蒙层";
        }
        return platformName;
    }

    private String buildShareStateStr(int shareState) {
        String shareStateStr = "";
        if (shareState == 0) {
            shareStateStr = "分享成功了";
        } else if (shareState == 1) {
            shareStateStr = "分享取消了";
        } else if (shareState == 2) {
            shareStateStr = "分享失败了";
        }
        return shareStateStr;
    }
}
