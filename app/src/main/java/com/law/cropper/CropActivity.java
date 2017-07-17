package com.law.cropper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.law.cropper.crop.CropIwaView;
import com.zxy.tiny.common.TinyException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jungle on 2017/7/14.
 */

public class CropActivity extends AppCompatActivity {
    private static final String RATIO_WIDTH_KEY = "width";
    private static final String RATIO_HEIGHT_KEY = "height";
    private static final String OUT_WIDTH_KEY = "width";
    private static final String OUT_HEIGHT_KEY = "height";

    @BindView(R.id.crop_view)
    CropIwaView vCropIwaView;

    private int widthRatio;
    private int heightRatio;
    private int outWidth;
    private int outHeight;


    public static void navigetToCropActivityWithRatio(Activity activity, int widthRatio, int heightRatio, int requestCode) {
        Intent intent = new Intent();
        intent.putExtra(RATIO_WIDTH_KEY, widthRatio);
        intent.putExtra(RATIO_HEIGHT_KEY, heightRatio);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void navigetToCropActivityWithSize(Activity activity, int width, int height, int requestCode) {
        Intent intent = new Intent();
        intent.putExtra(OUT_WIDTH_KEY, width);
        intent.putExtra(OUT_HEIGHT_KEY, height);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if (getIntent().hasExtra(RATIO_WIDTH_KEY) && getIntent().hasExtra(RATIO_HEIGHT_KEY)) {
            widthRatio = getIntent().getIntExtra(RATIO_WIDTH_KEY, 0);
            heightRatio = getIntent().getIntExtra(RATIO_HEIGHT_KEY, 0);
            if (widthRatio == 0 || heightRatio == 0) {
                throw new TinyException.IllegalArgumentException("");
            }
        } else {

        }


    }
}
