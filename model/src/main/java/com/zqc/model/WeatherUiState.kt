package com.zqc.model

sealed class WeatherUiState<out R> {
    fun isLoading() = this is WeatherLoading
    fun isSuccessful() = this is WeatherSuccess
    fun isNoContent() = this is WeatherNoContent
    fun isError() = this is  WeatherError

    override fun toString(): String {
        return when (this) {
            is WeatherSuccess<*> -> "Success[data=$data]"
            is WeatherNoContent -> "Success[reason=$reason]"
            is WeatherError -> "Error[exception=${errorMsg}]"
            WeatherLoading -> "Loading"
        }
    }
}

data class WeatherSuccess<out T>(val data: T) : WeatherUiState<T>()
data class WeatherNoContent(val reason: String) : WeatherUiState<Nothing>()
data class WeatherError(val errorMsg: String?) : WeatherUiState<Nothing>()
object WeatherLoading : WeatherUiState<Nothing>()