package com.example.yelamansa.darwether

import com.example.yelamansa.darwether.model.ApiWeather
import com.example.yelamansa.darwether.model.City
import com.example.yelamansa.darwether.model.OpenWeather
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.converter.gson.GsonConverterFactory
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor


interface ApiService {

    @GET("data/2.5/group?units=metric")
    fun searchCitiesWeatherById(@Query("id")  cityIds:String, @Query("APPID") appId:String):Single<Response<OpenWeather>>

    companion object Factory {
        fun create(): ApiService {

            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val okhttp = OkHttpClient.Builder()
                .connectTimeout(60L * 3, TimeUnit.SECONDS)
                .readTimeout(60L * 3, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://api.openweathermap.org/")
                .client(okhttp)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }


}