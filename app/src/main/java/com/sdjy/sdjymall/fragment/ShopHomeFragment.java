package com.sdjy.sdjymall.fragment;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sdjy.sdjymall.R;
import com.sdjy.sdjymall.common.fragment.LazyFragment;
import com.sdjy.sdjymall.util.StringUtils;

import butterknife.Bind;

/**
 * 店铺首页
 */
public class ShopHomeFragment extends LazyFragment {

    @Bind(R.id.web_view)
    WebView webView;
    private String data;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_shop_home);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);// 启用支持javascript
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 优先使用缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存

        if (!StringUtils.strIsEmpty(data)) {
            webView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
        }
    }

    public void setData(String data) {
        this.data = data;
        if (!StringUtils.strIsEmpty(data)) {
            webView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);
        }
    }
}
