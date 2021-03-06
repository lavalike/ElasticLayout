package com.wangzhen.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wangzhen.elastic.ElasticLayout;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * WebActivity
 * Created by wangzhen on 2019/2/22.
 */
public class WebActivity extends AppCompatActivity {
    private ElasticLayout elasticLayout;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setTitle("WebView示例");

        elasticLayout = (ElasticLayout) findViewById(R.id.pull_layout);
        webView = (WebView) findViewById(R.id.webview);

        String url = "https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA==&amp;mid=2247483998&amp;idx=1&amp;sn=03cb1942533247ac23876448fdf1b39a&amp;chksm=96cda313a1ba2a050f8cc2325e36b468f620d3d167d9f0dbc3108127c5ba16d14dc83cabc3c6&amp;scene=21#wechat_redirect";
        webView.loadUrl(url);

        View headerView = getLayoutInflater().inflate(R.layout.layout_pull_behind, null);
        ((TextView) headerView.findViewById(R.id.tv_tip)).setText("网页由 " + getUrlHost(url) + " 提供");
        elasticLayout.setHeaderView(headerView);
        elasticLayout.setDragFactor(0.5f);
    }


    /**
     * 获取url的Host
     *
     * @param url url
     * @return Host
     */
    private static String getUrlHost(String url) {
        if (TextUtils.isEmpty(url) || url.trim().equals("")) {
            return "";
        }
        String host = "";
        try {
            URI uri = new URI(url);
            host = uri.getHost();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return host;
    }
}
