package com.pigchen.awesomeseries.example;

/**
 * Created by chendong on 2017/8/10.
 */
public interface onGestureListener {
    public void slideDownToBottom();//下拉到最底部
    public void slideDownBack();//下拉小于阈值回弹
    public void wipeUpToTop();//上拉至顶部
    public void wipeUpBack();//上拉回弹至底部
    public void normalHoldScroll(float deltaY);//普通拖拽滑动
}
