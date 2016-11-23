package com.openxu.lc;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * author : openXu
 * create at : 2016/11/22 10:14
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : LimitScrollerView
 * class name : LimitScrollerView
 * version : 1.0
 * class describe：
 */
public class LimitScrollerView extends LinearLayout implements View.OnClickListener{

    private String TAG = "LimitScrollerView";

    private LinearLayout ll_content1, ll_content2;
    private LinearLayout ll_now, ll_down;   //当前可见的，下面不可见的（切换）
    private int limit;          //可见条目数量
    private int durationTime;   //动画执行时间
    private int periodTime;     //间隔时间
    private int scrollHeight;   //滚动高度（控件高度）


    private int dataIndex;

    private boolean isCancel;
    private boolean boundData;   //是否已经第一次绑定过数据


    private final int MSG_SETDATA = 1;
    private final int MSG_SCROL = 2;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_SETDATA){
                boundData(true);
            }else if(msg.what == MSG_SCROL){
                startAnimation();
            }
        }
    };

    public LimitScrollerView(Context context) {
        this(context, null);
    }

    public LimitScrollerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LimitScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.limit_scroller, this, true);
        ll_content1 = (LinearLayout) findViewById(R.id.ll_content1);
        ll_content2 = (LinearLayout) findViewById(R.id.ll_content2);
        ll_now = ll_content1;
        ll_down = ll_content2;
        if(attrs!=null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LimitScroller);
            limit = ta.getInt(R.styleable.LimitScroller_limit, 1);
            durationTime = ta.getInt(R.styleable.LimitScroller_durationTime, 1000);
            periodTime = ta.getInt(R.styleable.LimitScroller_periodTime, 1000);
            ta.recycle();  //注意回收
            Log.v(TAG, "limit="+limit);
            Log.v(TAG, "durationTime="+durationTime);
            Log.v(TAG, "periodTime="+periodTime);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //EXACTLY=1073741824
        //AT_MOST=-2147483648
/*        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.w(TAG, "specMode="+specMode);
        Log.w(TAG, "specSize="+specSize);
        int newHeightSpec = MeasureSpec.makeMeasureSpec(specSize, MeasureSpec.EXACTLY);
        int childCount = ll_content1.getChildCount();
        if(childCount>0){
            View item = ll_content1.getChildAt(0);
            item.measure(widthMeasureSpec, newHeightSpec);
            Log.w(TAG, "条目高度="+   item.getMeasuredHeight());
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight()/2);
            scrollHeight = getMeasuredHeight();
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);*/

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置高度为整体高度的一般，以达到遮盖预备容器的效果
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight()/2);
        //此处记下控件的高度，此高度就是动画执行时向上滚动的高度
        scrollHeight = getMeasuredHeight();
        Log.w(TAG, "getMeasuredWidth="+getMeasuredWidth());
        Log.w(TAG, "getMeasuredHeight="+getMeasuredHeight());
        Log.w(TAG, "scrollHeight="+scrollHeight);
    }

    private void startAnimation(){
        if(isCancel)
            return;
        Log.i(TAG, "滚动");
        //当前展示的容器，从当前位置（0）,向上滚动scrollHeight
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(ll_now, "Y",ll_now.getY(), ll_now.getY()-scrollHeight);
        //预备容器，从当前位置，向上滚动scrollHeight
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(ll_down, "Y",ll_down.getY(), ll_down.getY()-scrollHeight);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(durationTime);
        animSet.playTogether(anim1, anim2);
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
//                Log.v(TAG, "ll_now动画开始前位置："+ll_now.getX()+"*"+ll_now.getY());
//                Log.v(TAG, "ll_down动画开始前位置："+ll_down.getX()+"*"+ll_down.getY());
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                //滚动结束后，now的位置变成了-scrollHeight，这时将他移动到最底下
                ll_now.setY(scrollHeight);
                //down的位置变为0，也就是当前看见的
                ll_down.setY(0);
//                Log.v(TAG, "1调整之后ll_now位置："+ll_now.getX()+"*"+ll_now.getY());
//                Log.v(TAG, "1调整之后ll_down位置："+ll_down.getX()+"*"+ll_down.getY());
                LinearLayout temp = ll_now;
                ll_now = ll_down;
                ll_down = temp;
//                Log.v(TAG, "2调整之后ll_now位置："+ll_now.getX()+"*"+ll_now.getY());
//                Log.v(TAG, "2调整之后ll_down位置："+ll_down.getX()+"*"+ll_down.getY());
                //给不可见的控件绑定新数据
                boundData(false);
                //重复动画
                handler.sendEmptyMessageDelayed(MSG_SCROL, periodTime);

            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animSet.start();
    }

    /**
     * 向容器中添加子条目
     * @param first
     */
    private void boundData(boolean first){
        if(adapter==null || adapter.getCount()<=0)
            return;
        if(first){
            //第一次绑定数据，需要为两个容器添加子条目
            boundData = true;
            ll_now.removeAllViews();
            for(int i = 0; i<limit; i++){
                if(dataIndex>=adapter.getCount())
                    dataIndex = 0;
                View view = adapter.getView(dataIndex);

                //设置点击监听
                view.setClickable(true);
                view.setOnClickListener(this);

                ll_now.addView(view);
                dataIndex ++;
            }
        }

        //每次动画结束之后，为预备容器添加新条目
        ll_down.removeAllViews();
        for(int i = 0; i<limit; i++){
            if(dataIndex>=adapter.getCount())
                dataIndex = 0;
            View view = adapter.getView(dataIndex);
            //设置点击监听
            view.setClickable(true);
            view.setOnClickListener(this);
            ll_down.addView(view);
            dataIndex ++;
        }
    }

    @Override
    public void onClick(View v) {
        if(clickListener!=null){
            Object obj = v.getTag();
            clickListener.onItemClick(obj);
        }
    }

    interface LimitScrollAdapter{
        public int getCount();
        public View getView(int index);
    }
    private LimitScrollAdapter adapter;

    interface OnItemClickListener{
        public void onItemClick(Object obj);
    }
    private OnItemClickListener clickListener;
    /**********************public API 以下为暴露的接口***********************/

    /**
     * 1、设置数据适配器
     * @param adapter
     */
    public void setDataAdapter(LimitScrollAdapter adapter){
        this.adapter = adapter;
        handler.sendEmptyMessage(MSG_SETDATA);
    }

    /**
     * 2、开始滚动
     * 应该在两处调用此方法：
     * ①、Activity.onStart()
     * ②、MyLimitScrllAdapter.setDatas()
     */
    public void startScroll(){
        if(adapter==null||adapter.getCount()<=0)
            return;
        if(!boundData){
            handler.sendEmptyMessage(MSG_SETDATA);
        }
        isCancel = false;
        handler.sendEmptyMessageDelayed(MSG_SCROL, periodTime);
    }
    /**
     * 3、停止滚动
     * 当在Activity不可见时，在Activity.onStop()中调用
     */
    public void cancel(){
        isCancel = true;
    }

    /**
     * 4、设置条目点击事件
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.clickListener = listener;
    }

}
