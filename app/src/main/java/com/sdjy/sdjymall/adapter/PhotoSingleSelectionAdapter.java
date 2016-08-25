package com.sdjy.sdjymall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.common.util.ScreenUtils;
import com.sdjy.sdjymall.model.ImageItem;
import com.sdjy.sdjymall.view.ViewHolder;


public class PhotoSingleSelectionAdapter extends TAdapter<ImageItem> {
//    private ImageOptions imageOptions;

    public PhotoSingleSelectionAdapter(Context mContext) {
        super(mContext);
//        imageOptions = new ImageOptions.Builder()
//                .setAutoRotate(true)
//                .setLoadingDrawableId(R.mipmap.bg_img)
//                .setFailureDrawableId(R.mipmap.load_failure)
//                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_photo_single_selection, null);
        }

        ImageView photo = ViewHolder.get(convertView, R.id.iv_photo);
        //照片宽高为屏幕宽度减掉内外边距
        int width = (ScreenUtils.getScreenWidth(mContext) - 32) / 3;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, width);
        photo.setLayoutParams(layoutParams);
        ImageItem item = mList.get(position);
        if (item != null) {
            Glide.with(mContext)
                    .load(item.sourcePath)
                    .placeholder(R.mipmap.bg_img)
                    .error(R.mipmap.load_failure)
                    .into(photo);
//            x.image().bind(photo, item.sourcePath, imageOptions);
        }
        return convertView;
    }
}
