package com.pasc.lib.share.config;

import java.io.Serializable;

/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2018/8/15
 * 分享内容定制
 */
public class ShareContent implements Serializable {

    /** 默认好友分享的title */
    private String title = "";
    /** 分享的描述 */
    private String content = "";
    /** 分享的地址 */
    private String shareUrl = "";
    /** 图片地址 */
    private String imageUrl = "";
    /** 短信分享的内容 */
    private String smsContent = "";
    /** 调用更多分享的内容 */
    private String emailContent = "";
    /** 调用邮箱分享的标题 */
    private String emailTitle = "";
    /** 调用邮箱分享的邮件地址 */
    private String emailAddress = "";
    /** 复制链接Url */
    private String copyLinkUrl = "";
    /** 更多分享的内容 */
    private String moreContent = "";

    private Platform platformForQQ;
    private Platform platformForWeChat;
    private Platform platformForWeChatCircle;
    private Platform platformForQzone;

    private ShareContent() {
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public String getCopyLinkUrl() {
        return copyLinkUrl;
    }

    public String getMoreContent() {
        return moreContent;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public Platform getPlatformForQQ() {
        return platformForQQ;
    }

    public Platform getPlatformForWeChat() {
        return platformForWeChat;
    }

    public Platform getPlatformForWeChatCircle() {
        return platformForWeChatCircle;
    }

    public Platform getPlatformForQzone() {
        return platformForQzone;
    }

    public static class Builder implements Serializable{
        /** 默认好友分享的title */
        private String title = "";
        /** 分享的描述 */
        private String content = "";
        /** 分享的地址 */
        private String shareUrl = "";
        /** 图片地址 */
        private String imageUrl = "";
        /** 短信分享的内容 */
        private String smsContent = "";
        /** 调用邮箱分享的内容 */
        private String emailContent = "";
        /** 调用邮箱分享的标题 */
        private String emailTitle = "";
        /** 调用邮箱分享的邮件地址 */
        private String emailAddress = "";
        /** 复制链接Url */
        private String copyLinkUrl = "";
        /** 更多分享的内容 */
        private String moreContent = "";

        private Platform platformForQQ;
        private Platform platformForWeChat;
        private Platform platformForWeChatCircle;
        private Platform platformForQzone;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
            return this;
        }

        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setSmsContent(String smsContent) {
            this.smsContent = smsContent;
            return this;
        }

        public Builder setEmailContent(String emailContent) {
            this.emailContent = emailContent;
            return this;
        }

        public Builder setEmailTitle(String emailTitle) {
            this.emailTitle = emailTitle;
            return this;
        }

        public Builder setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public Builder setCopyLinkUrl(String copyLinkUrl) {
            this.copyLinkUrl = copyLinkUrl;
            return this;
        }

        public Builder setMoreContent(String moreContent) {
            this.moreContent = moreContent;
            return this;
        }

        public Builder setPlatformForQQ(Platform platformForQQ) {
            this.platformForQQ = platformForQQ;
            return this;
        }

        public Builder setPlatformForWeChat(Platform platformForWeChat) {
            this.platformForWeChat = platformForWeChat;
            return this;
        }

        public Builder setPlatformForWeChatCircle(Platform platformForWeChatCircle) {
            this.platformForWeChatCircle = platformForWeChatCircle;
            return this;
        }

        public Builder setPlatformForQzone(Platform platformForQzone) {
            this.platformForQzone = platformForQzone;
            return this;
        }

        public ShareContent build() {
            ShareContent shareContent = new ShareContent();
            shareContent.content = content;
            shareContent.title = title;
            shareContent.shareUrl = shareUrl;
            shareContent.imageUrl = imageUrl;
            shareContent.copyLinkUrl = copyLinkUrl;
            shareContent.smsContent = smsContent;
            shareContent.emailContent = emailContent;
            shareContent.emailTitle = emailTitle;
            shareContent.emailAddress = emailAddress;
            shareContent.emailContent = emailContent;
            shareContent.moreContent = moreContent;
            shareContent.platformForQQ = platformForQQ;
            shareContent.platformForQzone = platformForQzone;
            shareContent.platformForWeChat = platformForWeChat;
            shareContent.platformForWeChatCircle = platformForWeChatCircle;
            return shareContent;
        }
    }

    /**
     * 各个平台分享内容定制
     */
    public static class Platform implements Serializable {
        private String content = "";
        private String shareUrl = "";

        public Platform(String content, String shareUrl) {
            this.content = content;
            this.shareUrl = shareUrl;
        }

        public String getContent() {
            return content;
        }

        public String getShareUrl() {
            return shareUrl;
        }
    }
}
