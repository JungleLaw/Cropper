package com.imageloader.config.builder;

import android.net.Uri;
import android.support.annotation.DrawableRes;


import com.imageloader.config.Params;

import java.io.File;

/**
 * 设置加载地址或文件或其他
 * Created by Jungle on 2017/6/6.
 */
public class RequestBuilder {
    private Params params;

    public RequestBuilder(Params params) {
        this.params = params;
    }

    /**
     * @param resId 资源ID
     * @return
     */
    public ScaleBuilder load(@DrawableRes int resId) {
        params.resId = resId;
        return new ScaleBuilder(params);
    }

    /**
     * @param url 网络URL
     * @return
     */
    public ScaleBuilder load(String url) {
        params.url = url;
        return new ScaleBuilder(params);
    }

    /**
     * @param file 文件
     * @return
     */
    public ScaleBuilder load(File file) {
        params.file = file;
        return new ScaleBuilder(params);
    }

    /**
     * @param uri 文件URI
     * @return
     */
    public ScaleBuilder load(Uri uri) {
        params.uri = uri;
        return new ScaleBuilder(params);
    }

}
