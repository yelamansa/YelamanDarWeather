package com.example.yelamansa.darwether.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class WeatherCache:RealmObject() {
    var id:Int? = null
    var cityName:String? = null
    var recordTime:Long = 0
    var apiWeathers: RealmList<ApiWeather>? = null
}