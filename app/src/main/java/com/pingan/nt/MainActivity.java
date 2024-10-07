package com.pingan.nt;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pasc.lib.share.ShareManager;
import com.pasc.lib.share.callback.AuthActionCallBack;
import com.pasc.lib.share.callback.ShareActionCallBack;
import com.pasc.lib.share.config.DialogConfig;
import com.pasc.lib.share.config.ShareContent;
import com.pasc.lib.share.config.SharePlatformConfig;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String[] PLATFORMS = new String[]{"微信好友", "朋友圈", "QQ", "QQ空间", "短信", "邮件", "复制链接", "更多"};
    private List<String> platformList = new ArrayList<>();

    private EditText t1, t2, t3, t4;


    //授权回调信息，可以在这里获取基本的授权返回的信息
    private AuthActionCallBack authActionCallBack = new AuthActionCallBack() {
        @Override
        public void onComplete(int platform, String authJson) {
            //输出所有授权信息
            Log.d("cdx", authJson);
            if (platform == ShareManager.PLATFORM_QQ)
                ResultActivity.startActivity(MainActivity.this, "QQ授权登录成功，授权结果是：" + authJson);
        }

        @Override
        public void onCancel(int platform) {
            if (platform == ShareManager.PLATFORM_QQ)
                ResultActivity.startActivity(MainActivity.this, "QQ授权登录取消");
        }

        @Override
        public void onError(int platform, Throwable var3) {
            if (platform == ShareManager.PLATFORM_QQ)
                ResultActivity.startActivity(MainActivity.this, "QQ授权登录失败，失败原因：" + var3.getMessage());
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_1) {//不传分享平台默认采用全部分享方式
            ShareManager.getInstance().setPlatformConfig(null);
            shareContentAllPlatforms();
        } else if (item.getItemId() == R.id.action_2) {//自定义分享平台
            platformList.clear();
            showSelectPlatformsDialog();
        } else if (item.getItemId() == R.id.action_3) {//QQ授权登录
            ShareManager.getInstance().authorizeForQQ(MainActivity.this, authActionCallBack);
        } else if (item.getItemId() == R.id.action_4) {//微信授权登录
            Toast.makeText(MainActivity.this, "南通百通没有开通微信快捷登录", Toast.LENGTH_LONG).show();
        }
        return super.onContextItemSelected(item);
    }


    // 初始化菜单并添加逻辑
    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("首页");
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);

        setListener();
    }

    private void setListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn1://不传分享平台默认采用全部分享方式
                        ShareManager.getInstance().setPlatformConfig(null);
                        shareContentAllPlatforms();
                        break;
                    case R.id.btn2://自定义分享平台
                        platformList.clear();
                        showSelectPlatformsDialog();
                        break;
                    case R.id.btn3://QQ授权登录
                        ShareManager.getInstance().authorizeForQQ(MainActivity.this, authActionCallBack);
                        break;
                    case R.id.btn4://微信授权登录
                        Toast.makeText(MainActivity.this, "南通百通没有开通微信快捷登录", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        findViewById(R.id.btn1).setOnClickListener(onClickListener);
        findViewById(R.id.btn2).setOnClickListener(onClickListener);
        findViewById(R.id.btn3).setOnClickListener(onClickListener);
        findViewById(R.id.btn4).setOnClickListener(onClickListener);
    }

    private void shareContentAllPlatforms() {
        ShareContent shareContent = getShareContent();
        if (shareContent == null) {
            return;
        }
        DialogConfig.Builder dialogConfigBuilder = getDialogConfigBuilder();
        ShareManager.getInstance()
//                .setDialogConfig(dialogConfigBuilder.build())
                .shareContent(this, shareContent, new ShareActionCallBack() {
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

    private void showSelectPlatformsDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMultiChoiceItems(PLATFORMS, new boolean[]{false, false, false, false, false, false, false, false}, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (which == 0) {
                    buildPlatform(isChecked, "0");
                } else if (which == 1) {
                    buildPlatform(isChecked, "1");
                } else if (which == 2) {
                    buildPlatform(isChecked, "2");
                } else if (which == 3) {
                    buildPlatform(isChecked, "3");
                } else if (which == 4) {
                    buildPlatform(isChecked, "4");
                } else if (which == 5) {
                    buildPlatform(isChecked, "5");
                } else if (which == 6) {
                    buildPlatform(isChecked, "6");
                } else if (which == 7) {
                    buildPlatform(isChecked, "7");
                }
            }

        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //自定义分享平台
                SharePlatformConfig.Builder platformBuilder = new SharePlatformConfig.Builder();
                if (platformList.contains("0")) {
                    platformBuilder.setWX();
                }
                if (platformList.contains("1")) {
                    platformBuilder.setWxCircle();
                }
                if (platformList.contains("2")) {
                    platformBuilder.setQQ();
                }
                if (platformList.contains("3")) {
                    platformBuilder.setQZONE();
                }
                if (platformList.contains("4")) {
                    platformBuilder.setSMS();
                }
                if (platformList.contains("5")) {
                    platformBuilder.setEmail();
                }
                if (platformList.contains("6")) {
                    platformBuilder.setCopyLink();
                }
                if (platformList.contains("7")) {
                    platformBuilder.setMore();
                }
                shareContentMutilPlatforms(platformBuilder);

            }
        }).setNegativeButton("取消", null).create().show();
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
                .shareContent(MainActivity.this, shareContent, new ShareActionCallBack() {
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

    private void buildPlatform(boolean isChecked, String s) {
        if (isChecked) {
            if (!platformList.contains(s)) {
                platformList.add(s);
            }
        } else {
            platformList.remove(s);
        }
    }

    private ShareContent getShareContent() {
        String trim = t1.getText().toString().trim();
        String trim1 = t2.getText().toString().trim();
        String trim2 = t3.getText().toString().trim();
        String trim3 = t4.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            Toast.makeText(MainActivity.this, "分享标题不能为空！", Toast.LENGTH_LONG).show();
            return null;
        }

        if (TextUtils.isEmpty(trim1)) {
            Toast.makeText(MainActivity.this, "分享内容不能为空！", Toast.LENGTH_LONG).show();
            return null;
        }

        if (TextUtils.isEmpty(trim2) && !trim2.startsWith("http")) {
            Toast.makeText(MainActivity.this, "分享地址不符合规范！", Toast.LENGTH_LONG).show();
            return null;
        }

        if (TextUtils.isEmpty(trim3) && !trim3.startsWith("http") && (!trim3.toLowerCase().endsWith("jpg") || !trim3.toLowerCase().endsWith("png"))) {
            Toast.makeText(MainActivity.this, "分享图片不符合规范！", Toast.LENGTH_LONG).show();
            return null;
        }

        ShareContent.Builder contentBuilder = new ShareContent.Builder();
        contentBuilder.setTitle(trim)
                .setContent(trim1)
                .setShareUrl(trim2)
                .setImageUrl(trim3)

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
                .setMoreContent("更多分享的内容")
                //qq内容分享定制,参数1：分享内容，参数2：分享地址url
                .setPlatformForQQ(new ShareContent.Platform("我们都是好孩子", "https://www.baidu.com"))
                //qq空间内容分享定制,参数1：分享内容，参数2：分享地址url
                .setPlatformForQzone(new ShareContent.Platform(trim1, trim2))
                //微信内容分享定制,参数1：分享内容，参数2：分享地址url
                .setPlatformForWeChatCircle(new ShareContent.Platform(trim1, trim2))
                //微信朋友圈内容分享定制,参数1：分享内容，参数2：分享地址url
                .setPlatformForWeChat(new ShareContent.Platform(trim1, trim2));

        return contentBuilder.build();
    }

    private void handleShareCallBackData(int platformFlag, int shareState) {
        String platformName = buildPlatformName(platformFlag);
        String shareStateStr = buildShareStateStr(shareState);
        String result = shareStateStr + "，平台是：" + platformName;
//        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
        ResultActivity.startActivity(this, result);
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
