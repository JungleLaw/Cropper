package com.law.cropper;

import android.content.Intent;
import android.os.Bundle;
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
import com.law.cropper.photoloader.entity.Media;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMG_SINGLE_REQUEST_CODE = 0x1001;
    private static final int PICK_IMG_MULTI_REQUEST_CODE = 0x1002;

    private static final int PICK_MAX_SIZE = 9;

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
    @BindView(R.id.et_ratio)
    EditText vEtRatio;

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
            Toast.makeText(this, "通过Pick Single选择一张待裁剪图片", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (view.getId()) {
            case R.id.btn_crop_default:

                break;
            case R.id.btn_crop_ratio:
                break;
            case R.id.btn_crop_size:
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
                break;
        }
    }
}
