package com.sdjy.sdjymall.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.activity.base.BaseActivity;
import com.sdjy.sdjymall.common.util.DialogUtils;
import com.sdjy.sdjymall.common.util.T;
import com.sdjy.sdjymall.progress.ProgressDialogHandler;
import com.sdjy.sdjymall.util.CommonUtils;
import com.sdjy.sdjymall.util.DataCleanManager;
import com.sdjy.sdjymall.util.FileUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity {

    @Bind(R.id.tv_cache_file)
    TextView cacheFileView;
    @Bind(R.id.tv_version)
    TextView versionView;

    @Override
    public void loadLoyout() {
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void init() {
        try {
            versionView.setText("V" + CommonUtils.getVersionName(this));
            cacheFileView.setText(DataCleanManager.getCacheSize(new File(FileUtils.getmDownLoadFile())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.rl_cache_file)
    public void cacheFile() {
        DialogUtils.showDialog(this, "温馨提示:", "确定清理缓存？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialogHandler mProgressDialogHandler = new ProgressDialogHandler(SettingsActivity.this, null, true);
                mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();

                DataCleanManager.cleanApplicationData(SettingsActivity.this);
                T.showShort(SettingsActivity.this, "缓存清理完毕！");
                try {
                    cacheFileView.setText(DataCleanManager.getTotalSize(SettingsActivity.this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        }, "取消");
    }

    @OnClick(R.id.rl_about)
    public void about() {
        startActivity(new Intent(this,AboutActivity.class));
    }

    @OnClick(R.id.btn_logout)
    public void logout() {

    }
}
