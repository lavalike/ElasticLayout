# ElasticLayout
> 可自定义Header的弹性布局

```gradle
implementation 'com.wangzhen:ElasticLayout:0.0.3'
```

### 属性说明
**drag_factor**
> 0~1的值，控制滚动灵敏度，默认0.3

**drag_enable**
> 是否启用弹性下拉，默认true

### 布局
``` xml
<?xml version="1.0" encoding="utf-8"?>
<com.wangzhen.elastic.ElasticLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/pull_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    app:drag_enable="true"
    app:drag_factor="0.3">

    <WebView
        android:id="@+id/webview"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</com.wangzhen.elastic.ElasticLayout>

```

### 代码使用
```java
View headerView = getLayoutInflater().inflate(R.layout.xxx, null);
((TextView) headerView.findViewById(R.id.xxx)).setText("网页由 " + getUrlHost(url) + " 提供");
elasticLayout.setHeaderView(headerView);
```