package com.law.cropper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.base.App;
import com.law.cropper.adapter.ImageAdapter;
import com.law.cropper.photoloader.Configuration;
import com.law.cropper.photoloader.PhotoLoader;
import com.law.cropper.photoloader.entity.Album;
import com.law.cropper.photoloader.entity.Media;
import com.law.cropper.photoloader.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jungle on 2017/7/14.
 */

public class ImagePickActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 0x1521;

    public static final int DEFAULT_MAX_SIZE = 9;

    public static final int MODE_SINGLE = 0;
    public static final int MODE_MULTI = 1;

    private static final String RESULT_KEY = "result";

    private static final String MODE_KEY = "mode";
    private static final String SIZE_KEY = "size";

    @BindView(R.id.gv_image)
    GridView vGvImage;
    @BindView(R.id.btn_confirm)
    TextView vBtnConfirm;

    private ArrayList<Album> mAlbums;
    private ArrayList<Media> mMedias;
    private ArrayList<Media> mSelectedList = new ArrayList<>();

    private int mode;
    private int maxSize = -1;
    private ImageAdapter mImageAdapter;

    public static void navigateToImagePickActivity(Activity activity, int mode, int requestCode) {
        Intent intent = new Intent(activity, ImagePickActivity.class);
        intent.putExtra(MODE_KEY, mode);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void navigateToImagePickActivity(Activity activity, int mode, int maxSize, int requestCode) {
        Intent intent = new Intent(activity, ImagePickActivity.class);
        intent.putExtra(MODE_KEY, mode);
        intent.putExtra(SIZE_KEY, maxSize);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_pick);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                ArrayList<Integer> selectedList = data.getIntegerArrayListExtra(GalleryActivity.SELECTED_RESULT_KEY);
                mImageAdapter.setSelectedList(selectedList);
                for (Integer position : selectedList) {
                    mMedias.get(position).setSelected(true);
                }
                mImageAdapter.refresh(mMedias);
                break;
        }
    }

    private void init() {
        mode = getIntent().getIntExtra(MODE_KEY, MODE_SINGLE);
        switch (mode) {
            case MODE_SINGLE:
                mImageAdapter = new ImageAdapter(this, mMedias, mode);
                mImageAdapter.setCallback(new SimpleItemClickCallback() {
                    @Override
                    public void itemClick(int position) {
                        Toast.makeText(ImagePickActivity.this, position + "", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra(Constants.PICK_SINGLE_RESULT, mMedias.get(position).getPath());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                vBtnConfirm.setVisibility(View.GONE);
                break;
            case MODE_MULTI:
                maxSize = getIntent().getIntExtra(SIZE_KEY, DEFAULT_MAX_SIZE);
                Logger.i(maxSize);
                mImageAdapter = new ImageAdapter(this, mMedias, mode, maxSize);
                mImageAdapter.setCallback(new SimpleItemClickCallback() {
                    @Override
                    public void itemClick(int position) {
                        Toast.makeText(ImagePickActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                        GalleryActivity.navigateToGalleryActivity(ImagePickActivity.this, position, maxSize, mMedias, mImageAdapter.getSelectedList(), GALLERY_REQUEST_CODE);
                    }

                    @Override
                    public void itemSelect(int count) {
                        if (count == 0) {
                            vBtnConfirm.setText("确定");
                        } else {
                            vBtnConfirm.setText("确定(" + count + "/" + maxSize + ")");
                        }
                    }
                });
                break;
        }
        vGvImage.setAdapter(mImageAdapter);
        vGvImage.setVerticalSpacing(Configuration.GalleryConstants.divider);
        vGvImage.setHorizontalSpacing(Configuration.GalleryConstants.divider);
        vGvImage.setNumColumns(Configuration.GalleryConstants.numColumns);
        vGvImage.setPadding(Configuration.GalleryConstants.divider, Configuration.GalleryConstants.divider, Configuration.GalleryConstants.divider, Configuration.GalleryConstants.divider);
        loadMedia(0);
    }

    private void loadMedia(final int albumIndex) {
        PhotoLoader.getInstance().loadPhotos(App.getInstance(), new PhotoLoader.PhotosLoadHandler() {
            @Override
            public void getPhotosSuc(List<Album> albums) {
                for (Album album : albums) {
                    Logger.d(album.toString());
                }
                int index = albumIndex;
                mAlbums = new ArrayList<>();
                mAlbums.addAll(albums);
                if (index >= mAlbums.size()) {
                    index = 0;
                }
//                mAlbumAdapter.refresh(mAlbums);
//                updateHeaderView(index);
                mMedias = new ArrayList<>();
                mMedias.addAll(mAlbums.get(index).getMedias());
                mImageAdapter.refresh(mMedias);
            }

            @Override
            public void getPhotosFail() {
                Toast.makeText(ImagePickActivity.this, "获取多媒体文件", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_confirm)
    public void confirm(View view) {
        Intent intent = new Intent();
        ArrayList<String> medias = new ArrayList<>();
        for (Integer position : mImageAdapter.getSelectedList()) {
            medias.add(mMedias.get(position).getPath());
        }
        intent.putExtra(Constants.PICK_MULTI_RESULT, medias);
        setResult(RESULT_OK, intent);
        finish();
    }
}
