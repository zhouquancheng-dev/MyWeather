package com.zqc.mdoel.permission

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.zqc.utils.R

/**
 * 各个厂商的权限跳转配置管理
 */
class PermissionPageManagement {

    /**
     * Build.MANUFACTURER
     */
    private val huawei = "HuaWei" //华为

    private val meiZu = "MeiZu" //魅族

    private val xiaomi = "Xiaomi" //小米

    private val sony = "Sony" //索尼

    private val oppo = "OPPO" //oppo

    private val lg = "LG"  //LG

    private val viVo = "ViVo" //vi vo

//    private val samsung = "Samsung" //三星

//    private val zte = "ZTE" //中兴

//    private val yuLong = "YuLong" //酷派

    private val lenovo = "LENOVO" //联想

    fun startSettingAppPermission(context: Context) {
        when (Build.MANUFACTURER) {
            huawei -> huawei(context)
            meiZu -> meiZu(context)
            xiaomi -> xiaomi(context)
            sony -> sony(context)
            oppo -> oppo(context)
            lg -> lg(context)
            viVo -> viVo(context)
            lenovo -> lenovo(context)
            else -> {
                //没有包括的厂商就跳转到系统默认的应用设置页
                startApplicationInfo(context)
            }
        }
    }

    private fun huawei(context: Context) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", context.applicationInfo?.packageName)
            val comp = ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity"
            )
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            startIntentSetting(context)
        }
    }

    private fun meiZu(context: Context) {
        try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", context.packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            startIntentSetting(context)
        }
    }

    private fun xiaomi(context: Context) {
        try {
            val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
            intent.putExtra("extra_pkgname", context.packageName)
            val componentName = ComponentName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity"
            )
            intent.component = componentName
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            startIntentSetting(context)
        }
    }

    private fun sony(context: Context) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", context.packageName)
            val comp = ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity")
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            startIntentSetting(context)
        }
    }

    private fun oppo(context: Context) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", context.packageName)
            val comp = ComponentName(
                "com.coloros.securitypermission",
                "com.coloros.securitypermission.permission.PermissionAppAllPermissionActivity"
            ) //R11t 7.1.1 os-v3.2
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            startIntentSetting(context)
        }
    }

    private fun viVo(context: Context) {
        val localIntent: Intent
        if (Build.MODEL.contains("Y85") && !Build.MODEL.contains("Y85A") || Build.MODEL.contains("vivo Y53L")) {
            localIntent = Intent()
            localIntent.setClassName(
                "com.vivo.permissionmanager",
                "com.vivo.permissionmanager.activity.PurviewTabActivity"
            )
            localIntent.putExtra("packagename", context.packageName)
            localIntent.putExtra("tabId", "1")
            context.startActivity(localIntent)
        } else {
            localIntent = Intent()
            localIntent.setClassName(
                "com.vivo.permissionmanager",
                "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity"
            )
            localIntent.action = "secure.intent.action.softPermissionDetail"
            localIntent.putExtra("packagename", context.packageName)
            context.startActivity(localIntent)
        }
    }

    private fun lg(context: Context) {
        try {
            val intent = Intent("android.intent.action.MAIN")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", context.packageName)
            val comp = ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$AccessLockSummaryActivity"
            )
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            startIntentSetting(context)
        }
    }

    private fun lenovo(context: Context) {
        try {
            val intent = Intent("com.zui.safecenter.permissionmanager.AppPermission")
            intent.putExtra("title", context.getString(R.string.app_name))
            intent.putExtra("package", context.packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            startIntentSetting(context)
        }
    }

    /**
     * 应用信息界面
     * @param context
     */
    private fun startApplicationInfo(context: Context) {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        localIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        localIntent.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(localIntent)
    }

    /**
     * 默认打开应用详细页
     */
    private fun startIntentSetting(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}