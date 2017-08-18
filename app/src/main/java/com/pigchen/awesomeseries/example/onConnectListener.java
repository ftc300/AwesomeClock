package com.pigchen.awesomeseries.example;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/15
 * @ 描述:     监听设备蓝牙状态
 */

public interface onConnectListener {
    public void onConnecting();//正在连接中
    public void onConnected();//连接成功
    public void onTimeOut();//连接失败
}
