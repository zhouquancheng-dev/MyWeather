package com.zqc.weather.permission

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.zqc.weather.MyApplication
import javax.inject.Inject

/**
 * 高德定位
 */
class MyLocationListener @Inject constructor() : DefaultLifecycleObserver {
    //声明AMapLocationClient类对象
    private var mLocationClient: AMapLocationClient? = null
    //声明mLocationOption对象
    private var mLocationOption: AMapLocationClientOption? = null

    private lateinit var locationListener: LocationListener

    /**
     * 给外部提供的定位结果回调接口
     * @property onSuccess 定位成功
     * @property onFailure 定位失败
     */
    interface LocationListener {
        fun onSuccess(aMapLocation: AMapLocation)
        fun onFailure()
    }

    fun setLocationListener(locationListener: LocationListener) {
        this.locationListener = locationListener
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        destroyLocation()
    }

    //初始化定位
    fun initLocation() {
        Log.i("${MyLocationListener::class.simpleName}", "initLocation invoked: 初始化定位")
        try {
            //高德定位隐私政策同意
            AMapLocationClient.updatePrivacyShow(MyApplication.getInstance(), true, true)
            AMapLocationClient.updatePrivacyAgree(MyApplication.getInstance(), true)
            //new AMapLocationClient()对象
            mLocationClient = AMapLocationClient(MyApplication.getInstance())
            //new AMapLocationClientOption()对象
            mLocationOption = AMapLocationClientOption()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (mLocationClient != null) {
            /**
             * setApiKey必须在启动Activity或者Application的onCreate里进行， 在其他地方使用有可能无效,
             * 如果使用setApiKey设置key，则AndroidManifest.xml里的key会失效
             */
//            AMapLocationClient.setApiKey("key值")

            //设置定位监听接口回调
            mLocationClient?.setLocationListener(mAMapLocationListener)

            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption?.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            //设置定位间隔,单位毫秒,默认为2000ms,最小1000ms
            mLocationOption?.interval = 1000
            //获取一次定位结果
            mLocationOption?.isOnceLocation = true
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false
            mLocationOption?.isOnceLocationLatest = true
            //设置是否返回地址信息（默认返回地址信息）, 如果isNeedAddress设置为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息
            mLocationOption?.isNeedAddress = true
            //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒
            mLocationOption?.httpTimeOut = 20000
            //缓存机制，当开启定位缓存功能，在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存，不区分单次定位还是连续定位。GPS定位结果不会被缓存。
            mLocationOption?.isLocationCacheEnable = false

            //给定位客户端对象设置定位参数 AMapLocationClientOption
            mLocationClient?.setLocationOption(mLocationOption)
        }
    }

    /**
     * 启动定位
     */
    fun startLocation() {
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用client.onDestroy()方法
        // 在单次定位情况下（mLocationOption?.isOnceLocation = true），定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mLocationClient?.startLocation()
        Log.i("${MyLocationListener::class.simpleName}", "startLocation invoked: 启动定位")
    }

    /**
     * 停止定位
     */
    private fun stopLocation() {
        //获取到定位信息后停止定位后（本地定位服务并不会被销毁）
        mLocationClient?.stopLocation()
        Log.i("${MyLocationListener::class.simpleName}", "stopLocation invoked: 停止定位")
    }

    /**
     * 销毁定位客户端，同时销毁本地定位服务；销毁定位客户端之后，若要重新开启定位需重新New一个AMapLocationClient对象。
     * 如果AMapLocationClient是在当前Activity实例化的，
     * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
     */
    private fun destroyLocation() {
        mLocationClient?.stopLocation()
        mLocationClient?.unRegisterLocationListener(mAMapLocationListener)
        mLocationClient?.onDestroy()
        mLocationClient = null
        mLocationOption = null
        Log.i("${MyLocationListener::class.simpleName}", "destroyLocation invoked: 销毁定位客户端，同时销毁本地定位服务")
    }

    /**
     * 返回的定位信息
    ```
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = Date(aMapLocation.time)
    Log.i("AMapLocationInfo", "定位时间: ${simpleDateFormat.format(date)}")
    Log.i("AMapLocationInfo", "定位来源: ${aMapLocation.locationType}")
    Log.i("AMapLocationInfo", "定位精度信息: ${aMapLocation.accuracy}")
    Log.i("AMapLocationInfo", "纬度 lat: ${aMapLocation.latitude} 经度 lng: ${aMapLocation.longitude}")
    Log.i("AMapLocationInfo", "全地址: ${aMapLocation.address}")
    Log.i("AMapLocationInfo", "国家信息: ${aMapLocation.country}")
    Log.i("AMapLocationInfo", "省信息: ${aMapLocation.province}")
    Log.i("AMapLocationInfo", "城市信息: ${aMapLocation.city}")
    Log.i("AMapLocationInfo", "城区信息: ${aMapLocation.district}")
    Log.i("AMapLocationInfo", "街道信息: ${aMapLocation.street}")
    Log.i("AMapLocationInfo", "街道门牌号信息: ${aMapLocation.streetNum}")
    Log.i("AMapLocationInfo", "城市编码: ${aMapLocation.cityCode}")
    Log.i("AMapLocationInfo", "地区编码: ${aMapLocation.adCode}")
    Log.i("AMapLocationInfo", "当前定位点的AOI信息: ${aMapLocation.aoiName}")
    Log.i("AMapLocationInfo", "当前室内定位的建筑物Id: ${aMapLocation.buildingId}")
    Log.i("AMapLocationInfo", "当前室内定位的楼层: ${aMapLocation.floor}")
    Log.i("AMapLocationInfo", "GPS当前的状态: ${aMapLocation.gpsAccuracyStatus}")
    ```
     */
    //高德接口异步监听
    private val mAMapLocationListener = AMapLocationListener { aMapLocation ->
        if (aMapLocation != null) {
            //errorCode为 0即定位成功
            if (aMapLocation.errorCode == 0) {
                if (this::locationListener.isInitialized) {
                    locationListener.onSuccess(aMapLocation)
                    stopLocation()
                    Log.i("mAMapLocationListener", "longitude: ${aMapLocation.longitude} latitude: ${aMapLocation.latitude}")
                }
            } else {
                //显示错误信息 ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e(
                    "AMapError",
                    "location Error, ErrCode: ${aMapLocation.errorCode}," +
                            " errInfo:${aMapLocation.errorInfo} errMsg: ${aMapLocation.locationDetail}"
                )
                if (this::locationListener.isInitialized) {
                    locationListener.onFailure()
                }
            }
        } else {
            Log.i("AMapLocation", "onLocationChanged: 定位失败 aMapLocation is null")
        }
    }
}
