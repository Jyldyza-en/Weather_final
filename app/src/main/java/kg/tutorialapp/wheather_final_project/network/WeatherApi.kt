package kg.tutorialapp.wheather_final_project.network

import kg.tutorialapp.wheather_final_project.ForeCast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("onecall?lat=42.8746&lon=74.5698&exclude=minutely&appid=7888dd77c388dc656f9784de8954c8cb&lang=ru&units=metric")
    //fun getWeather(): Call<ForeCast>
    fun fetchWeather(): Call<ForeCast>

    @GET("onecall")
    fun fetchWeatherUsingQuery(
            @Query("lat") lat: Double = 42.8746,
            @Query("lon") lon: Double = 74.5698,
            @Query("exclude") exclude: String = "minutely",
            @Query("appid") appid: String = "7888dd77c388dc656f9784de8954c8cb",
            @Query("lang") lang: String = "ru",
            @Query("units") units: String = "metric"
    ):Call<ForeCast>
}