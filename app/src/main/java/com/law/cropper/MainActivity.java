package com.law.cropper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.imageloader.ImageDisplayUtils;
import com.law.cropper.adapter.PickedAdapter;
import com.law.cropper.photoloader.utils.Logger;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMG_SINGLE_REQUEST_CODE = 0x1001;
    private static final int PICK_IMG_MULTI_REQUEST_CODE = 0x1002;
    private static final int TAKE_CAMERA_REQUEST_CODE = 0x1003;
    private static final int TAKE_CROP_REQUEST_CODE = 0x1004;

    private static final int PICK_MAX_SIZE = 9;

    @BindView(R.id.btn_camera)
    Button vBtnCamera;
    @BindView(R.id.btn_crop_default)
    Button vBtnCropDefault;
    @BindView(R.id.btn_crop_ratio)
    Button vBtnCropRatio;
    @BindView(R.id.btn_crop_size)
    Button vBtnCropSize;
    @BindView(R.id.btn_pick_single)
    Button vBtnPickSingle;
    @BindView(R.id.btn_pick_multi)
    Button vBtnPickMulti;
    @BindView(R.id.et_width)
    EditText vEtWidth;
    @BindView(R.id.et_height)
    EditText vEtHeight;
    @BindView(R.id.et_ratio_width)
    EditText vEtRatioWidth;
    @BindView(R.id.et_ratio_height)
    EditText vEtRatioHeight;

    @BindView(R.id.img_display)
    ImageView vImgDisplay;
    @BindView(R.id.recyclerview)
    RecyclerView vRecyclerView;

    private PickedAdapter mAdapter;
    private List<String> mMedias;

    private String cropSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMedias = new ArrayList<>();
        vRecyclerView.setHasFixedSize(true);
        vRecyclerView.setNestedScrollingEnabled(false);
        vRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new PickedAdapter(this, mMedias);
        vRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.btn_camera)
    public void takeCamera() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 使用相机拍照
            Intent takephotoIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            File mImageTemp = null;
            mImageTemp = new File(getExternalFilesDir(null), "Mge_" + System.currentTimeMillis() + "_camera.jpg");
            cropSrc = mImageTemp.getAbsolutePath();
            takephotoIntent.putExtra(
                    android.provider.MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(mImageTemp));
            startActivityForResult(takephotoIntent, TAKE_CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(this, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick({R.id.btn_pick_single, R.id.btn_pick_multi})
    public void pickPhoto(View view) {
        switch (view.getId()) {
            case R.id.btn_pick_single:
                ImagePickActivity.navigateToImagePickActivity(this, ImagePickActivity.MODE_SINGLE, PICK_IMG_SINGLE_REQUEST_CODE);
                break;
            case R.id.btn_pick_multi:
                ImagePickActivity.navigateToImagePickActivity(this, ImagePickActivity.MODE_MULTI, PICK_MAX_SIZE, PICK_IMG_MULTI_REQUEST_CODE);
                break;
        }
    }

    @OnClick({R.id.btn_crop_default, R.id.btn_crop_ratio, R.id.btn_crop_size})
    public void crop(View view) {
        if (TextUtils.isEmpty(cropSrc)) {
            Toast.makeText(this, "通过Camera或者Pick Single选择一张待裁剪图片", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()) {
            case R.id.btn_crop_default:
                CropActivity.navigetToCropActivity(this, cropSrc, TAKE_CROP_REQUEST_CODE);
                break;
            case R.id.btn_crop_ratio:
                if (TextUtils.isEmpty(vEtRatioWidth.getText()) || TextUtils.isEmpty(vEtRatioHeight.getText())) {
                    Toast.makeText(this, "设置比例参数", Toast.LENGTH_SHORT).show();
                    return;
                }
                int widthRatio = Integer.parseInt(vEtRatioWidth.getText().toString().trim());
                int heightRatio = Integer.parseInt(vEtRatioHeight.getText().toString().trim());
                CropActivity.navigetToCropActivityWithRatio(this, cropSrc, widthRatio, heightRatio, TAKE_CROP_REQUEST_CODE);
                break;
            case R.id.btn_crop_size:
                if (TextUtils.isEmpty(vEtWidth.getText()) || TextUtils.isEmpty(vEtHeight.getText())) {
                    Toast.makeText(this, "设置输出参数", Toast.LENGTH_SHORT).show();
                    return;
                }
                int outWidth = Integer.parseInt(vEtWidth.getText().toString().trim());
                int outHeight = Integer.parseInt(vEtHeight.getText().toString().trim());
                CropActivity.navigetToCropActivityWithOutSize(this, cropSrc, outWidth, outHeight, TAKE_CROP_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_IMG_SINGLE_REQUEST_CODE:
                vImgDisplay.setVisibility(View.VISIBLE);
                vRecyclerView.setVisibility(View.GONE);
                cropSrc = data.getStringExtra(Constants.PICK_SINGLE_RESULT);
                ImageDisplayUtils.display(this, cropSrc, vImgDisplay);
                break;
            case PICK_IMG_MULTI_REQUEST_CODE:
                vImgDisplay.setVisibility(View.GONE);
                vRecyclerView.setVisibility(View.VISIBLE);
                mMedias.clear();
                ArrayList<String> mediasSelected = data.getStringArrayListExtra(Constants.PICK_MULTI_RESULT);
                mMedias.addAll(mediasSelected);
                mAdapter.notifyDataSetChanged();
                cropSrc = null;
                break;
            case TAKE_CAMERA_REQUEST_CODE:
                Logger.i("cameraFilePath = " + cropSrc);
//                File file = new File(cameraFilePath);
                compress(cropSrc);
                break;

            case TAKE_CROP_REQUEST_CODE:
                cropSrc = data.getStringExtra(Constants.CROP_IMG_RESULT);
                ImageDisplayUtils.display(MainActivity.this, cropSrc, vImgDisplay);
                Logger.i("TAKE_CROP_REQUEST_CODE : " + cropSrc);
                scanFile(cropSrc);
                break;
        }
    }


    private void compress(String filePath) {
        try {
            File outfile = new File(getExternalFilesDir(null), "Mge_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(outfile);
            FileInputStream fis = new FileInputStream(new File(filePath));
            byte[] buffer = new byte[4096];
            int len = -1;
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            fis.close();

            Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
            compressOptions.config = Bitmap.Config.RGB_565;
            Tiny.getInstance().source(outfile).asFile().withOptions(compressOptions).compress(new FileCallback() {
                @Override
                public void callback(boolean isSuccess, String outFilePath) {
                    if (!isSuccess) {
                        Toast.makeText(MainActivity.this, "压缩失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    vImgDisplay.setVisibility(View.VISIBLE);
                    vRecyclerView.setVisibility(View.GONE);
                    ImageDisplayUtils.display(MainActivity.this, outFilePath, vImgDisplay);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scanFile(String filePath) {
        Uri fileUri = Uri.fromFile(new File(filePath));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(fileUri);
        sendBroadcast(intent);
    }

}
