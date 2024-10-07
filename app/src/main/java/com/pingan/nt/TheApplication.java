package com.pingan.nt;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.pasc.lib.base.AppProxy;
import com.pasc.lib.base.util.AppUtils;
import com.pasc.lib.share.ShareManager;
import com.pasc.lib.share.config.AppSecretConfig;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Modified by chenruihan410 on 2018/07/17. 去掉QbSdk的初始化，将初始化放到首页，因为需要权限判断.
 */
public class TheApplication extends Application {

    private static Context applicationContext;

    // 这个配置后面统一放BusinessBase里面
    private static String PRODUCT_HOST = "http://ntgsc-smt.pingan.com.cn/";
    private static String BETA_HOST = "http://smt-app-stg.pingan.com.cn/";

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        CrashReport.initCrashReport(getApplicationContext(), "76ca0b9ceb", true);
        if (AppUtils.getPIDName(this).equals(getPackageName())) {//主进程
            AppProxy.getInstance().init(this, false)
                    .setIsDebug(true)
                    .setProductType(2)
                    .setHost(AppProxy.TYPE_PRODUCT_PRODUCT == 2 ? PRODUCT_HOST : BETA_HOST)
                    .setVersionName(BuildConfig.VERSION_NAME);


            //分享模块
            AppSecretConfig.Builder builder = new AppSecretConfig.Builder();
            builder.setQqAppId("1106937651")
                    .setWechatAppId("wx782701887e39d58c");

            ShareManager.getInstance().init(this, builder.build());
        }
        //奔溃捕获相关初始化---PRODUCT环境关闭奔溃信息显性展示
//        CrashManager.getInstance().init(this, BuildConfig.DEBUG);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static Context getApplication() {
        return applicationContext;
    }


}

