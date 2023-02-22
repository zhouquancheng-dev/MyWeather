package com.zqc.mdoel.nav

import android.os.Bundle
import androidx.navigation.NavController

/**
 * 回传给指定route的上级栈参数
 *
 * @param route 返回到route的路由地址
 * @param inclusive 是否也应该弹出给定的目的地
 * @param autoPop 是否启用弹栈
 * @param callback 回传的 Bundle 数据
 */
fun NavController.toBackRouteWithParams(
    route: String,
    inclusive: Boolean,
    autoPop: Boolean = true,
    callback: (Bundle.() -> Unit)? = null
) {
    getBackStackEntry(route).arguments?.let {
        callback?.invoke(it)
    }
    if (autoPop) {
        popBackStack(route = route, inclusive = inclusive)
    }
}

/**
 * 回传给上级栈参数
 *
 * @param autoPop 是否启用弹栈
 * @param callback 回传的 Bundles 数据
 */
fun NavController.toBackWithParams(
    autoPop: Boolean = true,
    callback: (Bundle.() -> Unit)? = null
) {
    previousBackStackEntry?.arguments?.let {
        callback?.invoke(it)
    }
    if (autoPop) {
        popBackStack()
    }
}