package kg.tutorialapp.wheather_final_project.repo

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.wheather_final_project.models.ForeCast
import kg.tutorialapp.wheather_final_project.network.WeatherApi
import kg.tutorialapp.wheather_final_project.storage.ForeCastDatabase

class WeatherRepo (
    private val db: ForeCastDatabase,
    private val weatherApi: WeatherApi
) {

    fun getWeatherFromApi(): Single<ForeCast> {
        return weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())
            .map {
                db.forecastDao().insert(it)
                it
            }
            .observeOn(AndroidSchedulers.mainThread())

    }

    fun getForeCastFromDbAsLive() = db.forecastDao().getAll()

}