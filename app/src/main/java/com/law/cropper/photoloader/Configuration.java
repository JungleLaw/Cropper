package com.law.cropper.photoloader;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Law on 2016/9/9.
 */
public class Configuration {
    public static void initGalleryConstants(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        GalleryConstants.screenWidth = metrics.widthPixels;
        GalleryConstants.screenHeight = metrics.heightPixels;
        GalleryConstants.numColumns = 3;
        for (int i = 1; ; i++) {
            if ((GalleryConstants.screenWidth - (GalleryConstants.numColumns + 1) * i) % GalleryConstants.numColumns == 0) {
                GalleryConstants.lenght = (GalleryConstants.screenWidth - (GalleryConstants.numColumns + 1) * i) / GalleryConstants.numColumns;
                GalleryConstants.divider = i;
                break;
            }
        }
        GalleryConstants.albumHeight = (int) (GalleryConstants.lenght * 1.25f);
    }

    public static class GalleryConstants {
        public static int screenWidth;
        public static int screenHeight;
        public static int numColumns;
        public static int lenght;
        public static int divider;
        public static int albumHeight;
    }
}
