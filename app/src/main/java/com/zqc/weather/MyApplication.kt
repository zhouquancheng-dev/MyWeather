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

    /**
     * 懒汉式单例 线程不安全，
     * 要线程安全需要同步锁synchronized；使用同步锁应双重检查，减少同步粒度
     */
    companion object {
        private var instances: Application? = null
        /**
         * 全局context
         */
        fun getInstance(): Application {
            // 第一层检查
            if (instances == null) {
                synchronized(Application::class.java) {
                    // 第二层检查
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
        applicationScope.launch {}
    }
}