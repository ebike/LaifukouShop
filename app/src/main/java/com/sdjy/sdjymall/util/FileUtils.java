package com.sdjy.sdjymall.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.sdjy.sdjymall.common.util.T;

import java.io.File;

/**
 * 文件、图片等得处理
 */
public class FileUtils {
    private static String mChacheFile = "/sdjymall/mchachefile/";
    private static String mDownLoadFile = "/sdjymall/download/";
    private static String compressedImage = "/sdjymall/compressedImage/";
    public static String apkerrorurl = "/sdcard/sdjymall/";//异常报错是记录的位置
    public static final String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    public static String getmChacheFile() {
        return mChacheFile;
    }

    public static String getmDownLoadFile() {
        return Environment.getExternalStorageDirectory() + mDownLoadFile;
    }

    public static String getCompressedImage() {
        return Environment.getExternalStorageDirectory() + compressedImage;
    }

    public static String getApkerrorurl() {
        return apkerrorurl;
    }

    /**
     * 根据后缀判断文件是否是图片
     *
     * @param type
     * @return
     */
    public static boolean isPicture(String type) {
        type = type.toLowerCase();
        if (type.equals("jpg") || type.equals("jpeg") || type.equals("png") || type.equals("gif")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据后缀判断文件是否是word
     *
     * @param type
     * @return
     */
    public static boolean isWord(String type) {
        type = type.toLowerCase();
        if (type.equals("doc") || type.equals("docx")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据后缀判断文件是否是excel
     *
     * @param type
     * @return
     */
    public static boolean isExcel(String type) {
        type = type.toLowerCase();
        if (type.equals("xls") || type.equals("xlsx")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据后缀判断文件是否是ppt
     *
     * @param type
     * @return
     */
    public static boolean isPpt(String type) {
        type = type.toLowerCase();
        if (type.equals("ppt") || type.equals("pptx")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据后缀判断文件是否是pdf
     *
     * @param type
     * @return
     */
    public static boolean isPdf(String type) {
        type = type.toLowerCase();
        if (type.equals("pdf")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据后缀判断文件是否是txt
     *
     * @param type
     * @return
     */
    public static boolean isTxt(String type) {
        type = type.toLowerCase();
        if (type.equals("txt")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据后缀判断文件是否是压缩文件
     *
     * @param type
     * @return
     */
    public static boolean isCompressedFile(String type) {
        type = type.toLowerCase();
        if (type.equals("rar") || type.equals("zip") || type.equals("7z")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断文件是否存在项目下载文件夹中
     *
     * @return
     */
    public static boolean isExistFile(String fileName) {
        boolean flag = false;
        File destDir = new File(FileUtils.getmDownLoadFile());
        if (destDir.exists()) {
            File destFile = new File(destDir.getPath() + "/" + fileName);
            if (destFile.exists()) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 根据文件名获取下载过的文件
     *
     * @return
     */
    public static File getFileByFileName(String fileName) {
        return new File(FileUtils.getmDownLoadFile() + fileName);
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
    /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    /**
     * 给文件加后缀
     * 为了让下载的文件不重复，因此将文件的ID作为后缀标识
     * 文件名_ID作为唯一判断
     */
    public static String getSingleFileName(String fileName, String id) {
        String singleFileName = "";
        if (!StringUtils.strIsEmpty(fileName)) {
            String first = fileName.substring(0, fileName.lastIndexOf("."));
            String last = fileName.substring(fileName.lastIndexOf("."));
            singleFileName = first + "_" + id + last;
        }
        return singleFileName;
    }

    //打开文件
    public static void openFile(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = FileUtils.getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(Uri.fromFile(file), type);
        //跳转
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            T.showLong(context, "手机上未安装能打开该文件的应用");
        }
    }

}
