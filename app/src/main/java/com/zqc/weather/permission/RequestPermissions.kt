package com.zqc.weather.permission

import android.Manifest
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.zqc.mdoel.dialog.ShowDialog
import com.zqc.mdoel.permission.PermissionPageManagement
import com.zqc.weather.R
import com.zqc.weather.ui.weather.viewmodel.WeatherViewModel

@ExperimentalPermissionsApi
@Composable
fun FeatureThatRequiresLocationPermissions(weatherViewModel: WeatherViewModel) {
    val context = LocalContext.current
    val alertDialog = remember { mutableStateOf(false) }
    val locationPermissionState = rememberMultiplePermissionsState(
        arrayListOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    ) { map: Map<String, Boolean> ->
        //逐个检查权限通过或拒绝情况
        map.forEach { entry ->
            Log.i("locationPermissions", "请求结果: 权限${entry.key} ${if (entry.value) "已通过" else "拒绝"}")
        }
        //利用操作符函数判断是否全部通过或拒绝
        val isGranted = map.values.reduce { acc, nextValue -> (acc && nextValue) }
        if (isGranted) {
            //当第一次弹出请求UI后所有权限都被同意
            Toast.makeText(context, "已全部授权", Toast.LENGTH_SHORT).show()
        } else {
            //当第一次弹出请求UI后选择 ”拒绝“ 所有权限都被拒绝（以及Android 13后 用户未做出任何操作）
            //第二次弹出选择 ”拒绝且不再询问“（或者Android 13后用户依旧未做出任何操作）
            Toast.makeText(context, "定位需要您授予定位权限", Toast.LENGTH_SHORT).show()
            alertDialog.value = true
        }
    }

    when {
        //已有权限
        locationPermissionState.allPermissionsGranted -> {
            LaunchedEffect(Unit) {
                weatherViewModel.getLocation()
            }
        }
        //当前没有权限，需要申请权限
        locationPermissionState.shouldShowRationale ||
                !locationPermissionState.allPermissionsGranted -> {
            LaunchedEffect(Unit) {
                locationPermissionState.launchMultiplePermissionRequest()
            }
        }
    }

    //用户拒绝权限，弹出对话框提示用户跳转设置页
    ShowDialog(
        alertDialog = alertDialog,
        title = stringResource(id = R.string.permission_title),
        content = stringResource(id = R.string.permission_content),
        cancelString = stringResource(id = R.string.permission_cancel),
        confirmString = stringResource(id = R.string.permission_sure)
    ) {
        val permissionPageManagement = PermissionPageManagement()
        permissionPageManagement.startSettingAppPermission(context)
    }
}

@Composable
fun LocationDiaLog(
    alertDialog: MutableState<Boolean>,
    context: Context
) {
    //用户拒绝权限，弹出对话框提示用户跳转设置页
    ShowDialog(
        alertDialog = alertDialog,
        title = stringResource(id = R.string.permission_title),
        content = stringResource(id = R.string.permission_location_button),
        cancelString = stringResource(id = R.string.permission_cancel),
        confirmString = stringResource(id = R.string.permission_sure)
    ) {
        val permissionPageManagement = PermissionPageManagement()
        permissionPageManagement.startSettingAppPermission(context)
    }
}