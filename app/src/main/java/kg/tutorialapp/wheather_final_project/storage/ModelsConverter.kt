package kg.tutorialapp.wheather_final_project.storage

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kg.tutorialapp.wheather_final_project.CurrentForeCast
import kg.tutorialapp.wheather_final_project.ForeCast

class ModelsConverter {

    fun fromCurrentForeCastToJson(forecast: ForeCast):String =
        Gson().toJson(forecast)

    fun fromJsonToCurrentForeCast(json: String): ForeCast =
        Gson().fromJson(json, object: TypeToken<CurrentForeCast>() {}.type)
}