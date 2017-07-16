package com.law.cropper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.law.cropper.adapter.DisplayPagerAdapter;
import com.law.cropper.photoloader.entity.Media;
import com.law.cropper.view.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jungle on 2017/7/16.
 */

public class GalleryActivity extends AppCompatActivity {
    public static final String SELECTED_RESULT_KEY = "result";

    private static final String MEDIAS_KEY = "medias";
    private static final String INDEX_KEY = "index";
    private static final String SELECTED_KEY = "selected";
    private static final String SIZE_KEY = "size";

    @BindView(R.id.vp_imgs)
    HackyViewPager vViewPager;
    @BindView(R.id.title)
    TextView vTitle;
    @BindView(R.id.tv_select)
    TextView vTvSelect;
    @BindView(R.id.btn_confirm)
    TextView vBtnConfirm;

    private int index;
    private int maxSize;
    private List<Media> mMedias;
    private ArrayList<Integer> selectedList;
    private DisplayPagerAdapter mDisplayPagerAdapter;

    public static void navigateToGalleryActivity(Activity activity, int index, int maxSize, ArrayList<Media> medias, ArrayList<Integer> selectedList, int requestCode) {
        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putExtra(INDEX_KEY, index);
        intent.putExtra(SIZE_KEY, maxSize);
        intent.putParcelableArrayListExtra(MEDIAS_KEY, medias);
        intent.putIntegerArrayListExtra(SELECTED_KEY, selectedList);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        index = getIntent().getIntExtra(INDEX_KEY, 0);
        maxSize = getIntent().getIntExtra(SIZE_KEY, 0);
        mMedias = new ArrayList<>();
        ArrayList<Media> medias = getIntent().getParcelableArrayListExtra(MEDIAS_KEY);
        mMedias.addAll(medias);
        selectedList = getIntent().getIntegerArrayListExtra(SELECTED_KEY);


        vTitle.setText(index + "/" + mMedias.size());
        if (selectedList.size() == 0) {
            vBtnConfirm.setText("确定");
        } else {
            vBtnConfirm.setText("确定(" + selectedList.size() + "/" + maxSize + ")");
        }
        mDisplayPagerAdapter = new DisplayPagerAdapter(getSupportFragmentManager(), mMedias);
        vViewPager.setPageMargin(10);
        vViewPager.setOffscreenPageLimit(2);
        vViewPager.setAdapter(mDisplayPagerAdapter);
        if (selectedList.contains((Integer) index)) {
            vTvSelect.setSelected(true);
        } else {
            vTvSelect.setSelected(false);
        }

        vViewPager.setCurrentItem(index);

        vViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (selectedList.contains((Integer) position)) {
                    vTvSelect.setSelected(true);
                } else {
                    vTvSelect.setSelected(false);
                }
            }
        });
    }

    @OnClick(R.id.tv_select)
    public void selectImg(View view) {
        if (selectedList.size() == maxSize) {
            Toast.makeText(this, "至多可以选择" + maxSize + "张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        int currentPosition = vViewPager.getCurrentItem();
        if (selectedList.contains((Integer) currentPosition)) {
            vTvSelect.setSelected(false);
            selectedList.remove((Integer) currentPosition);
        } else {
            vTvSelect.setSelected(true);
            selectedList.add((Integer) currentPosition);
        }

        if (selectedList.size() == 0) {
            vBtnConfirm.setText("确定");
        } else {
            vBtnConfirm.setText("确定(" + selectedList.size() + "/" + maxSize + ")");
        }
    }

    @OnClick(R.id.btn_confirm)
    public void confirm(View view) {
        Intent intent = new Intent();
        intent.putIntegerArrayListExtra(SELECTED_RESULT_KEY, selectedList);
        setResult(RESULT_OK, intent);
        finish();
    }
}
