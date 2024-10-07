package com.pasc.lib.share.callback;

import com.pasc.lib.share.ShareManager;

/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2018/9/3
 * 分享点击回调
 */
public interface ShareActionListener extends ShareActionCallBack {
    /**
     * 分享平台点击
     *
     * @param platform 分享平台常量
     */
    void onPlatformClick(@ShareManager.SharePlatform int platform);
}
