package com.zqc.model

/**
 * 彩云天气 Base bean
 * @param status 请求状态
 * @param api_version api版本
 * @param api_status
 * @param lang 返回的自然语言 zh_CN简体中文 zh_TW 繁体中文 en_US 美式英语 en_GB 英式英语 ja 日语
 * @param unit 返回的数据的单位 imperial 英制  metric 默认公制
 * @param tzshift
 * @param timezone 时区
 * @param server_time 服务器时间戳，单位是 Unix 时间戳
 * @param location 经纬度
 * @param result 子bean
 * @param primary
 */
data class BaseModel<T>(
    val status: String,
    val api_version: String,
    val api_status: String,
    val lang: String,
    val unit: String,
    val tzshift: Int,
    val timezone: String,
    val server_time: Long,
    val location: List<Float>,
    val result: T,
    val primary: Int
)