package com.marklynch.compose.viewmodel

import androidx.lifecycle.ViewModel
import com.marklynch.compose.data.ManualLocation
import com.marklynch.compose.livedata.WeatherLiveData

open class MainViewModel : ViewModel() {

    //Weather
    val weatherLiveData: WeatherLiveData = WeatherLiveData()

    fun getWeather() = weatherLiveData.value

    fun fetchWeather(manualLocation: ManualLocation) {
            weatherLiveData.fetchWeather(manualLocation.latitude, manualLocation.longitude)
    }
}