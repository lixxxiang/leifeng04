package com.cgwx.yyfwptz.lixiang.leifeng0_2.models.modelImpl;


import com.cgwx.yyfwptz.lixiang.leifeng0_2.models.modelInterface.HomeFragmentNormalModelInterface;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.models.modelInterface.OnSendStringListener;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.utils.Constants;


/**
 * Created by yyfwptz on 2017/3/27.
 */

public class HomeFragmentNormalModelImpl implements HomeFragmentNormalModelInterface {

    @Override
    public void geturl(String request, OnSendStringListener listener) {
        if(request.equals(Constants.homeFragmentNormal)){
            listener.sendString(Constants.homeFragmentNormalURL);
        }
    }
}
