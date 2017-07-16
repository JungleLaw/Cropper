package com.imageloader.config.builder;

import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;

import com.imageloader.config.Params;


/**
 * Created by Jungle on 2017/6/6.
 */

public class DrawableBuilder extends Builder {

    public DrawableBuilder(Params params) {
        super(params);
    }

    public DrawableBuilder placeHolder(@DrawableRes int placeHolderResId) {
        params.placeHolderResId = placeHolderResId;
        return this;
    }

    public DrawableBuilder error(@DrawableRes int errorResId) {
        params.errorResId = errorResId;
        return this;
    }

    public DrawableBuilder override(int width, int height) {
        params.width = width;
        params.height = height;
        return this;
    }

    public DrawableBuilder thumb(@FloatRange(from = 0.0, to = 1.0) float thumbnail) {
        params.thumbnail = thumbnail;
        return this;
    }

}
