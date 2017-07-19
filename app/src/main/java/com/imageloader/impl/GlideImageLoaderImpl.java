package com.imageloader.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.imageloader.ImageLoader;
import com.imageloader.MyUtil;
import com.imageloader.config.GlobalConfig;
import com.imageloader.config.ScaleMode;
import com.imageloader.config.SingleConfig;
import com.imageloader.i.FileGetter;
import com.imageloader.i.ILoader;

import java.io.File;

//import com.bumptech.glide.DrawableTypeRequest;
//import com.bumptech.glide.request.animation.GlideAnimation;

/**
 * Created by Jungle on 2017/6/6.
 */

public class GlideImageLoaderImpl implements ILoader {
    @Override
    public void init(Context context, int cacheSizeInM) {
        Glide.get(context).setMemoryCategory(MemoryCategory.NORMAL);
    }

    @Override
    public void request(final SingleConfig config) {
        if (config.getParams().getBitmapListener() != null) {
            RequestManager requestManager = Glide.with(config.getParams().context);
            RequestBuilder requestBuilder = getRequest(config, requestManager.asBitmap());
            if (config.getParams().getWidth() > 0 && config.getParams().getHeight() > 0) {
                requestBuilder.apply(RequestOptions.overrideOf(config.getParams().getWidth(), config.getParams().getHeight()));
            }

            SimpleTarget target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    // do something with the bitmap
                    // for demonstration purposes, let's just set it to an ImageView
                    // BitmapPool mBitmapPool = Glide.get(BigLoader.context).getBitmapPool();
                    //bitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight())

                    config.getParams().getBitmapListener().onSuccess(bitmap);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    config.getParams().getBitmapListener().onFail();
                }
            };
            // setShapeModeAndBlur(config, request);
            requestBuilder.into(target);

        } else {
            RequestManager requestManager = Glide.with(config.getParams().context);
//            RequestManager requestManager = null;
            RequestBuilder requestBuilder = null;
            if (config.getParams().asBitmap) {
//                requestBuilder = Glide.with(config.getParams().context).asBitmap();
                requestBuilder = getRequest(config, requestManager.asBitmap());
            } else if (config.getParams().asGif) {
//                request.asGif().into((ImageView) config.getParams().getTarget());
//                requestBuilder = Glide.with(requestManager.asGif();
                requestBuilder = getRequest(config, requestManager.asGif());
            } else {
                requestBuilder = getDrawableTypeRequest(config, requestManager);
            }

            if (requestBuilder == null) {
                return;
            }

            int scaleMode = config.getParams().getMode();
            switch (scaleMode) {
                case ScaleMode.FIT_CENTER:
                    requestBuilder.apply(RequestOptions.fitCenterTransform());
                    break;
                case ScaleMode.CENTER_CROP:
                case ScaleMode.CENTER_INSIDE:
                case ScaleMode.FIT_XY:
                case ScaleMode.FIT_END:
                case ScaleMode.FOCUS_CROP:
                case ScaleMode.CENTER:
                case ScaleMode.FIT_START:
//                    request.centerCrop();
                    requestBuilder.apply(RequestOptions.centerCropTransform());
                    break;
                default:
                    break;
            }

            if (config.getParams().getWidth() > 0 && config.getParams().getHeight() > 0) {
//                request.override(config.getParams().getWidth(), config.getParams().getHeight());
                requestBuilder.apply(RequestOptions.overrideOf(config.getParams().getWidth(), config.getParams().getHeight()));
            }

            if (config.getParams().getPlaceHolderResId() > 0) {
//                request.placeholder(config.getParams().getPlaceHolderResId());
                requestBuilder.apply(RequestOptions.placeholderOf(config.getParams().getPlaceHolderResId()));
            }

            if (config.getParams().getThumbnail() > 0) {
                requestBuilder.thumbnail(config.getParams().getThumbnail());
            }

            if (config.getParams().getErrorResId() > 0) {
//                request.error(config.getParams().getErrorResId());
                requestBuilder.apply(RequestOptions.errorOf(config.getParams().getErrorResId()));
            }

            if (config.getParams().getTarget() instanceof ImageView) {
                requestBuilder.into((ImageView) config.getParams().getTarget());
            }
        }
    }

    @Nullable
    private RequestBuilder getDrawableTypeRequest(SingleConfig config, RequestManager requestManager) {
        RequestBuilder<Drawable> request = null;
        if (!TextUtils.isEmpty(config.getParams().getUrl())) {
            request = requestManager.load(config.getParams().getUrl());
            //request.diskCacheStrategy(DiskCacheStrategy.SOURCE);//只缓存原图
        } else if (config.getParams().getFile() == null) {
            request = requestManager.load(config.getParams().getFile());
        } else if (!TextUtils.isEmpty(config.getParams().getUri().getPath())) {
            request = requestManager.load(config.getParams().getUri());
        } else if (config.getParams().getResId() > 0) {
            request = requestManager.load(config.getParams().getResId());
        }
        return request;
    }

    private RequestBuilder getRequest(SingleConfig config, RequestBuilder builder) {
        RequestBuilder request = null;
        if (!TextUtils.isEmpty(config.getParams().getUrl())) {
            request = builder.load(config.getParams().getUrl());
            //request.diskCacheStrategy(DiskCacheStrategy.SOURCE);//只缓存原图
        } else if (config.getParams().getFile() == null) {
            request = builder.load(config.getParams().getFile());
        } else if (!TextUtils.isEmpty(config.getParams().getUri().getPath())) {
            request = builder.load(config.getParams().getUri());
        } else if (config.getParams().getResId() > 0) {
            request = builder.load(config.getParams().getResId());
        }
        return request;
    }

    @Override
    public void pause() {
        Glide.with(GlobalConfig.mContext).pauseRequestsRecursive();
    }

    @Override
    public void resume() {
        Glide.with(GlobalConfig.mContext).resumeRequestsRecursive();
    }

    @Override
    public void clearDiskCache() {
        Glide.get(ImageLoader.context).clearDiskCache();
    }

    @Override
    public void clearMomoryCache() {
        Glide.get(ImageLoader.context).clearMemory();
    }

    @Override
    public long getCacheSize() {
        return MyUtil.getCacheSize();
    }

    @Override
    public void clearCacheByUrl(String url) {

    }

    @Override
    public void clearMomoryCache(SingleConfig config, View view) {
        Glide.with(config.getParams().context).clear(view);
    }

    @Override
    public void clearMomoryCache(String url) {

    }

    @Override
    public File getFileFromDiskCache(String url) {
        return null;
    }

    @Override
    public void getFileFromDiskCache(String url, final FileGetter getter) {
        Glide.with(ImageLoader.context)
                .load(url)
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, Transition<? super File> transition) {
                        if (resource.exists() && resource.isFile()) {//&& resource.length() > 70
                            getter.onSuccess(resource);
                        } else {
                            getter.onFail();
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        getter.onFail();
                    }
                });
    }

    @Override
    public boolean isCached(String url) {
        return false;
    }

    @Override
    public void trimMemory(int level) {
        Glide.with(GlobalConfig.mContext).onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        Glide.with(GlobalConfig.mContext).onLowMemory();
    }
}
