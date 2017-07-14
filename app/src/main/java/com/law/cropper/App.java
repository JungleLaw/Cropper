package com.law.cropper;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.zhy.autolayout.config.AutoLayoutConifg;

import imageloader.ImageLoader;
import imageloader.impl.GlideImageLoaderImpl;

/**
 * Created by Jungle on 2017/7/14.
 */

public class App extends Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AutoLayoutConifg.getInstance().useDeviceSize();
        ImageLoader.init(this, 10, new GlideImageLoaderImpl());
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoader.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        ImageLoader.trimMemory(level);
    }

    public static Application getInstance() {
        return instance;
    }
}
