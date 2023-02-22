package com.zqc.weather.ui.baseviewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
open class ViewPagerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val homePageSelectedIndex = "home_page_selected_index"

    private val mSelectStateFlow = MutableStateFlow(0)

    fun getSelectIndex(): StateFlow<Int> {
        val index = savedStateHandle.get<Int>(homePageSelectedIndex) ?: 0
        mSelectStateFlow.value = index
        return mSelectStateFlow
    }

    fun saveSelectIndex(selectIndex: Int) {
        savedStateHandle[homePageSelectedIndex] = selectIndex
        mSelectStateFlow.value = selectIndex
    }

    fun removeIndex() {
        savedStateHandle.remove<Int>(homePageSelectedIndex)
    }

    override fun onCleared() {
        super.onCleared()
        savedStateHandle.remove<Int>(homePageSelectedIndex)
        Log.i("${ViewPagerViewModel::class.simpleName}", "onCleared invoke")
    }
}