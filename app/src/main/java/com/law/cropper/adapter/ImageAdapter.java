package com.law.cropper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.imageloader.ImageDisplayUtils;
import com.law.cropper.ImagePickActivity;
import com.law.cropper.R;
import com.law.cropper.photoloader.Configuration;
import com.law.cropper.photoloader.entity.Media;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jungle on 2017/7/15.
 */

public class ImageAdapter extends BaseAdapter {
    private static final int[] COLORSID = {R.color.color_for_photo_fri, R.color.color_for_photo_sec, R.color.color_for_photo_thi, R.color.color_for_photo_fou, R.color.color_for_photo_fif};

    private Context mContext;
    private List<Media> mMedias;
    private int mode;
    private ArrayList<Long> selectedPositions = null;
    private int maxSize;
    private ItemClickCallback callback;

    public void setCallback(ItemClickCallback callback) {
        this.callback = callback;
    }

    public ImageAdapter(Context mContext, List<Media> mMedias, int mode) {
        this.mContext = mContext;
        this.mMedias = mMedias;
        this.mode = mode;
        selectedPositions = new ArrayList<>();
    }

    public ImageAdapter(Context mContext, List<Media> mMedias, int mode, int maxSize) {
        this.mContext = mContext;
        this.mMedias = mMedias;
        this.mode = mode;
        this.maxSize = maxSize;
        selectedPositions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mMedias == null ? 0 : mMedias.size();
    }

    @Override
    public Object getItem(int i) {
        return mMedias == null ? null : mMedias.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        final Media media = mMedias.get(i);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.row_image_item, viewGroup, false);
            holder = new ViewHolder(view);
            holder.vLayoutRoot.setLayoutParams(new AbsListView.LayoutParams(Configuration.GalleryConstants.lenght, Configuration.GalleryConstants.lenght));
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        ImageDisplayUtils.displayWithPlaceholder(mContext, media.getPath(), holder.vIvPhoto, COLORSID[i % COLORSID.length]);
        switch (mode) {
            case ImagePickActivity.MODE_MULTI:
                holder.vIvCheckBox.setVisibility(View.VISIBLE);
                if (selectedPositions.contains(media.getId())) {
                    holder.vIvCheckBox.setSelected(true);
                } else {
                    holder.vIvCheckBox.setSelected(false);
                }
                holder.vIvCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedPositions.contains(media.getId())) {
                            media.setSelected(false);
                            selectedPositions.remove(media.getId());
                        } else {
                            if (selectedPositions.size() == maxSize) {
                                Toast.makeText(mContext, "至多可以选择" + maxSize + "张图片", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            media.setSelected(true);
                            selectedPositions.add(media.getId());
                        }
                        if (callback != null) {
                            callback.itemSelect(selectedPositions.size());
                        }
                        notifyDataSetChanged();
                    }
                });

                holder.vLayoutRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (callback != null)
                            callback.itemClick(i);
                    }
                });
                break;
            case ImagePickActivity.MODE_SINGLE:
                holder.vLayoutRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (callback != null)
                            callback.itemClick(i);
                    }
                });
                break;
        }
        return view;
    }

    public void refresh(List<Media> medias) {
        this.mMedias = medias;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        @BindView(R.id.layout_photos_item_root)
        RelativeLayout vLayoutRoot;
        @BindView(R.id.img_photos_item)
        ImageView vIvPhoto;
        @BindView(R.id.iv_item_select)
        ImageView vIvCheckBox;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemClickCallback {
        void itemClick(int position);

        void itemSelect(int count);
    }

    public ArrayList<Long> getSelectedList() {
        return selectedPositions;
    }

    public void setSelectedList(ArrayList<Long> selectedPositions) {
        this.selectedPositions = selectedPositions;
        notifyDataSetChanged();
    }
}
