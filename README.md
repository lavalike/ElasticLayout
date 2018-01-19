# ElasticLayout
> 可自定义背景的弹性布局

###属性说明
**el_factor**
0~1的值，控制滚动灵敏度
**el_direction**
> none --> 不可下拉
> top --> 从顶部下拉

**el_behind_view**
设置背景View

###布局
``` xml
<com.wangzhen.elastic.ElasticLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/pull_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:el_behind_view="your layout"
    app:el_direction="none|top"
    app:el_factor="0.3">

    <WebView
        android:id="@+id/webview"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</com.wangzhen.elastic.ElasticLayout>
```

###代码使用
```java
String url = "https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA==&amp;mid=2247483998&amp;idx=1&amp;sn=03cb1942533247ac23876448fdf1b39a&amp;chksm=96cda313a1ba2a050f8cc2325e36b468f620d3d167d9f0dbc3108127c5ba16d14dc83cabc3c6&amp;scene=21#wechat_redirect";
webView.loadUrl(url);
View behindView = elasticLayout.getBehindView();
if (behindView != null) {
   ((TextView) behindView.findViewById(R.id.tv_tip)).setText("网页由 " + getUrlHost(url) + " 提供");
}
```