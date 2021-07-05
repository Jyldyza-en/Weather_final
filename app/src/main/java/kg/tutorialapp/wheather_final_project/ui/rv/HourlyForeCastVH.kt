package kg.tutorialapp.wheather_final_project.ui.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kg.tutorialapp.wheather_final_project.models.HourlyForeCast
import kg.tutorialapp.wheather_final_project.R
import kg.tutorialapp.wheather_final_project.extensions.format
import kg.tutorialapp.wheather_final_project.models.Constants
import kotlin.math.roundToInt

class HourlyForeCastVH(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bind(item: HourlyForeCast){
        val tvTime = itemView.findViewById<TextView>(R.id.tv_time)
        val tvPrecipitation = itemView.findViewById<TextView>(R.id.tv_precipitation)
        val tvTemp = itemView.findViewById<TextView>(R.id.tv_temp)
        val ivWeatherIcon = itemView.findViewById<ImageView>(R.id.iv_weather_icon)

        tvTime.text = item.date.format("HH:mm")
        item.probability?.let {
            tvPrecipitation.text = "${(it * 100).roundToInt()} %"
        }
        tvTemp.text = item.temp?.roundToInt()?.toString()

        Glide.with(itemView.context)
            .load("${Constants.iconUri}${item.weather?.get(0)?.icon}${Constants.iconFormat}")
            .into(ivWeatherIcon)
    }

    companion object{
        fun create(parent: ViewGroup): HourlyForeCastVH{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hourly_forecast, parent, false)

            return HourlyForeCastVH(view)
        }
    }

}