package com.imageloader.impl;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.imageloader.config.GlobalConfig;

import java.io.File;

/**
 * Created by Jungle on 2017/6/6.
 */

public class GlideModelConfig implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(new DiskLruCacheFactory(new File(context.getCacheDir(), GlobalConfig.cacheFolderName).getAbsolutePath(),
                GlobalConfig.cacheSize * 1024 * 1024));
    }

    @Override
    public void registerComponents(Context context, Registry registry) {

    }
}
