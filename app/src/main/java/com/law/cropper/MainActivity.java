package com.law.cropper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
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
    EditText vEtWidthRatio;
    @BindView(R.id.et_ratio_height)
    EditText vEtHeightRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
