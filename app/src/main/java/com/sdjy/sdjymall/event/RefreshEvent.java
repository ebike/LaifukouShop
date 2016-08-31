package com.sdjy.sdjymall.event;

/**
 * 刷新数据
 */
public class RefreshEvent {

    //页面类名
    public String simpleName;

    public RefreshEvent() {
    }

    public RefreshEvent(String simpleName) {
        this.simpleName = simpleName;
    }
}
