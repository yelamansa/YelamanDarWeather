package com.example.yelamansa.darwether.view

import com.example.yelamansa.darwether.model.ApiWeather

interface WeatherView {
    fun setCities(cities: List<ApiWeather>)
    fun showLoading()
    fun hideLoading()
    fun showMessage(message:String)
    fun setCityName(cityName:String)
}