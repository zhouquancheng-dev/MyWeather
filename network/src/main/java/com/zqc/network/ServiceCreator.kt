package com.zqc.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {
    private const val CAI_YUN_URL = "https://api.caiyunapp.com"
    private const val CONNECT_TIMEOUT = 30L //连接超时时间
    private const val READ_TIMEOUT = 10L    //读取超时时间

    /**
     * get ServiceApi
     * @param serviceClass Service接口所对应的Class类型
     */
    fun <T> createCaiYun(serviceClass: Class<T>): T = createCaiYun().create(serviceClass)

    /**
     * reified get ServiceApi
     * @sample createCaiYunApi
     */
    inline fun <reified T> createCaiYunApi(): T = createCaiYun(T::class.java)

    private fun createCaiYun(): Retrofit {
        //创建 okHttpClientBuilder
        val mOkHttpClient = OkHttpClient().newBuilder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        }
        return RetrofitBuild(
            url = CAI_YUN_URL,
            client = mOkHttpClient.build(),
            gsonConverterFactory = GsonConverterFactory.create()
        ).retrofitCaiYun
    }
}

private class RetrofitBuild(
    url: String,
    client: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
) {
    val retrofitCaiYun: Retrofit = Retrofit.Builder().apply {
        baseUrl(url)
        client(client)
        addConverterFactory(gsonConverterFactory)
    }.build()
}