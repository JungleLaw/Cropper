package com.imageloader.config.builder;


import com.imageloader.config.Params;

/**
 * Created by Jungle on 2017/6/6.
 */

public class GifTypeBuilder extends DrawableBuilder {

    public GifTypeBuilder(Params params) {
        super(params);
    }

    public DrawableBuilder asBitmap() {
        params.asBitmap = true;
        params.asGif = false;
        return this;
    }

    public DrawableBuilder asGif() {
        params.asGif = true;
        params.asBitmap = false;
        return this;
    }
}
