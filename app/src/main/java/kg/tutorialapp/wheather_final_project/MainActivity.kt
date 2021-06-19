package kg.tutorialapp.wheather_final_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    lateinit var textView2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)

        fetchWeatherUsingQuery()

    }

    private fun fetchWeatherUsingQuery() {
        val call = WeatherClient.weatherApi.fetchWeatherUsingQuery(lat = 40.513999, lon = 72.816098)

        call.enqueue(object : Callback<ForeCast> {

            override fun onResponse(call: Call<ForeCast>, response: Response<ForeCast>) {

                if (response.isSuccessful) {
                    val foreCast = response.body()

                    foreCast?.let {

                        textView.text = it.current?.weather!![0].description

                        textView2.text = it.current?.temp.toString()

                        //Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ForeCast>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}

