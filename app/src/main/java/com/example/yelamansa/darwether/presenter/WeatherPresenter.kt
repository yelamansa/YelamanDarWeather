package com.example.yelamansa.darwether.presenter

import android.content.Context
import com.example.yelamansa.darwether.WeatherRepository
import com.example.yelamansa.darwether.model.WeatherCache
import com.example.yelamansa.darwether.view.WeatherView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class WeatherPresenter {

    private val repository = WeatherRepository()
    var view:WeatherView? = null
    var disposable: Disposable? = null

    fun loadLocalCityWeathers(){

        val result:WeatherCache? = repository.getLocalCityWeathers()
        if (result!=null){
            if (isLessThanOneHour(result.recordTime)){
                if (result.apiWeathers!=null&&result.cityName!=null){
                    view?.setCities(result.apiWeathers!!.toList())
                    view?.setCityName(result.cityName!!)
                }
            }else{
                repository.clearWeathers()
            }
        }
    }

    private fun isLessThanOneHour(recordTime:Long):Boolean{
        val oneHourMs = 3600000
        return System.currentTimeMillis()-recordTime < oneHourMs
    }

    private var timer = Timer()
    private val DELAY: Long = 300

    fun searchCitiesWeather(city:String, context: Context){

        if(city.length<2){
            return
        }

        timer.cancel()
        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    searchRemoteCitiesWeather(city, context)
                }
            },
            DELAY
        )
    }

    fun searchRemoteCitiesWeather(cityName:String, context: Context){
        disposable =  repository.searchCitiesWeather(cityName, context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.isSuccessful) {
                    view?.setCities(it.body().list)
                }
                else {
                    view?.setCities(listOf())
                    view?.showMessage(it.errorBody().string())
                }
            }, {
                view?.setCities(listOf())
                view?.showMessage(it.message.toString())
            })
    }

    fun onDestroy(){
        view = null
        disposable?.dispose()
        repository.onFinish()
    }
}