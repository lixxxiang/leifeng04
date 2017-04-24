package com.cgwx.yyfwptz.lixiang.leifeng0_2.presenters.HomeFragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.R;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.entities.Icon;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.models.modelImpl.HomeFragmentWithMapModelImpl;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.models.modelInterface.OnSendArrayListener;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.presenters.BasePresenter;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.view.activity.MainActivity;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.presenters.mainActivitypresenter.MainActivityPresenter.homeFragmentNormal;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.presenters.mainActivitypresenter.MainActivityPresenter.homeFragmentWithMap2;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap.baiduMap;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap.bitmapDescriptor;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap.mapView;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap.requestLocButton;


/**
 * Created by yyfwptz on 2017/3/27.
 */

public class HomeFragmentWithMapPresenter extends BasePresenter<HomeFragmentWithMap, HomeFragmentWithMapModelImpl> {

    private FragmentManager fragmentManager;
    private MapStatusUpdate mapStatusUpdate;
    private Button hideButton;
    private Marker markerA;
    private InfoWindow infoWindow;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private LocationClient mLocClient;
    private HomeFragmentWithMapPresenter.MyLocationListenner myListener = new MyLocationListenner();


    @Override
    protected HomeFragmentWithMapModelImpl getModel() {
        return new HomeFragmentWithMapModelImpl();
    }


    public void changeFragment() {
        fragmentManager = MainActivity.mainActivity.getFragmentManager();
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
//        fTransaction.hide(homeFragmentWithMap);
        fTransaction.hide(homeFragmentWithMap2);
        fTransaction.show(homeFragmentNormal);
        fTransaction.commit();
    }

    public void setIcon(Icon[] icons) {
        for (Icon i : icons) {
            Log.e("---", String.valueOf(i.getLatitude()));

            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
            baiduMap = mapView.getMap();
            baiduMap.setMyLocationEnabled(true);
            /**
             * 缩放等级
             */
            mapStatusUpdate = MapStatusUpdateFactory.zoomTo(14.0f);
            baiduMap.setMapStatus(mapStatusUpdate);

            baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                public boolean onMarkerClick(final Marker marker) {
                    hideButton = new Button(MainActivity.mainActivity.getApplicationContext());
                    InfoWindow.OnInfoWindowClickListener listener = null;
                    if (marker == markerA) {
                        /**
                         * 透明
                         */
                        hideButton.setBackgroundColor(0x000000);
                        listener = new InfoWindow.OnInfoWindowClickListener() {
                            public void onInfoWindowClick() {
//                            Toast.makeText(MainActivity.mainActivity, "dd", Toast.LENGTH_SHORT).show();
                            }
                        };
                        LatLng ll = marker.getPosition();
                        infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(hideButton), ll, -47, listener);
                        baiduMap.showInfoWindow(infoWindow);
                    }
                    return true;
                }
            });
            initOverlay(i.getLatitude(), i.getLangitude());
        }
    }

    public void initOverlay(double latitude, double langitude) {
        LatLng latLng = new LatLng(latitude, langitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptor)
                .zIndex(9)
                .draggable(true);
        markerOptions.animateType(MarkerOptions.MarkerAnimateType.drop);
        markerA = (Marker) (baiduMap.addOverlay(markerOptions));
        baiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    public void setLocationMode() {
        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
        requestLocButton.setText("普通");
        View.OnClickListener btnClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (mCurrentMode) {
                    case NORMAL:
                        requestLocButton.setText("跟随");
                        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                        baiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, bitmapDescriptor));
                        break;
                    case COMPASS:
                        requestLocButton.setText("普通");
                        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                        baiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, bitmapDescriptor));
                        break;
                    case FOLLOWING:
                        requestLocButton.setText("罗盘");
                        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                        baiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, bitmapDescriptor));
                        break;
                    default:
                        break;
                }
            }
        };
        requestLocButton.setOnClickListener(btnClickListener);

        mLocClient = new LocationClient(MainActivity.mainActivity);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    public void getIcons() { //start from here

        model.getIcons(new OnSendArrayListener() {

            @Override
            public void sendArray(Object[] objects) {
                getView().getIcons(objects);
            }

        });
    }

    public class MyLocationListenner implements BDLocationListener {
        boolean isFirstLoc = true;


        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null)
                return;

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
