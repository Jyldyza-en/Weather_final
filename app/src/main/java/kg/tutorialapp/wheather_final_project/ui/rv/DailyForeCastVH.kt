package kg.tutorialapp.wheather_final_project.ui.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.tutorialapp.wheather_final_project.DailyForeCast
import kg.tutorialapp.wheather_final_project.R
import kg.tutorialapp.wheather_final_project.extensions.format
import kg.tutorialapp.wheather_final_project.models.Constants
import kotlin.math.roundToInt

class DailyForeCastVH(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: DailyForeCast){
        val tvWeekday = itemView.findViewById<TextView>(R.id.tv_weekday)
        val tvPrecipitation = itemView.findViewById<TextView>(R.id.tv_precipitation)
        val tvTempMax = itemView.findViewById<TextView>(R.id.tv_temp_max)
        val tvTempMin = itemView.findViewById<TextView>(R.id.tv_min_temp)
        val ivWeatherIcon = itemView.findViewById<ImageView>(R.id.iv_weather_icon)

        tvWeekday.text = item.date.format("dd/MM")
        item.probability?.let {
            tvPrecipitation.text = "${(it * 100).roundToInt()} %"
        }
        tvTempMax.text = item.temp?.max?.roundToInt()?.toString()
        tvTempMin.text = item.temp?.min?.roundToInt()?.toString()

        Glide.with(itemView.context)
            .load("${Constants.iconUri}${item.weather?.get(0)?.icon}${Constants.iconFormat}")
            .into(ivWeatherIcon)
    }

    companion object{
        fun create(parent: ViewGroup): DailyForeCastVH{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_daily_forecast, parent, false)

            return DailyForeCastVH(view)
        }
    }


}