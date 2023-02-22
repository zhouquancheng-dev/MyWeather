package com.zqc.weather

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.zqc.mdoel.view.BarsLightDarkTheme
import com.zqc.weather.permission.LocationDiaLog
import com.zqc.weather.ui.city.CityManagePage
import com.zqc.weather.ui.city.viewmodel.CityViewModel
import com.zqc.weather.ui.daily.DailyWeatherPage
import com.zqc.weather.ui.daily.viewmodel.DailyViewModel
import com.zqc.weather.ui.lifeindex.BottomLifeIndexContent
import com.zqc.weather.ui.setting.WeatherSettingScreen
import com.zqc.weather.ui.weather.WeatherViewPager
import com.zqc.weather.ui.weather.viewmodel.WeatherViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

sealed class WeatherDestinations(val route: String) {
    object HomePage : WeatherDestinations("home_page")
    object CityManagePage : WeatherDestinations("cityManage_page")
    object DailyWeatherPage : WeatherDestinations("dailyWeather_page/{locationName}/{longitude}/{latitude}/{index}") {
        fun onNavigateToRoute(locationName: String, longitude: String, latitude: String, index: Int) =
            "dailyWeather_page/$locationName/$longitude/$latitude/$index"
    }
    object SettingPage : WeatherDestinations("setting_page")
}

@ExperimentalPagerApi
@ExperimentalPermissionsApi
@FlowPreview
@ExperimentalMaterialApi
@ExperimentalTextApi
@ExperimentalLifecycleComposeApi
@ExperimentalAnimationApi
@ExperimentalMaterialNavigationApi
@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    bottomSheetNavigator: BottomSheetNavigator,
    navController: NavHostController,
    startDestination: String = WeatherDestinations.HomePage.route
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(0)
    val actions = remember(navController) { NavActions(navController) } //导航动作
    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    ) {
        AnimatedNavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination,
            enterTransition = { fadeIn(spring(stiffness = Spring.StiffnessLow)) + slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { fadeOut(spring(stiffness = Spring.StiffnessLow)) + slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { fadeIn(spring(stiffness = Spring.StiffnessLow)) + slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { fadeOut(spring(stiffness = Spring.StiffnessLow)) + slideOutHorizontally(targetOffsetX = { it }) }
        ) {
            composable(WeatherDestinations.HomePage.route) { backStackEntry ->
                BarsLightDarkTheme(isSystemInDarkTheme())
                val backSelect: Int by backStackEntry.savedStateHandle.getStateFlow("select_page", -1).collectAsStateWithLifecycle()
                val weatherViewModel = hiltViewModel<WeatherViewModel>()
                WeatherViewPager(
                    weatherViewModel = weatherViewModel,
                    pagerState = pagerState,
                    backSelect = backSelect,
                    toSettingClick = actions.toSetting,
                    toCityManage = {
                        backStackEntry.savedStateHandle.remove<Int>("select_page")
                        actions.toCityManage()
                    },
                    toDailyClick = { name, longitude, latitude, index ->
                        actions.toDailyWeather(name, longitude, latitude, index)
                    },
                    toBottomSheetClick = { actions.toBottomSheet(it) }
                )
            }

            composable(WeatherDestinations.CityManagePage.route) {
                BarsLightDarkTheme()
                val cityViewModel = hiltViewModel<CityViewModel>()
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
                        cityViewModel.getLocation {
                            scope.launch {
                                pagerState.scrollToPage(0)
                            }
                            actions.cityListToWeather(0)
                        }
                    } else {
                        //当第一次弹出请求UI后选择 ”拒绝“ 所有权限都被拒绝（以及Android 13后 用户未做出任何操作）
                        //第二次弹出选择 ”拒绝且不再询问“（或者Android 13后用户依旧未做出任何操作）
                        Toast.makeText(context, "定位需要您授予定位权限", Toast.LENGTH_SHORT).show()
                        alertDialog.value = true
                    }
                }
                //权限dialog
                LocationDiaLog(alertDialog = alertDialog, context = context)
                CityManagePage(
                    cityViewModel = cityViewModel,
                    onBackClick = actions.upPress,
                    onLocationClick = {
                        when {
                            //已有权限
                            locationPermissionState.allPermissionsGranted -> {
                                cityViewModel.getLocation {
                                    scope.launch {
                                        pagerState.scrollToPage(0)
                                    }
                                    actions.cityListToWeather(0)
                                }
                            }
                            //当前没有权限，需要申请权限
                            locationPermissionState.shouldShowRationale || !locationPermissionState.allPermissionsGranted -> {
                                    locationPermissionState.launchMultiplePermissionRequest()
                            }
                        }
                    },
                    toWeatherClick = { index ->
                        scope.launch {
                            pagerState.scrollToPage(index)
                        }
                        actions.cityListToWeather(index)
                    },
                    toDailyClick = { locationName, longitude, latitude ->
                        actions.toDailyWeather(locationName, longitude, latitude, 1)
                    },
                    onDeleteCityClick = { cityInfo ->
                        cityViewModel.deleteCityInfo(cityInfo)
                    }
                )
            }

            composable(
                route = WeatherDestinations.DailyWeatherPage.route,
                arguments = listOf(
                    navArgument("locationName") { type = NavType.StringType },
                    navArgument("longitude") { type = NavType.StringType },
                    navArgument("latitude") { type = NavType.StringType },
                    navArgument("index") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                BarsLightDarkTheme()
                val locationName = requireNotNull(backStackEntry.arguments).getString("locationName") ?: ""
                val longitude = requireNotNull(backStackEntry.arguments).getString("longitude") ?: ""
                val latitude = requireNotNull(backStackEntry.arguments).getString("latitude") ?: ""
                val index = requireNotNull(backStackEntry.arguments).getInt("index")
                val dailyViewModel = hiltViewModel<DailyViewModel>()
                LaunchedEffect(Unit) {
                    dailyViewModel.getDailyWeather(longitude, latitude)
                }
                DailyWeatherPage(
                    dailyViewModel = dailyViewModel,
                    title = locationName,
                    position = index,
                    onBackClick = actions.upPress,
                    toWeatherClick = {
                        scope.launch {
                            val rowId = dailyViewModel.insertCityInfo(locationName, longitude, latitude)
                            if (rowId != -1 && rowId != -2) {
                                pagerState.scrollToPage(rowId - 1)
                                actions.dailyListToWeather()
                            }
                        }
                    },
                    onRetryClick = { dailyViewModel.getDailyWeather(longitude, latitude) }
                )
            }

            composable(WeatherDestinations.SettingPage.route) {
                BarsLightDarkTheme()
                WeatherSettingScreen(actions.upPress)
            }

            bottomSheet("bottom_life/{title}") { backStackEntry ->
                val text = requireNotNull(backStackEntry.arguments).getString("title") ?: ""
                BottomLifeIndexContent(text)
            }
        }
    }
}

class NavActions(navController: NavController) {
    val toCityManage: () -> Unit = {
        navController.navigate(WeatherDestinations.CityManagePage.route)
    }

    val toDailyWeather: (String, String, String, Int) -> Unit = { locationName, longitude, latitude, index ->
        navController.navigate(WeatherDestinations.DailyWeatherPage.onNavigateToRoute(locationName, longitude, latitude, index))
    }

    val cityListToWeather: (Int) -> Unit = {
        navController.previousBackStackEntry?.savedStateHandle?.set("select_page", it)
        navController.popBackStack()
    }

    val dailyListToWeather: () -> Unit = {
        navController.popBackStack(WeatherDestinations.HomePage.route, false)
    }

    val toBottomSheet: (title: String) -> Unit = {
        navController.navigate("bottom_life/$it")
    }

    val toSetting: () -> Unit = {
        navController.navigate(WeatherDestinations.SettingPage.route)
    }

    val upPress: () -> Unit = {
        navController.popBackStack()
    }
}