package com.law.cropper.photoloader;


import com.law.cropper.photoloader.entity.Album;

import java.util.Comparator;

public class AlbumsComparators {
    boolean ascending = true;

    public AlbumsComparators(boolean ascending) {
        this.ascending = ascending;
    }


    public Comparator<Album> getSizeComparator() {
        return new Comparator<Album>() {
            public int compare(Album f1, Album f2) {
                return ascending ? f1.getCount() - f2.getCount() : f2.getCount() - f1.getCount();
            }
        };
    }
}
