package com.openxu.lc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        //API：4、设置条目点击事件
        limitScroll.setOnItemClickListener(new LimitScrollerView.OnItemClickListener() {
            @Override
            public void onItemClick(Object obj) {
                if(obj instanceof DataBean){
                    //强制转换
                    DataBean bean = (DataBean)obj;
                    Toast.makeText(MainActivity.this, "点击了："+bean.getText(), Toast.LENGTH_SHORT).show();
                    Log.v("oepnxu", "点击了："+bean.getText());
                }

            }
        });

        //API:1、设置数据适配器
        adapter = new MyLimitScrllAdapter();
        limitScroll.setDataAdapter(adapter);

        initData();
    }


    private void initData(){

        //TODO 模拟获取服务器数据操作，此处需要修改
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                List<DataBean> datas = new ArrayList<>();
                datas.add(new DataBean(R.mipmap.ic_launcher, "1.劲爆促销中，凡在此商场消费满888的顾客，请拿着小票到前台咨询处免费领取美女一枚"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "2.劲爆促销中，凡在此商场消费满888的顾客，请拿着小票到前台咨询处免费领取美女一枚"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "3.劲爆促销中，凡在此商场消费满888的顾客，请拿着小票到前台咨询处免费领取美女一枚"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "4.劲爆促销中，凡在此商场消费满888的顾客，请拿着小票到前台咨询处免费领取美女一枚"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "5.劲爆促销中，凡在此商场消费满888的顾客，请拿着小票到前台咨询处免费领取美女一枚"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "6.劲爆促销中，凡在此商场消费满888的顾客，请拿着小票到前台咨询处免费领取美女一枚"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "7.劲爆促销中，凡在此商场消费满888的顾客，请拿着小票到前台咨询处免费领取美女一枚"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "8.劲爆促销中，凡在此商场消费满888的顾客，请拿着小票到前台咨询处免费领取美女一枚"));
                datas.add(new DataBean(R.mipmap.ic_launcher, "9.劲爆促销中，凡在此商场消费满888的顾客，请拿着小票到前台咨询处免费领取美女一枚"));

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
            itemView.setTag(data);
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
