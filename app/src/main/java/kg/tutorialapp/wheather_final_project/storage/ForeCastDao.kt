package kg.tutorialapp.wheather_final_project.storage

import androidx.room.Dao
import androidx.room.Insert
import kg.tutorialapp.wheather_final_project.ForeCast


@Dao
interface ForeCastDao {

    @Insert
    fun insert(forecast: ForeCast)
}