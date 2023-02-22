package com.zqc.mdoel.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.zqc.mdoel.LogUtil
import kotlinx.coroutines.*

/**
 * 监听网络连接状态
 */
class ConnectivityWatcher(private val context: Context): LiveData<Boolean>() {

    private val tag = ConnectivityWatcher::class.simpleName.toString()

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private lateinit var broadcastReceiver: BroadcastReceiver

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    override fun setValue(value: Boolean?) {
        if (getValue() == value)
            return
        super.setValue(value)
    }

    override fun onActive() {
        super.onActive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //创建networkCallback()实例
            connectivityManagerCallback = networkCallback()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback)
            } else {
                val networkRequest = NetworkRequest.Builder().apply {
                    addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                }.build()
                connectivityManager.registerNetworkCallback(
                    networkRequest,
                    connectivityManagerCallback
                )
            }
        } else {
            @Suppress("DEPRECATION")
            val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            //创建networkBroadReceiver()实例
            broadcastReceiver = networkBroadReceiver()
            context.registerReceiver(broadcastReceiver, intentFilter)
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
        } else {
            context.unregisterReceiver(broadcastReceiver)
        }
    }

    /**
     *  Android 10（API 级别 29）及更高版本为目标平台的应用使用 NetworkCallbacks 监听网络回调
     */
    private fun networkCallback() = object : ConnectivityManager.NetworkCallback() {
        private var mCurrentNetwork: Network? = null
        private val handler = Handler(Looper.getMainLooper())

        /**
         * 网络连接成功回调
         */
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            mCurrentNetwork = network
            postValue(true)

            connectivityManager.getNetworkCapabilities(network)?.let { capabilities ->
                val isCurrentWifiNetwork =
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                val isCurrentMobileNetwork =
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                val isCurrentVpn = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                val isInternet =
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val isValidated = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    LogUtil.i(
                        tag,
                        "onAvailable: " +
                                "isInternet = $isInternet, " +
                                "isValidated = $isValidated, " +
                                "isWifi = $isCurrentWifiNetwork, " +
                                "isCellular = $isCurrentMobileNetwork, " +
                                "isVPN = $isCurrentVpn"
                    )
                } else {
                    LogUtil.i(
                        tag,
                        "onAvailable: " +
                                "isInternet = $isInternet, " +
                                "isWifi = $isCurrentWifiNetwork, " +
                                "isCellular = $isCurrentMobileNetwork, " +
                                "isVPN = $isCurrentVpn"
                    )
                }
            }
        }

        /**
         * 网络断开连接
         */
        override fun onLost(network: Network) {
            super.onLost(network)
            LogUtil.i(tag, "onLost: $network")
            if (network == mCurrentNetwork) {
                mCurrentNetwork = null
                handler.postDelayed(Runnable {
                    if (mCurrentNetwork == null) {
                        postValue(false)
                    }
                }, 3000)
            }
        }

        /**
         * 网络状态变化
         */
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities,
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            LogUtil.i(tag, "onCapabilitiesChanged: $networkCapabilities")
            // NetworkCapabilities.NET_CAPABILITY_INTERNET	表示是否连接上了互联网（不关心是否可以上网）
            // NetworkCapabilities.NET_CAPABILITY_VALIDATED	表示能够和互联网通信（为true表示能够上网）
            if (networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                )
                && networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
            ) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    LogUtil.i(tag, "wifi已连接")
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    LogUtil.i(tag, "蜂窝数据已连接")
                } else {
                    LogUtil.i(tag, "其它网络连接")
                }
            }
        }
    }

    /**
     * 广播接收器
     */
    @Suppress("DEPRECATION")
    private fun networkBroadReceiver() = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnected == true
            postValue(isConnected)
            if (isConnected) {
                LogUtil.i(tag, "网络已连接")
            } else {
                LogUtil.i(tag, "网络连接已断开")
            }
        }
    }

}