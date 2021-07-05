package kg.tutorialapp.wheather_final_project.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.wheather_final_project.ForeCast
import kg.tutorialapp.wheather_final_project.R
import kg.tutorialapp.wheather_final_project.network.WeatherClient
import kg.tutorialapp.wheather_final_project.extensions.format
import kg.tutorialapp.wheather_final_project.models.Constants
import kg.tutorialapp.wheather_final_project.storage.ForeCastDatabase
import kg.tutorialapp.wheather_final_project.ui.rv.DailyForeCastAdapter
import kotlin.math.roundToInt

@SuppressLint("CheckResult")
class MainActivity : AppCompatActivity() {

    private val db by lazy {
        ForeCastDatabase.getInstance(applicationContext)
    }

    private lateinit var progress: ProgressBar

    private lateinit var dailyForeCastAdapter : DailyForeCastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupRecyclerViews()
        getWeatherFromApi()
        subscribeToLiveData()
    }

    private fun setupViews() {
        val tvRefresh = findViewById<TextView>(R.id.tv_refresh)

        progress = findViewById(R.id.progress)

        tvRefresh.setOnClickListener {
            showLoading()
            getWeatherFromApi()
        }
    }

    private fun setupRecyclerViews() {
        val rv_daily_forecast = findViewById<RecyclerView>(R.id.rv_daily_forecast)

        dailyForeCastAdapter = DailyForeCastAdapter()
        rv_daily_forecast.adapter = dailyForeCastAdapter
    }

    private fun showLoading() {
        progress.visibility = View.VISIBLE

    }

    private fun hideLoading(){
        progress.visibility = View.GONE
    }

    private fun getWeatherFromApi() {
        WeatherClient.weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())
            .map {
                db.forecastDao().insert(it)
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                       hideLoading()
            },
                {
                    hideLoading()
                    Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()

                })
    }

    private fun subscribeToLiveData() {
        db.forecastDao().getAll().observe(this, {
            it?.let {
                setValuesToViews(it)
                loadWeatherIcon(it)
                it.daily?.let { dailyList ->
                    dailyForeCastAdapter.setItems(dailyList) }
            }
        })
    }

    private fun setValuesToViews(it: ForeCast) {
        val tvTemperature = findViewById<TextView>(R.id.tv_temperature)
        val tvDate = findViewById<TextView>(R.id.tv_date)
        val tvTempMax = findViewById<TextView>(R.id.tv_temp_max)
        val tvTempMin = findViewById<TextView>(R.id.tv_temp_min)
        val tvFeelsLike = findViewById<TextView>(R.id.tv_feels_like)
        val tvWeather = findViewById<TextView>(R.id.tv_weather)
        val tvSunrise = findViewById<TextView>(R.id.tv_sunrise)
        val tvSunset = findViewById<TextView>(R.id.tv_sunset)
        val tvHumidity = findViewById<TextView>(R.id.tv_humidity)

        tvTemperature.text = it.current?.temp?.toString()
        tvDate.text = it.current?.date.format()
        tvTempMax.text = it.daily?.get(0)?.temp?.max?.roundToInt()?.toString()
        tvTempMin.text = it.daily?.get(0)?.temp?.min?.roundToInt()?.toString()
        tvFeelsLike.text = it.current?.feels_like?.roundToInt()?.toString()
        tvWeather.text = it.current?.weather?.get(0)?.description
        tvSunrise.text = it.current?.sunrise.format("hh:mm")
        tvSunset.text = it.current?.sunset.format("hh:mm")
        tvHumidity.text = "${it.current?.humidity?.toString()} %"
    }

    private fun loadWeatherIcon(it: ForeCast) {
        val ivWeatherIcon = findViewById<ImageView>(R.id.iv_weather_icon)

        it.current?.weather?.get(0)?.icon?.let { icon ->
            Glide.with(this)
                .load("${Constants.iconUri}${icon}${Constants.iconFormat}")
                .into(ivWeatherIcon)
        }
    }
}

