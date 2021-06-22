package kg.tutorialapp.wheather_final_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    lateinit var textView2: TextView

    private var workResult = 0

    private lateinit var tvCounter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)

        tvCounter = findViewById(R.id.tv_counter)

        //fetchWeatherUsingQuery()

        setup()

    }

    private fun setup() {
        val btnStart = findViewById<Button>(R.id.btn_start)

        btnStart.setOnClickListener {
            doSomeWork()
        }

        val btnShowToast = findViewById<Button>(R.id.btn_show_toast)

        btnShowToast.setOnClickListener {
            Toast.makeText(this, "Hello",Toast.LENGTH_LONG).show()
        }
    }

    private fun doSomeWork() {

        Thread(Runnable {
            for (i in 0..4){
                Thread.sleep(1000)
                workResult++
            }
            runOnUiThread {
                tvCounter.text = workResult.toString()
            }
        }).start()

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

