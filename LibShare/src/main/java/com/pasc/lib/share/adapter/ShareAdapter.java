package com.pasc.lib.share.adapter;

import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasc.lib.base.util.DensityUtils;
import com.pasc.lib.share.R;
import com.pasc.lib.share.ShareManager;
import com.pasc.lib.share.config.DialogConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
/**
 * Copyright (C) 2016 pasc Licensed under the Apache License, Version 2.0 (the "License");
 *
 * @author chendaixi947
 * @version 1.0
 * @date 2018/9/3
 * 分享adapter
 */
public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
    public static final int MODE_LINEAR_LAYOUT = 1;
    public static final int MODE_GRID_LAYOUT = 2;
    @ModeType
    private int type;

    public ShareAdapter setType(@ModeType int type) {
        this.type = type;
        return this;
    }

    /**
     * 分享渠道信息
     */
    private List<DialogConfig.PlatformInfo> platInfoList;
    private OnItemClickListener onItemClickListener;

    public ShareAdapter(int type, List<DialogConfig.PlatformInfo> platInfoList) {
        this.type = type;
        this.platInfoList = platInfoList;
    }

    public interface OnItemClickListener {
        /**
         * 分享渠道点击
         *
         * @param view     点击view
         * @param position 点击pos
         * @param platform 分享渠道tag
         */
        void onItemClick(View view, int position, @ShareManager.SharePlatform int platform);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_lib_item_share, parent, false);
        return new ShareAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position2) {
        final int position = holder.getAdapterPosition();
        final DialogConfig.PlatformInfo platInfo = platInfoList.get(position);
        holder.mImg.setImageResource(platInfo.getIconResId());
        holder.mText.setText(platInfo.getPlatName());

        DialogConfig dialogConfig = ShareManager.getInstance().getDialogConfig();
        if (dialogConfig != null) {
            String colorForTvPlatformName = dialogConfig.getColorForTvPlatformName();
            if (colorForTvPlatformName != null &&
                    colorForTvPlatformName.startsWith("#")) {
                holder.mText.setTextColor(Color.parseColor(colorForTvPlatformName));
            }

            int textSizeForTvPlatformName = dialogConfig.getTextSizeForTvPlatformName();
            if (textSizeForTvPlatformName != 0) {
                holder.mText.setTextSize(textSizeForTvPlatformName);
            }
        }

        if (type == MODE_LINEAR_LAYOUT) {
            //从左往右排列
            RecyclerView.LayoutParams params =
                    new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT);
            if (position == 0) {
                params.setMargins(DensityUtils.dp2px(24), 0, 0, 0);
                holder.itemView.setLayoutParams(params);
            } else if (position == platInfoList.size() - 1) {
                params.setMargins(DensityUtils.dp2px(30), 0, DensityUtils.dp2px(24), 0);
                holder.itemView.setLayoutParams(params);
            } else {
                params.setMargins(DensityUtils.dp2px(30), 0, DensityUtils.dp2px(0), 0);
                holder.itemView.setLayoutParams(params);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position, platInfo.getPlatform());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return platInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mText;
        ImageView mImg;

        ViewHolder(View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.share_gr_item_image);
            mText = itemView.findViewById(R.id.share_gr_item_name);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_GRID_LAYOUT, MODE_LINEAR_LAYOUT})
    public @interface ModeType {
    }
}
