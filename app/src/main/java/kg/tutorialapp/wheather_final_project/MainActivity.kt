package kg.tutorialapp.wheather_final_project

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.wheather_final_project.storage.ForeCastDatabase

class MainActivity : AppCompatActivity() {

    private val db by lazy {
        ForeCastDatabase.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup()

    }

    private fun getForecastFromInput(): ForeCast{

        val et_id = findViewById<EditText>(R.id.et_id)
        val et_lat = findViewById<EditText>(R.id.et_lat)
        val et_long = findViewById<EditText>(R.id.et_long)
        val et_description = findViewById<EditText>(R.id.et_description)

        val id = et_id.text?.toString().takeIf { !it.isNullOrEmpty() }?.toLong()
        val lat = et_lat.text?.toString().takeIf { !it.isNullOrEmpty() }?.toDouble()
        val long = et_long.text?.toString().takeIf { !it.isNullOrEmpty() }?.toDouble()
        val description = et_description?.text.toString()
        val current = CurrentForeCast(weather = listOf(Weather(description = description)))

        return ForeCast(id = id, lat = lat, lon = long, current = current)
    }

    private fun setup() {
        val btn_insert = findViewById<Button>(R.id.btn_insert)
        val btn_update = findViewById<Button>(R.id.btn_update)
        val btn_delete = findViewById<Button>(R.id.btn_delete)
        val btn_query = findViewById<Button>(R.id.btn_query)
        val btn_query_get_all = findViewById<Button>(R.id.btn_query_get_all)

        val tv_forecast_list = findViewById<TextView>(R.id.tv_forecast_list)

        btn_insert.setOnClickListener {
            db
                .forecastDao()
                .insert(getForecastFromInput())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { }
        }

        btn_update.setOnClickListener {
            db
                .forecastDao()
                .update(getForecastFromInput())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { }
        }

        btn_delete.setOnClickListener {
            db
                .forecastDao()
                .delete(getForecastFromInput())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { }
        }

        btn_query_get_all.setOnClickListener {
            db
                .forecastDao()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    {
                        var text = ""

                        it.forEach {
                            text += it.toString()
                        }

                        tv_forecast_list.text = text
                    },
                    {

                    })
        }

        btn_query.setOnClickListener {
            db
                .forecastDao()
                .deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                    {



                    },
                    {

                    })
        }
    }


    @SuppressLint("CheckResult")
    private fun makeRxCall() {
        WeatherClient.weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {
                Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()

            })
    }

}

