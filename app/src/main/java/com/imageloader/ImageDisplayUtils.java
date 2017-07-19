package com.imageloader;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;


import java.io.File;

import com.imageloader.bean.MgeImage;
import com.imageloader.config.Params;

/**
 * Created by Jungle on 2017/6/7.
 */

public class ImageDisplayUtils {
    public static final class MODE {
        public static final int MODE_BITMAP = 10000;
        public static final int MODE_GIF = 10001;
    }

    public static void display(Context context, String url, ImageView target) {
        display(context, url, target, -1, -1, 0, 0, 0);
    }

    public static void display(Context context, File file, ImageView target) {
        display(context, file, target, -1, -1, 0, 0, 0);
    }

    public static void display(Context context, Uri uri, ImageView target) {
        display(context, uri, target, -1, -1, 0, 0, 0);
    }

    public static void display(Context context, int resId, ImageView target) {
        display(context, resId, target, -1, -1, 0, 0, 0);
    }

    public static void display(Context context, MgeImage mgeImage, ImageView target) {
        display(context, mgeImage, target, -1, -1, 0, 0, 0);
    }

    public static void displayWithPlaceholder(Context context, String url, ImageView target, int placeholderResId) {
        display(context, url, target, placeholderResId, -1, 0, 0, 0);
    }

    public static void displayWithPlaceholder(Context context, File file, ImageView target, int placeholderResId) {
        display(context, file, target, placeholderResId, -1, 0, 0, 0);
    }

    public static void displayWithPlaceholder(Context context, Uri uri, ImageView target, int placeholderResId) {
        display(context, uri, target, placeholderResId, -1, 0, 0, 0);
    }

    public static void displayWithPlaceholder(Context context, int resId, ImageView target, int placeholderResId) {
        display(context, resId, target, placeholderResId, -1, 0, 0, 0);
    }

    public static void displayWithPlaceholder(Context context, MgeImage mgeImage, ImageView target, int placeholderResId) {
        display(context, mgeImage, target, placeholderResId, -1, 0, 0, 0);
    }

    public static void displayWithMode(Context context, String url, ImageView target, int mode) {
        display(context, url, target, -1, -1, mode, 0, 0);
    }

    public static void displayWithMode(Context context, File file, ImageView target, int mode) {
        display(context, file, target, -1, -1, mode, 0, 0);
    }

    public static void displayWithMode(Context context, Uri uri, ImageView target, int mode) {
        display(context, uri, target, -1, -1, mode, 0, 0);
    }

    public static void displayWithMode(Context context, int resId, ImageView target, int mode) {
        display(context, resId, target, -1, -1, mode, 0, 0);
    }

    public static void displayWithMode(Context context, MgeImage mgeImage, ImageView target, int mode) {
        display(context, mgeImage, target, -1, -1, mode, 0, 0);
    }

    public static void displayWithOverride(Context context, String url, ImageView target, int width, int height) {
        display(context, url, target, -1, -1, 0, width, height);
    }

    public static void displayWithOverride(Context context, File file, ImageView target, int width, int height) {
        display(context, file, target, -1, -1, 0, width, height);
    }

    public static void displayWithOverride(Context context, Uri uri, ImageView target, int width, int height) {
        display(context, uri, target, -1, -1, 0, width, height);
    }

    public static void displayWithOverride(Context context, int resId, ImageView target, int width, int height) {
        display(context, resId, target, -1, -1, 0, width, height);
    }

    public static void displayWithOverride(Context context, MgeImage mgeImage, ImageView target, int width, int height) {
        display(context, mgeImage, target, -1, -1, 0, width, height);
    }

    public static void display(Context context, String url, ImageView target, int placeholderResId, int errorResId, int mode, int width, int height) {
        switch (mode) {
            case MODE.MODE_BITMAP:
                ImageLoader.with(context).load(url).asBitmap().placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
            case MODE.MODE_GIF:
                ImageLoader.with(context).load(url).asGif().placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
            default:
                ImageLoader.with(context).load(url).placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
        }
    }

    public static void display(Context context, File file, ImageView target, int placeholderResId, int errorResId, int mode, int width, int height) {
        switch (mode) {
            case MODE.MODE_BITMAP:
                ImageLoader.with(context).load(file).asBitmap().placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
            case MODE.MODE_GIF:
                ImageLoader.with(context).load(file).asGif().placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
            default:
                ImageLoader.with(context).load(file).placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
        }
    }

    public static void display(Context context, Uri uri, ImageView target, int placeholderResId, int errorResId, int mode, int width, int height) {
        switch (mode) {
            case MODE.MODE_BITMAP:
                ImageLoader.with(context).load(uri).asBitmap().placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
            case MODE.MODE_GIF:
                ImageLoader.with(context).load(uri).asGif().placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
            default:
                ImageLoader.with(context).load(uri).placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
        }
    }

    public static void display(Context context, int resId, ImageView target, int placeholderResId, int errorResId, int mode, int width, int height) {
        switch (mode) {
            case MODE.MODE_BITMAP:
                ImageLoader.with(context).load(resId).asBitmap().placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
            case MODE.MODE_GIF:
                ImageLoader.with(context).load(resId).asGif().placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
            default:
                ImageLoader.with(context).load(resId).placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
        }
    }

    public static void display(Context context, MgeImage mgeImage, ImageView target, int placeholderResId, int errorResId, int mode, int width, int height) {
        switch (mode) {
            case MODE.MODE_BITMAP:
                ImageLoader.with(context).load(mgeImage.getCurrentBestUrl(context)).asBitmap().placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
            case MODE.MODE_GIF:
                ImageLoader.with(context).load(mgeImage.getCurrentBestUrl(context)).asGif().placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
            default:
                ImageLoader.with(context).load(mgeImage.getCurrentBestUrl(context)).placeHolder(placeholderResId).error(errorResId).override(width, height).into(target);
                break;
        }
    }

    public static void displayTask(Context context, String url, Params.BitmapListener listener) {
        ImageLoader.with(context).load(url).asBitmap().setBitmapListener(listener);
    }

    public static void displayTask(Context context, File file, Params.BitmapListener listener) {
        ImageLoader.with(context).load(file).asBitmap().setBitmapListener(listener);
    }

    public static void displayTask(Context context, Uri uri, Params.BitmapListener listener) {
        ImageLoader.with(context).load(uri).asBitmap().setBitmapListener(listener);
    }

    public static void displayTask(Context context, int resId, Params.BitmapListener listener) {
        ImageLoader.with(context).load(resId).asBitmap().setBitmapListener(listener);
    }

    public static void displayTask(Context context, String url, Params.BitmapListener listener, int placeholderResId, int errorResId) {
        ImageLoader.with(context).load(url).asBitmap().placeHolder(placeholderResId).error(errorResId).setBitmapListener(listener);
    }

    public static void displayTask(Context context, File file, Params.BitmapListener listener, int placeholderResId, int errorResId) {
        ImageLoader.with(context).load(file).asBitmap().placeHolder(placeholderResId).error(errorResId).setBitmapListener(listener);
    }

    public static void displayTask(Context context, Uri uri, Params.BitmapListener listener, int placeholderResId, int errorResId) {
        ImageLoader.with(context).load(uri).asBitmap().placeHolder(placeholderResId).error(errorResId).setBitmapListener(listener);
    }

    public static void displayTask(Context context, int resId, Params.BitmapListener listener, int placeholderResId, int errorResId) {
        ImageLoader.with(context).load(resId).asBitmap().placeHolder(placeholderResId).error(errorResId).setBitmapListener(listener);
    }
}
