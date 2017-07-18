package com.law.cropper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.law.cropper.adapter.DisplayPagerAdapter;
import com.law.cropper.adapter.ImagePreviewAdapter;
import com.law.cropper.photoloader.entity.Media;
import com.law.cropper.view.HackyViewPager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jungle on 2017/7/14.
 */

public class PreviewActivity extends AppCompatActivity {
    public static final String SELECTED_RESULT_KEY = "result";

    private static final String MEDIAS_KEY = "medias";
    private static final String SELECTED_KEY = "selected";
    private static final String SIZE_KEY = "size";

    @BindView(R.id.vp_imgs)
    HackyViewPager vViewPager;
    @BindView(R.id.tv_index)
    TextView vIndex;
    @BindView(R.id.tv_select)
    TextView vTvSelect;
    @BindView(R.id.btn_confirm)
    TextView vBtnConfirm;
    @BindView(R.id.recyclerview)
    RecyclerView vRecyclerView;

    private List<Media> mMedias;
    private ArrayList<Long> selectedList;
    private DisplayPagerAdapter mDisplayPagerAdapter;
    private int maxSize;
    private ImagePreviewAdapter mPreviewAdapter;

    public static void navigateToPreviewActivity(Activity activity, int maxSize, ArrayList<Media> medias, ArrayList<Long> selectedList, int requestCode) {
        Intent intent = new Intent(activity, PreviewActivity.class);
        intent.putExtra(SIZE_KEY, maxSize);
        intent.putParcelableArrayListExtra(MEDIAS_KEY, medias);
        intent.putExtra(SELECTED_KEY, selectedList);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        maxSize = getIntent().getIntExtra(SIZE_KEY, 0);
        mMedias = new ArrayList<>();
        ArrayList<Media> medias = getIntent().getParcelableArrayListExtra(MEDIAS_KEY);
        mMedias.addAll(medias);
        selectedList = (ArrayList<Long>) getIntent().getSerializableExtra(SELECTED_KEY);

        vIndex.setText(1 + "/" + mMedias.size());
        if (selectedList.size() == 0) {
            vBtnConfirm.setText("确定");
        } else {
            vBtnConfirm.setText("确定(" + selectedList.size() + "/" + maxSize + ")");
        }
        mDisplayPagerAdapter = new DisplayPagerAdapter(getSupportFragmentManager(), mMedias);
        vViewPager.setPageMargin(10);
        vViewPager.setOffscreenPageLimit(2);
        vViewPager.setAdapter(mDisplayPagerAdapter);
        if (selectedList.contains(mMedias.get(0).getId())) {
            vTvSelect.setSelected(true);
        } else {
            vTvSelect.setSelected(false);
        }
        vViewPager.setCurrentItem(0);
        vViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                vIndex.setText((position + 1) + "/" + mMedias.size());
                if (selectedList.contains(mMedias.get(position).getId())) {
                    vTvSelect.setSelected(true);
                } else {
                    vTvSelect.setSelected(false);
                }
                mPreviewAdapter.setIndex(position);
                vRecyclerView.getLayoutManager().scrollToPosition(position);
            }
        });

        vRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        List<Media> previewMedias = new ArrayList<>();
//        previewMedias.addAll(medias);

        mPreviewAdapter = new ImagePreviewAdapter(this, medias, selectedList);
        mPreviewAdapter.setCallback(new ImagePreviewAdapter.Callback() {
            @Override
            public void imgClick(int position) {
                vViewPager.setCurrentItem(position);
                mPreviewAdapter.setIndex(position);
            }
        });
        vRecyclerView.setAdapter(mPreviewAdapter);
    }

    @OnClick(R.id.tv_select)
    public void selectImg(View view) {
        if (selectedList.size() == maxSize) {
            Toast.makeText(this, "至多可以选择" + maxSize + "张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        int currentPosition = vViewPager.getCurrentItem();
        if (selectedList.contains(mMedias.get(currentPosition).getId())) {
            vTvSelect.setSelected(false);
            selectedList.remove(mMedias.get(currentPosition).getId());
        } else {
            vTvSelect.setSelected(true);
            selectedList.add(currentPosition, mMedias.get(currentPosition).getId());
        }

        mPreviewAdapter.setSelectedMediasId(selectedList);

        if (selectedList.size() == 0) {
            vBtnConfirm.setText("确定");
        } else {
            vBtnConfirm.setText("确定(" + selectedList.size() + "/" + maxSize + ")");
        }
    }

    @OnClick(R.id.btn_back)
    public void back(View view) {
        finish();
    }

    @OnClick(R.id.btn_confirm)
    public void confirm(View view) {
        Intent intent = new Intent();
        intent.putExtra(SELECTED_RESULT_KEY, selectedList);
        setResult(RESULT_OK, intent);
        finish();
    }

}
