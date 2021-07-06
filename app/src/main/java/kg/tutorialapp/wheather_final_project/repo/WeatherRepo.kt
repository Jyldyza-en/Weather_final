package kg.tutorialapp.wheather_final_project.repo

import kg.tutorialapp.wheather_final_project.network.WeatherApi
import kg.tutorialapp.wheather_final_project.storage.ForeCastDatabase

class WeatherRepo (
    private val db: ForeCastDatabase,
    private val weatherApi: WeatherApi
) {


}