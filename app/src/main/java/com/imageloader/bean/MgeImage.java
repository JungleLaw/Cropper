package com.imageloader.bean;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * 标准图片结构，服务器会返回各种压缩过的图片路径，对应的结构就是这个来
 * <p>
 * Created by WindProtect on 2015/12/28.
 */
public class MgeImage implements Parcelable, Serializable {

    /**
     * 缩略图，150x150, 质量60%
     */
    private String s;
    /**
     * 中图，400x400, 质量80%
     */
    private String m;
    /**
     * 大图，等比缩放，限制最长边长800, 质量90%
     */
    private String l;
    /**
     * 原图，客户端上传前做等比缩放，限制最长边长1080, 质量100%
     */
    private String h;

    private String key;

    public MgeImage() {
    }

    public MgeImage(String path) {
        s = path;
        m = path;
        l = path;
        h = path;
    }

    /**
     * 缩略图 150x150, 质量60%
     */
    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    /**
     * 中图 400x400, 质量80%
     */
    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    /**
     * 大图，等比缩放，限制最长边长800, 质量90%
     */
    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    /**
     * 原图，客户端上传前做等比缩放，限制最长边长1080, 质量100%
     */
    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    /**
     * 获得本图片的键
     *
     * @return 没有地址的话会返回null
     */
    public String getKey() {
        if (!TextUtils.isEmpty(key)) {
            return key;
        }

        String url = null;
        if (!TextUtils.isEmpty(this.h)) {
            url = this.h;
        } else if (!TextUtils.isEmpty(this.l)) {
            url = this.l;
        } else if (!TextUtils.isEmpty(this.m)) {
            url = this.m;
        } else if (!TextUtils.isEmpty(this.s)) {
            url = this.s;
        }

        if (TextUtils.isEmpty(url)) {
            return "default";
        }

        StringBuilder sb = new StringBuilder(url);
        int startIndex = sb.indexOf("?");
        if (startIndex > -1)
            sb.delete(startIndex, sb.length());

        key = sb.toString();
        return key;
    }

    /**
     * 获得当前状态下最佳地址，判断条件如下
     * <p>
     * -- 移动环境下，普通图片获取{@link #getM()}地址,头像获取{@link #getS()}
     * -- WIFI环境下，普通图片获取{@link #getL()}地址，头像获取{@link #getS()}地址
     *
     * @param context
     */
    public String getCurrentBestUrl(Context context) {
        return getCurrentBestUrl(context, false);
    }

    /**
     * 获得当前状态下最佳地址，判断条件如下
     * <p>
     * -- 移动环境下，普通图片获取{@link #getM()}地址,头像获取{@link #getS()}
     * -- WIFI环境下，普通图片获取{@link #getL()}地址，头像获取{@link #getS()}地址
     *
     * @param context
     * @param isAvatar 是否是头像, 默认否
     */
    public String getCurrentBestUrl(Context context, boolean isAvatar) {
        int networkStatus = getNetworkStatus(context);

        String url = null;

        if (networkStatus > 1) {
            // 移动环境
            url = isAvatar ? getS() : getM();
        } else {
            url = isAvatar ? getM() : getL();
        }

        if (TextUtils.isEmpty(url)) {
            url = "file:///android_asset/default_picture.png";
        }

        return url;
    }

    /**
     * 获取网络情况
     *
     * @param context
     * @return <ul>
     * <li>-1.无网络</li>
     * <li>0.其他</li>
     * <li>1.wifi</li>
     * <li>2.2G</li>
     * <li>3.3G</li>
     * <li>4.4G</li>
     * </ul>
     * @author ben75(http://stackoverflow.com/users/1818045/ben75)
     */
    public static int getNetworkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        // 未连接
        if (info == null || !info.isConnected())
            // 无网络
            return -1;
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            // wifi
            return 1;
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    // 2G
                    return 2;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    // 3G
                    return 3;
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    // 4G
                    return 4;
                default:
                    // 其他
                    return 0;
            }
        }
        // 其他
        return 0;
    }

    protected MgeImage(Parcel in) {
        s = in.readString();
        m = in.readString();
        l = in.readString();
        h = in.readString();
    }

    public static final Creator<MgeImage> CREATOR = new Creator<MgeImage>() {
        @Override
        public MgeImage createFromParcel(Parcel in) {
            return new MgeImage(in);
        }

        @Override
        public MgeImage[] newArray(int size) {
            return new MgeImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(s);
        dest.writeString(m);
        dest.writeString(l);
        dest.writeString(h);
    }

}

