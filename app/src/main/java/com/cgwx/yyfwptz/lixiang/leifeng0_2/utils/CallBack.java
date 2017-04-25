package com.cgwx.yyfwptz.lixiang.leifeng0_2.utils;

import android.widget.Toast;

import com.cgwx.yyfwptz.lixiang.leifeng0_2.view.activity.MainActivity;
import com.yixia.camera.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;


/**
 * Created by yyfwptz on 2017/4/24.
 */


public class CallBack extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("search".equals(action)) {
            search(args.getString(0));
            return true;
        }
        return false;
    }

    public void search(String content) {
        Log.e("发布的问题： ", content);
        Toast.makeText(MainActivity.mainActivity, "问题已发布", Toast.LENGTH_LONG).show();
    }
}