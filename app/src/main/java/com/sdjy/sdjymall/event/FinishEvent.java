package com.sdjy.sdjymall.event;

/**
 * 关闭页面
 */
public class FinishEvent {
    //页面类名
    public String simpleName;

    public FinishEvent() {
    }

    public FinishEvent(String simpleName) {
        this.simpleName = simpleName;
    }
}
