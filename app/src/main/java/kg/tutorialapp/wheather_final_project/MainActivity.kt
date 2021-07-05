package kg.tutorialapp.wheather_final_project

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.wheather_final_project.storage.ForeCastDatabase
import kotlin.math.roundToInt

@SuppressLint("CheckResult")
class MainActivity : AppCompatActivity() {

    private val db by lazy {
        ForeCastDatabase.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWeatherFromApi()

        subscribeToLiveData()

    }

    private fun subscribeToLiveData() {
        db.forecastDao().getAll().observe(this, {

            it?.let {
                val tvTemperature = findViewById<TextView>(R.id.tv_temperature)
                val tvDate = findViewById<TextView>(R.id.tv_date)
                val tvTempMax = findViewById<TextView>(R.id.tv_temp_max)
                val tvTempMin = findViewById<TextView>(R.id.tv_temp_min)
                val tvFeelsLike = findViewById<TextView>(R.id.tv_feels_like)
                val tvWeather = findViewById<TextView>(R.id.tv_weather)
                val tvSunrise = findViewById<TextView>(R.id.tv_sunrise)
                val tvSunset = findViewById<TextView>(R.id.tv_sunset)
                val tvHumidity = findViewById<TextView>(R.id.tv_humidity)
                val ivWeatherIcon = findViewById<ImageView>(R.id.iv_weather_icon)


                tvTemperature.text = it.current?.temp?.toString()
                tvDate.text = it.current?.date.format()
                tvTempMax.text = it.daily?.get(0)?.temp?.max?.roundToInt()?.toString()
                tvTempMin.text = it.daily?.get(0)?.temp?.min?.roundToInt()?.toString()
                tvFeelsLike.text = it.current?.feels_like?.roundToInt()?.toString()
                tvWeather.text = it.current?.weather?.get(0)?.description
                tvSunrise.text = it.current?.sunrise.format("hh:mm")
                tvSunset.text = it.current?.sunset.format("hh:mm")
                tvHumidity.text = "${it.current?.humidity?.toString()} %"

                it.current?.weather?.get(0)?.icon?.let { icon ->
                    Glide.with(this)
                        .load("https://openweathermap.org/img/wn/${icon}@2x.png")
                        .into(ivWeatherIcon)
                }

            }
        })
    }


    private fun getWeatherFromApi() {
        WeatherClient.weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())
            .map {
                db.forecastDao().insert(it)
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},
                {
                Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()

            })
    }



}

