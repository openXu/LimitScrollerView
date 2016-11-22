package com.openxu.lc;

/**
 * author : openXu
 * create at : 2016/11/22 12:12
 * blog : http://blog.csdn.net/xmxkf
 * gitHub : https://github.com/openXu
 * project : LimitScrollerView
 * class name : DataBean
 * version : 1.0
 * class describe： 数据
 */
public class DataBean {
    public DataBean(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    private int icon;
    private String text;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
