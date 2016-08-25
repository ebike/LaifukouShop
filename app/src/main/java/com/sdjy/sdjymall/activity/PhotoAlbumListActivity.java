package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.adapter.ImageBucketAdapter;
import com.sdjy.sdjymall.event.FinishEvent;
import com.sdjy.sdjymall.model.ImageBucket;
import com.sdjy.sdjymall.model.ImageFetcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 选择相册
 */
public class PhotoAlbumListActivity extends BaseActivity {

    @Bind(R.id.grid_view)
    GridView gridView;
    private ImageFetcher fetcher;
    //相册列表
    private List<ImageBucket> photoAlbumList = new ArrayList<ImageBucket>();
    //相册列表适配器
    private ImageBucketAdapter adapter;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_photo_album_list);
        EventBus.getDefault().register(this);
    }

    @Override
    public void init() {
        fetcher = ImageFetcher.getInstance(getApplicationContext());
        photoAlbumList = fetcher.getImagesBucketList(false);
        adapter = new ImageBucketAdapter(this, photoAlbumList);
        gridView.setAdapter(adapter);

        //进入选中相册
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PhotoAlbumListActivity.this, PhotoSingleSelectionActivity.class);
                intent.putExtra("photoAlbum", photoAlbumList.get(position));
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void back() {
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
