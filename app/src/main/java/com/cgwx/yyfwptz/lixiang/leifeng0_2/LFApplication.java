package com.cgwx.yyfwptz.lixiang.leifeng0_2;

import android.app.Application;


import com.yixia.camera.VCamera;

import java.io.File;

/**
 * Created by yyfwptz on 2017/3/25.
 */

public class LFApplication extends Application{

    public static String VIDEO_PATH =  "/sdcard/LeiFengRecordedDemo/";

    @Override
    public void onCreate() {
        super.onCreate();
        VIDEO_PATH += String.valueOf(System.currentTimeMillis());
        File file = new File(VIDEO_PATH);
        if(!file.exists()) file.mkdirs();

        //设置视频缓存路径
        VCamera.setVideoCachePath(VIDEO_PATH);

        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(true);

        // 初始化拍摄SDK，必须
        VCamera.initialize(this);
    }
}
