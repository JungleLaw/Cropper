package com.law.cropper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imageloader.ImageDisplayUtils;
import com.law.cropper.R;
import com.law.cropper.photoloader.entity.Media;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jungle on 2017/7/18.
 */

public class ImagePreviewAdapter extends RecyclerView.Adapter<ImagePreviewAdapter.ViewHolder> {
    private Context context;
    private List<Media> medias;
    private List<Long> selectedMediasId;
    private int index = 0;

    private Callback callback;

    public ImagePreviewAdapter(Context context, List<Media> medias, List<Long> selectedMediasId) {
        this.context = context;
        this.medias = medias;
        this.selectedMediasId = selectedMediasId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_img_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Media media = medias.get(position);
        ImageDisplayUtils.display(context, media.getPath(), holder.vIvImg);
        if (selectedMediasId.contains(media.getId())) {
            holder.vMask.setVisibility(View.GONE);
        } else {
            holder.vMask.setVisibility(View.VISIBLE);
        }

        if (index == position) {
            holder.vBox.setVisibility(View.VISIBLE);
        } else {
            holder.vBox.setVisibility(View.GONE);
        }
        holder.vIvImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.imgClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return medias == null ? 0 : medias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        ImageView vIvImg;
        @BindView(R.id.mask)
        View vMask;
        @BindView(R.id.box)
        View vBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setSelectedMediasId(List<Long> selectedMediasId) {
        this.selectedMediasId = selectedMediasId;
        notifyDataSetChanged();
    }

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void imgClick(int position);
    }
}
