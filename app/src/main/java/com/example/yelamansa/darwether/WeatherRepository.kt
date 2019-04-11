package com.example.yelamansa.darwether

import android.content.Context
import com.example.yelamansa.darwether.model.ApiWeather
import com.example.yelamansa.darwether.model.City
import com.example.yelamansa.darwether.model.OpenWeather
import com.example.yelamansa.darwether.model.WeatherCache
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.single.SingleCreate
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.io.IOException
import java.nio.charset.Charset
import org.json.JSONException
import org.json.JSONArray
import retrofit2.Response
import io.realm.RealmResults



class WeatherRepository {

    val apiKey = "883e9b0aa42d16a19fedcc4e7803aa40"
    val apiService = ApiService.create()
    lateinit var realm: Realm


    fun searchCitiesWeather(cityName:String, context: Context):Single<Response<OpenWeather>>{
        return  getCityIds(cityName, context).flatMap {
            val result = apiService.searchCitiesWeatherById(it, apiKey)
            result
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({if (it.isSuccessful){
                saveCityWeathers(cityName, it.body().list)

            }}, {})
             result
        }
    }

    private fun saveCityWeathers(cityName: String, cityWeathers: List<ApiWeather>){
        var realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val weatherCache = realm.createObject(WeatherCache::class.java)
        weatherCache.cityName = cityName
        weatherCache.recordTime = getRecordTime()
        val managedList = realm.copyToRealm(cityWeathers)
        weatherCache.apiWeathers?.addAll(managedList)
        realm.commitTransaction()
        realm.close()
    }

    private fun getRecordTime() = System.currentTimeMillis()

    fun getLocalCityWeathers():WeatherCache?{
        realm = Realm.getDefaultInstance()
        val result =  realm.where(WeatherCache::class.java).findAll()
        return if (result!=null&&result.size!=0)
             result.last()
        else  null

    }


    private fun getCityIds(cityName: String, context:Context):Single<String>{
        return getLocalCities(context).flatMap {
            var cityIds = ""
            it
                .filter {item-> item.name.contains(cityName)} //Filter by name
                .take(20) // Get first 20 item, weather api can retrieve only 20 city id
                .map { item -> cityIds+="${ if(cityIds.isNotEmpty()) "," else ""}${item.id}" } //Convert form list to string with ids
            SingleCreate.create { emitter:SingleEmitter<String> ->
                if(cityIds.isEmpty()) emitter.onError(Throwable(message = "Город не найден"))
                else emitter.onSuccess(cityIds) }
        }
    }

    //Get all cities to get their ids
    private fun getLocalCities(context: Context): Single<List<City>> {
        return Single.create{emitter ->
            emitter.onSuccess(convertLocalCities(context))
        }
    }

    private fun convertLocalCities(context: Context):List<City>{
        val cities:MutableList<City> = mutableListOf()
        try {

            val array = JSONArray(loadJSONFromAsset(context))

            for (i in 0 until array.length()){
                val obj = array.getJSONObject(i)
                cities.add(City(obj.getLong("id"),obj.getString("name")))
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return  cities;
    }

    private fun loadJSONFromAsset(context: Context): String? {
        var json: String?
        try {
            val inputStream =context.assets.open("city.short.list.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    fun clearWeathers() {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm -> realm.deleteAll() }
    }

    fun onFinish(){
        realm?.close()
    }


}