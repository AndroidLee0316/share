package com.pasc.lib.share.config;

import com.pasc.lib.share.ShareManager;

/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2018/9/3
 * 分享平台定制
 */
public class SharePlatformConfig {

    private int mPlatformFlags;

    public int getPlatformFlag() {
        return mPlatformFlags;
    }

    private SharePlatformConfig() {
    }

    public static class Builder {
        private int mPlatformFlags;

        public Builder() {
        }

        public Builder setQQ() {
            mPlatformFlags = mPlatformFlags | ShareManager.PLATFORM_QQ;
            return this;
        }

        public Builder setWX() {
            mPlatformFlags = mPlatformFlags | ShareManager.PLATFORM_WX;
            return this;
        }

        public Builder setWxCircle() {
            mPlatformFlags = mPlatformFlags | ShareManager.PLATFORM_WX_CIRCLE;
            return this;
        }

        public Builder setQZONE() {
            mPlatformFlags = mPlatformFlags | ShareManager.PLATFORM_QZONE;
            return this;
        }

        public Builder setEmail() {
            mPlatformFlags = mPlatformFlags | ShareManager.PLATFORM_EMAIL;
            return this;
        }

        public Builder setSMS() {
            mPlatformFlags = mPlatformFlags | ShareManager.PLATFORM_SMS;
            return this;
        }

        public Builder setCopyLink() {
            mPlatformFlags = mPlatformFlags | ShareManager.PLATFORM_COPY_LINK;
            return this;
        }

        public Builder setMore() {
            mPlatformFlags = mPlatformFlags | ShareManager.PLATFORM_MORE;
            return this;
        }

        public SharePlatformConfig build() {
            SharePlatformConfig shareSharePlatformConfig = new SharePlatformConfig();
            shareSharePlatformConfig.mPlatformFlags = mPlatformFlags;
            return shareSharePlatformConfig;
        }
    }
}
