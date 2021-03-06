package com.cgwx.yyfwptz.lixiang.leifeng0_2.presenters.HomeFragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
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
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.R;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.entities.Icon;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.models.modelImpl.HomeFragmentNormalModelImpl;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.models.modelImpl.HomeFragmentWithMapModelImpl;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.models.modelInterface.OnSendArrayListener;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.models.modelInterface.OnSendStringListener;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.presenters.BasePresenter;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.utils.Constants;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.view.activity.MainActivity;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.view.activity.PanoramaActivity;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.DetectFragmentWithMap;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2;
import com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.MyOrientationListener;
import com.yinglan.scrolllayout.ScrollLayout;
import java.util.ArrayList;
import java.util.List;

import static com.cgwx.yyfwptz.lixiang.leifeng0_2.presenters.mainActivitypresenter.MainActivityPresenter.homeFragmentNormal;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.presenters.mainActivitypresenter.MainActivityPresenter.homeFragmentWithMap2;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2.URL;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2.baiduMap;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2.bitmapDescriptor;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2.clusterManager;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2.mapStatus;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2.mapView;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2.locationMode;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2.mlocationClient;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2.mIconLocation;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2.myOrientationListener;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2.context;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.utils.Utils.icon_format;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2.scrollLayout;
import static com.cgwx.yyfwptz.lixiang.leifeng0_2.view.frgms.HomeFragmentWithMap2.systemWebView;

/**
 * Created by yyfwptz on 2017/3/27.
 */

public class HomeFragmentWithMap2Presenter extends BasePresenter<HomeFragmentWithMap2, HomeFragmentWithMapModelImpl> implements OnGetGeoCoderResultListener {

    private FragmentManager fragmentManager;
    private MapStatusUpdate mapStatusUpdate;
    private Button hideButton;
    private Marker[] markers;
    private Marker locationIcon;
    private InfoWindow infoWindow;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private LocationClient mLocClient;
    private HomeFragmentWithMap2Presenter.MyLocationListenner mlistener;
    private LatLng currentPt;
    private float mCurrentX;
    public static BitmapDescriptor setLocationIcon;
    private GeoCoder geoCoder;
    public static double latitude;
    public static double longitude;
    public static String geoInfo;
    List<MyItem> items = new ArrayList<MyItem>();
    private LatLng iconLatLng;


    @Override
    protected HomeFragmentWithMapModelImpl getModel() {
        return new HomeFragmentWithMapModelImpl();
    }


    /**
     * 切换视图
     */
    public void changeFragment() {
        fragmentManager = MainActivity.mainActivity.getFragmentManager();
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        fTransaction.hide(homeFragmentWithMap2);
        fTransaction.show(homeFragmentNormal);
        fTransaction.commit();
    }

    /**
     * 添加地标  测试
     *
     * @param icons
     */
    public void setIcon(Icon[] icons, ClusterManager clusterManager) {
        int index = 0;
        markers = new Marker[icons.length];

        setIcon(index);

//        for (Icon ic : icons) {
//            Bitmap bitmap = icon_format(BitmapFactory.decodeResource(MainActivity.mainActivity.getResources(), R.drawable.icon_gcoding), 75, 75);
//            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
//            setIcon(index);
//            initOverlay(ic.getLatitude(), ic.getLangitude(), bitmapDescriptor, index);
//            index++;
//        }
    }

    public void testSetIcon(Icon[] icons) {
        int index = 1;
        mapStatus = new MapStatus.Builder().target(new LatLng(39.914935, 116.403119)).zoom(8).build();//地图状态创建者,LatLng(39.914935, 116.403119):设置中心点坐标；zoom(8)：设置地图缩放级别
        baiduMap = mapView.getMap();
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
        clusterManager = new ClusterManager<MyItem>(MainActivity.mainActivity, baiduMap);
        for (Icon icon : icons) {
            LatLng l = new LatLng(icon.getLatitude(), icon.getLangitude());
            items.add(new MyItem(l, index));
            index++;
        }
        clusterManager.addItems(items);
        baiduMap.setOnMapStatusChangeListener(clusterManager);
        baiduMap.setOnMarkerClickListener(clusterManager);
        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                Toast.makeText(MainActivity.mainActivity, "请放大点击", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                scrollLayout.setToExit();
                getURLRequest(Constants.homeFragmentIconInfoURL);
                systemWebView.loadUrl(URL);
                scrollLayout.setMinOffset(300);
                scrollLayout.setToClosed();
                return false;
            }
        });
    }

    private void setIcon(final int index) {
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(17.0f);
        baiduMap.setMapStatus(mapStatusUpdate);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                hideButton = new Button(MainActivity.mainActivity.getApplicationContext());
                InfoWindow.OnInfoWindowClickListener listener = null;
                if (marker == markers[index]) {
                    scrollLayout.setToExit();
                    getURLRequest(Constants.homeFragmentIconInfoURL);
                    systemWebView.loadUrl(URL);
                    scrollLayout.setMinOffset(300);
                    scrollLayout.setToClosed();
                    hideButton.setBackgroundColor(0x000000);
                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {

                        }
                    };
                    LatLng ll = marker.getPosition();
                    infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(hideButton), ll, -47, listener);
                    baiduMap.showInfoWindow(infoWindow);
                }
                return true;
            }
        });
    }

    private void setIcon() {
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(17.0f);
        baiduMap.setMapStatus(mapStatusUpdate);

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                hideButton = new Button(MainActivity.mainActivity.getApplicationContext());
                InfoWindow.OnInfoWindowClickListener listener = null;
                if (marker == locationIcon) {
                    hideButton.setBackgroundColor(0x000000);
                    listener = new InfoWindow.OnInfoWindowClickListener() {
                        public void onInfoWindowClick() {
                        }
                    };
                    LatLng ll = marker.getPosition();
                    infoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(hideButton), ll, -47, listener);
                    baiduMap.showInfoWindow(infoWindow);
                }
                return true;
            }
        });
    }

    /**
     * 添加标志物 子方法
     *
     * @param latitude
     * @param langitude
     * @param bitmapDescriptor
     */
    private void initOverlay(double latitude, double langitude, BitmapDescriptor bitmapDescriptor, int index) {
        LatLng latLng = new LatLng(latitude, langitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptor)
                .zIndex(9)
                .draggable(true);
        markers[index] = (Marker) (baiduMap.addOverlay(markerOptions));

        MapStatus mMapStatus = new MapStatus.Builder()
                .target(latLng)
                .zoom(17)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        baiduMap.setMapStatus(mMapStatusUpdate);
        baiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }


    private void initOverlay(double latitude, double langitude, BitmapDescriptor bitmapDescriptor) {
        LatLng latLng = new LatLng(latitude, langitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptor)
                .zIndex(9)
                .draggable(true);
        locationIcon = (Marker) (baiduMap.addOverlay(markerOptions));

        MapStatus mMapStatus = new MapStatus.Builder()
                .target(latLng)
                .zoom(17)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        baiduMap.setMapStatus(mMapStatusUpdate);
        baiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    /**
     * 获取图标信息
     */
    public void getIcons() {

        model.getIcons(new OnSendArrayListener() {

            @Override
            public void sendArray(Object[] objects) {
                getView().getIcons(objects);
            }

        });
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity.mainActivity, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String strInfo = String.format("纬度：%f 经度：%f", geoCodeResult.getLocation().latitude, geoCodeResult.getLocation().longitude);
        Toast.makeText(MainActivity.mainActivity, strInfo, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity.mainActivity, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        geoInfo = reverseGeoCodeResult.getAddress();
        systemWebView.loadUrl("javascript:showAddressInfo(\"" + geoInfo + "\")");


    }

    /**
     *
     */
    public class MyLocationListenner implements BDLocationListener {
        private boolean isFirstIn = true;

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            MyLocationData data = new MyLocationData.Builder()
                    .direction(mCurrentX)
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            baiduMap.setMyLocationData(data);
            MyLocationConfiguration configuration
                    = new MyLocationConfiguration(locationMode, true, mIconLocation);
            baiduMap.setMyLocationConfigeration(configuration);

            if (isFirstIn) {
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.setMapStatus(msu);
                baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(17).build()));
                isFirstIn = false;
            }
        }


        public void onConnectHotSpotMessage(String s, int i) {

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /**
     * 初始化定位
     *
     * @param res
     */
    public void initLocation(Resources res) {
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;
        mlocationClient = new LocationClient(MainActivity.mainActivity);
        mlistener = new MyLocationListenner();
        mlocationClient.registerLocationListener(mlistener);
        LocationClientOption mOption = new LocationClientOption();
        mOption.setCoorType("bd09ll");
        mOption.setIsNeedAddress(true);
        mOption.setOpenGps(true);
        int span = 1000;
        mOption.setScanSpan(span);
        mlocationClient.setLocOption(mOption);
        mlocationClient.start();
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.location_marker, options);
        bitmap = rotateBitmap(icon_format(bitmap, 100, 100), 330);
        mIconLocation = BitmapDescriptorFactory.fromBitmap(bitmap);
        myOrientationListener = new MyOrientationListener(context);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });
    }

    /**
     * R.drawable.location_marker 图标有问题 对图标的旋转操作
     *
     * @param origin
     * @param alpha
     * @return
     */
    private Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin))
            return newBM;
        origin.recycle();
        return newBM;
    }

    /**
     * 长按添加当前点图标
     *
     * @param res
     */
    public void setLocation(final Resources res) {
        final int[] flag = {0};
        baiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (flag[0] == 0) {
                    showIcon(res, latLng);
                    getURLRequest(Constants.homeFragmentWithMapUpPullURL);
                    systemWebView.loadUrl(URL);
                    scrollLayout.setToOpen();
                    flag[0] = 1;
                } else {
                    removeIcon();
                    getURLRequest(Constants.homeFragmentWithMapUpPullURL);
                    systemWebView.loadUrl(URL);
                    scrollLayout.setToOpen();
                    showIcon(res, latLng);
                }
            }
        });
    }

    /**
     * 添加图标
     *
     * @param res
     * @param latLng
     */
    private void showIcon(Resources res, LatLng latLng) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.icon_focus_marka, options);
        setLocationIcon = BitmapDescriptorFactory.fromBitmap(icon_format(bitmap, 75, 120));
        currentPt = latLng;
        showGeoInfo(currentPt);
        latitude = currentPt.latitude;
        longitude = currentPt.longitude;
        setPosition(latitude, longitude, setLocationIcon);
        Log.e("sdfsf", "" + currentPt.latitude + currentPt.longitude);
    }

    private void showGeoInfo(LatLng latLng) {
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));

    }

    /**
     * 点击新位置 删除前一个位置的icon
     */
    private void removeIcon() {
        locationIcon.remove();
    }

    /**
     * 长按添加图标 子方法
     *
     * @param latitude
     * @param langitude
     * @param bitmapDescriptor
     */
    private void setPosition(double latitude, double langitude, BitmapDescriptor bitmapDescriptor) {
        setIcon();
        initOverlay(latitude, langitude, bitmapDescriptor);
    }

    /**
     * 上拉菜单
     *
     * @param scrollLayout
     */
    public void initialScrollLayout(ScrollLayout scrollLayout) {
        scrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        scrollLayout.getBackground().setAlpha(0);
    }

    /**
     * 上拉菜单
     */
    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {
                    precent = 0;
                }
                scrollLayout.getBackground().setAlpha(255 - (int) precent);
            }
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) {

            }
        }

        @Override
        public void onChildScroll(int top) {
        }
    };

    public void getURLRequest(String request) {
        model.geturl(request, new OnSendStringListener() {
            @Override
            public void sendString(String string) {
                getView().getURL(string);
            }
        });
    }

    public void panoramaView() {
        Intent intent = new Intent(MainActivity.mainActivity, PanoramaActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("geoInfo", geoInfo);
        Log.e("ddf", latitude + longitude + geoInfo);
        MainActivity.mainActivity.startActivity(intent);
    }

    public void getLotionInfo() {

    }

    public class MyItem implements ClusterItem {//是ClusterItem接口的实现类，此类主要用来生成地图最终显示的marker，所以包含了经纬度坐标，marker的icon图标，
        private final LatLng mPosition;
        private final int index;

        public MyItem(LatLng latLng, int i) {
            mPosition = latLng;
            index = i;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }//返回marker的坐标

        @Override
        public BitmapDescriptor getBitmapDescriptor() {//返回marker的图标
            Bitmap bitmap = icon_format(BitmapFactory.decodeResource(MainActivity.mainActivity.getResources(), R.drawable.icon_gcoding), 75, 75);
            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
            return bitmapDescriptor;
        }
    }
}
