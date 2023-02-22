package com.zqc.weather

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zqc.weather.ui.theme.MyWeatherTheme
import kotlinx.coroutines.FlowPreview

@ExperimentalPagerApi
@ExperimentalPermissionsApi
@FlowPreview
@ExperimentalLifecycleComposeApi
@ExperimentalTextApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalMaterialNavigationApi
@Composable
fun WeatherApplication() {
    MyWeatherTheme {
        val bottomSheetNavigator = rememberBottomSheetNavigator()
        val navController = rememberAnimatedNavController(bottomSheetNavigator)
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = MaterialTheme.colors.background
        ) { innerPaddingModifier ->
            AppNavGraph(
                modifier = Modifier.padding(innerPaddingModifier),
                bottomSheetNavigator = bottomSheetNavigator,
                navController = navController
            )
        }
    }
}