package kg.tutorialapp.wheather_final_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import kg.tutorialapp.wheather_final_project.network.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    lateinit var textView2: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)

        val call = weatherApi.getWeather()

        call.enqueue(object: Callback<ForeCast> {

            override fun  onResponse(call: Call<ForeCast>, response: Response<ForeCast>){

                if (response.isSuccessful){
                    val foreCast = response.body()

                    foreCast?.let {

                        textView.text = it.current?.weather!![0].description

                        textView2.text = it.timezone

                        Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ForeCast>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private val okHttp by lazy {
        val interceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create()).client(okHttp)
            .build()
    }

    private val weatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}