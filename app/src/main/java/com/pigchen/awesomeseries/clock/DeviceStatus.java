package com.pigchen.awesomeseries.clock;

/**
 * @ 创建者:   CoderChen
 * @ 时间:     2017/8/14
 * @ 描述:
 */
public enum DeviceStatus {
    CONNECTING,//连接中
    CONNECTINGED,//连接中过度到连接成功
    CONNECTED,//连接成功
    TIMEOUT//连接超时
}
