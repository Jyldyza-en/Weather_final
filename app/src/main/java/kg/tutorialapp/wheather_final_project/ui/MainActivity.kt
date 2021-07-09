package kg.tutorialapp.wheather_final_project.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import kg.tutorialapp.wheather_final_project.models.ForeCast
import kg.tutorialapp.wheather_final_project.R
import kg.tutorialapp.wheather_final_project.extensions.format
import kg.tutorialapp.wheather_final_project.extensions.formatTime
import kg.tutorialapp.wheather_final_project.models.Constants
import kg.tutorialapp.wheather_final_project.ui.rv.DailyForeCastAdapter
import kg.tutorialapp.wheather_final_project.ui.rv.HourlyForeCastAdapter
import org.koin.android.viewmodel.ext.android.getViewModel
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var vm : MainViewModel

    private lateinit var progress: ProgressBar

    private lateinit var dailyForeCastAdapter : DailyForeCastAdapter

    private lateinit var hourlyForeCastAdapter: HourlyForeCastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vm = getViewModel(MainViewModel::class)
        setupViews()
        setupRecyclerViews()
        subscribeToLiveData()

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.i("Token", it)
        }

        intent.getStringExtra("EXTRA")?.let {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupViews() {
        val tvRefresh = findViewById<TextView>(R.id.tv_refresh)

        progress = findViewById(R.id.progress)

        tvRefresh.setOnClickListener {
            vm.showLoading()
            vm.getWeatherFromApi()
        }
    }

    private fun setupRecyclerViews() {
        val rvDailyForecast = findViewById<RecyclerView>(R.id.rv_daily_forecast)

        dailyForeCastAdapter = DailyForeCastAdapter()
        rvDailyForecast.adapter = dailyForeCastAdapter

        val rvHourlyForecast = findViewById<RecyclerView>(R.id.rv_hourly_forecast)

        hourlyForeCastAdapter = HourlyForeCastAdapter()
        rvHourlyForecast.adapter = hourlyForeCastAdapter
    }

    private fun subscribeToLiveData() {
        vm.getForeCastAsLive().observe(this, {
            it?.let {
                setValuesToViews(it)
                loadWeatherIcon(it)
                setDataToRecyclerViews(it)
            }
        })

        vm._isLoading.observe(this, Observer {
            when(it){
                true -> showLoading()
                false -> hideLoading()
            }
        })
    }

    private fun setDataToRecyclerViews(it: ForeCast) {
        it.daily?.let { dailyList ->
            dailyForeCastAdapter.setItems(dailyList)
        }
        it.hourly?.let { hourlyList ->
            hourlyForeCastAdapter.setItems(hourlyList)
        }
    }

    private fun showLoading() {
        progress.post {
            progress.visibility = View.VISIBLE
        }
    }

    private fun hideLoading(){
        progress.postDelayed({
            progress.visibility = View.INVISIBLE
        }, 2000)
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
        tvSunrise.text = it.current?.sunrise.format("HH:mm")
        tvSunset.text = it.current?.sunset.format("HH:mm")
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

