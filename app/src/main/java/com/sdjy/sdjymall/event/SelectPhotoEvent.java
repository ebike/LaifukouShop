package com.sdjy.sdjymall.event;


import com.sdjy.sdjymall.model.ImageItem;

public class SelectPhotoEvent {
    private ImageItem item;

    public SelectPhotoEvent(ImageItem item) {
        this.item = item;
    }

    public ImageItem getItem() {
        return item;
    }

    public void setItem(ImageItem item) {
        this.item = item;
    }
}
