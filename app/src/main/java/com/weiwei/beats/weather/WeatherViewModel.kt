package com.weiwei.beats.weather

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    val cities = listOf<City>(
        City("Banqiao", "板橋區"),
        City("Xinzhuang", "新莊區"),
        City("Tucheng", "土城區")
    )

    //extract city names into two seperated lists
    val cities_ch = MutableList(cities.size, {cities[it].cName})
    val cities_en = MutableList(cities.size, {cities[it].eName})
    //add the selection hint text to be the first item
    init {
        cities_ch.add(0, "請選擇")
        cities_en.add(0, "Select one city")
    }

    //livedata
    val selectedCityWeather = MutableLiveData<CityWeather>()

    //Callback version
//    fun sendRetrofitRequest(location: String) {
//        //send a https request
//        GetService.retrofitService.getAppData(location,"metric", "zh_tw", WeatherViewModel.API_KEY).enqueue(object:
//            Callback<WeatherData> {
//            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
//                response.body()?.let {
//                    //update UI
//                    val temp = CityWeather(
//                   it.name,
//                   it.main.temp,
//                   it.weather[0].description,
//                   it.weather[0].icon
//               )
//               selectedCityWeather.value = temp
//                    //Log.d("Main", response.body().toString())
//                }
//            }
//            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
//                Log.d("Main", t.message.toString())
//            }
//        })
//    }

    //Coroutine version
    fun sendRetrofitRequest(location: String) {
        viewModelScope.launch {
            try {
                val result =
                    GetService.retrofitService.getAppData(location, "metric", "zh_tw",
                        API_KEY
                    )
                val temp = CityWeather(
                    result.name,
                    result.main.temp_min,
                    result.main.temp_max,
                    result.main.feels_like,
                    result.weather[0].description,
                    result.weather[0].icon
                )
                selectedCityWeather.value = temp
                Log.d("Main", temp.toString())
            } catch (e: Exception) {
                Log.d("Main", "Fail to access: ${e.message}")
            }
        }
    }

    companion object {  //static global constants
        const val API_URL = "https://api.openweathermap.org/"
        const val ICON_URL = "https://openweathermap.org/img/wn/"
        const val API_KEY = "5446cea7c0e250b85d9b6a663da609a9"
    }
}