package com.example.yelamansa.darwether.utils

import android.support.v7.util.DiffUtil
import com.example.yelamansa.darwether.model.ApiWeather

class WeatherDiffUtilCallback(private val oldList: List<ApiWeather>, private val newList: List<ApiWeather>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return oldProduct.id == newProduct.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldProduct = oldList[oldItemPosition]
        val newProduct = newList[newItemPosition]
        return oldProduct.name == newProduct.name && oldProduct.main?.temp == newProduct.main?.temp
    }
}