package com.zqc.weather

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zqc.mdoel.view.AdaptationScreenHeight
import com.zqc.mdoel.view.BarsLightDarkTheme
import com.zqc.weather.ui.theme.MyWeatherTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@ExperimentalPagerApi
@ExperimentalPermissionsApi
@FlowPreview
@ExperimentalMaterialApi
@ExperimentalTextApi
@ExperimentalLifecycleComposeApi
@ExperimentalMaterialNavigationApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarsLightDarkTheme()
            AdaptationScreenHeight {
                MyWeatherTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        WeatherApplication()
                    }
                }
            }
        }
    }
}