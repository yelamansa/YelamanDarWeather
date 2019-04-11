package com.example.yelamansa.darwether.model

import io.realm.RealmObject

open class ApiWeather:RealmObject() {
    var id: Int? = null
    var main: Main? = null
    var name: String?=null
}