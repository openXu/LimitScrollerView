使用方法：

1、文件拷贝：
  ①、将`limit_scroller.xml`、`limit_scroller_item.xml`拷贝到`layout`文件夹中
  ②、将`attrs.xml`拷贝到`values`目录下
  ③、将`LimitScrollerView`自定义控件拷贝到项目源码目录下
  
2、在activity布局中使用自定义控件
```
    <com.openxu.lc.LimitScrollerView
      android:id="@+id/limitScroll"
      android:layout_width="match_parent"
      android:layout_height="wrap_content"
      openxu:limit="2"
      openxu:durationTime="3000"
      openxu:periodTime="5000"/>
```

3、Activity中：
  ①、初始化控件
```
  limitScroll = (LimitScrollerView)findViewById(R.id.limitScroll);
``` 
  ②、设置数据适配器，需要实现`LimitScrollerView.LimitScrllAdapter`，详情请见`MainActivity.MyLimitScrllAdapter`
```
    //API:1、设置数据适配器
    adapter = new MyLimitScrllAdapter();
    limitScroll.setDataAdapter(adapter);
```
  
  ③、请求到服务器数据后填充数据
```
    adapter.setDatas(datas);
```    
  ④、同步生命周期方法
  onStart()方法中调用
```
  //API:2、开始滚动
  limitScroll.startScroll();    
```
  onStop()方法中调用
```
  //API:3、停止滚动
  limitScroll.cancel();  
```