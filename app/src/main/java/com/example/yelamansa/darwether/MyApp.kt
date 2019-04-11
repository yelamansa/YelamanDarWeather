package com.example.yelamansa.darwether

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration



class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
        config.name("weather")
        config.deleteRealmIfMigrationNeeded()
        Realm.setDefaultConfiguration(config.build())
    }
}