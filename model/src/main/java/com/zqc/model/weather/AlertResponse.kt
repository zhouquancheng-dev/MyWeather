package com.zqc.model.weather


data class AlertResponse(val alert: Alert) {

    /**
     * 预警数据Bean
     * @param status  请求状态
     * @param content 预警信息
     * @param adcodes 行政区划层级信息
     */
    data class Alert(
        val status: String,
        val content: List<AlertDesc>,
        val adcodes: List<AlertCodes>,
    ) {
        /**
         * @param province  省，如"广东省"
         * @param status  预警信息的状态，如"预警中"
         * @param code 预警代码，如"0902" 预警类型编码＋预警级别编码
         * @param description 描述，如"深圳市气象台 2023 年 01 月 01 日 12 时 00 分继续发布雷电黄色预警信号：预计未来 6 小时我市有雷电活动，局地伴有短时强降水、6-8 级雷雨大风等强对流天气。请注意防范！"
         * @param regionId 区域编码，如"101010200"
         * @param county 县级
         * @param pubtimestamp 发布时间，单位是 Unix 时间戳
         * @param latlon 经纬度
         * @param city 市级，如"深圳市"
         * @param alertId 预警 ID
         * @param title 标题，如"深圳市气象台发布雷电黄色预警[Ⅲ 级/较重]",
         * @param adcode 区域代码，如 "350400"
         * @param source 发布单位，如"国家预警信息发布中心",
         * @param location 位置，如"广东省深圳市"
         * @param request_status 请求状态，如"ok"
         */
        data class AlertDesc(
            val province: String,
            val status: String,
            val code: String,
            val description: String,
            val regionId: String,
            val county: String,
            val pubtimestamp: Long,
            val latlon: List<Float>,
            val city: String,
            val alertId: String,
            val title: String,
            val adcode: String,
            val source: String,
            val location: String,
            val request_status: String,
        )

        /**
         * @param adcode 区域代码，如 "350400"
         * @param name 区域
         */
        data class AlertCodes(
            val adcode: Int,
            val name: String,
        )
    }

}