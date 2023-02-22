package com.zqc.weather

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.zqc.mdoel.broadcast.ConnectivityWatcher
import com.zqc.mdoel.view.transparentStatusBar
import com.zqc.weather.permission.MyLocationListener

open class BaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        installSplashScreen()   // 加载动画
        transparentStatusBar()  //状态栏沉浸
        netChangeObserve()
        myLocationListenerObserve()
        predictiveBackGesture()
    }

    //观察网络连接监听
    private fun netChangeObserve() {
        ConnectivityWatcher(this.applicationContext).observe(this) { isNet ->
            Log.i("${BaseActivity::class.simpleName}", "netChangeObserve: Network connection is $isNet")
            if (!isNet) Toast.makeText(this, "网络连接断开", Toast.LENGTH_SHORT).show()
        }
    }

    //android 13 预测性手势简单适配；在主页中点击back按钮行为，直接关闭Activity活动
    private fun predictiveBackGesture() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this@BaseActivity.finish()
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    //添加定位观察者，MyLocationListener以达到生命周期感知
    private fun myLocationListenerObserve() {
        val myLocationListener = MyLocationListener()
        lifecycle.addObserver(myLocationListener)
    }
}