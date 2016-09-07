package com.sdjy.sdjymall.event;

/**
 * 切换首页菜单
 */
public class ChangeHomeTabEvent {

    public int position;

    public ChangeHomeTabEvent(int position) {
        this.position = position;
    }
}
