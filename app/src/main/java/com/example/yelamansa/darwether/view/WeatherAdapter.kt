package com.example.yelamansa.darwether.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yelamansa.darwether.R
import com.example.yelamansa.darwether.model.ApiWeather
import kotlinx.android.synthetic.main.item_weather.view.*

class WeatherAdapter(val context: Context):RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    var items:List<ApiWeather> = listOf()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WeatherViewHolder {
        return WeatherViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.item_weather,
                p0,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.itemView.nameTextView.text = items[position].name
        holder.itemView.degreeTextView.text = "${items[position].main?.temp} ℃"
    }


    class  WeatherViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
}