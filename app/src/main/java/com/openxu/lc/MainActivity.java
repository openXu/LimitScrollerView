package com.openxu.lc;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private LimitScrollerView limitScroll;
    private MyLimitScrllAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        limitScroll = (LimitScrollerView)findViewById(R.id.limitScroll);
        adapter = new MyLimitScrllAdapter();
        //API:1、设置数据适配器
        limitScroll.setDataAdapter(adapter);

        initData();
    }


    private void initData(){

        //TODO 一、模拟获取服务器数据，此处需要修改
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                List<DataBean> datas = new ArrayList<>();
                datas.add(new DataBean(R.mipmap.ic_launcher, "我是文本1"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "我是文本2"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "我是文本3"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "我是文本4"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "我是文本5"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "我是文本6"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "我是文本7"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "我是文本8"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "我是文本9"));

                adapter.setDatas(datas);

            }
        }).start();



    }

    @Override
    protected void onStart() {
        super.onStart();
        limitScroll.startScroll();    //API:2、开始滚动
    }


    //TODO 修改适配器绑定数据
    class MyLimitScrllAdapter implements LimitScrollerView.LimitScrllAdapter{

        private List<DataBean> datas;
        public void setDatas(List<DataBean> datas){
            this.datas = datas;
            //API:2、开始滚动
            limitScroll.startScroll();
        }
        @Override
        public int getCount() {
            return datas==null?0:datas.size();
        }

        @Override
        public View getView(int index) {
            View itemView = LayoutInflater.from(MainActivity.this).inflate(R.layout.limit_scroller_item, null, false);
            ImageView iv_icon = (ImageView)itemView.findViewById(R.id.iv_icon);
            TextView tv_text = (TextView)itemView.findViewById(R.id.tv_text);

            //绑定数据
            DataBean data = datas.get(index);
            iv_icon.setImageResource(data.getIcon());
            tv_text.setText(data.getText());
            return itemView;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        //API:3、停止滚动
        limitScroll.cancel();
    }
}
