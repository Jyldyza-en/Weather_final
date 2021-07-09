package kg.tutorialapp.wheather_final_project.extensions


import java.text.SimpleDateFormat
import java.util.*


fun Long?.format(pattern: String? = "dd/MM/yyyy"): String{
    this?.let {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("Asia/Bishkek")
        return sdf.format(Date(this * 1000))
    }
    return ""
}














