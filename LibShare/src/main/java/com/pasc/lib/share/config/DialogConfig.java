package com.pasc.lib.share.config;

import com.pasc.lib.share.ShareManager;

/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2018/9/26
 * <p>
 * 分享弹窗样式相关配置
 */
public class DialogConfig {
    /** 分享弹窗标题的颜色,格式如"#FF0000" */
    private String mColorForTvTitle;
    /** 分享弹窗平台文字的颜色，格式如"#FF0000" */
    private String mColorForTvPlatformName;
    /** 分享弹窗取消按钮的颜色，格式如"#FF0000" */
    private String mColorForTvCancel;

    /** 分享弹窗标题的文字大小,单位为px */
    private int mTextSizeForTvTitle;
    /** 分享弹窗平台文字的颜色，单位为px； */
    private int mTextSizeForTvPlatformName;
    /** 分享弹窗取消按钮的颜色，单位为px； */
    private int mTextSizeForTvCancel;

    private PlatformInfo mWxInfo;
    private PlatformInfo mQQInfo;
    private PlatformInfo mQzoneInfo;
    private PlatformInfo mWxCircleInfo;
    private PlatformInfo mSMSInfo;
    private PlatformInfo mEmailInfo;
    private PlatformInfo mCopyLinkInfo;
    private PlatformInfo mMoreInfo;

    private DialogConfig() {
    }

    public String getColorForTvTitle() {
        return mColorForTvTitle;
    }

    public String getColorForTvPlatformName() {
        return mColorForTvPlatformName;
    }

    public String getColorForTvCancel() {
        return mColorForTvCancel;
    }

    public int getTextSizeForTvTitle() {
        return mTextSizeForTvTitle;
    }

    public int getTextSizeForTvPlatformName() {
        return mTextSizeForTvPlatformName;
    }

    public int getTextSizeForTvCancel() {
        return mTextSizeForTvCancel;
    }

    public PlatformInfo getWxInfo() {
        return mWxInfo;
    }

    public PlatformInfo getQQInfo() {
        return mQQInfo;
    }

    public PlatformInfo getQzoneInfo() {
        return mQzoneInfo;
    }

    public PlatformInfo getWxCircleInfo() {
        return mWxCircleInfo;
    }

    public PlatformInfo getSMSInfo() {
        return mSMSInfo;
    }

    public PlatformInfo getEmailInfo() {
        return mEmailInfo;
    }

    public PlatformInfo getCopyLinkInfo() {
        return mCopyLinkInfo;
    }

    public PlatformInfo getMoreInfo() {
        return mMoreInfo;
    }

    public static class Builder {
        private String mColorForTvTitle;
        private String mColorForTvPlatformName;
        private String mColorForTvCancel;

        /** 分享弹窗标题的文字大小,单位为px； */
        private int mTextSizeForTvTitle;
        /** 分享弹窗平台文字的颜色，单位为px； */
        private int mTextSizeForTvPlatformName;
        /** 分享弹窗取消按钮的颜色，单位为px； */
        private int mTextSizeForTvCancel;

        private PlatformInfo mWxInfo;
        private PlatformInfo mQQInfo;
        private PlatformInfo mQzoneInfo;
        private PlatformInfo mWxCircleInfo;
        private PlatformInfo mSMSInfo;
        private PlatformInfo mEmailInfo;
        private PlatformInfo mCopyLinkInfo;
        private PlatformInfo mMoreInfo;

        public Builder setColorForTvTitle(String colorForTvTitle) {
            this.mColorForTvTitle = colorForTvTitle;
            return this;
        }

        public Builder setColorForTvPlatformName(String colorForTvPlatformName) {
            this.mColorForTvPlatformName = colorForTvPlatformName;
            return this;
        }

        public Builder setColorForTvCancel(String colorForTvCancel) {
            this.mColorForTvCancel = colorForTvCancel;
            return this;
        }

        public Builder setTextSizeForTvTitle(int textSizeForTvTitle) {
            this.mTextSizeForTvTitle = textSizeForTvTitle;
            return this;
        }

        public Builder setTextSizeForTvPlatformName(int textSizeForTvPlatformName) {
            this.mTextSizeForTvPlatformName = textSizeForTvPlatformName;
            return this;
        }

        public Builder setTextSizeForTvCancel(int textSizeForTvCancel) {
            this.mTextSizeForTvCancel = textSizeForTvCancel;
            return this;
        }

        public Builder setWxInfo(PlatformInfo wxInfo) {
            this.mWxInfo = wxInfo;
            return this;
        }

        public Builder setQQInfo(PlatformInfo qqInfo) {
            this.mQQInfo = qqInfo;
            return this;
        }

        public Builder setQzoneInfo(PlatformInfo qzoneInfo) {
            this.mQzoneInfo = qzoneInfo;
            return this;
        }

        public Builder setWxCircleInfo(PlatformInfo wxCircleInfo) {
            this.mWxCircleInfo = wxCircleInfo;
            return this;
        }

        public Builder setSMSInfo(PlatformInfo smsInfo) {
            this.mSMSInfo = smsInfo;
            return this;
        }

        public Builder setEmailInfo(PlatformInfo emailInfo) {
            this.mEmailInfo = emailInfo;
            return this;
        }

        public Builder setCopyLinkInfo(PlatformInfo copyLinkInfo) {
            this.mCopyLinkInfo = copyLinkInfo;
            return this;
        }

        public Builder setMoreInfo(PlatformInfo moreInfo) {
            this.mMoreInfo = moreInfo;
            return this;
        }

        public DialogConfig build() {
            DialogConfig dialogConfig = new DialogConfig();
            dialogConfig.mColorForTvCancel = mColorForTvCancel;
            dialogConfig.mColorForTvPlatformName = mColorForTvPlatformName;
            dialogConfig.mColorForTvTitle = mColorForTvTitle;
            dialogConfig.mTextSizeForTvTitle = mTextSizeForTvTitle;
            dialogConfig.mTextSizeForTvPlatformName = mTextSizeForTvPlatformName;
            dialogConfig.mTextSizeForTvCancel = mTextSizeForTvCancel;
            dialogConfig.mEmailInfo = mEmailInfo;
            dialogConfig.mWxCircleInfo = mWxCircleInfo;
            dialogConfig.mQQInfo = mQQInfo;
            dialogConfig.mSMSInfo = mSMSInfo;
            dialogConfig.mWxInfo = mWxInfo;
            dialogConfig.mQzoneInfo = mQzoneInfo;
            dialogConfig.mCopyLinkInfo = mCopyLinkInfo;
            dialogConfig.mMoreInfo = mMoreInfo;
            return dialogConfig;
        }
    }

    public static class PlatformInfo {
        private String platName;
        private int iconResId;
        private int platform;

        public String getPlatName() {
            return platName;
        }

        public PlatformInfo setPlatName(String platName) {
            this.platName = platName;
            return this;
        }

        public int getIconResId() {
            return iconResId;
        }

        public PlatformInfo setIconResId(int iconResId) {
            this.iconResId = iconResId;
            return this;
        }

        @ShareManager.SharePlatform
        public int getPlatform() {
            return platform;
        }

        public PlatformInfo setPlatform(@ShareManager.SharePlatform int platform) {
            this.platform = platform;
            return this;
        }
    }
}
