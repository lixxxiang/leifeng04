package com.cgwx.yyfwptz.lixiang.leifeng0_2.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.PanoramaViewListener;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.R;


/**
 * 全景Demo主Activity
 */
public class PanoramaActivity extends Activity {
    private static final String LTAG = "BaiduPanoSDKDemo";
    private PanoramaView mPanoView;
    private TextView textTitle;
    private BMapManager bMapManager;
//    public BMapManager mBMapManager = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        initBMapManager();
        bMapManager = new BMapManager(getApplication());
        setContentView(R.layout.activity_panorama);
        initView();
        Intent intent = getIntent();
        if (intent != null) {
            testPanoByType(intent.getIntExtra("type", -1));
        }
    }

//    private void initBMapManager() {
//        mBMapManager = new BMapManager(getApplication());
//    }

    private void initView() {
        textTitle = (TextView) findViewById(R.id.panodemo_main_title);
        mPanoView = (PanoramaView) findViewById(R.id.panorama);
    }

    private void testPanoByType(int type) {
        mPanoView.setShowTopoLink(true);
        mPanoView.setPanoramaViewListener(new PanoramaViewListener() {

            @Override
            public void onLoadPanoramaBegin() {
                Log.i(LTAG, "onLoadPanoramaStart...");
            }

            @Override
            public void onLoadPanoramaEnd(String json) {
                Log.i(LTAG, "onLoadPanoramaEnd : " + json);
            }

            @Override
            public void onLoadPanoramaError(String error) {
                Log.i(LTAG, "onLoadPanoramaError : " + error);
            }

            @Override
            public void onDescriptionLoadEnd(String json) {

            }

            @Override
            public void onMessage(String msgName, int msgType) {

            }

            @Override
            public void onCustomMarkerClick(String key) {

            }
        });
        textTitle.setText("通过百度经纬度坐标获取全景");
        double lat = 43.977;
        double lon = 125.389;
        mPanoView.setPanorama(lon, lat);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mPanoView.destroy();
        super.onDestroy();
    }

}

