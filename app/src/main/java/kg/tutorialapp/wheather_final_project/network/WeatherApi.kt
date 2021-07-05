package kg.tutorialapp.wheather_final_project.network

import io.reactivex.Single
import kg.tutorialapp.wheather_final_project.models.ForeCast
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("onecall")
    fun fetchWeather(
            @Query("lat") lat: Double = 42.8746,
            @Query("lon") lon: Double = 74.5698,
            @Query("exclude") exclude: String = "minutely",
            @Query("appid") appid: String = "7888dd77c388dc656f9784de8954c8cb",
            @Query("lang") lang: String = "ru",
            @Query("units") units: String = "metric"
    ): Single<ForeCast>
}