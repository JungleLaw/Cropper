package com.law.cropper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.law.cropper.crop.AspectRatio;
import com.law.cropper.crop.CropIwaView;
import com.law.cropper.photoloader.utils.Logger;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jungle on 2017/7/14.
 */

public class CropActivity extends AppCompatActivity {
    private static final String PATH_KEY = "path";
    private static final String RATIO_WIDTH_KEY = "width_ratio";
    private static final String RATIO_HEIGHT_KEY = "height_ratio";
    private static final String OUT_WIDTH_KEY = "out_width";
    private static final String OUT_HEIGHT_KEY = "out_height";

    private static final int MODE_DYNAMIC = 1;
    private static final int MODE_RATIO = 2;
    private static final int MODE_SIZE = 3;


    @BindView(R.id.crop_view)
    CropIwaView vCropIwaView;

    private int widthRatio = -1;
    private int heightRatio = -1;
    private int outWidth = -1;
    private int outHeight = -1;

    private String path;
    private int mode = -1;

    public static void navigetToCropActivity(Activity activity, String path, int requestCode) {
        Intent intent = new Intent(activity, CropActivity.class);
        intent.putExtra(PATH_KEY, path);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void navigetToCropActivityWithRatio(Activity activity, String path, int widthRatio, int heightRatio, int requestCode) {
        Intent intent = new Intent(activity, CropActivity.class);
        intent.putExtra(PATH_KEY, path);
        intent.putExtra(RATIO_WIDTH_KEY, widthRatio);
        intent.putExtra(RATIO_HEIGHT_KEY, heightRatio);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void navigetToCropActivityWithSize(Activity activity, String path, int width, int height, int requestCode) {
        Intent intent = new Intent(activity, CropActivity.class);
        intent.putExtra(PATH_KEY, path);
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
            if (widthRatio <= 0 || heightRatio <= 0) {
                throw new IllegalArgumentException("widthRatio and heightRatio must be > 0.");
            }
            mode = MODE_RATIO;
        } else if (getIntent().hasExtra(OUT_WIDTH_KEY) && getIntent().hasExtra(OUT_HEIGHT_KEY)) {
            outWidth = getIntent().getIntExtra(OUT_WIDTH_KEY, 0);
            outHeight = getIntent().getIntExtra(OUT_HEIGHT_KEY, 0);
            if (outWidth <= 0 || outHeight <= 0) {
                throw new IllegalArgumentException("outWidth and outHeight must be > 0.");
            }
            mode = MODE_SIZE;
        } else {
            mode = MODE_DYNAMIC;
        }

        Logger.i("mode = " + mode);

        path = getIntent().getStringExtra(PATH_KEY);
        Uri uri = Uri.fromFile(new File(path));


        vCropIwaView.configureOverlay().setOverlayColor(ContextCompat.getColor(this, R.color.cropiwa_default_overlay_color));
        vCropIwaView.configureImage().setImageScaleEnabled(true).apply();
        vCropIwaView.configureImage().setScale(0.02f).apply();
        vCropIwaView.setImageUri(uri);

        switch (mode) {
            case MODE_RATIO:
                vCropIwaView.configureOverlay().setAspectRatio(new AspectRatio(widthRatio, heightRatio)).apply();
                break;
            case MODE_SIZE:
                int divisor = getMaxCommonDivisor(outWidth, outHeight);
                vCropIwaView.configureOverlay().setAspectRatio(new AspectRatio(outWidth / divisor, outHeight / divisor)).apply();
                break;
            case MODE_DYNAMIC:
                vCropIwaView.configureOverlay().setDynamicCrop(true).setAspectRatio(AspectRatio.IMG_SRC).apply();
                break;
        }
    }

    @OnClick(R.id.save)
    public void save(View view) {

    }

    //辗转相除法：返回公约数
    public static int getMaxCommonDivisor(int x, int y) {
        int a, b, c;
        a = x;
        b = y;
        while (b != 0) {
            c = a % b;
            a = b;
            b = c;
        }
        return a;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detach();
    }

    public void detach() {
        widthRatio = -1;
        heightRatio = -1;
        outWidth = -1;
        outHeight = -1;

        path = null;
        mode = -1;
        vCropIwaView = null;
    }
}
