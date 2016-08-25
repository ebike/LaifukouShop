package com.sdjy.sdjymall.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.adapter.PhotoSingleSelectionAdapter;
import com.sdjy.sdjymall.event.FinishEvent;
import com.sdjy.sdjymall.event.SelectPhotoEvent;
import com.sdjy.sdjymall.model.ImageBucket;
import com.sdjy.sdjymall.model.ImageItem;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class PhotoSingleSelectionActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.grid_view)
    GridView gridView;
    //相册对象
    private ImageBucket imageBucket;

    private PhotoSingleSelectionAdapter adapter;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_photo_single_selection);
    }

    @Override
    public void init() {
        imageBucket = (ImageBucket) getIntent().getSerializableExtra("photoAlbum");

        titleView.setText(imageBucket.bucketName);
        adapter = new PhotoSingleSelectionAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setList(imageBucket.imageList);

        //显示选中照片
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem imageItem = (ImageItem) parent.getItemAtPosition(position);
                EventBus.getDefault().post(new SelectPhotoEvent(imageItem));
                EventBus.getDefault().post(new FinishEvent());
                PhotoSingleSelectionActivity.this.finish();
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void back(){
        finish();
    }

    public void onEvent(FinishEvent event) {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
