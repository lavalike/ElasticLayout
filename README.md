# elastic-layout
> 可自定义Header的弹性布局

### 依赖导入

```gradle
implementation 'com.wangzhen:ElasticLayout:0.0.3'
```

### 属性说明
**drag_factor**
> 0~1的值，控制滚动灵敏度，默认0.3

**drag_enable**
> 是否启用弹性下拉，默认true

### 代码示例
```java
View headerView = getLayoutInflater().inflate(R.layout.xxx, null);
((TextView) headerView.findViewById(R.id.xxx)).setText("网页由 " + getUrlHost(url) + " 提供");
elasticLayout.setHeaderView(headerView);
```