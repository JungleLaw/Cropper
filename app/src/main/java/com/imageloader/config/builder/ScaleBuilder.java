package com.imageloader.config.builder;


import com.imageloader.config.Params;
import com.imageloader.config.ScaleMode;

/**
 * SCALETYPE建造类
 * Created by Jungle on 2017/6/6.
 */
public class ScaleBuilder extends GifTypeBuilder {
    public ScaleBuilder(Params params) {
        super(params);
    }

    public GifTypeBuilder fitCenter() {
        params.mode = ScaleMode.FIT_CENTER;
        return this;
    }


    public GifTypeBuilder centerCrop() {
        params.mode = ScaleMode.CENTER_CROP;
        return this;
    }

}
