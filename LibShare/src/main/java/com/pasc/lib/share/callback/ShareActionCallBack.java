package com.pasc.lib.share.callback;

import com.pasc.lib.share.ShareManager;
/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2018/9/3
 * 分享回调
 */
public interface ShareActionCallBack {
    /**
     * 分享成功
     *  @param platform 平台tag
     */
    void onComplete(@ShareManager.SharePlatform int platform);

    /**
     * 分享取消
     * @param platform 平台tag
     */
    void onCancel(@ShareManager.SharePlatform int platform);

    /**
     * 分享失败
     * @param platform 平台tag
     * @param var3  异常
     */
    void onError(@ShareManager.SharePlatform int platform, Throwable var3);

}
