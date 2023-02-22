package com.zqc.weather

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class MyApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        private var instances: Application? = null
        /**
         * 全局context
         */
        fun getInstance(): Application {
            if (instances == null) {
                synchronized(Application::class.java) {
                    if (instances == null) {
                        instances = Application()
                    }
                }
            }
            return instances!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        instances = this
        initData()
    }

    /**
     * 初始化各数据
     */
    private fun initData() {
        applicationScope.launch {

        }
    }
}