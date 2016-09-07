package com.sdjy.sdjymall.constants;

import android.os.Environment;

/**
 * 内存中的常量
 * 全局使用
 */
public class FinalValues {

    //APP模式
    public static final boolean IS_DEBUG = true;
    //当前选择的照片位置
    public static final String CAMERA_PIC_PATH = Environment.getExternalStorageDirectory() + "/sdjymall/CameraPic/";
    //压缩图片路径
    public static final String compressedImage = Environment.getExternalStorageDirectory() + "/sdjymall/compressedImage/";
    //支付宝支付
    public static final int SDK_PAY_FLAG = 1;
}
