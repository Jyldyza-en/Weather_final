package kg.tutorialapp.wheather_final_project

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.wheather_final_project.storage.ForeCastDatabase

class MainActivity : AppCompatActivity() {

    private val db by lazy {
        ForeCastDatabase.getInstance(applicationContext)
    }

    private lateinit var tvForecastList: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeRxCall()


        db.forecastDao().getAll().observe(this, {
            tvForecastList = findViewById(R.id.tv_forecast_list)
            tvForecastList.text = it?.toString()
        })

    }



    @SuppressLint("CheckResult")
    private fun makeRxCall() {
        WeatherClient.weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())
            .map {
                db.forecastDao().deleteAll()
                db.forecastDao().insert(it)
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {
                Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()

            })
    }



}

