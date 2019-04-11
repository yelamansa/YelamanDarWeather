package com.example.yelamansa.darwether.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.yelamansa.darwether.R
import com.example.yelamansa.darwether.presenter.WeatherPresenter
import kotlinx.android.synthetic.main.activity_weather.*
import java.util.*
import com.example.yelamansa.darwether.model.ApiWeather
import android.support.v7.util.DiffUtil
import android.view.View
import com.example.yelamansa.darwether.utils.WeatherDiffUtilCallback




class WeatherActivity : AppCompatActivity(), WeatherView {

    var presenter = WeatherPresenter()
    lateinit var weatherAdapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        initTextChangeListener()

        setupRecyclerView()

        presenter.view = this

        presenter.loadLocalCityWeathers()
    }

    private fun setupRecyclerView(){
        weatherAdapter = WeatherAdapter(this)
        cityRecyclerView?.adapter = weatherAdapter
        cityRecyclerView?.layoutManager = LinearLayoutManager(this)
    }

    private fun updateList(items:List<ApiWeather>){
        val productDiffUtilCallback = WeatherDiffUtilCallback(weatherAdapter.items, items)
        val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)

        weatherAdapter.items = items
        productDiffResult.dispatchUpdatesTo(weatherAdapter)
    }

    private fun disableHandleCityInput() = cityEditText?.addTextChangedListener(cityTextWatcher)

    private fun enableHandleCityInput() = cityEditText?.addTextChangedListener(cityTextWatcher)

    lateinit var cityTextWatcher:TextWatcher

    private fun initTextChangeListener(){
        cityTextWatcher = object : TextWatcher{
            override fun afterTextChanged(s: Editable) {
                presenter.searchCitiesWeather(s.toString(), this@WeatherActivity )
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        enableHandleCityInput()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun setCities(cities: List<ApiWeather>) {
        updateList(cities)
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun setCityName(cityName: String) {
        disableHandleCityInput()
        cityEditText?.setText(cityName)
        enableHandleCityInput()
    }
}
