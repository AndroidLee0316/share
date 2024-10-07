package com.pasc.lib.share.config;
/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2018/9/3
 * 第三方平台申请的key
 */
public class AppSecretConfig {
    /**qq的appId*/
    private String mQqAppId;
    /**微信的appId*/
    private String mWechatAppId;

    private AppSecretConfig() {
    }

    public String getQqAppId() {
        return mQqAppId;
    }

    public String getWechatAppId() {
        return mWechatAppId;
    }

    public static class Builder {
        private String mQqAppId;
        private String mWechatAppId;

        public Builder setQqAppId(String qqAppId) {
            this.mQqAppId = qqAppId;
            return this;
        }

        public Builder setWechatAppId(String wechatAppId) {
            this.mWechatAppId = wechatAppId;
            return this;
        }

        public AppSecretConfig build() {
            AppSecretConfig config = new AppSecretConfig();
            config.mQqAppId = mQqAppId;
            config.mWechatAppId = mWechatAppId;
            return config;
        }
    }
}
